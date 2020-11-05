package com.webank.wecrosssdk.rpc.annotation;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.response.XAResponse;
import com.webank.wecrosssdk.rpc.service.AuthenticationManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import javax.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionalAopHandler {

    private final Logger logger = LoggerFactory.getLogger(TransactionalAopHandler.class);
    @Resource private WeCrossRPC weCrossRPC;
    private Class<? extends Throwable>[] es;

    private String xaTransactionID;
    private String[] paths;

    @Around("@annotation(com.webank.wecrosssdk.rpc.annotation.Transactional)")
    public Object transactionProceed(ProceedingJoinPoint pjp) throws Throwable {
        Object result;

        xaTransactionID = UUID.randomUUID().toString().replace("-", "").toLowerCase();

        logger.info("TransactionProceed transactionID: {}", xaTransactionID);

        setPathsFromAnnotation(pjp);
        if (paths.length == 0) {
            logger.error("TransactionProceed: can't get Paths from annotation.");
            throw new WeCrossSDKException(
                    ErrorCode.FIELD_MISSING, "Can't get Paths from annotation.");
        }
        if (AuthenticationManager.getCurrentUser() == null) {
            logger.error("TransactionProceed: Lack of authentication, can't get getCurrentUser.");
            throw new WeCrossSDKException(
                    ErrorCode.LACK_AUTHENTICATION, "Can't get getCurrentUser, please login first.");
        }
        setTransactionalRollbackFor(pjp);

        XAResponse response = weCrossRPC.startXATransaction(xaTransactionID, paths).send();
        if (response.getErrorCode() != 0) {
            logger.error(
                    "Transactional.startTransaction fail, errorCode:{}, message:{}",
                    response.getErrorCode(),
                    response.getMessage());
            throw new WeCrossSDKException(
                    ErrorCode.RPC_ERROR,
                    "Transactional.startTransaction fail, response message: "
                            + response.getMessage());
        }
        if (response.getXARawResponse().getStatus() != 0) {
            logger.error(
                    "Transactional.startTransaction fail, message:{}",
                    Arrays.toString(response.getXARawResponse().getChainErrorMessages().toArray()));
            throw new WeCrossSDKException(
                    ErrorCode.RPC_ERROR,
                    "Transactional.startTransaction fail, response message: "
                            + Arrays.toString(
                                    response.getXARawResponse().getChainErrorMessages().toArray()));
        }
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            completeTransactionAfterThrowing(throwable);
            throw throwable;
        }
        doCommit();
        return result;
    }

    private void doRollback() throws WeCrossSDKException {
        try {
            XAResponse response = weCrossRPC.rollbackXATransaction(xaTransactionID, paths).send();
            logger.info(
                    "Transactional rollback, transactionID is {},response: {}",
                    xaTransactionID,
                    response);
        } catch (WeCrossSDKException e) {
            logger.error(
                    "Transactional rollback transaction, errorCode:{} and errorMessage:",
                    e.getErrorCode(),
                    e);
            throw new WeCrossSDKException(
                    e.getErrorCode(),
                    "Transactional rollback occurs error, message: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Something error occurs in Transactional.doRollback, errorMessage:", e);
            throw new WeCrossSDKException(
                    ErrorCode.INTERNAL_ERROR,
                    "Something error occurs in Transactional.doRollback.");
        }
    }

    private void doCommit() throws WeCrossSDKException {
        try {
            XAResponse response = weCrossRPC.commitXATransaction(xaTransactionID, paths).send();
            logger.info(
                    "Transactions committed, transactionID is {}, response: {}",
                    xaTransactionID,
                    response);
        } catch (WeCrossSDKException e) {
            logger.error(
                    "Transactional commit transaction, errorCode:{} and errorMessage:",
                    e.getErrorCode(),
                    e);
            throw new WeCrossSDKException(
                    e.getErrorCode(),
                    "Transactional commit occurs error, message: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Something error occurs in Transactional.doCommit, errorMessage:", e);
            throw new WeCrossSDKException(
                    ErrorCode.INTERNAL_ERROR,
                    "Something error occurs in Transactional.doCommit." + e.getMessage());
        }
    }

    private void completeTransactionAfterThrowing(Throwable throwable) throws WeCrossSDKException {
        if (es != null && es.length > 0) {
            for (Class<? extends Throwable> e : es) {
                if (e.isAssignableFrom(throwable.getClass())) {
                    logger.info("Error occurs and did rollback, this error is {}", e);
                    doRollback();
                } else {
                    logger.info("Error occurs but did not rollback, this error is {}", e);
                    doCommit();
                }
            }
        }
    }

    private void setPathsFromAnnotation(ProceedingJoinPoint pjp) {
        Object[] params = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        // [param][annotation]
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Object param = params[i];
            Annotation[] paramAnn = annotations[i];
            if (param == null || paramAnn.length == 0) {
                continue;
            }
            for (Annotation annotation : paramAnn) {
                if (annotation.annotationType().equals(Path.class)) {
                    this.paths = (String[]) param;
                }
            }
        }
    }

    private void setTransactionalRollbackFor(JoinPoint jp) throws NoSuchMethodException {
        String methodName = jp.getSignature().getName();
        Class<?> classTarget = jp.getTarget().getClass();
        Class<?>[] par = ((MethodSignature) jp.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, par);
        Transactional transactional = objMethod.getDeclaredAnnotation(Transactional.class);
        if (transactional != null) {
            es = transactional.rollbackFor();
        } else {
            logger.warn(
                    "Can't get Transactional annotation value, and turn to default Exception.class");
        }
    }
}

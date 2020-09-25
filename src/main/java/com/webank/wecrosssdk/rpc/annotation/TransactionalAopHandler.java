package com.webank.wecrosssdk.rpc.annotation;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.response.RoutineResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.UUID;
import javax.annotation.Resource;
import com.webank.wecrosssdk.rpc.service.AuthenticationManager;
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

    private String transactionID;
    private String[] paths;

    @Around("@annotation(com.webank.wecrosssdk.rpc.annotation.Transactional)")
    public Object TransactionProceed(ProceedingJoinPoint pjp) throws Throwable {
        Object result;

        transactionID = UUID.randomUUID().toString().replace("-", "").toLowerCase();

        logger.info("TransactionProceed transactionID: {}", transactionID);

        setPathsFromAnnotation(pjp);
        if (paths.length == 0) {
            logger.error("Exception: can't get Paths from annotation.");
            return null;
        }
        if(AuthenticationManager.getCurrentUser()==null){
            logger.error("Exception: can't get getCurrentUser, please login first.");
            return null;
        }
        setTransactionalRollbackFor(pjp);

        RoutineResponse response =
                weCrossRPC.startTransaction(transactionID, paths).send();
        if (response.getErrorCode() != 0) {
            logger.error(
                    "Transactional.startTransaction fail, errorCode:{}, errorMessage:{}", response.getErrorCode(),response.getMessage());
            return null;
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
            RoutineResponse response =
                    weCrossRPC.rollbackTransaction(transactionID, paths).send();
            logger.info(
                    "Transactions rollback, transactionID is {},response: {}",
                    transactionID,
                    response.toString());
        } catch (WeCrossSDKException e) {
            logger.error(
                    "Transactional rollback transaction, errorCode:{} and errorMessage:{}",
                    e.getErrorCode(),
                    e.getMessage());
            throw new WeCrossSDKException(e.getErrorCode(), "Transactional rollback occurs error!");
        } catch (Exception e) {
            logger.error(
                    "Something error occurs in Transactional.doRollback, errorMessage:{}",
                    e.getMessage());
            throw new WeCrossSDKException(
                    ErrorCode.INTERNAL_ERROR,
                    "Something error occurs in Transactional.doRollback.");
        }
    }

    private void doCommit() throws WeCrossSDKException {
        try {
            RoutineResponse response =
                    weCrossRPC.commitTransaction(transactionID, paths).send();
            logger.info(
                    "Transactions committed, transactionID is {},response: {}",
                    transactionID,
                    response.toString());
        } catch (WeCrossSDKException e) {
            logger.error(
                    "Transactional commit transaction, errorCode:{} and errorMessage:{}",
                    e.getErrorCode(),
                    e.getMessage());
            throw new WeCrossSDKException(e.getErrorCode(), "Transactional commit occurs error!");
        } catch (Exception e) {
            logger.error(
                    "Something error occurs in Transactional.doCommit, errorMessage:{}",
                    e.getMessage());
            throw new WeCrossSDKException(
                    ErrorCode.INTERNAL_ERROR, "Something error occurs in Transactional.doCommit.");
        }
    }

    private void completeTransactionAfterThrowing(Throwable throwable) throws WeCrossSDKException {
        if (es != null && es.length > 0) {
            for (Class<? extends Throwable> e : es) {
                if (e.isAssignableFrom(throwable.getClass())) {
                    logger.info("Error occurs and did rollback, this error is {}", e.toString());
                    doRollback();
                } else {
                    logger.info(
                            "Error occurs but did not rollback, this error is {}", e.toString());
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

    private void setTransactionalRollbackFor(JoinPoint jp) throws Exception {
        String methodName = jp.getSignature().getName();
        Class<?> classTarget = jp.getTarget().getClass();
        Class<?>[] par = ((MethodSignature) jp.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, par);
        Transactional transactional = objMethod.getDeclaredAnnotation(Transactional.class);
        if (transactional != null) {
            es = transactional.rollbackFor();
        } else {
            logger.warn("Can't get Transactional annotation value!");
            throw new WeCrossSDKException(
                    ErrorCode.FIELD_MISSING, "Can't get Transactional annotation value.");
        }
    }
}

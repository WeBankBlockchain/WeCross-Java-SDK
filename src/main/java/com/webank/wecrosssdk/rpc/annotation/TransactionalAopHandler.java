package com.webank.wecrosssdk.rpc.annotation;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.common.TransactionContext;
import com.webank.wecrosssdk.rpc.methods.response.RoutineResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
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
    private WeCrossRPC weCrossRPC;
    private Class<? extends Throwable>[] es;

    private String transactionID;
    private String[] accounts;
    private String[] paths;

    @Around("@annotation(com.webank.wecrosssdk.rpc.annotation.Transactional)")
    public Object TransactionProceed(ProceedingJoinPoint pjp) throws Throwable {
        Object result;

        setTransactionID();
        initWeCrossRPC();
        logger.info("TransactionProceed:{}", transactionID);
        TransactionContext.txThreadLocal.set(transactionID);
        TransactionContext.seqThreadLocal.set(new AtomicInteger(1));

        setAccountsAndPathsFromAnnotation(pjp);
        if (accounts.length == 0 || paths.length == 0) {
            logger.error("Exception: can't get Accounts or Paths from annotation.");
            return null;
        }
        setTransactionalRollbackFor(pjp);

        RoutineResponse response =
                weCrossRPC.startTransaction(transactionID, accounts, paths).send();
        System.out.println("transactionID: " + transactionID);
        System.out.println("Transactional.startTransaction: " + response.getMessage());

        if (response.getErrorCode() != 0) {
            logger.error(
                    "Transactional.startTransaction fail, errorCode:{}", response.getErrorCode());
            return null;
        }
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            completeTransactionAfterThrowing(throwable);
            throw throwable;
        } finally {
            TransactionContext.txThreadLocal.remove();
            TransactionContext.seqThreadLocal.remove();
        }
        doCommit();
        return result;
    }

    private void doRollBack() {
        try {
            if (weCrossRPC == null) {
                initWeCrossRPC();
            }
            RoutineResponse response =
                    weCrossRPC.rollbackTransaction(transactionID, accounts, paths).send();
            logger.info(
                    "Transactions rollback, transactionID is {},response: {}",
                    transactionID,
                    response.toString());
        } catch (WeCrossSDKException e) {
            logger.error(
                    "Transactional rollback transaction, errorCode:{} and errorMessage:{}",
                    e.getErrorCode(),
                    e.getMessage());
        } catch (Exception e) {
            logger.error(
                    "Something error occurs in Transactional.doRollback, errorMessage:{}",
                    e.getMessage());
        }
    }

    private void doCommit() {
        try {
            if (weCrossRPC == null) {
                initWeCrossRPC();
            }
            RoutineResponse response =
                    weCrossRPC.commitTransaction(transactionID, accounts, paths).send();
            System.out.println(
                    "Transactions committed, transactionID is"
                            + transactionID
                            + ",response:"
                            + response.toString());
            logger.info(
                    "Transactions committed, transactionID is {},response: {}",
                    transactionID,
                    response.toString());
        } catch (WeCrossSDKException e) {
            logger.error(
                    "Transactional commit transaction, errorCode:{} and errorMessage:{}",
                    e.getErrorCode(),
                    e.getMessage());
        } catch (Exception e) {
            logger.error(
                    "Something error occurs in Transactional.doCommit, errorMessage:{}",
                    e.getMessage());
        }
    }

    private void completeTransactionAfterThrowing(Throwable throwable) {
        if (es != null && es.length > 0) {
            for (Class<? extends Throwable> e : es) {
                if (e.isAssignableFrom(throwable.getClass())) {
                    logger.info("Error occurs and did rollback, this error is {}", e.toString());
                    doRollBack();
                } else {
                    logger.info(
                            "Error occurs but did not rollback, this error is {}", e.toString());
                    doCommit();
                }
            }
        }
    }

    private void initWeCrossRPC() throws WeCrossSDKException {
        WeCrossService weCrossService = new WeCrossRPCService();
        weCrossRPC = WeCrossRPCFactory.build(weCrossService);
    }

    private void setAccountsAndPathsFromAnnotation(ProceedingJoinPoint pjp) {
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
                if (annotation.annotationType().equals(Account.class)) {
                    this.accounts = (String[]) param;
                }
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
            logger.warn("");
        }
    }

    public void setTransactionID() {
        transactionID = UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
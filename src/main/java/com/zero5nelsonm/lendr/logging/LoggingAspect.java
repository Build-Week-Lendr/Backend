package com.zero5nelsonm.lendr.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by satyendra on 11/7/16.
 * http://www.tothenew.com/blog/logging-with-spring-aop-and-custom-annotations-in-java-application/
 */

@Component
@Aspect
public class LoggingAspect {

    @Around(value = "@within(com.zero5nelsonm.lendr.logging.Loggable) || @annotation(com.zero5nelsonm.lendr.logging.Loggable)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        Loggable loggableMethod = method.getAnnotation(Loggable.class);

        Loggable loggableClass = proceedingJoinPoint.getTarget()
                .getClass()
                .getAnnotation(Loggable.class);

        // Get current log level
        LogLevel logLevel = loggableMethod != null ? loggableMethod.value() : loggableClass.value();

        String star = "**********";
        // Before
        LogWriter.write(proceedingJoinPoint.getTarget()
                        .getClass(),
                logLevel,
                star + proceedingJoinPoint.getTarget()
                        .getClass() + "." + method.getName() + "() start execution" + star);

        // Show params
        boolean showParams = loggableMethod != null ? loggableMethod.params() : loggableClass.params();
        if (showParams) {

            if (proceedingJoinPoint.getArgs() != null && proceedingJoinPoint.getArgs().length > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < proceedingJoinPoint.getArgs().length; i++) {
                    sb.append(method.getParameterTypes()[i].getName() + ":" + proceedingJoinPoint.getArgs()[i]);
                    if (i < proceedingJoinPoint.getArgs().length - 1) {
                        sb.append(", ");
                    }
                }

                LogWriter.write(proceedingJoinPoint.getTarget()
                                .getClass(),
                        logLevel,
                        proceedingJoinPoint.getTarget()
                                .getClass() + "." + method.getName() + "() args " + sb);
            }
        }

        long startTime = System.currentTimeMillis();

        // Start method execution
        Object result = proceedingJoinPoint.proceed();

        long endTime = System.currentTimeMillis();

        // Show results
        if (result != null) {
            boolean showResults = loggableMethod != null ? loggableMethod.result() : loggableClass.result();
            if (showResults) {
                LogWriter.write(proceedingJoinPoint.getTarget()
                                .getClass(),
                        logLevel,
                        proceedingJoinPoint.getTarget()
                                .getClass() + "." + method.getName() + "() Result : " + result);
            }
        }

        // Show after
        LogWriter.write(proceedingJoinPoint.getTarget()
                        .getClass(),
                logLevel,
                star + proceedingJoinPoint.getTarget()
                        .getClass() + "." + method.getName() + "() finished execution and takes " + (endTime - startTime) + " millis time to execute " + star);

        return result;
    }
}
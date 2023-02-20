package it.vitalegi.mangar.metrics;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;

@Log4j2
@Aspect
@Component
public class PerformanceAspect {

    @Value("${metrics.skipIfLessThan}")
    int skipIfLessThan;

    @Around(value = "@target(Performance) && within(it.vitalegi..*)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Type type = type(joinPoint);
        LocalDateTime now = LocalDateTime.now();
        try {
            Object out = joinPoint.proceed();
            long duration = time(start);
            if (skipIfLessThan == -1 || duration >= skipIfLessThan) {
                log.info("target={}, type={}, time={}, status=OK", getName((joinPoint)), type, duration);
            }
            return out;
        } catch (Throwable e) {
            Throwable root = root(e);
            log.error("target={}, type={}, time={}, status=KO, e={}, e_msg={}, root={}, root_msg={}",
                    getName((joinPoint)), type, time(start), exName(e), e.getMessage(), exName(root),
                    root.getMessage());
            throw e;
        }
    }

    protected String exName(Throwable e) {
        return e.getClass()
                .getSimpleName();
    }

    protected String getName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature()
                        .getName();
    }

    protected Throwable root(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }

    protected long time(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    protected Type type(ProceedingJoinPoint joinPoint) {
        Annotation annotation = joinPoint.getSignature()
                                         .getDeclaringType()
                                         .getAnnotation(Performance.class);
        if (annotation != null) {
            return ((Performance) annotation).value();
        }
        return null;
    }
}

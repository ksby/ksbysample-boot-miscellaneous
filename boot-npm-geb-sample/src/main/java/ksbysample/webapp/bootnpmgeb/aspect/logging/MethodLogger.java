package ksbysample.webapp.bootnpmgeb.aspect.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * ???
 */
@Slf4j
@Aspect
@Component
public class MethodLogger {

    @SuppressWarnings({"PMD.UnusedPrivateMethod", "UnusedMethod"})
    @Pointcut("execution(* ksbysample.webapp.bootnpmgeb.web..*.*(..))"
            + "&& @within(org.springframework.stereotype.Controller)")
    private void pointcutControllerMethod() {
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod", "UnusedMethod"})
    @Pointcut("execution(* ksbysample.webapp.bootnpmgeb.service..*.*(..))"
            + "&& @within(org.springframework.stereotype.Service)")
    private void pointcutServiceMethod() {
    }

    /**
     * @param pjp ???
     * @return ???
     * @throws Throwable
     */
    @Around(value = "pointcutControllerMethod() || pointcutServiceMethod()")
    public Object logginMethod(ProceedingJoinPoint pjp)
            throws Throwable {
        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();

        logginBeginMethod(className, methodName, pjp.getArgs());
        Object ret = pjp.proceed();
        logginEndMethod(className, methodName, ret);

        return ret;
    }

    @SuppressWarnings({"PMD.UseVarargs"})
    private void logginBeginMethod(String className, String methodName, Object[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("call : ")
                .append(className)
                .append('#')
                .append(methodName)
                .append('(')
                .append(ToStringBuilder.reflectionToString(args, ToStringStyle.SIMPLE_STYLE))
                .append(')');
        log.info(sb.toString());
    }

    private void logginEndMethod(String className, String methodName, Object ret) {
        StringBuilder sb = new StringBuilder();
        sb.append("ret = ")
                .append(ret)
                .append(" : ")
                .append(className)
                .append('#')
                .append(methodName);
        log.info(sb.toString());
    }

}

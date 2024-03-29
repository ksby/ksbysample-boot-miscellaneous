package ksbysample.webapp.bootnpmgeb.aspect.logging;

import com.google.common.collect.Iterators;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * ???
 */
@SuppressWarnings("PMD.TooManyMethods")
@Slf4j
@Aspect
@Component
public class RequestAndResponseLogger {

    private static final String POINTCUT_ALL_CLASS_AND_METHOD_UNDER_APPLICATION_PACKAGE
            = "execution(* ksbysample.webapp.bootnpmgeb..*.*(..))";

    private static final String LOG_REQUEST_INFO = "[req][info  ] ";
    private static final String LOG_REQUEST_HEADER = "[req][header] ";
    private static final String LOG_REQUEST_COOKIE = "[req][cookie] ";
    private static final String LOG_REQUEST_PARAMETER = "[req][param ] ";

    private static final String LOG_RESPONSE_INFO = "[res][info  ] ";
    private static final String LOG_RESPONSE_HEADER = "[res][header] ";

    /**
     * Web アプリケーションの package 配下の全てのクラスのメソッドを Join Point にする Pointcut
     */
    @Pointcut(POINTCUT_ALL_CLASS_AND_METHOD_UNDER_APPLICATION_PACKAGE)
    public void allClassAndMethodUnderApplicationPackage() {
    }

    /**
     * {@link RequestMapping} アノテーションが付加されたメソッドを Join Point にする Pointcut
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMappingAnnotation() {
    }

    /**
     * {@link GetMapping}, {@link PostMapping} 等 {@link RequestMapping} アノテーションを使用した
     * composed annotation が付加されたメソッドを Join Point にする Pointcut
     */
    @Pointcut("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    public void requestMappingComposedAnnotations() {
    }

    /**
     * ???
     *
     * @param pjp ???
     * @return ???
     * @throws Throwable ???
     */
    @SuppressWarnings("ReturnValueIgnored")
    @Around(value = "allClassAndMethodUnderApplicationPackage()"
            + " && (requestMappingAnnotation() || requestMappingComposedAnnotations())")
    public Object logginRequestAndResponse(ProceedingJoinPoint pjp)
            throws Throwable {
        HttpServletRequest request
                = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        loggingRequest(request);
        Object ret = pjp.proceed();
        Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .flatMap(requestAttributes -> Optional.ofNullable(requestAttributes.getResponse()))
                .flatMap(response -> {
                    loggingResponse(response);
                    return Optional.empty();
                });

        return ret;
    }

    /**
     * ???
     */
    @SuppressWarnings("ReturnValueIgnored")
    @After(value = "allClassAndMethodUnderApplicationPackage()"
            + " && @annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void logginResponseAfterExceptionHandler() {
        Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .flatMap(requestAttributes -> Optional.ofNullable(requestAttributes.getResponse()))
                .flatMap(response -> {
                    loggingResponse(response);
                    return Optional.empty();
                });
    }

    private void loggingRequest(HttpServletRequest request) {
        loggingRequestInfo(request);
        loggingRequestHeaders(request);
        loggingRequestCookies(request);
        loggingRequestParameters(request);
    }

    private void loggingResponse(HttpServletResponse response) {
        loggingResponseInfo(response);
        loggingResponseHeaders(response);
    }

    private void loggingRequestInfo(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        append(sb, "REQUEST_URI", request.getRequestURI());
        append(sb, ", SERVLET_PATH", request.getServletPath());
        logging(LOG_REQUEST_INFO, null, sb.toString());

        sb = new StringBuilder();
        append(sb, "CHARACTER_ENCODING", request.getCharacterEncoding());
        append(sb, ", CONTENT_LENGTH", String.valueOf(request.getContentLength()));
        append(sb, ", CONTENT_TYPE", request.getContentType());
        append(sb, ", LOCALE", String.valueOf(request.getLocale()));
        append(sb, ", SCHEME", request.getScheme());
        logging(LOG_REQUEST_INFO, null, sb.toString());

        sb = new StringBuilder();
        append(sb, "REMOTE_ADDR", request.getRemoteAddr());
        append(sb, ", REMOTE_HOST", request.getRemoteHost());
        append(sb, ", SERVER_NAME", request.getServerName());
        append(sb, ", SERVER_PORT", String.valueOf(request.getServerPort()));
        logging(LOG_REQUEST_INFO, null, sb.toString());

        sb = new StringBuilder();
        append(sb, "CONTEXT_PATH", request.getContextPath());
        append(sb, ", REQUEST_METHOD", request.getMethod());
        append(sb, ", QUERY_STRING", request.getQueryString());
        append(sb, ", PATH_INFO", request.getPathInfo());
        append(sb, ", REMOTE_USER", request.getRemoteUser());
        logging(LOG_REQUEST_INFO, null, sb.toString());
    }

    private void loggingRequestHeaders(HttpServletRequest request) {
        Iterable<String> headerNameList = () -> Iterators.forEnumeration(request.getHeaderNames());
        StreamSupport.stream(headerNameList.spliterator(), false)
                .filter(headerName -> !StringUtils.equals(headerName, "cookie"))
                .forEach(headerName -> {
                    Iterable<String> headerList = () -> Iterators.forEnumeration(request.getHeaders(headerName));
                    headerList.forEach(header -> logging(LOG_REQUEST_HEADER, headerName, header));
                });
    }

    private void loggingRequestCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Arrays.asList(request.getCookies()).forEach(cookie -> {
                StringBuilder sb = new StringBuilder();
                sb.append("name = ")
                        .append(cookie.getName())
                        .append(", value = ")
                        .append(cookie.getValue());
                logging(LOG_REQUEST_COOKIE, null, sb.toString());
            });
        }
    }

    private void loggingRequestParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, values) -> {
            Arrays.asList(values).forEach(value -> logging(LOG_REQUEST_PARAMETER, key, value));
        });
    }

    private void loggingResponseInfo(HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
        append(sb, "RESPONSE_STATUS", String.valueOf(response.getStatus()));
        append(sb, ", CHARACTER_ENCODING", response.getCharacterEncoding());
        append(sb, ", CONTENT_TYPE", response.getContentType());
        append(sb, ", LOCALE", String.valueOf(response.getLocale()));
        logging(LOG_RESPONSE_INFO, null, sb.toString());
    }

    private void loggingResponseHeaders(HttpServletResponse response) {
        response.getHeaderNames().forEach(headerName -> {
            response.getHeaders(headerName).forEach(header -> logging(LOG_RESPONSE_HEADER, headerName, header));
        });
    }

    private void logging(String title, String name, String value) {
        StringBuilder sb = new StringBuilder(title);
        if (name != null) {
            sb.append(name);
            sb.append(" = ");
        }
        sb.append(value);
        log.info(sb.toString().replaceAll("[\r\n]", ""));
    }

    private void append(StringBuilder sb, String name, String value) {
        sb.append(name)
                .append(" = ")
                .append(value);
    }

}

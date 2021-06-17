package org.lean.proxy;

import java.util.StringJoiner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class HeadersLoggerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler)
    {
        log.info("New request intercepted, headers extracted: \n {}", extractHeaders(requestServlet));
        return true;
    }

    private String extractHeaders(HttpServletRequest requestServlet) {
        StringJoiner joiner = new StringJoiner( System.lineSeparator(),"[",  "\n]");
        var headers = requestServlet.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            joiner.add(String.format("\t%s, %s", headerName, requestServlet.getHeader(headerName)));
        }
        return joiner.toString();
    }
}

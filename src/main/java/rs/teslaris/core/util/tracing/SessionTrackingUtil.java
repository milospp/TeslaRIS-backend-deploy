package rs.teslaris.core.util.tracing;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class SessionTrackingUtil {

    public static String getJSessionId() {
        var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attrs)) {
            return "Anonymous";
        }

        var request = attrs.getRequest();
        if (Objects.isNull(request.getCookies())) {
            return "Anonymous";
        }

        for (var cookie : request.getCookies()) {
            if ("JSESSIONID".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return "Anonymous";
    }

    public static String getCurrentClientIP() {
        try {
            return Objects.requireNonNullElse(MDC.get(TraceMDCKeys.CLIENT_IP), "N/A");
        } catch (IllegalArgumentException e) {
            return "N/A";
        }
    }

    public static String getCurrentTracingContextId() {
        try {
            var tracingContextID = MDC.get(TraceMDCKeys.TRACING_CONTEXT_ID);

            if (Objects.isNull(tracingContextID)) {
                return "N/A";
            }

            return tracingContextID;
        } catch (IllegalArgumentException e) {
            log.error("CRITICAL - Unable to obtain MDC tracing context id.");
            return "N/A";
        }
    }
}

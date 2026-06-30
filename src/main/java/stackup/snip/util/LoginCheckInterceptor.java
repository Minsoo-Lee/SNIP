package stackup.snip.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("[url] {}", requestURI);

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("memberId") == null) {
            response.sendRedirect("/login");
            return false; // 요청 차단
        }

        request.setAttribute(
                "loginMemberId",
                session.getAttribute("memberId")
        );

        return true; // 통과
    }
}
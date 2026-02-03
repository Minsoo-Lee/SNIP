package stackup.snip.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws Exception {

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
package gr.aueb.cf.schoolapp.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("User {} logged in successfully", authentication.getName());   // username

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
//            response.sendRedirect(savedRequest.getRedirectUrl());   // relative URL
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
            return;
        }

//        Set<String> authorities = new HashSet<>();
//        for (GrantedAuthority authority : authentication.getAuthorities()) {
//            authorities.add(authority.getAuthority());
//        }
//        if (authorities.contains("ROLE_ADMIN")) {
//            redirectStrategy.sendRedirect(request, response, "/admin-dashboard");
//        } else {
//            redirectStrategy.sendRedirect(request, response, "/employee-dashboard");
//        }

        redirectStrategy.sendRedirect(request, response, "/teachers");
    }
}

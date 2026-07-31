package gr.aueb.cf.schoolapp.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

//    private static final Map<String, String> ROLE_LANDING_PAGES = new LinkedHashMap<>(Map.of()) {{
//        put("ROLE_ADMIN",     "/admin-dashboard");
//        put("ROLE_ΕΜΠΟΛΥΕΕ",   "/employee-dashboard");
//        put("ROLE_CITIZEN",   "/citizen-dashboard");
//    }};


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


//        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

//        if (authorities.contains("ROLE_ADMIN")) {
//            redirectStrategy.sendRedirect(request, response, "/admin-dashboard");
//        } else {
//            redirectStrategy.sendRedirect(request, response, "/employee-dashboard");
//        }

//        String targetUrl = ROLE_LANDING_PAGES.entrySet().stream()
//                .filter(entry -> authorities.contains(entry.getKey()))
//                .map(Map.Entry::getValue)
//                .findFirst()
//                .orElse(FALLBACK_URL);
//
//        redirectStrategy.sendRedirect(request, response, targetUrl);

        // Θα πρέπει στο security config να ελεγχθούν τα target urls με τους ρόλους
        // π.χ. .requestMatchers("/admin-dashboard/**").hasRole("ADMIN")

        redirectStrategy.sendRedirect(request, response, "/teachers");
    }
}

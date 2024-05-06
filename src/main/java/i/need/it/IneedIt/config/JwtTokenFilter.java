package i.need.it.IneedIt.config;

import i.need.it.IneedIt.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static i.need.it.IneedIt.config.SecurityUtils.getCurrentUserId;

@Slf4j
@Configuration
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtVerifier;

    public JwtTokenFilter(JwtService jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        System.out.println("Authorization header: " + header); // Debug output

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        if (jwtVerifier.verifyToken(token)) {
            log.info("The token is valid");
            Claims claims = jwtVerifier.getClaims(token);
            String userId = claims.getSubject();
            log.info("UserId found: "+ userId);
            PreAuthenticatedAuthenticationToken authentication =
                    new PreAuthenticatedAuthenticationToken(userId, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.err.println("Invalid or expired token");
        }
        filterChain.doFilter(request, response);
    }
}

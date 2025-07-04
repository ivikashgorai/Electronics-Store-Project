package com.electronics.store.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component // or configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    //this method gets called at the very first when user request with token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get token from request
        //token eg - Bearer cijdibdobdbncojs.insvodj.isn
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header {}",requestHeader);

        String username = null;
        String token = null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer")){
            token = requestHeader.substring(7);
            try {
                username = jwtHelper.getUsernameFromToken(token);
                logger.info("Username {}", username);
            } catch (ExpiredJwtException e) {
                logger.error("Token expired: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = new ObjectMapper().writeValueAsString(
                        Map.of("error", "Token Expired")
                );
                response.getWriter().write(json);
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = new ObjectMapper().writeValueAsString(
                        Map.of("error", "Invalid Token")
                );
                response.getWriter().write(json);
                return;
            }
        }
        else{
            logger.info("Header is null or not start with Bearer");
        }

        //below
//        To verify the JWT token on each request and set the logged-in user into Spring Security's context, so the system knows who the user is and what permissions they have.
        /*✅ First, it checks that:
            A username was successfully extracted from the JWT token.
            No user is yet authenticated for this request (i.e., avoid overriding an existing login).*/
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Loads the user’s details from your UserDetailsService (which typically fetches from a database).
            //This gives you the user's password hash, roles, account status, etc.
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails)) {
//                Creates a Spring Security Authentication object.
//                userDetails → the authenticated user
//                null → no password needed (because it's token-based login)
//                userDetails.getAuthorities() → the user's roles/authorities.
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            Sets additional request details into the authentication token (like IP address, session ID, etc.), which can be useful for auditing.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                This is the final step: it puts the Authentication object into the Spring SecurityContext, making the user "logged in" for the current request.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);

    }
}

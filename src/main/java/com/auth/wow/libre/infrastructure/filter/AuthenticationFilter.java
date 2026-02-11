package com.auth.wow.libre.infrastructure.filter;

import com.auth.wow.libre.domain.model.constant.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.model.security.*;
import com.auth.wow.libre.domain.ports.in.jwt.*;
import com.fasterxml.jackson.databind.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.util.matcher.*;

import java.io.*;
import java.util.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String PATH_AUTHENTICATION_FILTER = "/api/auth/login";
    private final AuthenticationProvider authenticationProvider;
    private final JwtPort jwtPort;

    public AuthenticationFilter(AuthenticationProvider authenticationProvider,
                                JwtPort jwtPort) {
        this.authenticationProvider = authenticationProvider;
        this.jwtPort = jwtPort;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(buildPattern(), "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        String transactionId = request.getHeader(Constants.HEADER_TRANSACTION_ID);
        ThreadContext.put(CONSTANT_UNIQUE_ID, transactionId);
        UserModel user = getParamsUser(request, transactionId);
        return authenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(user.username, user.password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException {

        final String transactionId = request.getHeader(Constants.HEADER_TRANSACTION_ID);

        Map<String, Object> body = new HashMap<>();
        ThreadContext.put(CONSTANT_UNIQUE_ID, transactionId);

        try {
            final JwtDto jwt = generateToken(authResult);
            body.put("message", "ok");
            body.put("code", 200);
            body.put("data", jwt);
            body.put(Constants.HEADER_TRANSACTION_ID, transactionId);

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(200);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        } catch (Exception e) {
            body.put("error", "invalid data");
            body.put("message", Constants.Errors.CONSTANT_GENERIC_ERROR_MESSAGE);
            body.put("message_trace", e.getMessage());
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Please verify the information provided.");
        body.put("message", Constants.Errors.CONSTANT_GENERIC_ERROR_MESSAGE);
        body.put("message_trace", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }


    private String buildPattern() {
        return PATH_AUTHENTICATION_FILTER;
    }

    private UserModel getParamsUser(HttpServletRequest request, String transactionId) {
        try {
            return new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
        } catch (java.io.IOException e) {
            logger.error("Invalid parameters, please check your information");
            throw new UnauthorizedException(Constants.Errors.CONSTANT_GENERIC_ERROR_MESSAGE, transactionId);
        }
    }

    private JwtDto generateToken(Authentication authResult) {
        CustomUserDetails customUserDetails = ((CustomUserDetails) authResult.getPrincipal());
        final String token = jwtPort.generateToken(customUserDetails);
        final Date expiration = jwtPort.extractExpiration(token);
        final String refreshToken = jwtPort.generateRefreshToken(customUserDetails);
        return new JwtDto(token, refreshToken, expiration);
    }
}

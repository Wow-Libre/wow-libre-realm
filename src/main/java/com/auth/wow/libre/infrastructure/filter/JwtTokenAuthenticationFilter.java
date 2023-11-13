package com.auth.wow.libre.infrastructure.filter;

import com.auth.wow.libre.domain.model.security.UserModel;
import com.auth.wow.libre.domain.model.shared.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {


  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }


  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {


    Map<String, Object> body = new HashMap<>();

    try {
      String token = generateToken(authResult);

      body.put("token", token);
      response.getWriter().write(new ObjectMapper().writeValueAsString(body));
      response.setStatus(200);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    } catch (Exception e) {
      body.put("error", "oauth_invalid");
      body.put("message",
              "An unexpected error has occurred and it was not possible to authenticate to the system, please try again later.");
      body.put("message_trace", e.getMessage());
      response.getWriter().write(new ObjectMapper().writeValueAsString(body));
      response.setStatus(401);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

  }

  protected void unsuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException failed) throws IOException {

    Map<String, Object> body = new HashMap<>();
    body.put("error", "Please verify the information provided.");
    body.put("message",
            "An unexpected error has occurred and it was not possible to authenticate to the system, please try again later.");
    body.put("message_trace", failed.getMessage());

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setStatus(401);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }


  public String buildPattern() {
    return "/oauth/login";
  }

  public UserModel getParamsUser(HttpServletRequest request, String transactionId) {

    try {
      return new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
    } catch (IOException e) {
      logger.error(
              "Unable to authenticate user. because your data was not sent correctly, please validate the information provided.");
      throw new RuntimeException(e.getMessage());
    }
  }

  public String generateToken(Authentication authResult) {
    return "token";
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            null, null, null);
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    context.setAuthentication(authToken);
    SecurityContextHolder.setContext(context);

    filterChain.doFilter(request, response);
  }
}

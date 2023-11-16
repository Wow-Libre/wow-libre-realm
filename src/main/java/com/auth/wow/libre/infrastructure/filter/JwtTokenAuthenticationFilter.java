package com.auth.wow.libre.infrastructure.filter;

import com.auth.wow.libre.domain.model.exception.GenericErrorException;
import com.auth.wow.libre.domain.model.security.UserDetailsServiceCustom;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.jwt.JwtTokenProvider;
import com.auth.wow.libre.domain.ports.in.jwt.JwtPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_USERNAME_JWT;


@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {


  private final JwtPort jwtPort;
  private final UserDetailsServiceCustom userDetailsServiceCustom;

  public JwtTokenAuthenticationFilter(JwtTokenProvider jwtPort, UserDetailsServiceCustom userDetailsServiceCustom) {
    this.jwtPort = jwtPort;
    this.userDetailsServiceCustom = userDetailsServiceCustom;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    GenericResponse<Void> responseBody = new GenericResponse<>();
    final String authHeader = request.getHeader("Authorization");

    try {

      if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }

      RequestWrapper requestWrapper = new RequestWrapper(request);
      ResponseWrapper responseWrapper = new ResponseWrapper(response);

      final String jwt = authHeader.substring(7);
      final String username = jwtPort.extractUsername(jwt);
      requestWrapper.setHeader(HEADER_USERNAME_JWT, username);

      if (StringUtils.isNotEmpty(username)
              && SecurityContextHolder.getContext().getAuthentication() == null) {

        UserDetails userDetails = userDetailsServiceCustom.loadUserByUsername(username);

        if (jwtPort.isTokenValid(jwt, userDetails)) {
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          context.setAuthentication(authToken);
          SecurityContextHolder.setContext(context);
        }

      }

      filterChain.doFilter(requestWrapper, responseWrapper);
      response.setStatus(responseWrapper.getStatus());
      response.setContentType(request.getContentType());
      response.getOutputStream().write(responseWrapper.getByteArray());

    } catch (GenericErrorException e) {
      responseBody.setMessage(e.getMessage());
      responseBody.setTransactionId(e.transactionId);
      response.setStatus(e.httpStatus.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(responseBody));
    }
  }
}

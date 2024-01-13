package com.auth.wow.libre.infrastructure.filter;

import com.auth.wow.libre.domain.model.exception.GenericErrorException;
import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.jwt.JwtTokenProvider;
import com.auth.wow.libre.domain.ports.in.jwt.JwtPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_USERNAME_JWT;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


  private final JwtPort jwtPort;

  public JwtAuthenticationFilter(JwtTokenProvider jwtPort) {
    this.jwtPort = jwtPort;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
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

      if (SecurityContextHolder.getContext().getAuthentication() == null) {


        if (jwtPort.isTokenValid(jwt)) {
          Collection<GrantedAuthority> authority = jwtPort.extractRoles(jwt);

          UsernamePasswordAuthenticationToken authenticationToken =
                  new UsernamePasswordAuthenticationToken(username, null,
                                                          authority);
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
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
    } catch (ExpiredJwtException e) {
      responseBody.setMessage("Invalid JWT, has expired");
      responseBody.setCode(401);
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(responseBody));
    }
  }
}

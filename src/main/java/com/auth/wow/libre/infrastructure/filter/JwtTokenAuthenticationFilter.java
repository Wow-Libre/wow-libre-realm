package com.auth.wow.libre.infrastructure.filter;

import com.auth.wow.libre.domain.model.security.UserDetailsServiceCustom;
import com.auth.wow.libre.domain.model.shared.jwt.JwtTokenProvider;
import com.auth.wow.libre.domain.ports.in.jwt.JwtPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


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

    final String authHeader = request.getHeader("Authorization");

    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt = authHeader.substring(7);
    final String userEmail = jwtPort.extractUsername(jwt);

    if (StringUtils.isNotEmpty(userEmail)
            && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = userDetailsServiceCustom.loadUserByUsername(userEmail);

      if (jwtPort.isTokenValid(jwt, userDetails)) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
      }

    }

    filterChain.doFilter(request, response);
  }
}

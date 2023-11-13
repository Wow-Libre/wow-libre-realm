package com.auth.wow.libre.infrastructure.conf;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JwtProperties {
  private String secretKey = "rzxlszyykpbgqcflzxsqcysyhljt";
  private long validityInMs = 3600000; // 1h
}

package io.lovelacetech.server;

import io.lovelacetech.server.configuration.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LovelaceAssetManagerServerApplication {

  @Bean
  public FilterRegistrationBean jwtFilter() {
    final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new JwtFilter());
    registrationBean.addUrlPatterns("/api/secure/*");

    return registrationBean;
  }

  public static void main(String[] args) {
    SpringApplication.run(LovelaceAssetManagerServerApplication.class, args);
  }
}

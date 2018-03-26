package io.lovelacetech.server;

import io.lovelacetech.server.configuration.JwtFilter;
import io.lovelacetech.server.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
public class LovelaceAssetManagerServerApplication {
  private UserRepository userRepository;

  public LovelaceAssetManagerServerApplication(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Bean
  public FilterRegistrationBean jwtFilter() {
    final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new JwtFilter().setUserRepository(userRepository));
    registrationBean.addUrlPatterns("/api/secure/*");

    return registrationBean;
  }

  public static void main(String[] args) {
    SpringApplication.run(LovelaceAssetManagerServerApplication.class, args);
  }
}

package io.lovelacetech.server.configuration;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridBean {
  @Value("${SENDGRID_API_KEY}")
  private String sendgridApiKey;

  @Bean
  public SendGrid sendGrid() {
    return new SendGrid(sendgridApiKey);
  }
}

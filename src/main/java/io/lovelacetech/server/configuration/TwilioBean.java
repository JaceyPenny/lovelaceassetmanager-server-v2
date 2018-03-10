package io.lovelacetech.server.configuration;

import com.twilio.Twilio;
import io.lovelacetech.server.service.TwilioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioBean {
  @Value("${TWILIO_ACCOUNT_SID}")
  private String TWILIO_ACCOUNT_SID;

  @Value("${TWILIO_AUTH_TOKEN}")
  private String TWILIO_AUTH_TOKEN;

  @Value("${TWILIO_SOURCE_NUMBER}")
  private String TWILIO_SOURCE_NUMBER;

  @Bean
  public TwilioService twilioService() {
    Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
    TwilioService service = new TwilioService();
    service.setSourcePhoneNumber(TWILIO_SOURCE_NUMBER);
    return service;
  }
}

package com.fastcampus.pharmacy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
public class RetryConfig {

//  @Bean
//  public RetryTemplate retryTemplate() {
//    return new RetryTemplate();
//  }

}

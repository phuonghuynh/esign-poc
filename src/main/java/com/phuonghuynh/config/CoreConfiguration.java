package com.phuonghuynh.config;


import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.social.oauth2.OAuth2Template;

@Configuration
@ComponentScan(basePackages = "com.phuonghuynh")
@PropertySources({
  @PropertySource("classpath:application.properties"),
  @PropertySource(value = "classpath:override.properties", ignoreResourceNotFound = true)})
@EnableAspectJAutoProxy
public class CoreConfiguration {

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public OAuth2Template esignOAuth2Template() {
    return new OAuth2Template("C7YL2R4Y3B3KXG", "_FyDcV9W9Iq7p81sDuzQuFCpDItjvMZ4",
      "https://secure.echosign.com/oauth", "https://secure.echosign.com/oauth/token");
  }
}

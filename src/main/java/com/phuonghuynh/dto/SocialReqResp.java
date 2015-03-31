package com.phuonghuynh.dto;

/**
 * Created by phuonghqh on 12/11/14.
 */
public class SocialReqResp {

  private String token;
  private String clientId;
  private String code;
  private String redirectUri;
  private String widgetUrl;
  private String baseUrl;
  private String widgetId;

  public String getWidgetId() {
    return widgetId;
  }

  public void setWidgetId(String widgetId) {
    this.widgetId = widgetId;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getWidgetUrl() {
    return widgetUrl;
  }

  public void setWidgetUrl(String widgetUrl) {
    this.widgetUrl = widgetUrl;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}

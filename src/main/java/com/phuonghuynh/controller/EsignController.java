package com.phuonghuynh.controller;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.phuonghuynh.dto.SocialReqResp;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by phuonghqh on 3/31/15.
 */
@Controller
public class EsignController {

  @Resource
  private OAuth2Template esignOAuth2Template;

  @Value("classpath:create-widget.json")
  private org.springframework.core.io.Resource createWidgetRes;

  @ResponseBody
  @RequestMapping({"/auth/esign"})
  public SocialReqResp authentication(@RequestBody SocialReqResp reqResp) throws UnirestException, IOException {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("code", reqResp.getCode());
    map.add("client_id", reqResp.getClientId());
    map.add("client_secret", "_FyDcV9W9Iq7p81sDuzQuFCpDItjvMZ4");
    map.add("redirect_uri", reqResp.getRedirectUri());
    map.add("grant_type", "authorization_code");

    AccessGrant rs = esignOAuth2Template.exchangeForAccess(reqResp.getCode(), "https://localhost:8080/authentication", map);
    String accessToken = rs.getAccessToken();
    SocialReqResp rsp = new SocialReqResp();
    rsp.setToken(accessToken);
    return rsp;
  }

  @ResponseBody
  @RequestMapping("/url")
  public SocialReqResp getWdUrl(@RequestBody SocialReqResp socialReqResp, HttpSession httpSession) throws UnirestException, IOException {
    JsonNode uriJson = Unirest.get("https://api.echosign.com/api/rest/v3/base_uris")
      .header("Access-Token", socialReqResp.getToken()).header("Content-Type", "application/json").asJson().getBody();
    String baseUrl = uriJson.getObject().getString("api_access_point");
    socialReqResp.setBaseUrl(baseUrl);
    httpSession.setAttribute("socialReqResp", socialReqResp);

    JsonNode wgJson = new JsonNode(new String(Files.readAllBytes(Paths.get(createWidgetRes.getURI()))));
    JsonNode wgCreateJson = Unirest.post(baseUrl + "/api/rest/v3/widgets")
      .header("Access-Token", socialReqResp.getToken()).header("Content-Type", "application/json").header("accept", "application/json")
      .body(wgJson.toString()).asJson().getBody();

    SocialReqResp rs = new SocialReqResp();
    rs.setWidgetUrl(wgCreateJson.getObject().getString("url"));
    socialReqResp.setWidgetId(wgCreateJson.getObject().getString("widgetId"));
    return rs;
  }

  @RequestMapping("/done")
  public void done(@RequestParam("documentKey") String documentKey, HttpSession httpSession, HttpServletResponse httpServletResponse) throws UnirestException, IOException {
    SocialReqResp socialReqResp = (SocialReqResp) httpSession.getAttribute("socialReqResp");
    String csv = Unirest.get(String.format("%s/api/rest/v3/widgets/%s/formData", socialReqResp.getBaseUrl(), socialReqResp.getWidgetId()))
      .header("Accept", "text/csv")
      .header("Access-Token", socialReqResp.getToken())
      .header("Content-Type", "application/json").header("accept", "application/json")
      .asString().getBody();
    System.out.println(csv);
    httpServletResponse.getWriter().write(csv);
  }

  @RequestMapping("/getInfo")
  public void getInfo(HttpSession httpSession, HttpServletResponse httpServletResponse) throws UnirestException, IOException {
    SocialReqResp socialReqResp = (SocialReqResp) httpSession.getAttribute("socialReqResp");
    String csv = Unirest.get(String.format("%s/api/rest/v3/widgets/%s/formData", socialReqResp.getBaseUrl(), socialReqResp.getWidgetId()))
      .header("Accept", "text/csv")
      .header("Access-Token", socialReqResp.getToken())
      .header("Content-Type", "application/json").header("accept", "application/json")
      .asString().getBody();
    System.out.println(csv);
    httpServletResponse.getWriter().write(csv);
  }
}

package com.phuonghuynh.config;


import com.phuonghuynh.service.TinCheckService;
import com.phuonghuynh.ws.TinWebService;
import com.phuonghuynh.ws.model.TinName;
import com.phuonghuynh.ws.model.TinServiceUser;
import com.phuonghuynh.ws.model.ValidateTinNameResult;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.social.oauth2.OAuth2Template;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

  @Bean
  public JaxWsProxyFactoryBean pvsServiceFactory() {
    JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
    jaxWsProxyFactoryBean.setServiceClass(TinWebService.class);
    jaxWsProxyFactoryBean.setAddress("https://www.tincheck.com/pvsws/pvsservice.asmx");
    jaxWsProxyFactoryBean.setInInterceptors(Arrays.asList(new LoggingInInterceptor()));
    jaxWsProxyFactoryBean.setOutInterceptors(Arrays.asList(new LoggingOutInterceptor()));

    Map<String, Object> values = new HashMap<>();
    values.put("pvs", "http://www.TinCheck.com/WebServices/PVSService/");
    values.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
    Map<String, Object> properties = new HashMap<>();
    properties.put("soap.env.ns.map", values);
    properties.put("disable.outputstream.optimization", true);
    jaxWsProxyFactoryBean.setProperties(properties);
    return jaxWsProxyFactoryBean;
  }

  @Bean
  public TinWebService pvsService() {
    return (TinWebService) pvsServiceFactory().create();
  }

  public static void main(String[] args) throws IOException {
//    String json = FileUtils.readFileToString(new File("src/main/resources/abc.json"), "UTF-8");;// Files.toString(new java.io.File("/home/x1/text.log"), Charsets.UTF_8);// StringReader(Main.class.getResourceAsStream("abc.json"));
////    System.out.println(json);
//    Object document = com.jayway.jsonpath.Configuration.defaultConfiguration().jsonProvider()
//      .parse(json);
//    String name = JsonPath.read(document, "$.signers[0].tabs.textTabs[0].value");
//    System.out.println(name);
//
//    System.out.println(JsonPath.read(document, "$...textTabs[?(@.name=='Text 18')].value").toString());
//    String ssn = String.format("%s%s%s",
//      ((JSONArray)JsonPath.read(document, "$.signers[0].tabs.textTabs[?(@.name=='Text 18')].value")).get(0),
//      ((JSONArray)JsonPath.read(document, "$.signers[0].tabs.textTabs[?(@.name=='Text 19')].value")).get(0),
//      ((JSONArray)JsonPath.read(document, "$.signers[0].tabs.textTabs[?(@.name=='Text 20')].value")).get(0));
//    System.out.println(ssn);

//    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CoreConfiguration.class);
    TinWebService ws = new CoreConfiguration().pvsService();
    ValidateTinNameResult validateTinNameResult = ws.validateTinName(
      TinName.TinNameBuilder.tinName().withTin("123456789").withName("Phuong Huynh").build(),
      TinServiceUser.TinServiceUserBuilder.tinServiceUser().withUserLogin("phuong.hqh@gmail.com").withUserPassword("waiting2212").build()
    );
    System.out.println(validateTinNameResult);
//    ServiceStatusResult pvsServiceStatus = ws.serviceStatus(PvsServiceUser.PvsServiceUserBuilder.pvsServiceUser()
//      .withUserLogin("phuong.hqh@gmail.com")
//      .withUserPassword("waiting2212").build());
//    System.out.println(pvsServiceStatus);
  }

}

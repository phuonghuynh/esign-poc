package com.phuonghuynh.config;


import com.google.common.io.Files;
import com.phuonghuynh.ws.TinWebService;
import com.phuonghuynh.ws.model.TinName;
import com.phuonghuynh.ws.model.TinServiceUser;
import com.phuonghuynh.ws.model.ValidateTinNameResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.social.oauth2.OAuth2Template;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

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

    Map<String, Object> values = new HashMap<String, Object>();
    values.put("pvs", "http://www.TinCheck.com/WebServices/PVSService/");
    values.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
    Map<String, Object> properties = new HashMap<String, Object>();
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
    TinWebService ws = new CoreConfiguration().pvsService();
    TinServiceUser wsUser = TinServiceUser.TinServiceUserBuilder.tinServiceUser().withUserLogin("phuonghqh@gmail.com").withUserPassword("waiting2212").build();
    List<Integer> resultOKs = Arrays.asList(1, 6, 7, 8);
    File tinResultFile = new File("tinResult.csv");
    tinResultFile.delete();
    Files.append("Payee,Tax ID (SSN),Response Code,Result\n", tinResultFile, Charset.forName("UTF-8"));

    CSVParser parser = CSVParser.parse(new File("src/main/resources/TIN Payee and Address Validations.csv"),
            Charset.forName("UTF-8"), CSVFormat.EXCEL);
    for (CSVRecord csvRecord : parser) {
      TinName tinName = TinName.TinNameBuilder.tinName().withTin(csvRecord.get(1).replaceAll("-", "")).withName(csvRecord.get(0)).build();
      ValidateTinNameResult result = ws.validateTinName(tinName, wsUser);
      Files.append(String.format("%s,%s,%s,%s\n", csvRecord.get(0).indexOf(",") >= 0 ? String.format("\"%s\"", csvRecord.get(0)) : csvRecord.get(0), csvRecord.get(1),
          result.getTinNameCode(), resultOKs.contains(result.getTinNameCode())),
        tinResultFile, Charset.forName("UTF-8"));
    }

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

//    BufferedReader payeesReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/TIN Payee and Address Validations.csv"))));


//    payeesReader.lines().distinct().map(line -> {
//      String[] items = line.split(",");
//      return TinName.TinNameBuilder.tinName().withTin(items[1].replaceAll("-", "")).withName(items[0]).build();
//    }).forEach(tinName -> {
//      ValidateTinNameResult validateTinNameResult = ws.validateTinName(tinName, wsUser);
//      try {
//        Files.append(String.format("%s,%s,%s,%s\n", tinName.getName(), tinName.getTin(),
//            validateTinNameResult.getTinNameCode(), resultOKs.contains(validateTinNameResult.getTinNameCode())),
//          tinResultFile, StandardCharsets.UTF_8);
//      }
//      catch (IOException e) {
//        e.printStackTrace();
//      }
//    });

//    List<Person> persons = br.lines()
//      .substream(1)
//      .map(mapToPerson)
//      .filter(person -> person.getAge() > 17)
//      .limit(50)
//      .collect(toList());

//    TinWebService ws = new CoreConfiguration().pvsService();
//    ValidateTinNameResult validateTinNameResult = ws.validateTinName(
//      TinName.TinNameBuilder.tinName().withTin("123456789").withName("Phuong Huynh").build(),
//      TinServiceUser.TinServiceUserBuilder.tinServiceUser().withUserLogin("phuonghqh@gmail.com").withUserPassword("waiting2212").build()
//    );
//    System.out.println(validateTinNameResult);
//    ServiceStatusResult pvsServiceStatus = ws.serviceStatus(PvsServiceUser.PvsServiceUserBuilder.pvsServiceUser()
//      .withUserLogin("phuong.hqh@gmail.com")
//      .withUserPassword("waiting2212").build());
//    System.out.println(pvsServiceStatus);
  }

}

package com.phuonghuynh.config.web;

import com.phuonghuynh.config.CoreConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletRegistration;

/**
 * Created by phuonghqh on 3/11/15.
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  protected Class<?>[] getRootConfigClasses() {
    return null;
  }

  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[]{
      CoreConfiguration.class,
      WebConfiguration.class
    };
  }

  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

  protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    registration.setInitParameter("dispatchOptionsRequest", "true");
  }
}

package com.phuonghuynh;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.webapp.WebAppContext;

import java.util.Collections;

/**
 * Created by phuonghqh on 3/19/15.
 */
public class HerokuMain {

  public static void main(String[] args) throws Exception {
    String webappDir = "src/main/webapp/";

    String webPort = System.getenv("PORT");
    if (webPort == null || webPort.isEmpty()) {
      webPort = "8080";
    }

    Server server = new Server(Integer.valueOf(webPort));

    LoginService loginService = new HashLoginService("MyRealm",
      "src/main/resources/heroku.properties");
    server.addBean(loginService);

    ConstraintSecurityHandler security = new ConstraintSecurityHandler();
    server.setHandler(security);

    Constraint constraint = new Constraint();
    constraint.setName("auth");
    constraint.setAuthenticate(true);
    constraint.setRoles(new String[]{"user"});

    ConstraintMapping mapping = new ConstraintMapping();
    mapping.setPathSpec("/*");
    mapping.setConstraint(constraint);

    security.setConstraintMappings(Collections.singletonList(mapping));
    security.setAuthenticator(new BasicAuthenticator());
    security.setLoginService(loginService);

    WebAppContext root = new WebAppContext();
    root.setContextPath("/");
    root.setResourceBase(webappDir);
    root.setParentLoaderPriority(true);
    security.setHandler(root);

    server.setHandler(security);

    server.start();
    server.join();
  }
}

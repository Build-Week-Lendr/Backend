package com.zero5nelsonm.lendr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class LendrApplication {

    private static final Logger logger = LoggerFactory.getLogger(com.zero5nelsonm.lendr.LendrApplication.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(com.zero5nelsonm.lendr.LendrApplication.class, args);

        DispatcherServlet dispatcherServlet = (DispatcherServlet) ctx.getBean("dispatcherServlet");
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    }
}

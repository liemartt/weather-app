package com.liemartt.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebListener
public class ThymeleafUtil implements ServletContextListener {
    private static final TemplateEngine engine = templateEngine();
    
    public static WebContext getWebContext(HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) {
        IWebExchange iWebExchange = JakartaServletWebApplication.buildApplication(servletContext)
                .buildExchange(req, resp);
        return new WebContext(iWebExchange);
    }
    
    public static void process(WebContext context, String templateName, HttpServletResponse resp) throws IOException {
        engine.process(templateName, context, resp.getWriter());
    }
    
    private static TemplateEngine templateEngine() {
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }
    
    private static ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    
    
}

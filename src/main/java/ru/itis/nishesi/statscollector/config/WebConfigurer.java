package ru.itis.nishesi.statscollector.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

public class WebConfigurer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        Dynamic registration = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        registration.addMapping("/");
        registration.setLoadOnStartup(1);

        servletContext
                .addFilter(
                        "securityFilter",
                        new DelegatingFilterProxy("springSecurityFilterChain"))
                .addMappingForUrlPatterns(null, false, "/*");
    }
}

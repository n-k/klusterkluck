package com.github.nk.klusterfuck.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@Order(value = 0)
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/**")
                .addResourceLocations(
                        "file:./admin/src/main/ui/dist/"
                )
                .resourceChain(false);
//        registry.addResourceHandler("/**")
//                .addResourceLocations(
//                        "classpath:static/"
//                )
//                .resourceChain(false);
    }
}

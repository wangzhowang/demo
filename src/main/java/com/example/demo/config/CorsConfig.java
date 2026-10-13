package com.example.demo.config;

import com.example.demo.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor ;

    // ⭐ 构造器注入 TokenInterceptor
    public CorsConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/**")          // 拦截所有 /api/** 的请求
                .excludePathPatterns(                //允许通行接口，提供测试
                        "/api/login",
                        "/api/product/**",
                        "/api/getalluserdata",
                        "/apitest/**"
                );  // 登录接口不拦截
    }
}

package com.abugrov.realtask.config;

import com.cloudinary.Cloudinary;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;
    @Value("${cloudinary.cloud_name}")
    private String cloud_name;
    @Value("${cloudinary.api_key}")
    private String api_key;
    @Value("${cloudinary.api_secret}")
    private String api_secret;
    @Value("${hostname}")
    private String host;
    @Value("${fixie.user}")
    private String fixieUser;
    @Value("${fixie.password}")
    private String fixiePassword;
    @Value("${fixie.host}")
    private String fixieHost;

    @Bean
    public RestTemplate getRestTemplate() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        Authenticator proxyAuthenticator = (route, response) -> {
            String credential = Credentials.basic(fixieUser, fixiePassword);
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credential)
                    .build();
        };

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(fixieHost, 80));
        clientBuilder.proxy(proxy).proxyAuthenticator(proxyAuthenticator);

        return new RestTemplate(new OkHttp3ClientHttpRequestFactory(clientBuilder.build()));
    }

    @Bean
    public Cloudinary getCloudinary() {
        return new Cloudinary("cloudinary://" + api_key + ":" + api_secret + "@" + cloud_name);
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations(uploadPath);
    }
}
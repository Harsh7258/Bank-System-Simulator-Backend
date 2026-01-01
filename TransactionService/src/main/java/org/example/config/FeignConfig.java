package org.example.config;

import feign.codec.ErrorDecoder;
import org.example.feign.AccountFeignErrors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder(){
        return new AccountFeignErrors();
    }
}

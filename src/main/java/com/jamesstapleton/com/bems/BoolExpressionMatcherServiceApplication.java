package com.jamesstapleton.com.bems;

import com.jamesstapleton.com.bems.config.BemsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationPropertiesScan(basePackageClasses = BemsProperties.class)
@EnableConfigurationProperties(BemsProperties.class)
@SpringBootApplication(exclude = HypermediaAutoConfiguration.class)
public class BoolExpressionMatcherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoolExpressionMatcherServiceApplication.class, args);
    }

}

package com.jamesstapleton.com.bems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;

@SpringBootApplication(exclude = HypermediaAutoConfiguration.class)
public class BoolExpressionMatcherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoolExpressionMatcherServiceApplication.class, args);
    }

}

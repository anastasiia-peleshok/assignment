package com.anastasiia.assignment.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties("assignment")
public class ApplicationProperties {
    private int majorityAge;
}
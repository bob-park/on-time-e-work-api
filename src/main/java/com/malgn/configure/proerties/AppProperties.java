package com.malgn.configure.proerties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("e-work")
public record AppProperties(@NestedConfigurationProperty GoogleProperties google) {

}

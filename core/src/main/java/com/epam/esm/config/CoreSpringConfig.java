package com.epam.esm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@EntityScan( basePackages = {"com.epam.esm.entity"} )
@Configuration
public class CoreSpringConfig {

}

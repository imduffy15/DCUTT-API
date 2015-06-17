package com.dcutt.api.configuration;

import net.fortuna.ical4j.data.CalendarBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CalenderBuilderConfiguration {

    @Bean
    public CalendarBuilder calendarBuilder() {
        return new CalendarBuilder();
    }

}

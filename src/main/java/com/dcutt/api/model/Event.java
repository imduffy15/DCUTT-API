package com.dcutt.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Comparable<Event> {
    private String courseCode;
    private Set<String> moduleCodes;
    private String moduleDescription;
    private Long startTime;
    private Long endTime;
    private Set<String> locations;
    private Type type;

    @Override
    public int compareTo(Event o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }
}
package com.dcutt.api.web.controller;

import com.dcutt.api.service.EventService;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.Set;

@RestController
@RequestMapping("/timetable/{course_code}")
public class TimetableController {

    private EventService eventService;

    @Value("#{'${dcutt.course.codes}'.split(',')}")
    private Set<String> courseCodes;

    @Autowired
    public TimetableController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<?> get(
            @PathVariable("course_code") String courseCode
    ) throws ParseException, ParserException, IOException {
        courseCode = courseCode.toUpperCase();
        if (!courseCodes.contains(courseCode)) throw new IllegalArgumentException(courseCode);
        return new ResponseEntity<>(eventService.get(courseCode), HttpStatus.OK);
    }
}

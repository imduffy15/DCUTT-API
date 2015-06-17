package com.dcutt.api.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/coursecode")
public class CourseCodeController {


    @Value("#{'${dcutt.course.codes}'.split(',')}")
    private Set<String> courseCodes;

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(courseCodes, HttpStatus.OK);
    }

}

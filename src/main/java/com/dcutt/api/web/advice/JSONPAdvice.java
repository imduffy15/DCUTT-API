package com.dcutt.api.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

@ControllerAdvice
public class JSONPAdvice extends AbstractJsonpResponseBodyAdvice {

    JSONPAdvice() {
        super("callback");
    }

}
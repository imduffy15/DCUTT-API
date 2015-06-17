package api.web;

import api.service.EventService;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/timetable/{course_code}")
public class TimetableController {

    private EventService eventService;

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
        return new ResponseEntity<>(eventService.get(courseCode), HttpStatus.OK);
    }
}

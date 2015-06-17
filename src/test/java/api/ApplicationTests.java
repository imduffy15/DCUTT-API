package api;

import api.model.Event;
import api.service.EventService;
import net.fortuna.ical4j.data.ParserException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests {

    @Autowired
    private EventService eventService;

    @Test
    public void testAddAndGetKey() throws ParseException, ParserException, IOException {
//        Map<Long, Event> foundEvents = eventService.get("CASE4");
//        System.out.println(foundEvents);
    }
}

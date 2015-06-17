package api.service;

import api.model.Event;
import com.sun.deploy.util.OrderedHashSet;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class EventService {

    private DcuTimetableService dcuTimetableService;
    private RedisTemplate<String, Map<Long, SortedSet<Event>>> template;

    @Value("${redis.entries.ttl}")
    private Integer redisEntriesTTL;

    @Autowired
    public EventService(DcuTimetableService dcuTimetableService, RedisTemplate<String, Map<Long, SortedSet<Event>>> template) {
        this.dcuTimetableService = dcuTimetableService;
        this.template = template;
    }

    @Transactional(readOnly = true)
    public Map<Long, SortedSet<Event>> get(String key) throws ParseException, ParserException, IOException {
        key = key.toUpperCase();
        Map<Long, SortedSet<Event>> events = template.opsForValue().get(key);
        if (events == null) {
            events = dcuTimetableService.getTimetable(key);
            set(key, events);
        }
        return events;
    }

    @Transactional
    public void set(String key, Map<Long, SortedSet<Event>> value) {
        key = key.toUpperCase();
        template.opsForValue().set(key, value);
        template.expire(key, redisEntriesTTL, TimeUnit.SECONDS);
    }

}

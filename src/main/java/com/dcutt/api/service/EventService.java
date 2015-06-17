package com.dcutt.api.service;

import com.dcutt.api.model.Event;
import net.fortuna.ical4j.data.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

@Service
public class EventService {

    private DcuTimetableService dcuTimetableService;
    private RedisTemplate<String, SortedMap<Long, SortedSet<Event>>> template;

    @Value("${redis.entry.ttl}")
    private Integer redisEntryTTL;

    @Autowired
    public EventService(DcuTimetableService dcuTimetableService, RedisTemplate<String, SortedMap<Long, SortedSet<Event>>> template) {
        this.dcuTimetableService = dcuTimetableService;
        this.template = template;
    }

    @Transactional(readOnly = true)
    public SortedMap<Long, SortedSet<Event>> get(String key) throws ParseException, ParserException, IOException {
        SortedMap<Long, SortedSet<Event>> events = template.opsForValue().get(key);
        if (events == null) {
            events = dcuTimetableService.getTimetable(key);
            set(key, events);
        }
        return events;
    }

    @Transactional
    private void set(String key, SortedMap<Long, SortedSet<Event>> value) {
        template.opsForValue().set(key, value);
        template.expire(key, redisEntryTTL, TimeUnit.SECONDS);
    }

}

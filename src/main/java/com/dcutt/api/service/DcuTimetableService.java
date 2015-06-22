package com.dcutt.api.service;

import com.dcutt.api.model.Event;
import com.dcutt.api.model.Type;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.PropertyList;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DcuTimetableService {

    private static final String COMPONENT_TYPE = "VEVENT";
    private static final String LOCATION_KEY = "LOCATION";
    private static final String SUMMARY_KEY = "SUMMARY";
    private static final String START_DATE_KEY = "DTSTART";
    private static final String END_DATE_KEY = "DTEND";
    private static final String MODULE_DESCRIPTION_KEY = "DESCRIPTION";
    private static final String BASE_URL = "http://localhost:9000/";

    private static final String LOCATION_SEPARATOR = ":";
    private static final String MODULE_SEPARATOR = "/";

    private CalendarBuilder calendarBuilder;
    private RestTemplate restTemplate;

    @Autowired
    public DcuTimetableService(CalendarBuilder calendarBuilder, RestTemplate restTemplate) {
        this.calendarBuilder = calendarBuilder;
        this.restTemplate = restTemplate;
    }

    private static Set<String> getModuleCode(String data) {
        data = data.replaceAll("\\s+", "");
        Pattern pattern = Pattern.compile("^[^\\[\\(]*");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return new HashSet<>(Arrays.asList(matcher.group(0).split(MODULE_SEPARATOR)));
        }
        throw new IllegalArgumentException();
    }

    private static Type getEventType(String data) {
        data = data.replaceAll("\\s+", "");
        Pattern pattern = Pattern.compile("^[^\\[\\(]*.*([lptsLPTS])[0-9].*");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return Type.getInstanceFromTypeValue(matcher.group(1).charAt(0));
        }
        return Type.UNKNOWN;
    }

    @Cacheable("timetables")
    public synchronized SortedMap<Long, SortedSet<Event>> getTimetable(String courseCode) throws IOException, ParserException, ParseException {
        String timetableData = getTimetableData(courseCode);
        Calendar calendar = buildCalendar(timetableData);
        return generateEvents(calendar, courseCode);
    }

    private String getTimetableData(String courseCode) {
        String url = BASE_URL + courseCode;
        return restTemplate.getForObject(url, String.class);
    }

    private Calendar buildCalendar(String data) throws IOException, ParserException {
        return calendarBuilder.build(new StringReader(data));
    }

    private SortedMap<Long, SortedSet<Event>> generateEvents(Calendar calendar, String courseCode) throws ParseException {
        SortedMap<Long, SortedSet<Event>> results = new TreeMap<>();

        ComponentList componentList = calendar.getComponents(COMPONENT_TYPE);

        for (Object obj : componentList) {
            PropertyList propertyList = ((net.fortuna.ical4j.model.Component) obj).getProperties();
            String moduleDescription = propertyList.getProperty(MODULE_DESCRIPTION_KEY) != null ? StringEscapeUtils.unescapeHtml(propertyList.getProperty(MODULE_DESCRIPTION_KEY).getValue()) : "";
            String summaryData = propertyList.getProperty(SUMMARY_KEY).getValue();
            Set<String> moduleCodes = getModuleCode(summaryData);
            Type eventType = getEventType(summaryData);
            Set<String> locations = new HashSet<>(Arrays.asList(propertyList.getProperty(LOCATION_KEY).getValue().split(LOCATION_SEPARATOR)));

            DateTime startDateTime = new DateTime(propertyList.getProperty(START_DATE_KEY).getValue());
            DateTime endDateTime = new DateTime(propertyList.getProperty(END_DATE_KEY).getValue());

            Long key = new Date(startDateTime.getYear(), startDateTime.getMonth(), startDateTime.getDate()).getTime();

            SortedSet<Event> sortedEvents = results.containsKey(key) ? results.get(key) : new TreeSet<>();

            sortedEvents.add(new Event(
                    courseCode,
                    moduleCodes,
                    moduleDescription,
                    startDateTime.getTime(),
                    endDateTime.getTime(),
                    locations,
                    eventType
            ));

            results.put(key, sortedEvents);

        }
        return results;
    }

}

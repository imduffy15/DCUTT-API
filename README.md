# DCUTT API

## Description

Wrapper around DCUs iCal timetables in order to make them easier to use.

## How it works

DCU exposes all student timetables at `http://ical.dcu.ie/gCal/default.aspx?PoSiCalOra&p1=<coursecode>` where `coursecode` is something like CASE4, AP2, etc.

DCU exposes the timetables as iCal files with each module expressed something like the following:

```
BEGIN:VEVENT
DTSTART;TZID=Europe/London:20141113T140000
DTEND;TZID=Europe/London:20141113T150000
DTSTAMP:20150610T040418
UID:09136F2C85784110221EA9F417D581422014-11-13T14:00:00+00:00
CLASS:PUBLIC
CREATED:20150610T040418
LAST-MODIFIED:20150610T040418
SEQUENCE:430
STATUS:CONFIRMED
SUMMARY:CA547[1] L1/01
DESCRIPTION:Cryptography &amp; Security Protocol
LOCATION:C165:
TRANSP:OPAQUE
END:VEVENT
```

DCUTT API transforms these into grouped JSON objects something like the following:

```
"1411340400000" : [ {
    "courseCode" : "CASE4",
    "moduleCode" : "CA427",
    "moduleDescription" : "OPERATIONS RESEARCH",
    "startTime" : 1411380000000,
    "endTime" : 1411383600000,
    "locations" : [ "Q158" ],
    "type" : "LECTURE"
  }, {
    "courseCode" : "CASE4",
    "moduleCode" : "CA448",
    "moduleDescription" : "COMPILER CONSTRUCTION 1",
    "startTime" : 1411383600000,
    "endTime" : 1411387200000,
    "locations" : [ "C114" ],
    "type" : "LECTURE"
  }, {
    "courseCode" : "CASE4",
    "moduleCode" : "Careers Service",
    "moduleDescription" : "",
    "startTime" : 1411387200000,
    "endTime" : 1411390800000,
    "locations" : [ "XG21" ],
    "type" : "SEMINAR"
  }, {
    "courseCode" : "CASE4",
    "moduleCode" : "CA448",
    "moduleDescription" : "COMPILER CONSTRUCTION I",
    "startTime" : 1411390800000,
    "endTime" : 1411394400000,
    "locations" : [ "C104" ],
    "type" : "LECTURE"
  }, {
    "courseCode" : "CASE4",
    "moduleCode" : "CA447",
    "moduleDescription" : "Software Process and  Improvement",
    "startTime" : 1411398000000,
    "endTime" : 1411405200000,
    "locations" : [ "Q120" ],
    "type" : "LECTURE"
  } ]
```

and caches it within Redis for an operator defined length of time.

## Usage

The timetables are exposed at `/timetable/course_code`.

e.g. `http://localhost:8080/timetable/case4`
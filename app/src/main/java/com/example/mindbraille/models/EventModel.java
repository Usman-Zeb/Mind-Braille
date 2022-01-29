package com.example.mindbraille.models;

import com.microsoft.graph.models.extensions.DateTimeTimeZone;
import com.microsoft.graph.models.extensions.Event;
import com.microsoft.graph.models.extensions.Location;
import com.microsoft.identity.common.internal.telemetry.TelemetryEventStrings;

import java.io.Serializable;

public class EventModel {

    private DateTimeTimeZone start;
    private DateTimeTimeZone end;
    private String subject;
    private Location location;

    public EventModel(DateTimeTimeZone start, DateTimeTimeZone end, String subject, Location location) {
        this.start = start;
        this.end = end;
        this.subject = subject;
        this.location = location;
    }

    public DateTimeTimeZone getStart() {
        return start;
    }

    public void setStart(DateTimeTimeZone start) {
        this.start = start;
    }

    public DateTimeTimeZone getEnd() {
        return end;
    }

    public void setEnd(DateTimeTimeZone end) {
        this.end = end;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

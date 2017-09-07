package com.example.jabs.customcalendar.Property;

/**
 * Created by nct121 on 11/20/2015.
 */
public class Events {
    int id, AlarmID;
    public String eventsName, eventFromDate, eventFromTime, eventToDate, eventToTime, eventLocation, eventDiscription;


    public Events() {

    }


    public Events(int AlarmId, String name, String fromdate, String fromtime, String todate, String totime, String location, String description, String reminders) {
        this.AlarmID = AlarmId;
        this.eventsName = name;
        this.eventFromDate = fromdate;
        this.eventFromTime = fromtime;
        this.eventToDate = todate;
        this.eventToTime = totime;
        this.eventLocation = location;
        this.eventDiscription = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventsName() {
        return eventsName;
    }

    public void setEventsName(String eventsName) {
        this.eventsName = eventsName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDiscription() {
        return eventDiscription;
    }

    public void setEventDiscription(String eventDiscription) {
        this.eventDiscription = eventDiscription;
    }

    public String getEventFromDate() {
        return eventFromDate;
    }

    public void setEventFromDate(String eventFromDate) {
        this.eventFromDate = eventFromDate;
    }

    public String getEventFromTime() {
        return eventFromTime;
    }

    public void setEventFromTime(String eventFromTime) {
        this.eventFromTime = eventFromTime;
    }

    public String getEventToDate() {
        return eventToDate;
    }

    public void setEventToDate(String eventToDate) {
        this.eventToDate = eventToDate;
    }

    public String getEventToTime() {
        return eventToTime;
    }

    public void setEventToTime(String eventToTime) {
        this.eventToTime = eventToTime;
    }
}

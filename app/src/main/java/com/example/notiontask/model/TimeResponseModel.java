package com.example.notiontask.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeResponseModel {
    @JsonProperty("abbreviation")
    private String abbreviation;

    @JsonProperty("client_ip")
    private String clientIp;

    @JsonProperty("datetime")
    private String dateTime;

    @JsonProperty("day_of_week")
    private int dayOfWeek;

    @JsonProperty("day_of_year")
    private int dayOfYear;

    @JsonProperty("dst")
    private boolean dst;

    @JsonProperty("dst_from")
    private Object dstFrom;

    @JsonProperty("dst_offset")
    private int dstOffset;

    @JsonProperty("dst_until")
    private Object dstUntil;

    @JsonProperty("raw_offset")
    private int rawOffset;

    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("unixtime")
    private long unixtime;

    @JsonProperty("utc_datetime")
    private String utcDateTime;

    @JsonProperty("utc_offset")
    private String utcOffset;

    @JsonProperty("week_number")
    private int weekNumber;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public boolean isDst() {
        return dst;
    }

    public void setDst(boolean dst) {
        this.dst = dst;
    }

    public Object getDstFrom() {
        return dstFrom;
    }

    public void setDstFrom(Object dstFrom) {
        this.dstFrom = dstFrom;
    }

    public int getDstOffset() {
        return dstOffset;
    }

    public void setDstOffset(int dstOffset) {
        this.dstOffset = dstOffset;
    }

    public Object getDstUntil() {
        return dstUntil;
    }

    public void setDstUntil(Object dstUntil) {
        this.dstUntil = dstUntil;
    }

    public int getRawOffset() {
        return rawOffset;
    }

    public void setRawOffset(int rawOffset) {
        this.rawOffset = rawOffset;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public long getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(long unixtime) {
        this.unixtime = unixtime;
    }

    public String getUtcDateTime() {
        return utcDateTime;
    }

    public void setUtcDateTime(String utcDateTime) {
        this.utcDateTime = utcDateTime;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getFormattedTime() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(dateTime);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}

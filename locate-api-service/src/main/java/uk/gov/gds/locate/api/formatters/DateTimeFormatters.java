package uk.gov.gds.locate.api.formatters;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.util.Locale;

public abstract class DateTimeFormatters {
//    public final static String internalDateFormatRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
//    public final static DateTimeFormatter internalDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(Locale.UK);
//
//    public final static DateTimeFormatter internalDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.UK);

    public final static DateFormat internalDateFormatter = DateFormat.getDateInstance();

}


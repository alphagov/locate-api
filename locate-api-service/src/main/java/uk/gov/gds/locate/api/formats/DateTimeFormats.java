package uk.gov.gds.locate.api.formats;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

public abstract class DateTimeFormats {
    public final static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(Locale.UK);
}

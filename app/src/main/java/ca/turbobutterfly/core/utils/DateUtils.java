package ca.turbobutterfly.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils
{
    private static DateFormat _iso8601Format = null;
    private static DateFormat _shortDateFormat = null;
    private static DateFormat _longDateFormat = null;
    private static DateFormat _shortTimeFormat = null;
    private static DateFormat _longTimeFormat = null;
    private static DateFormat _shortTimeSpanFormat = null;
    private static DateFormat _longTimeSpanFormat = null;

    public static DateFormat ISO8601Format()
    {
        if (_iso8601Format == null)
        {
            _iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        }
        return _iso8601Format;
    }

    public static DateFormat ShortDateFormat()
    {
        if (_shortDateFormat == null)
        {
            _shortDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        return _shortDateFormat;
    }

    public static DateFormat LongDateFormat()
    {
        if (_longDateFormat == null)
        {
            _longDateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL);
        }
        return _longDateFormat;
    }

    public static DateFormat ShortTimeFormat()
    {
        if (_shortTimeFormat == null)
        {
            _shortTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        return _shortTimeFormat;
    }

    public static DateFormat LongTimeFormat()
    {
        if (_longTimeFormat == null)
        {
            _longTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        }
        return _longTimeFormat;
    }

    public static DateFormat ShortTimeSpanFormat()
    {
        if (_shortTimeSpanFormat == null)
        {
            _shortTimeSpanFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            _shortTimeSpanFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return _shortTimeSpanFormat;
    }

    public static DateFormat LongTimeSpanFormat()
    {
        if (_longTimeSpanFormat == null)
        {
            _longTimeSpanFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH);
            _longTimeSpanFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return _longTimeSpanFormat;
    }

    public static String ISO8601(Date date)
    {
        return ISO8601Format().format(date);
    }

    public static Date ISO8601(String iso8601Date, Date defaultValue)
    {
        if (iso8601Date == null)
        {
            return defaultValue;
        }
        try
        {
            return ISO8601Format().parse(iso8601Date);
        }
        catch (ParseException e)
        {
            return defaultValue;
        }
    }

    public static String ShortDate(Date date)
    {
        return ShortDateFormat().format(date);
    }

    public static Date ShortDate(String value, Date defaultDate)
    {
        if (value == null)
        {
            return defaultDate;
        }
        try
        {
            return ShortDateFormat().parse(value);
        }
        catch (ParseException ex)
        {
            return defaultDate;
        }
    }

    public static String LongDate(Date date)
    {
        return LongDateFormat().format(date);
    }

    public static String ShortTime(Date date)
    {
        return ShortTimeFormat().format(date);
    }

    public static String LongTime(Date date)
    {
        return LongTimeFormat().format(date);
    }

    public static String ShortTimeSpan(Date date)
    {
        return ShortTimeSpanFormat().format(date);
    }

    public static String LongTimeSpan(Date date)
    {
        return LongTimeSpanFormat().format(date);
    }

    public static boolean equals(Date a, Date b)
    {
        return (a == null) ? (b == null) : a.equals(b);
    }
}

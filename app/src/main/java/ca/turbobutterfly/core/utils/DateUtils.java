package ca.turbobutterfly.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils
{
    private static SimpleDateFormat _iso8601Format = null;
    private static SimpleDateFormat _shortDateFormat = null;
    private static SimpleDateFormat _longDateFormat = null;
    private static SimpleDateFormat _shortTimeFormat = null;
    private static SimpleDateFormat _longTimeFormat = null;
    private static SimpleDateFormat _shortTimeSpanFormat = null;
    private static SimpleDateFormat _longTimeSpanFormat = null;

    public static SimpleDateFormat ISO8601Format()
    {
        if (_iso8601Format == null)
        {
            _iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        }
        return _iso8601Format;
    }

    public static SimpleDateFormat ShortDateFormat()
    {
        if (_shortDateFormat == null)
        {
            _shortDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        return _shortDateFormat;
    }

    public static SimpleDateFormat LongDateFormat()
    {
        if (_longDateFormat == null)
        {
            _longDateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());

//            final String format = Settings.System.getString(getContentResolver(), Settings.System.DATE_FORMAT);
//
//            if (TextUtils.isEmpty(format))
//            {
//                _longDateFormat = android.text.format.DateFormat.getMediumDateFormat(getApplicationContext());
//            }
//            else
//            {
//                _longDateFormat = new SimpleDateFormat(format);
//            }
        }
        return _longDateFormat;
    }

    public static SimpleDateFormat ShortTimeFormat()
    {
        if (_shortTimeFormat == null)
        {
            _shortTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        return _shortTimeFormat;
    }

    public static SimpleDateFormat LongTimeFormat()
    {
        if (_longTimeFormat == null)
        {
            _longTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        }
        return _longTimeFormat;
    }

    public static SimpleDateFormat ShortTimeSpanFormat()
    {
        if (_shortTimeSpanFormat == null)
        {
            _shortTimeSpanFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            _shortTimeSpanFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return _shortTimeSpanFormat;
    }

    public static SimpleDateFormat LongTimeSpanFormat()
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

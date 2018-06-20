package ca.turbobutterfly.wachadoin.android.data;

import android.content.ContentValues;

import ca.turbobutterfly.core.utils.IAdapter;
import ca.turbobutterfly.wachadoin.core.data.IDataLogEntry;

class ContentValuesLogEntry implements IDataLogEntry, IAdapter
{
    private ContentValues _values;

    public ContentValuesLogEntry(ContentValues values)
    {
        _values = values;
    }

    public Object Adaptee()
    {
        return _values;
    }

    @Override
    public String StartTime()
    {
        return _values.getAsString("StartTime");
    }

    @Override
    public void StartTime(String startTime)
    {
        _values.put("StartTime", startTime);
    }

    @Override
    public String EndTime()
    {
        return _values.getAsString("EndTime");
    }

    @Override
    public void EndTime(String endTime)
    {
        _values.put("EndTime", endTime);
    }

    @Override
    public String LogText()
    {
        return _values.getAsString("LogText");
    }

    @Override
    public void LogText(String logText)
    {
        _values.put("LogText", logText);
    }

    @Override
    public String FieldName()
    {
        return _values.getAsString("FieldName");
    }

    @Override
    public void FieldName(String fieldName)
    {
        _values.put("FieldName", fieldName);
    }

    public static ContentValues ConvertToContentValues(IDataLogEntry logEntry)
    {
        if (logEntry instanceof IAdapter)
        {
            //  logEntry is wrapping a ContentValues instance. unwrap it.
            IAdapter adapter = (IAdapter) logEntry;
            return (ContentValues) adapter.Adaptee();
        }

        //  logEntry is not wrapping a ContentValues instance.
        //  Create one, and wrap it temporarily to set the values.
        ContentValues values = new ContentValues();
        IDataLogEntry newLogEntry = new ContentValuesLogEntry(values);

        String value = logEntry.StartTime();
        if (value != null)
        {
            newLogEntry.StartTime(value);
        }

        value = logEntry.EndTime();
        if (value != null)
        {
            newLogEntry.EndTime(value);
        }

        value = logEntry.LogText();
        if (value != null)
        {
            newLogEntry.LogText(value);
        }

        value = logEntry.FieldName();
        if (value != null)
        {
            newLogEntry.FieldName(value);
        }

        return values;
    }
}

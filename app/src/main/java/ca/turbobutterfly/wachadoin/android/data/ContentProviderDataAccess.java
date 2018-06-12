package ca.turbobutterfly.wachadoin.android.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

import ca.turbobutterfly.android.data.DataCursorReader;
import ca.turbobutterfly.core.utils.DateUtils;
import ca.turbobutterfly.wachadoin.core.data.IDataAccess;
import ca.turbobutterfly.wachadoin.core.data.IDataLogEntry;
import ca.turbobutterfly.core.data.IDataReader;

public class ContentProviderDataAccess implements IDataAccess
{
    //  Fields -------------------------------------------------------------------------------------

    private final ContentResolver _contentResolver;

    //  Constructors -------------------------------------------------------------------------------

    public ContentProviderDataAccess(ContentResolver contentResolver)
    {
        _contentResolver = contentResolver;
    }

    //  Methods ------------------------------------------------------------------------------------

    @Override
    public long SaveLogEntry(IDataLogEntry logEntry)
    {
        ContentValues values = ContentValuesLogEntry.ConvertToContentValues(logEntry);

        String URL = Constants.PROVIDER_URL + "/log";
        Uri uri = Uri.parse(URL);

        Uri insertedUri = _contentResolver.insert(uri, values);
        if (insertedUri == null)
        {
            return 0;
        }
        String seg = insertedUri.getPathSegments().get(1);
        try
        {
            return Integer.parseInt(seg);
        }
        catch (Exception ex)
        {
            return 0;
        }
    }

    @Override
    public IDataReader GetLogSummary()
    {
        String URL = Constants.PROVIDER_URL + "/logSummary";
        Uri uri = Uri.parse(URL);

        Cursor cursor = _contentResolver.query(uri,
                null,
                null,
                null,
                null);

        return new DataCursorReader(cursor);
    }

    @Override
    public IDataReader GetLogDetails(Date rangeStart, Date rangeEnd)
    {
        String URL = Constants.PROVIDER_URL + "/log";
        Uri uri = Uri.parse(URL);

        String startTime = DateUtils.ISO8601(rangeStart);
        String endTime = DateUtils.ISO8601(rangeEnd);

        Cursor cursor = _contentResolver.query(uri,
                null,
                null,
                new String[] {startTime, endTime},
                null);

        return new DataCursorReader(cursor);
    }
}

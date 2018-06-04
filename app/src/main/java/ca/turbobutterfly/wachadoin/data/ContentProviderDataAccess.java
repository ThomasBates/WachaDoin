package ca.turbobutterfly.wachadoin.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

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
    public long SaveLog(ContentValues values)
    {
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
    public Cursor GetLogSummary()
    {
        String URL = Constants.PROVIDER_URL + "/logSummary";
        Uri uri = Uri.parse(URL);

        return _contentResolver.query(uri,
                null,
                null,
                null,
                null);
    }

    @Override
    public Cursor GetLogDetails()
    {
        String URL = Constants.PROVIDER_URL + "/log";
        Uri uri = Uri.parse(URL);

        return _contentResolver.query(uri,
                null,
                null,
                null,
                null);
    }
}

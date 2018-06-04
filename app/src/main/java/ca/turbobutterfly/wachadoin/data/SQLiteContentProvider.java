package ca.turbobutterfly.wachadoin.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SQLiteContentProvider extends ContentProvider
{
    private IDataAccess _dataAccess;

    static final int LOG = 1;
    static final int LOG_SUMMARY = 2;

    static final UriMatcher uriMatcher;
    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constants.PROVIDER_NAME, "log", LOG);
        uriMatcher.addURI(Constants.PROVIDER_NAME, "logSummary", LOG_SUMMARY);
    }

    @Override
    public boolean onCreate()
    {
        _dataAccess = new SQLiteDataAccess(getContext());

        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        switch (uriMatcher.match(uri))
        {
            case LOG:
                return InsertNewLogRecord(uri, values);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder)
    {
        switch (uriMatcher.match(uri))
        {
            case LOG:
                return GetLogDetails(uri);
            case LOG_SUMMARY:
                return GetLogSummary(uri);
            //case SETTING:
                //return GetSingleSettingRecord(uri);
            //case SETTINGS:
                //return GetAllSettingRecords(uri);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(
            @NonNull Uri uri,
            @Nullable String selection,
            @Nullable String[] selectionArgs)
    {
        int count = 0;
//        switch (uriMatcher.match(uri))
//        {
//            case LOG:
//                count = _database.delete(LOG_TABLE_NAME, selection, selectionArgs);
//                break;
//            case SETTINGS:
//                count = _database.delete(SETTINGS_TABLE_NAME, selection, selectionArgs);
//                break;
//            default:
//                throw new IllegalArgumentException("Unsupported URI: " + uri);
//        }

        Context context = getContext();
        if (context != null)
        {
            context.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(
            @NonNull Uri uri,
            @Nullable ContentValues values,
            @Nullable String selection,
            @Nullable String[] selectionArgs)
    {
        int count = 0;
//        switch (uriMatcher.match(uri))
//        {
//            case LOG:
//                count = _database.update(LOG_TABLE_NAME, values, selection, selectionArgs);
//                break;
//
//            case SETTINGS:
//                count = _database.update(SETTINGS_TABLE_NAME, values, selection, selectionArgs);
//                break;
//            default:
//                throw new IllegalArgumentException("Unsupported URI: " + uri);
//        }

        Context context = getContext();
        if (context != null)
        {
            context.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
//        switch (uriMatcher.match(uri))
//        {
//            //  Get all log records
//            case LOG:
//                return "vnd.android.cursor.dir/vnd.example.students";
//
//            //  Get all settings records
//            case SETTINGS:
//                return "vnd.android.cursor.item/vnd.example.students";
//
//            default:
//                throw new IllegalArgumentException("Unsupported URI: " + uri);
//        }
        return "";
    }

    private Uri InsertNewLogRecord(Uri uri, ContentValues values)
    {
        long rowID = _dataAccess.SaveLog(values);

        if (rowID > 0)
        {
            Uri insertedUri = ContentUris.withAppendedId(uri, rowID);
            Context context = getContext();
            if (context != null)
            {
                context.getContentResolver().notifyChange(insertedUri, null);
                return insertedUri;
            }
        }

        return null;
    }

    private Cursor GetLogDetails(Uri uri)
    {
        Cursor cursor = _dataAccess.GetLogDetails();

        //  register to watch a content URI for changes
        Context context = getContext();
        if (context != null)
        {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    private Cursor GetLogSummary(Uri uri)
    {
        Cursor cursor = _dataAccess.GetLogSummary();

        //  register to watch a content URI for changes
        Context context = getContext();
        if (context != null)
        {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }
}

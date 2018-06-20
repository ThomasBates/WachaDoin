package ca.turbobutterfly.wachadoin.android.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

import ca.turbobutterfly.core.utils.DateUtils;
import ca.turbobutterfly.core.utils.IAdapter;
import ca.turbobutterfly.wachadoin.core.data.IDataAccess;
import ca.turbobutterfly.wachadoin.core.data.IDataLogEntry;
import ca.turbobutterfly.core.data.IDataReader;

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
                return GetLogDetails(uri, selectionArgs);
            case LOG_SUMMARY:
                return GetLogSummary(uri);
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
//            default:
//                throw new IllegalArgumentException("Unsupported URI: " + uri);
//        }
//
//        Context context = getContext();
//        if (context != null)
//        {
//            context.getContentResolver().notifyChange(uri, null);
//        }
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
        switch (uriMatcher.match(uri))
        {
            case LOG:
                return UpdateLogRecord(uri, values);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
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
//            default:
//                throw new IllegalArgumentException("Unsupported URI: " + uri);
//        }
        return "";
    }

    private Uri InsertNewLogRecord(Uri uri, ContentValues values)
    {
        IDataLogEntry logEntry = new ContentValuesLogEntry(values);
        long rowID = _dataAccess.SaveLogEntry(logEntry);

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

    private int UpdateLogRecord(Uri uri, ContentValues values)
    {
        IDataLogEntry logEntry = new ContentValuesLogEntry(values);
        int count = _dataAccess.UpdateLogEntry(logEntry);

        if (count > 0)
        {
            Context context = getContext();
            if (context != null)
            {
                context.getContentResolver().notifyChange(uri, null);
            }
        }

        return count;
    }

    private Cursor GetLogDetails(Uri uri, String[] selectionArgs)
    {
        //  Just in case there are no selectionArgs, query everything.
        //  This should never happen.
        Date rangeStart = new Date(0);
        Date rangeEnd = new Date();

        if ((selectionArgs != null) && (selectionArgs.length == 2))
        {
            rangeStart = DateUtils.ISO8601(selectionArgs[0], rangeStart);
            rangeEnd = DateUtils.ISO8601(selectionArgs[1], rangeEnd);
        }

        IDataReader reader = _dataAccess.GetLogDetails(rangeStart, rangeEnd);
        Cursor cursor = (Cursor)((IAdapter)reader).Adaptee();

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
        IDataReader reader = _dataAccess.GetLogSummary();
        Cursor cursor = (Cursor)((IAdapter)reader).Adaptee();

        //  register to watch a content URI for changes
        Context context = getContext();
        if (context != null)
        {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }
}

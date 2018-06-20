package ca.turbobutterfly.wachadoin.android.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import ca.turbobutterfly.android.data.DataCursorReader;
import ca.turbobutterfly.core.utils.DateUtils;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.wachadoin.core.data.IDataAccess;
import ca.turbobutterfly.wachadoin.core.data.IDataLogEntry;
import ca.turbobutterfly.core.data.IDataReader;

public class SQLiteDataAccess implements IDataAccess
{
    //  Variables ----------------------------------------------------------------------------------

    //  Database specific constant declarations
    private SQLiteDatabase _database;
    static final String DATABASE_NAME = "WachaDoinLog";
    static final int DATABASE_VERSION = 2;

    //static final String SETTINGS_TABLE_NAME = "Settings";
    static final String LOG_TABLE_NAME = "Log";

    //  Helper class that actually creates and manages
    //  the provider's underlying data repository.
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("  --  SQLiteDataAccess.DatabaseHelper.onCreate  --  \n" +
                    "CREATE TABLE " + LOG_TABLE_NAME + " \n" +
                    "( \n" +
                    "  StartTime TEXT NOT NULL, \n" +
                    "  EndTime TEXT PRIMARY KEY NOT NULL, \n" +
                    "  LogText TEXT \n" +
                    ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("  --  SQLiteDataAccess.DatabaseHelper.onUpgrade  --  \n" +
                    "DROP TABLE IF EXISTS " + LOG_TABLE_NAME);
            onCreate(db);
        }
    }

    //  Constructors -------------------------------------------------------------------------------

    SQLiteDataAccess(Context context)
    {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        //  Create a writable _database which will trigger its creation if it doesn't already exist.
        _database = dbHelper.getWritableDatabase();
    }

    //  Methods ------------------------------------------------------------------------------------

    //  I have the count variable for debugging.
    @SuppressWarnings({"unused", "UnusedAssignment"})
    @Override
    public long SaveLogEntry(IDataLogEntry logEntry)
    {
        String startTime = logEntry.StartTime();
        String endTime = logEntry.EndTime();
        String logText = logEntry.LogText();

        int count;  //  for debugging.

        //  Delete entries fully covered by the new entry.
        count = _database.delete(
                LOG_TABLE_NAME,
                "(? <= StartTime) AND (EndTime <= ?)",
                new String[] {startTime, endTime});

        //  Is the new entry fully covered by an existing entry?
        Cursor cursor = _database.query(
                LOG_TABLE_NAME,
                new String[]{"StartTime", "EndTime", "LogText"},
                "(StartTime < ?) AND (? < EndTime)",
                new String[]{startTime, endTime},
                null,
                null,
                null);

        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int endTimeIndex = cursor.getColumnIndex("EndTime");
                int logTextIndex = cursor.getColumnIndex("LogText");

                String existingEndTime = cursor.getString(endTimeIndex);
                String existingLogText = cursor.getString(logTextIndex);

                //  If logText is the same, ignore the new entry.
                //  If logText is different, split the existing entry into two.
                if (!TextUtils.equals(logText, existingLogText))
                {
                    ContentValues updateValues = new ContentValues();
                    updateValues.put("EndTime", startTime);
                    count = _database.update(
                            LOG_TABLE_NAME,
                            updateValues,
                            "EndTime=?",
                            new String[]{existingEndTime});

                    ContentValues insertValues = new ContentValues();
                    insertValues.put("StartTime", endTime);
                    insertValues.put("EndTime", existingEndTime);
                    insertValues.put("LogText", existingLogText);
                    _database.insert(LOG_TABLE_NAME, "", insertValues);

                    insertValues = ContentValuesLogEntry.ConvertToContentValues(logEntry);
                    return _database.insert(LOG_TABLE_NAME, "", insertValues);
                }
            }
            cursor.close();
        }

        //  Is the start of the new entry overlapping an existing entry?
        boolean dayBoundary = startTime.contains("T00:00:00");

        if (!dayBoundary)
        {
            //  Get log entry at startTime.
            cursor = _database.query(
                    LOG_TABLE_NAME,
                    new String[]{"StartTime", "EndTime", "LogText"},
                    "(StartTime < ?) AND (? <= EndTime)",
                    new String[]{startTime, startTime},
                    null,
                    null,
                    null);

            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    int startTimeIndex = cursor.getColumnIndex("StartTime");
                    int endTimeIndex = cursor.getColumnIndex("EndTime");
                    int logTextIndex = cursor.getColumnIndex("LogText");

                    String lastStartTime = cursor.getString(startTimeIndex);
                    String lastEndTime = cursor.getString(endTimeIndex);
                    String lastLogText = cursor.getString(logTextIndex);

                    if (TextUtils.equals(logText, lastLogText))
                    {
                        //  LogText is the same.
                        //  Delete the existing entry and expand the new one.
                        count = _database.delete(
                                LOG_TABLE_NAME,
                                "EndTime = ?",
                                new String[] {lastEndTime});
                        startTime = lastStartTime;
                    }
                    else if (!TextUtils.equals(startTime, lastEndTime))
                    {
                        //  LogText is different and the entries overlap.
                        //  Adjust the existing entry to not overlap the new one.
                        ContentValues updateValues = new ContentValues();
                        updateValues.put("EndTime", startTime);
                        count = _database.update(
                                LOG_TABLE_NAME,
                                updateValues,
                                "EndTime=?",
                                new String[]{lastEndTime});
                    }
                }
                cursor.close();
            }
        }

        //  Is the end of the new entry overlapping an existing entry?
        dayBoundary = endTime.contains("T00:00:00");

        if (!dayBoundary)
        {
            //  Get log entry at startTime.
            cursor = _database.query(
                    LOG_TABLE_NAME,
                    new String[]{"StartTime", "EndTime", "LogText"},
                    "(StartTime <= ?) AND (? < EndTime) ",
                    new String[]{endTime, endTime},
                    null,
                    null,
                    null);

            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    int endTimeIndex = cursor.getColumnIndex("EndTime");
                    int logTextIndex = cursor.getColumnIndex("LogText");

                    String lastEndTime = cursor.getString(endTimeIndex);
                    String lastLogText = cursor.getString(logTextIndex);

                    if (TextUtils.equals(logText, lastLogText))
                    {
                        //  LogText is the same.
                        //  Delete the existing entry and expand the new one.
                        count = _database.delete(
                                LOG_TABLE_NAME,
                                "EndTime = ?",
                                new String[] {lastEndTime});
                        endTime = lastEndTime;
                    }
                    else if (!TextUtils.equals(startTime, lastEndTime))
                    {
                        //  LogText is different and the entries overlap.
                        //  Adjust the existing entry to not overlap the new one.
                        ContentValues updateValues = new ContentValues();
                        updateValues.put("StartTime", endTime);
                        count = _database.update(
                                LOG_TABLE_NAME,
                                updateValues,
                                "EndTime=?",
                                new String[]{lastEndTime});
                    }
                }
                cursor.close();
            }
        }


        ContentValues insertValues = new ContentValues();
        insertValues.put("StartTime", startTime);
        insertValues.put("EndTime", endTime);
        insertValues.put("LogText", logText);
        return _database.insert(LOG_TABLE_NAME, "", insertValues);

//        try
//        {
//            return _database.insertOrThrow(LOG_TABLE_NAME, "", insertValues);
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//            return -1;
//        }
    }

    @Override
    public int UpdateLogEntry(IDataLogEntry logEntry)
    {
        String fieldName = logEntry.FieldName();

        switch (fieldName)
        {
            case "StartTime":
                return UpdateStartTime(logEntry);
            case "EndTime":
                return UpdateEndTime(logEntry);
            case "LogText":
                return UpdateLogText(logEntry);
        }

        return 0;
    }

    @SuppressWarnings("UnusedAssignment")
    private int UpdateStartTime(IDataLogEntry logEntry)
    {
        String newStartTime = logEntry.StartTime();
        String endTime = logEntry.EndTime();
        String logText = logEntry.LogText();

        //  StartTime changed?
        Cursor cursor = _database.query(
                LOG_TABLE_NAME,
                new String[]{"StartTime"},
                "(EndTime = ?) AND (LogText = ?)",
                new String[] {endTime, logText},
                null,
                null,
                null);

        //  Nothing to update.
        if (cursor == null)
        {
            return 0;
        }

        //  Nothing to update.
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return 0;
        }

        String oldStartTime = cursor.getString(cursor.getColumnIndex("StartTime"));
        cursor.close();

        //  No need to update.
        if (TextUtils.equals(oldStartTime, newStartTime))
        {
            return 1;
        }

        int count;  //  for debugging.

        if (TextUtils.compare(oldStartTime, newStartTime) < 0)
        {
            //  New start time is later than previous start time.
            //  Update the entry just before with a later end time.
            ContentValues values = new ContentValues();
            values.put("EndTime", newStartTime);
            count = _database.update(
                    LOG_TABLE_NAME,
                    values,
                    "(EndTime = ?)",
                    new String[] {oldStartTime});
        }
        else
        {
            //  New start time is earlier than previous start time.
            //  Delete entries fully covered by the new start time.
            count = _database.delete(
                    LOG_TABLE_NAME,
                    "(StartTime >= ?) AND (StartTime < ?)",
                    new String[] {newStartTime, oldStartTime});

            //  Update entry partially covered by the new start time.
            ContentValues values = new ContentValues();
            values.put("EndTime", newStartTime);
            count = _database.update(
                    LOG_TABLE_NAME,
                    values,
                    "(EndTime > ?) AND (EndTime <= ?)",
                    new String[] {newStartTime, oldStartTime});
        }

        ContentValues values = new ContentValues();
        values.put("StartTime", newStartTime);

        count = _database.update(
                LOG_TABLE_NAME,
                values,
                "(EndTime = ?) AND (LogText = ?)",
                new String[] {endTime, logText});
        return count;
    }

    @SuppressWarnings("UnusedAssignment")
    private int UpdateEndTime(IDataLogEntry logEntry)
    {
        String startTime = logEntry.StartTime();
        String newEndTime = logEntry.EndTime();
        String logText = logEntry.LogText();

        //  EndTime changed?
        Cursor cursor = _database.query(
                LOG_TABLE_NAME,
                new String[]{"EndTime"},
                "(StartTime = ?) AND (LogText = ?)",
                new String[] {startTime, logText},
                null,
                null,
                null);

        //  Nothing to update.
        if (cursor == null)
        {
            return 0;
        }

        //  Nothing to update.
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return 0;
        }

        String oldEndTime = cursor.getString(cursor.getColumnIndex("EndTime"));
        cursor.close();

        //  No need to update.
        if (TextUtils.equals(newEndTime, oldEndTime))
        {
            return 1;
        }

        int count;

        if (TextUtils.compare(newEndTime, oldEndTime) < 0)
        {
            //  New end time is earlier than previous end time.
            //  Update the entry just after with an earlier start time.
            ContentValues values = new ContentValues();
            values.put("StartTime", newEndTime);
            count = _database.update(
                    LOG_TABLE_NAME,
                    values,
                    "(StartTime = ?)",
                    new String[] {oldEndTime});
        }
        else
        {
            //  New start time is earlier than previous start time.
            //  Delete entries fully covered by the new start time.
            count = _database.delete(
                    LOG_TABLE_NAME,
                    "(EndTime > ?) AND (EndTime <= ?)",
                    new String[] {oldEndTime, newEndTime});

            //  Update entry partially covered by the new start time.
            ContentValues values = new ContentValues();
            values.put("StartTime", newEndTime);
            count = _database.update(
                    LOG_TABLE_NAME,
                    values,
                    "(StartTime >= ?) AND (StartTime < ?)",
                    new String[] {oldEndTime, newEndTime});
        }

        ContentValues values = new ContentValues();
        values.put("EndTime", newEndTime);

        count = _database.update(
                LOG_TABLE_NAME,
                values,
                "(StartTime = ?) AND (LogText = ?)",
                new String[] {startTime, logText});
        return count;
    }

    @SuppressWarnings({"UnnecessaryLocalVariable"})
    private int UpdateLogText(IDataLogEntry logEntry)
    {
        String startTime = logEntry.StartTime();
        String endTime = logEntry.EndTime();
        String newLogText = logEntry.LogText();

        //  LogText changed?
        Cursor cursor = _database.query(
                LOG_TABLE_NAME,
                new String[]{"LogText"},
                "(StartTime = ?) AND (EndTime = ?)",
                new String[] {startTime, endTime},
                null,
                null,
                null);

        //  Nothing to update?
        if (cursor == null)
        {
            return 0;
        }

        //  Nothing to update?
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return 0;
        }

        //  No need to update?
        String oldLogText = cursor.getString(cursor.getColumnIndex("LogText"));
        if (TextUtils.equals(newLogText, oldLogText))
        {
            return 1;
        }
        cursor.close();

        //  Update with new LogText.
        ContentValues values = new ContentValues();
        values.put("LogText", newLogText);

        int count = _database.update(
                LOG_TABLE_NAME,
                values,
                "(StartTime = ?) AND (EndTime = ?)",
                new String[] {startTime, endTime});
        return count;
    }

    @Override
    public IDataReader GetLogSummary()
    {
        //  The cursor is closed by the consumer of this method.
        @SuppressLint("Recycle")
        Cursor cursor = _database.rawQuery("  --  SQLiteDataAccess.GetLogSummary  --  \n" +
                        "SELECT      MAX(EndTime) AS MaxEndTime, \n" +
                        "            LogText \n" +
                        "FROM        Log \n" +
                        "GROUP BY    LogText \n" +
                        "ORDER BY    MaxEndTime DESC",
                null);
        return new DataCursorReader(cursor);
    }

    @Override
    public IDataReader GetLogDetails(Date rangeStart, Date rangeEnd)
    {
        String startTime = DateUtils.ISO8601(rangeStart);
        String endTime = DateUtils.ISO8601(rangeEnd);

        //  The cursor is closed by the consumer of this method.
        @SuppressLint("Recycle")
        Cursor cursor = _database.query(
                LOG_TABLE_NAME,
                new String[] {"StartTime", "EndTime", "LogText"},
                "(StartTime < ?) AND (EndTime > ?)",
                new String[] {endTime, startTime},
                null,
                null,
                "EndTime");
        return new DataCursorReader(cursor);
    }
}

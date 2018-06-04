package ca.turbobutterfly.wachadoin.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.view.TextureView;

public class SQLiteDataAccess implements IDataAccess
{
    //  Variables ----------------------------------------------------------------------------------

    //  Database specific constant declarations
    private SQLiteDatabase _database;
    static final String DATABASE_NAME = "WachaDoinLog";
    static final int DATABASE_VERSION = 2;

    static final String SETTINGS_TABLE_NAME = "Settings";
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
                    "CREATE TABLE " + SETTINGS_TABLE_NAME + " \n" +
                    "( \n" +
                    "  Setting TEXT PRIMARY KEY NOT NULL, \n" +
                    "  Value TEXT \n" +
                    ");");
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
                    "DROP TABLE IF EXISTS " + SETTINGS_TABLE_NAME);
            db.execSQL("  --  SQLiteDataAccess.DatabaseHelper.onUpgrade  --  \n" +
                    "DROP TABLE IF EXISTS " + LOG_TABLE_NAME);
            onCreate(db);
        }
    }

    //  Constructors -------------------------------------------------------------------------------

    public SQLiteDataAccess(Context context)
    {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        //  Create a writable _database which will trigger its creation if it doesn't already exist.
        _database = dbHelper.getWritableDatabase();
    }

    //  Methods ------------------------------------------------------------------------------------

    @Override
    public long SaveLog(ContentValues values)
    {
        String startTimeValue = values.getAsString("StartTime");
        String endTimeValue = values.getAsString("EndTime");
        String logTextValue = values.getAsString("LogText");

        Cursor cursor = GetLogDetails();
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int endTimeIndex = cursor.getColumnIndex("EndTime");
                int logTextIndex = cursor.getColumnIndex("LogText");

                String lastEndTime = cursor.getString(endTimeIndex);
                String lastLogText = cursor.getString(logTextIndex);

                if (TextUtils.equals(logTextValue, lastLogText) &&
                        TextUtils.equals(startTimeValue, lastEndTime))
                {
                    ContentValues updateValues = new ContentValues();
                    updateValues.put("EndTime", endTimeValue);
                    return _database.update(
                            LOG_TABLE_NAME,
                            updateValues,
                            "EndTime=?",
                            new String[] {lastEndTime});
                }
            }
            cursor.close();
        }

        return _database.insert(LOG_TABLE_NAME, "", values);
    }

    @Override
    public Cursor GetLogSummary()
    {
        return _database.rawQuery("  --  SQLiteDataAccess.GetLogSummary  --  \n" +
                        "SELECT      MAX(EndTime) AS MaxEndTime, \n" +
                        "            LogText \n" +
                        "FROM        Log \n" +
                        "GROUP BY    LogText \n" +
                        "ORDER BY    MaxEndTime DESC",
                null);
    }

    @Override
    public Cursor GetLogDetails()
    {
        return _database.query(
                LOG_TABLE_NAME,
                new String[] {"StartTime", "EndTime", "LogText"},
                null,
                null,
                null,
                null,
                "EndTime DESC");

//        return _database.rawQuery("  --  SQLiteDataAccess.GetLogDetails  --  \n" +
//                        "SELECT      StartTime, \n" +
//                        "            EndTime, \n" +
//                        "            LogText \n" +
//                        "FROM        Log \n" +
//                        "ORDER BY    EndTime DESC",
//                null);
    }
}

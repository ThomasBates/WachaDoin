package ca.turbobutterfly.wachadoin.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

public interface IDataAccess
{
    long SaveLog(ContentValues values);

    Cursor GetLogSummary();

    Cursor GetLogDetails();
}

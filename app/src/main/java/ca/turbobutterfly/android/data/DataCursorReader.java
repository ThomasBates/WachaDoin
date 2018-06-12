package ca.turbobutterfly.android.data;

import android.database.Cursor;

import java.util.HashMap;

import ca.turbobutterfly.core.utils.IAdapter;
import ca.turbobutterfly.core.data.IDataReader;

public class DataCursorReader implements IDataReader, IAdapter
{
    private Cursor _cursor;

    private boolean _first = true;
    private HashMap<String, Integer> _indexByName = new HashMap<>();

    public DataCursorReader(Cursor cursor)
    {
        _cursor = cursor;
    }

    @Override
    public Object Adaptee()
    {
        return _cursor;
    }

    @Override
    public boolean Read()
    {
        if (_cursor == null)
        {
            return false;
        }

        if (_first)
        {
            _first = false;
            return _cursor.moveToFirst();
        }
        else
        {
            return _cursor.moveToNext();
        }
    }

    @Override
    public void Close()
    {
        if (_cursor == null)
        {
            return;
        }

        _cursor.close();
    }

    @Override
    public String getString(String columnName)
    {
        if (_cursor == null)
        {
            return null;
        }

        int columnIndex = _cursor.getColumnIndex(columnName);
        return getString(columnIndex);
    }

    @Override
    public String getString(int columnIndex)
    {
        if (_cursor == null)
        {
            return null;
        }

        return _cursor.getString(columnIndex);
    }
}

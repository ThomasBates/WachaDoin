package ca.turbobutterfly.android.options;

import android.content.SharedPreferences;

import java.util.Date;

import ca.turbobutterfly.core.options.IOptionsProvider;
import ca.turbobutterfly.core.utils.DateUtils;

public class OptionsPreferencesProvider implements IOptionsProvider
{
    //  Variables ----------------------------------------------------------------------------------

    private SharedPreferences _sharedPreferences;

    //  Constructors -------------------------------------------------------------------------------

    public OptionsPreferencesProvider(SharedPreferences sharedPreferences)
    {
        _sharedPreferences = sharedPreferences;
    }

    //  Properties ---------------------------------------------------------------------------------

    @Override
    public String NameSeparator()
    {
        return "_";
    }

    //  Methods ------------------------------------------------------------------------------------

    @Override
    public void WriteString(String name, String value)
    {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    @Override
    public String ReadString(String name, String defaultValue)
    {
        String result = _sharedPreferences.getString(name, defaultValue);
        return result;
    }

    @Override
    public void WriteInteger(String name, Integer value)
    {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    @Override
    public Integer ReadInteger(String name, Integer defaultValue)
    {
        return _sharedPreferences.getInt(name, defaultValue);
    }

    @Override
    public void WriteBoolean(String name, Boolean value)
    {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    @Override
    public Boolean ReadBoolean(String name, Boolean defaultValue)
    {
        Boolean result = _sharedPreferences.getBoolean(name, defaultValue);
        return result;
    }

    @Override
    public void WriteFloat(String name, Float value)
    {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putFloat(name, value);
        editor.apply();
    }

    @Override
    public Float ReadFloat(String name, Float defaultValue)
    {
        return _sharedPreferences.getFloat(name, defaultValue);
    }

    @Override
    public void WriteDate(String name, Date value)
    {
        String isoValue = DateUtils.ISO8601(value);

        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(name, isoValue);
        editor.apply();
    }

    @Override
    public Date ReadDate(String name, Date defaultValue)
    {
        String isoDefaultValue = DateUtils.ISO8601(defaultValue);

        String isoValue = _sharedPreferences.getString(name, isoDefaultValue);

        return DateUtils.ISO8601(isoValue, defaultValue);
    }
}

package ca.turbobutterfly.core.options;

import java.util.Date;

public interface IOptionsProvider
{
    //  Properties ---------------------------------------------------------------------------------

    String NameSeparator();

    //  Methods ------------------------------------------------------------------------------------

    void WriteString(String name, String value);
    String ReadString(String name, String defaultValue);

    void WriteInteger(String name, Integer value);
    Integer ReadInteger(String name, Integer defaultValue);

    void WriteBoolean(String name, Boolean value);
    Boolean ReadBoolean(String name, Boolean defaultValue);

    void WriteFloat(String name, Float value);
    Float ReadFloat(String name, Float defaultValue);

    void WriteDate(String name, Date value);
    Date ReadDate(String name, Date defaultValue);
}

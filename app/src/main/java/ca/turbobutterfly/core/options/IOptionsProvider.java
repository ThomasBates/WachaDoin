package ca.turbobutterfly.core.options;

import java.util.Date;

public interface IOptionsProvider
{
    //  Properties ---------------------------------------------------------------------------------

    String NameSeparator();

    //  Methods ------------------------------------------------------------------------------------

//    <T> void Write(String name, T value);
//    <T> T Read(String name, T defaultValue);

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

//    String[] GetChildNames(String name);

//    void Delete(String name);
}

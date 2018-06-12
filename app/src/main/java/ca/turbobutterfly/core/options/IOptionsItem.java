package ca.turbobutterfly.core.options;

import ca.turbobutterfly.core.events.IEvent;

interface IOptionsItem<T>
{
    //  Events -------------------------------------------------------------------------------------

    IEvent ValueChanged();

    //  Properties ---------------------------------------------------------------------------------

    IOptionsGroup Group();
    void Group(IOptionsGroup parentGroup);

    String Name();
    void Name(String name);
    String FullName();

    T Value();
    void Value(T value);

    T DefaultValue();
    void DefaultValue(T defaultValue);

//    String ValueString();
//    void ValueString(String valueString);
//
//    String DefaultString();
//    void DefaultString(String defaultString);
//
//    boolean IsEmpty();

//    String AsString();
//    void AsString(String asString);

    //  Methods ------------------------------------------------------------------------------------

//    void SetValueFromString();
//    void SetStringFromValue();
//
//    T GetValueFromString(String value, T defaultValue);
//    String GetStringFromValue(T value);
//
//    void RestoreDefaults();
}

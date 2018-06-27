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

    //  Methods ------------------------------------------------------------------------------------

}

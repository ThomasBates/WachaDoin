package ca.turbobutterfly.core.options;

import java.util.List;

import ca.turbobutterfly.core.events.IEvent;

public interface IOptionsGroup
{
    //  Events -------------------------------------------------------------------------------------

    IEvent ValueChanged();

    //  Properties ---------------------------------------------------------------------------------

    IOptionsProvider Provider();
    IOptionsGroup Group();
    String Name();

    String FullName();

    List<IOptionsGroup> Groups();
    List<IOptionsItem> Items();

    //  Methods ------------------------------------------------------------------------------------

    void NotifyValueChanged(String name);
}

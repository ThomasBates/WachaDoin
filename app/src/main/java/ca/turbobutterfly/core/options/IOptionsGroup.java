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

//    String[] GetGroupNames();
//    String[] GetItemNames();
//    String[] GetChildNames();
//    String[] GetChildNames(String groupPath);

    void NotifyValueChanged(String name);

//    <T> void Write(String name, T value);
//    <T> T Read(String name, T defaultValue);

//    void LoadAll();
//    void SaveAll();

//    void RestoreDefaults();
//    void Assign(IOptionsGroup source);

//    void Delete();
//    void Delete(String name);
}

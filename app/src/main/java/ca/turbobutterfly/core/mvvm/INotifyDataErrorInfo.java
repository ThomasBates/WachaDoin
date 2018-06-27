package ca.turbobutterfly.core.mvvm;

import java.util.Collection;

import ca.turbobutterfly.core.events.IEvent;

public interface INotifyDataErrorInfo
{
    IEvent OnErrorsChanged();
    Boolean HasErrors();
    Collection<String> GetErrors(String propertyName);
}

package ca.turbobutterfly.core.mvvm;

import java.util.Collection;

import ca.turbobutterfly.core.events.IEvent;

public interface INotifyDataErrorInfo
{
    public IEvent OnErrorsChanged();
    public Boolean HasErrors();
    public Collection<String> GetErrors(String propertyName);
}

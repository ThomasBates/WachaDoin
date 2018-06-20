package ca.turbobutterfly.core.mvvm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import ca.turbobutterfly.core.events.Event;
import ca.turbobutterfly.core.events.IEvent;
import ca.turbobutterfly.core.utils.TextUtils;

public class ValidatableViewModel extends ViewModel implements INotifyDataErrorInfo
{
    private IEvent _onDataErrorsChanged = new Event();

    private HashMap<String, List<String>> _dataErrors = new HashMap<>();

    @Override
    protected void NotifyPropertyChanged(String propertyName)
    {
        super.NotifyPropertyChanged(propertyName);
        ValidateAsync();
    }

    @Override
    public IEvent OnErrorsChanged()
    {
        return _onDataErrorsChanged;
    }

    protected void NotifyDataErrorsChanged(String propertyName)
    {
        _onDataErrorsChanged.Publish(this, new DataErrorsChangedEventArgs(propertyName));
    }

    @Override
    public Boolean HasErrors()
    {
        if ((_dataErrors == null) || _dataErrors.isEmpty())
        {
            return false;
        }

        for (List errorList : _dataErrors.values())
        {
            if ((errorList != null) && (!errorList.isEmpty()))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public Collection<String> GetErrors(String propertyName)
    {
        if ((_dataErrors == null) || _dataErrors.isEmpty())
        {
            return null;
        }

        if (!_dataErrors.containsKey(propertyName))
        {
            return null;
        }

        return _dataErrors.get(propertyName);
    }

    protected void ValidateAsync()
    {
        //  TODO: Async in Java.
        //Validate();
    }

    protected void Validate()
    {
        List<String> propertyNames = new ArrayList<>();

        ValidationContext validationContext = new ValidationContext(this, null, null);
        List<IValidationResult> validationResults = new ArrayList<>();
        Validator.TryValidateObject(this, validationContext, validationResults, true);

        //  Remove entries from _dataErrors that are not found in validationResults.
        for (String propertyName : _dataErrors.keySet())
        {
            boolean found = false;
            for (IValidationResult result : validationResults)
            {
                for (String memberName : result.MemberNames())
                {
                    if (TextUtils.equals(memberName, propertyName))
                    {
                        found = true;
                        break;
                    }
                }
                if (found)
                {
                    break;
                }
            }

            if (!found)
            {
                propertyNames.add(propertyName);
            }
        }

        _dataErrors.clear();

        //  Add entries to _dataErrors that are found in validationResults.
        for (IValidationResult result : validationResults)
        {
            for (String memberName : result.MemberNames())
            {
                if (!_dataErrors.containsKey(memberName))
                {
                    _dataErrors.put(memberName, new ArrayList<String>());
                }

                _dataErrors.get(memberName).add(result.ErrorMessage());

                if (!propertyNames.contains(memberName))
                {
                    propertyNames.add(memberName);
                }
            }
        }

        for (String propertyName : propertyNames)
        {
            NotifyDataErrorsChanged(propertyName);
        }
    }
}

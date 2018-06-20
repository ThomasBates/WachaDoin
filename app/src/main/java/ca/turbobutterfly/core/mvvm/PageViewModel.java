package ca.turbobutterfly.core.mvvm;

import ca.turbobutterfly.core.utils.TextUtils;

public class PageViewModel extends ViewModel
{
    private String _newPageName;

    public String NewPageName()
    {
        return _newPageName;
    }

    public void NewPageName(String newPageName)
    {
        if (TextUtils.equals(_newPageName, newPageName))
        {
            return;
        }
        _newPageName = newPageName;
        NotifyPropertyChanged("NewPageName");
    }
}

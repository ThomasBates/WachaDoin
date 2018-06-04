package ca.turbobutterfly.wachadoin.viewmodels;

import ca.turbobutterfly.data.IDataTable;
import ca.turbobutterfly.viewmodels.ViewModel;
import ca.turbobutterfly.wachadoin.data.IDataProvider;

public class ViewPageViewModel extends ViewModel
{
    //  region Variables ---------------------------------------------------------------------------

    //  Injected dependencies
    private IDataProvider _dataProvider;

    //  Properties ---------------------------------------------------------------------------------

    public IDataTable LogEntries()
    {
        return _dataProvider.GetLogEntries();
    }

    //  region Constructors ------------------------------------------------------------------------

    public ViewPageViewModel(IDataProvider dataProvider)
    {
        _dataProvider = dataProvider;
    }

    //  region Properties --------------------------------------------------------------------------
}

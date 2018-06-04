package ca.turbobutterfly.wachadoin.viewmodels;

import ca.turbobutterfly.data.IDataTable;
import ca.turbobutterfly.viewmodels.ViewModel;
import ca.turbobutterfly.wachadoin.data.IDataProvider;

public class ExportPageViewModel extends ViewModel
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

    public ExportPageViewModel(IDataProvider dataProvider)
    {
        _dataProvider = dataProvider;
    }

    //  region Properties --------------------------------------------------------------------------
}

package ca.turbobutterfly.wachadoin.core.viewmodels;

import ca.turbobutterfly.core.data.IDataTable;
import ca.turbobutterfly.core.viewmodels.ViewModel;
import ca.turbobutterfly.wachadoin.core.data.IDataProvider;
import ca.turbobutterfly.wachadoin.core.options.IMainOptions;

public class ExportPageViewModel extends ViewModel
{
    //  region Variables ---------------------------------------------------------------------------

    //  Injected dependencies
    private IDataProvider _dataProvider;
    private IMainOptions _mainOptions;

    //  Properties ---------------------------------------------------------------------------------

    public IDataTable LogEntries()
    {
        return null; //_dataProvider.GetLogEntries();
    }

    //  region Constructors ------------------------------------------------------------------------

    public ExportPageViewModel(
            IDataProvider dataProvider,
            IMainOptions mainOptions)
    {
        _dataProvider = dataProvider;
        _mainOptions = mainOptions;
    }

    //  region Properties --------------------------------------------------------------------------
}

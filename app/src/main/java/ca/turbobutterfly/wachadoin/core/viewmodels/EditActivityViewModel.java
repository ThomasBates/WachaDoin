package ca.turbobutterfly.wachadoin.core.viewmodels;

import android.graphics.pdf.PdfDocument;

import ca.turbobutterfly.core.events.EventHandler;
import ca.turbobutterfly.core.events.IEventArgs;
import ca.turbobutterfly.core.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.core.mvvm.PageViewModel;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.core.mvvm.ViewModel;

public class EditActivityViewModel extends PageViewModel
{
    //  Variables ----------------------------------------------------------------------------------

    //  Injected dependencies
    private ViewModel _editPageViewModel;
    private ViewModel _editRowPageViewModel;
    private ViewModel _insertRowPageViewModel;
    private ViewModel _deleteRowPageViewModel;

    //  Internal
    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();

            switch (propertyName)
            {
                case "NewPageName":
                    PageViewModel pageViewModel = (PageViewModel)sender;
                    NewPageName(pageViewModel.NewPageName());
                    break;
            }
        }
    };

    //  Constructors -------------------------------------------------------------------------------

    public EditActivityViewModel(
            ViewModel editPageViewModel,
            ViewModel editRowPageViewModel,
            ViewModel insertRowPageViewModel,
            ViewModel deleteRowPageViewModel)
    {
        _editPageViewModel = editPageViewModel;
        _editRowPageViewModel = editRowPageViewModel;
        _insertRowPageViewModel = insertRowPageViewModel;
        _deleteRowPageViewModel = deleteRowPageViewModel;

        _editPageViewModel.OnPropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);
//        _editRowPageViewModel.OnPropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);
//        _insertRowPageViewModel.OnPropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);
//        _deleteRowPageViewModel.OnPropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);

        NewPageName("EditPage");
    }

    //  Properties ---------------------------------------------------------------------------------

    public ViewModel EditPageViewModel()
    {
        return _editPageViewModel;
    }

    public ViewModel EditRowPageViewModel()
    {
        return _editRowPageViewModel;
    }

    public ViewModel InsertRowPageViewModel()
    {
        return _insertRowPageViewModel;
    }

    public ViewModel DeleteRowPageViewModel()
    {
        return _deleteRowPageViewModel;
    }

    //  Methods ------------------------------------------------------------------------------------

}

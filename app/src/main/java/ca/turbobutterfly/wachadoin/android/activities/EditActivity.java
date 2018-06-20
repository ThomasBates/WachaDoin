package ca.turbobutterfly.wachadoin.android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ca.turbobutterfly.android.views.FragmentView;
import ca.turbobutterfly.core.events.EventHandler;
import ca.turbobutterfly.core.events.IEventArgs;
import ca.turbobutterfly.core.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.core.mvvm.PageViewModel;
import ca.turbobutterfly.core.mvvm.ViewModel;
import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.android.bootstrapper.Bootstrapper;
import ca.turbobutterfly.wachadoin.android.views.EditPage;
import ca.turbobutterfly.wachadoin.core.viewmodels.EditActivityViewModel;
import ca.turbobutterfly.wachadoin.core.viewmodels.EditPageViewModel;

public class EditActivity extends AppCompatActivity
{
    private EditActivityViewModel _dataContext;

//    private FragmentView _editPage;
//    private FragmentView _editRowPage;
//    private FragmentView _insertRowPage;
//    private FragmentView _deleteRowPage;

    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();

            switch (propertyName)
            {
                case "NewPageName":
                    ChangePage(_dataContext.NewPageName());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        _dataContext = (EditActivityViewModel)Bootstrapper.ComposeEditActivityViewModel(this);
        _dataContext.OnPropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);

//        _editPage = (FragmentView) getSupportFragmentManager().findFragmentById(R.id.edit_page);
//        _editPage.DataContext(_dataContext.EditPageViewModel());
//
//        _editRowPage = (FragmentView) getSupportFragmentManager().findFragmentById(R.id.edit_row_page);
//        _editRowPage.DataContext(_dataContext.EditPageViewModel());
//
//        _insertRowPage = (FragmentView) getSupportFragmentManager().findFragmentById(R.id.insert_row_page);
//        _insertRowPage.DataContext(_dataContext.EditPageViewModel());
//
//        _deleteRowPage = (FragmentView) getSupportFragmentManager().findFragmentById(R.id.delete_row_page);
//        _deleteRowPage.DataContext(_dataContext.EditPageViewModel());

        ChangePage(_dataContext.NewPageName());
    }

    private void ChangePage(String pageName)
    {
        try
        {
            FragmentView view;
            switch (pageName)
            {
                case "EditPage":
                    view = new EditPage();
                    view.DataContext(_dataContext.EditPageViewModel());
                    break;
//            case "EditRowPage":
//                view = new EditRowPage();
//                view.DataContext(_dataContext.EditRowPageViewModel());
//                break;
//            case "InsertRowPage":
//                view = new InsertRowPage();
//                view.DataContext(_dataContext.EditRowPageViewModel());
//                break;
//            case "DeleteRowPage":
//                view = new DeleteRowPage();
//                view.DataContext(_dataContext.EditRowPageViewModel());
//                break;
                default:
                    return;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.edit_activity, view, view.toString());
            fragmentTransaction.addToBackStack(view.toString());
            fragmentTransaction.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

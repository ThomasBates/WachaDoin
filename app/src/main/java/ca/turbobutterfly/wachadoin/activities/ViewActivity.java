package ca.turbobutterfly.wachadoin.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import ca.turbobutterfly.viewmodels.ViewModel;
import ca.turbobutterfly.views.FragmentView;
import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.data.ContentProviderDataAccess;
import ca.turbobutterfly.wachadoin.data.DataProvider;
import ca.turbobutterfly.wachadoin.data.IDataAccess;
import ca.turbobutterfly.wachadoin.data.IDataProvider;
import ca.turbobutterfly.wachadoin.data.SQLiteDataAccess;
import ca.turbobutterfly.wachadoin.support.Bootstrapper;
import ca.turbobutterfly.wachadoin.viewmodels.ViewPageViewModel;

public class ViewActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wachadoin_view_activity);

        BootstrapView();
    }

    private void BootstrapView()
    {
        ViewModel dataContext = Bootstrapper.ComposeViewPageViewModel(this);

        FragmentView view = (FragmentView) getSupportFragmentManager().findFragmentById(R.id.view_page);
        view.DataContext(dataContext);
    }
}

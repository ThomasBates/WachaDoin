package ca.turbobutterfly.wachadoin.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ca.turbobutterfly.core.viewmodels.ViewModel;
import ca.turbobutterfly.android.views.FragmentView;
import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.android.bootstrapper.Bootstrapper;

public class ViewActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);

        BootstrapView();
    }

    private void BootstrapView()
    {
        ViewModel dataContext = Bootstrapper.ComposeViewPageViewModel(this);

        FragmentView view = (FragmentView) getSupportFragmentManager().findFragmentById(R.id.view_page);
        view.DataContext(dataContext);
    }
}

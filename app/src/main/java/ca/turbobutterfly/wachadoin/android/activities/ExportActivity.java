package ca.turbobutterfly.wachadoin.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ca.turbobutterfly.core.mvvm.ViewModel;
import ca.turbobutterfly.android.views.FragmentView;
import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.android.bootstrapper.Bootstrapper;

public class ExportActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_activity);

        ViewModel dataContext = Bootstrapper.ComposeExportPageViewModel(this);

        FragmentView view = (FragmentView) getSupportFragmentManager().findFragmentById(R.id.export_page);
        view.DataContext(dataContext);
    }
}

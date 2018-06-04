package ca.turbobutterfly.wachadoin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import ca.turbobutterfly.events.EventHandler;
import ca.turbobutterfly.events.IEventArgs;
import ca.turbobutterfly.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.viewmodels.ViewModel;
import ca.turbobutterfly.views.FragmentView;
import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.support.Bootstrapper;

public class MainActivity extends AppCompatActivity
{

    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();

            switch (propertyName)
            {
                case "AutoClose":
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wachadoin_main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BootstrapView();
    }

    private void BootstrapView()
    {
        ViewModel dataContext = Bootstrapper.ComposeMainPageViewModel(this);
        dataContext.PropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);

        FragmentView view = (FragmentView) getSupportFragmentManager().findFragmentById(R.id.main_page);
        view.DataContext(dataContext);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId())
        {
            case R.id.action_viewLog:
                ViewLog();
                break;
            case R.id.action_editLog:
                EditLog();
                break;
            case R.id.action_exportLog:
                ExportLog();
                break;
            case R.id.action_settings:
                ShowSettings();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ViewLog()
    {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

    private void EditLog()
    {
        //Intent intent = new Intent(this, EditActivity.class);
        //startActivity(intent);
    }

    private void ExportLog()
    {
        Intent intent = new Intent(this, ExportActivity.class);
        startActivity(intent);
    }

    private void ShowSettings()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}

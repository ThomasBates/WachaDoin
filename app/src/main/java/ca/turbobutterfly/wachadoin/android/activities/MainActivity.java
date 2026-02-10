package ca.turbobutterfly.wachadoin.android.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ca.turbobutterfly.core.events.EventHandler;
import ca.turbobutterfly.core.events.IEventArgs;
import ca.turbobutterfly.core.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.core.mvvm.ViewModel;
import ca.turbobutterfly.android.views.FragmentView;
import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.android.bootstrapper.Bootstrapper;
import ca.turbobutterfly.wachadoin.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity
{
    private static final int PERMISSION_REQUEST_CODE = 112;

    private MainActivityBinding binding;

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
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ViewModel dataContext = Bootstrapper.ComposeMainPageViewModel(this);
        dataContext.OnPropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);

        FragmentView view = (FragmentView) getSupportFragmentManager().findFragmentById(R.id.main_page);
        if (view != null) {
            view.DataContext(dataContext);
        }

        CheckNotificationPermission();
    }

    private void CheckNotificationPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Permission granted
            }
        }
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

        int id = item.getItemId();

        if (id == R.id.action_viewLog)
        {
            ViewLog();
        }
        else if (id == R.id.action_exportLog)
        {
            ExportLog();
        }
        else if (id == R.id.action_settings)
        {
            ShowSettings();
        }
        else if (id == R.id.action_about)
        {
            ShowAbout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ViewLog()
    {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
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

    private void ShowAbout()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.about_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(R.string.about_page_title);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

    }
}

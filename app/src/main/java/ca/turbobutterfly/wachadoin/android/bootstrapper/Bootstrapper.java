package ca.turbobutterfly.wachadoin.android.bootstrapper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ca.turbobutterfly.core.options.IOptionsProvider;
import ca.turbobutterfly.android.options.OptionsPreferencesProvider;
import ca.turbobutterfly.core.viewmodels.ViewModel;

import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.android.receivers.NotificationHelper;
import ca.turbobutterfly.wachadoin.android.data.ContentProviderDataAccess;
import ca.turbobutterfly.wachadoin.core.data.DataProvider;
import ca.turbobutterfly.wachadoin.core.data.IDataAccess;
import ca.turbobutterfly.wachadoin.core.data.IDataProvider;
import ca.turbobutterfly.wachadoin.core.options.IMainOptions;
import ca.turbobutterfly.wachadoin.core.options.MainOptions;
import ca.turbobutterfly.wachadoin.core.viewmodels.ExportPageViewModel;
import ca.turbobutterfly.wachadoin.core.viewmodels.MainPageViewModel;
import ca.turbobutterfly.wachadoin.core.viewmodels.ViewPageViewModel;

public class Bootstrapper
{
    public static IMainOptions ComposeMainOptions(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        IOptionsProvider provider = new OptionsPreferencesProvider(sharedPreferences);

        return new MainOptions(provider, "pref");
    }

    public static IDataProvider ComposeDataProvider(Context context)
    {
        IDataAccess dataAccess = new ContentProviderDataAccess(context.getContentResolver());
        //IDataAccess dataAccess = new SQLiteDataAccess(this);
        IMainOptions mainOptions = ComposeMainOptions(context);

        return new DataProvider(dataAccess, mainOptions);
    }

    public static ViewModel ComposeMainPageViewModel(Context context)
    {
        IDataProvider dataProvider = ComposeDataProvider(context);
        IMainOptions mainOptions = ComposeMainOptions(context);

        return new MainPageViewModel(
                dataProvider,
                mainOptions,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
                context.getString(R.string.log_prompt));
    }

    public static ViewModel ComposeViewPageViewModel(Context context)
    {
        IDataProvider dataProvider = ComposeDataProvider(context);
        IMainOptions mainOptions = ComposeMainOptions(context);

        return new ViewPageViewModel(dataProvider, mainOptions);
    }

    public static ViewModel ComposeExportPageViewModel(Context context)
    {
        IDataProvider dataProvider = ComposeDataProvider(context);
        IMainOptions mainOptions = ComposeMainOptions(context);

        return new ExportPageViewModel(dataProvider, mainOptions);
    }

    public static NotificationHelper ComposeNotificationHelper(Context context)
    {
        IDataProvider dataProvider = ComposeDataProvider(context);
        IMainOptions mainOptions = ComposeMainOptions(context);

        return new NotificationHelper(context, dataProvider, mainOptions.Notification());
    }
}

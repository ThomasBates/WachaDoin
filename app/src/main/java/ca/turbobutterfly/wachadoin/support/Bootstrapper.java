package ca.turbobutterfly.wachadoin.support;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ca.turbobutterfly.viewmodels.ViewModel;
import ca.turbobutterfly.views.FragmentView;
import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.activities.MainActivity;
import ca.turbobutterfly.wachadoin.activities.ViewActivity;
import ca.turbobutterfly.wachadoin.data.ContentProviderDataAccess;
import ca.turbobutterfly.wachadoin.data.DataProvider;
import ca.turbobutterfly.wachadoin.data.IDataAccess;
import ca.turbobutterfly.wachadoin.data.IDataProvider;
import ca.turbobutterfly.wachadoin.services.NotificationHelper;
import ca.turbobutterfly.wachadoin.viewmodels.ExportPageViewModel;
import ca.turbobutterfly.wachadoin.viewmodels.MainPageViewModel;
import ca.turbobutterfly.wachadoin.viewmodels.ViewPageViewModel;

public class Bootstrapper
{
    public static IDataProvider ComposeDataProvider(Context context)
    {
        IDataAccess dataAccess = new ContentProviderDataAccess(context.getContentResolver());
        //IDataAccess dataAccess = new SQLiteDataAccess(this);

        return new DataProvider(dataAccess);
    }

    public static ViewModel ComposeMainPageViewModel(Context context)
    {
        IDataProvider dataProvider = ComposeDataProvider(context);

        return new MainPageViewModel(
                dataProvider,
                PreferenceManager.getDefaultSharedPreferences(context),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
                context.getString(R.string.log_prompt));
    }

    public static ViewModel ComposeViewPageViewModel(Context context)
    {
        IDataProvider dataProvider = ComposeDataProvider(context);

        return new ViewPageViewModel(dataProvider);
    }

    public static ViewModel ComposeExportPageViewModel(Context context)
    {
        IDataProvider dataProvider = ComposeDataProvider(context);

        return new ExportPageViewModel(dataProvider);
    }

    public static NotificationHelper ComposeNotificationHelper(Context context)
    {
        IDataProvider dataProvider = ComposeDataProvider(context);

        return new NotificationHelper(context, dataProvider);
    }
}

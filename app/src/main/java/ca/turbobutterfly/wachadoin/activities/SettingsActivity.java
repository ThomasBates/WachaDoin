package ca.turbobutterfly.wachadoin.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.List;

import ca.turbobutterfly.activities.AppCompatPreferenceActivity;
import ca.turbobutterfly.wachadoin.R;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */

/*

Notifications
  - (Reminder enabled)
  - Popup app
  - Ringtone
  - Vibrate
  - Delay Time
  - Snooze Time

Schedule
  - Sunday
  - Monday
  - Tuesday
  - Wednesday
  - Thursday
  - Friday
  - Saturday

Export
  - Format (Text, Excel, Other)
  - Delivery (Email, USB, Other)
  - Recipient Email Address


 */
public class SettingsActivity extends AppCompatPreferenceActivity
{
    private static Preference.OnPreferenceClickListener _onPreferenceClickListener = new Preference.OnPreferenceClickListener()
    {
        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            switch (preference.getKey())
            {
                case "notifications_reminder_system_settings":
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                    {
                        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, "ca.turbobutterfly.wachadoin");
                        intent.putExtra(Settings.EXTRA_CHANNEL_ID, "ca.turbobutterfly.wachadoin.reminders.01");
                        preference.getContext().startActivity(intent);
                    }
                    break;
            }
            return true;
        }
    };

    private static PreferenceFragment _displayFragment;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener _onPreferenceChangeListener = new Preference.OnPreferenceChangeListener()
    {
//        private PreferenceFragment _activity;
//        private Preference _preferences;

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
//              //  TODO: This is a horrible hack, but I can't figure out any other way to do this.
//            if (preference == null)
//            {
//                _activity = (PreferenceActivity) value;
//                return false;
//            }

            String stringValue = newValue.toString();

            if (preference instanceof ListPreference)
            {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                return true;
            }

            if (preference instanceof RingtonePreference)
            {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue))
                {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                }
                else
                {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null)
                    {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    }
                    else
                    {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

                return true;
            }

            if (preference instanceof SwitchPreference)
            {
                if (TextUtils.equals(preference.getKey(), "pref_display_use_reporting_period"))
                {
                    if (_displayFragment == null)
                    {
                        return true;
                    }

                    Preference pref_display_reporting_period = _displayFragment.findPreference("pref_display_reporting_period");
                    Preference pref_display_days_per_page = _displayFragment.findPreference("pref_display_days_per_page");

                    if ((pref_display_reporting_period == null) ||
                        (pref_display_days_per_page == null))
                    {
                        return true;
                    }

                    boolean useReportingPeriod = (boolean) newValue;

                    pref_display_reporting_period.setEnabled(useReportingPeriod);
                    pref_display_days_per_page.setEnabled(!useReportingPeriod);

                    return true;
                }
            }

            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context)
    {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #_onPreferenceChangeListener
     */
    private static void SetupPreferenceVisible(
            PreferenceFragment fragment,
            Preference preference,
            boolean isVisible)
    {
        if (!isVisible)
        {
            PreferenceScreen screen = fragment.getPreferenceScreen();
            screen.removePreference(preference);
        }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #_onPreferenceChangeListener
     */
    private static void SetupPreferenceClick(
            PreferenceFragment fragment,
            String preferenceKey,
            boolean isVisible)
    {
        Preference preference = fragment.findPreference(preferenceKey);

        if (!isVisible)
        {
            PreferenceScreen screen = fragment.getPreferenceScreen();
            screen.removePreference(preference);
            return;
        }

        preference.setOnPreferenceClickListener(_onPreferenceClickListener);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #_onPreferenceChangeListener
     */
    private static void SetupPreferenceSummary(
            PreferenceFragment fragment,
            String preferenceKey,
            boolean isVisible)
    {
        Preference preference = fragment.findPreference(preferenceKey);

        if (!isVisible)
        {
            PreferenceScreen screen = fragment.getPreferenceScreen();
            screen.removePreference(preference);
            return;
        }

        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(_onPreferenceChangeListener);

        // Trigger the listener immediately with the preference's current value.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        Object value = preferences.getString(preferenceKey, "");

        _onPreferenceChangeListener.onPreferenceChange(preference, value);
    }

    private static void SetupPreferenceSummary(
            PreferenceFragment fragment,
            String preferenceKey)
    {
        SetupPreferenceSummary(fragment, preferenceKey, true);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #_onPreferenceChangeListener
     */
    private static void SetupPreferenceSwitch(
            PreferenceFragment fragment,
            String preferenceKey,
            boolean isVisible)
    {
        Preference preference = fragment.findPreference(preferenceKey);

        if (!isVisible)
        {
            PreferenceScreen screen = fragment.getPreferenceScreen();
            screen.removePreference(preference);
            return;
        }

        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(_onPreferenceChangeListener);

        // Trigger the listener immediately with the preference's current value.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        Object value = preferences.getBoolean(preferenceKey, false);

        _onPreferenceChangeListener.onPreferenceChange(preference, value);
    }

    private static void SetupPreferenceSwitch(
            PreferenceFragment fragment,
            String preferenceKey)
    {
        SetupPreferenceSwitch(fragment, preferenceKey, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //  For API 24 testing, this code is necessary.
    //  For API 27 testing, this code is not necessary, but doesn't break.
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane()
    {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName)
    {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DisplayPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || ExportPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id = item.getItemId();
            if (id == android.R.id.home)
            {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DisplayPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            _displayFragment = this;
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_display);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences to their values.
            // When their values change, their summaries are updated to reflect the new value,
            // per the Android Design guidelines.
            SetupPreferenceSwitch(this, "pref_display_use_reporting_period");
            SetupPreferenceSummary(this, "pref_display_reporting_period");
            SetupPreferenceSummary(this, "pref_display_reporting_period_start");
            SetupPreferenceSummary(this, "pref_display_days_per_page");
            SetupPreferenceSummary(this, "pref_display_order");
            SetupPreferenceSummary(this, "pref_display_snap");
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id = item.getItemId();
            if (id == android.R.id.home)
            {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            boolean preOreo = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O;

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences to their values.
            // When their values change, their summaries are updated to reflect the new value,
            // per the Android Design guidelines.
            SetupPreferenceClick(this, "pref_notifications_system_settings", !preOreo);
            SetupPreferenceSummary(this, "pref_notifications_ringtone", preOreo);
            SetupPreferenceSummary(this, "pref_notifications_vibrate", false);
            SetupPreferenceSummary(this, "pref_notifications_delay");
            SetupPreferenceSummary(this, "pref_notifications_snooze");
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id = item.getItemId();
            if (id == android.R.id.home)
            {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ExportPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_export);
            setHasOptionsMenu(true);

            //boolean preOreo = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O;

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences to their values.
            // When their values change, their summaries are updated to reflect the new value,
            // per the Android Design guidelines.
            SetupPreferenceSummary(this, "pref_export_order");
            SetupPreferenceSummary(this, "pref_export_snap");
            SetupPreferenceSummary(this, "pref_export_format");
            SetupPreferenceSummary(this, "pref_export_delivery");
            SetupPreferenceSummary(this, "pref_export_email");
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id = item.getItemId();
            if (id == android.R.id.home)
            {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}

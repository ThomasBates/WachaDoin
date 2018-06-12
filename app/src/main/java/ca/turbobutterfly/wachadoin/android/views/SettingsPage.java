package ca.turbobutterfly.wachadoin.android.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.turbobutterfly.wachadoin.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsPage extends Fragment
{
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.settings_page, container, false);
    }

    @Override
    public String toString()
    {
        return "Settings";
    }
}

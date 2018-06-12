package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsStringItem;

public interface INotificationOptions
{
    OptionsBooleanItem enabled();
    OptionsBooleanItem popup_app();
    OptionsStringItem ringtone();
    OptionsBooleanItem vibrate();
    OptionsStringItem delay();
    OptionsStringItem snooze();
}

package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.IOptionsGroup;
import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsGroup;
import ca.turbobutterfly.core.options.OptionsStringItem;

class NotificationOptions extends OptionsGroup implements INotificationOptions
{
    private OptionsBooleanItem _enabled = new OptionsBooleanItem(this, "enabled", true);
    private OptionsBooleanItem _popup_app = new OptionsBooleanItem(this, "popup_app", false);
    private OptionsStringItem _ringtone = new OptionsStringItem(this, "ringtone", "");
    private OptionsBooleanItem _vibrate = new OptionsBooleanItem(this, "vibrate", true);
    private OptionsStringItem _delay = new OptionsStringItem(this, "delay", "15");
    private OptionsStringItem _snooze = new OptionsStringItem(this, "snooze", "1");

    public NotificationOptions(IOptionsGroup group, String name)
    {
        super(group, name);
    }

    public OptionsBooleanItem enabled() { return _enabled; }
    public OptionsBooleanItem popup_app() { return _popup_app; }
    public OptionsStringItem ringtone() { return _ringtone; }
    public OptionsBooleanItem vibrate() { return _vibrate; }
    public OptionsStringItem delay() { return _delay; }
    public OptionsStringItem snooze() { return _snooze; }
}

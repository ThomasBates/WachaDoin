package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.IOptionsGroup;
import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsGroup;
import ca.turbobutterfly.core.options.OptionsIntegerItem;
import ca.turbobutterfly.core.options.OptionsStringItem;

class NotificationOptions extends OptionsGroup implements INotificationOptions
{
    private OptionsBooleanItem _enabled = new OptionsBooleanItem(this, "enabled", true);
    private OptionsBooleanItem _popup_app = new OptionsBooleanItem(this, "popup_app", false);
    private OptionsStringItem _ringtone = new OptionsStringItem(this, "ringtone", "");
    private OptionsBooleanItem _vibrate = new OptionsBooleanItem(this, "vibrate", true);
    private OptionsIntegerItem _delay = new OptionsIntegerItem(this, "delay", 15, true);
    private OptionsIntegerItem _snooze = new OptionsIntegerItem(this, "snooze", 1, true);

    public NotificationOptions(IOptionsGroup group, String name)
    {
        super(group, name);
    }

    public OptionsBooleanItem enabled() { return _enabled; }
    public OptionsBooleanItem popup_app() { return _popup_app; }
    public OptionsStringItem ringtone() { return _ringtone; }
    public OptionsBooleanItem vibrate() { return _vibrate; }
    public OptionsIntegerItem delay() { return _delay; }
    public OptionsIntegerItem snooze() { return _snooze; }
}

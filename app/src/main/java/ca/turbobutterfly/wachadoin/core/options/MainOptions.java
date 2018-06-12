package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.IOptionsProvider;
import ca.turbobutterfly.core.options.OptionsRoot;

public class MainOptions extends OptionsRoot implements IMainOptions
{
    private IGeneralOptions _general = new GeneralOptions(this, "general");
    private IDisplayOptions _display = new DisplayOptions(this, "display");
    private INotificationOptions _notification = new NotificationOptions(this, "notification");
    private IExportOptions _export = new ExportOptions(this, "export");

    public MainOptions(IOptionsProvider provider, String name)
    {
        super(provider, name);
    }

    public IGeneralOptions General() { return _general; }
    public IDisplayOptions Display() { return _display; }
    public INotificationOptions Notification() { return _notification; }
    public IExportOptions Export() { return _export; }
}

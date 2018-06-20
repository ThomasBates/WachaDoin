package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.IOptionsGroup;
import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsGroup;
import ca.turbobutterfly.core.options.OptionsIntegerItem;
import ca.turbobutterfly.core.options.OptionsStringItem;

class ExportOptions extends OptionsGroup implements IExportOptions
{
    private OptionsStringItem _format = new OptionsStringItem(this, "format", "Text");
    private OptionsStringItem _delivery = new OptionsStringItem(this, "delivery", "Email");
    private OptionsStringItem _email = new OptionsStringItem(this, "email", "");

    public ExportOptions(IOptionsGroup group, String name)
    {
        super(group, name);
    }

    public OptionsStringItem format() { return _format; }
    public OptionsStringItem delivery() { return _delivery; }
    public OptionsStringItem email() { return _email; }
}

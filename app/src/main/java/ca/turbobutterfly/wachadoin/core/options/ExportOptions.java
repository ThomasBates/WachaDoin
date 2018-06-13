package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.IOptionsGroup;
import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsGroup;
import ca.turbobutterfly.core.options.OptionsIntegerItem;
import ca.turbobutterfly.core.options.OptionsStringItem;

class ExportOptions extends OptionsGroup implements IExportOptions
{
    private OptionsBooleanItem _group_by_date = new OptionsBooleanItem(this, "group_by_date", true);
    private OptionsStringItem _order = new OptionsStringItem(this, "order", "Ascending");
    private OptionsIntegerItem _snap = new OptionsIntegerItem(this, "snap", 1, true);
    private OptionsStringItem _format = new OptionsStringItem(this, "format", "Text");
    private OptionsStringItem _delivery = new OptionsStringItem(this, "delivery", "Email");
    private OptionsStringItem _email = new OptionsStringItem(this, "email", "");

    public ExportOptions(IOptionsGroup group, String name)
    {
        super(group, name);
    }

    public OptionsBooleanItem group_by_date() { return _group_by_date; }
    public OptionsStringItem order() { return _order; }
    public OptionsIntegerItem snap() { return _snap; }
    public OptionsStringItem format() { return _format; }
    public OptionsStringItem delivery() { return _delivery; }
    public OptionsStringItem email() { return _email; }
}

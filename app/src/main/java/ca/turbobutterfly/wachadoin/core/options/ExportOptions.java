package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.IOptionsGroup;
import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsGroup;
import ca.turbobutterfly.core.options.OptionsStringItem;

class ExportOptions extends OptionsGroup implements IExportOptions
{
    private OptionsBooleanItem _group_by_date = new OptionsBooleanItem(this, "group_by_date", false);
    private OptionsStringItem _order = new OptionsStringItem(this, "order", "");
    private OptionsStringItem _snap = new OptionsStringItem(this, "snap", "");
    private OptionsStringItem _format = new OptionsStringItem(this, "format", "");
    private OptionsStringItem _delivery = new OptionsStringItem(this, "delivery", "");

    public ExportOptions(IOptionsGroup group, String name)
    {
        super(group, name);
    }

    public OptionsBooleanItem group_by_date() { return _group_by_date; }
    public OptionsStringItem order() { return _order; }
    public OptionsStringItem snap() { return _snap; }
    public OptionsStringItem format() { return _format; }
    public OptionsStringItem delivery() { return _delivery; }
}

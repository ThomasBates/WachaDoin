package ca.turbobutterfly.wachadoin.core.options;

import java.util.Date;

import ca.turbobutterfly.core.options.IOptionsGroup;
import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsDateItem;
import ca.turbobutterfly.core.options.OptionsGroup;
import ca.turbobutterfly.core.options.OptionsIntegerItem;
import ca.turbobutterfly.core.options.OptionsStringItem;

class DisplayOptions extends OptionsGroup implements IDisplayOptions
{
    private OptionsBooleanItem _use_reporting_period = new OptionsBooleanItem(this, "use_reporting_period", false);
    private OptionsIntegerItem _reporting_period = new OptionsIntegerItem(this, "reporting_period", 7, true);
    private OptionsDateItem _reporting_period_start = new OptionsDateItem(this, "reporting_period_start", new Date());
    private OptionsIntegerItem _days_per_page = new OptionsIntegerItem(this, "days_per_page", 7, true);
    private OptionsBooleanItem _group_by_date = new OptionsBooleanItem(this, "group_by_date", false);
    private OptionsStringItem _order = new OptionsStringItem(this, "order", "Ascending");
    private OptionsIntegerItem _snap = new OptionsIntegerItem(this, "snap", 1, true);

    public DisplayOptions(IOptionsGroup group, String name)
    {
        super(group, name);
    }

    public OptionsBooleanItem use_reporting_period() { return _use_reporting_period; }
    public OptionsIntegerItem reporting_period() { return _reporting_period; }
    public OptionsDateItem reporting_period_start() { return _reporting_period_start; }
    public OptionsIntegerItem days_per_page() { return _days_per_page; }
    public OptionsBooleanItem group_by_date() { return _group_by_date; }
    public OptionsStringItem order() { return _order; }
    public OptionsIntegerItem snap() { return _snap; }
}

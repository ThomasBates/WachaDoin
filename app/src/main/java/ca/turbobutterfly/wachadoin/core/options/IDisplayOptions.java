package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsDateItem;
import ca.turbobutterfly.core.options.OptionsIntegerItem;
import ca.turbobutterfly.core.options.OptionsStringItem;

public interface IDisplayOptions
{
    OptionsBooleanItem use_reporting_period();
    OptionsIntegerItem reporting_period();
    OptionsDateItem reporting_period_start();
    OptionsIntegerItem days_per_page();
    OptionsBooleanItem group_by_date();
    OptionsStringItem order();
    OptionsIntegerItem snap();
}

package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsIntegerItem;
import ca.turbobutterfly.core.options.OptionsStringItem;

public interface IExportOptions
{
    OptionsStringItem format();
    OptionsStringItem delivery();
    OptionsStringItem email();
}

package ca.turbobutterfly.wachadoin.core.options;

import ca.turbobutterfly.core.options.IOptionsGroup;
import ca.turbobutterfly.core.options.OptionsBooleanItem;
import ca.turbobutterfly.core.options.OptionsGroup;

class GeneralOptions extends OptionsGroup implements IGeneralOptions
{
    private OptionsBooleanItem _auto_close = new OptionsBooleanItem(this, "auto_close", false);

    public GeneralOptions(IOptionsGroup group, String name)
    {
        super(group, name);
    }

    public OptionsBooleanItem auto_close() { return _auto_close; }
}

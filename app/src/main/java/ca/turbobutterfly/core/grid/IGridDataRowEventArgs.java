package ca.turbobutterfly.core.grid;

import ca.turbobutterfly.core.events.IEventArgs;

public interface IGridDataRowEventArgs extends IEventArgs
{
    IGridDataRow Row();
    IGridDataColumn Column();
}

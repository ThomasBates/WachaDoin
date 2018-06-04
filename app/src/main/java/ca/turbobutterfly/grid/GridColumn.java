package ca.turbobutterfly.grid;

public class GridColumn
{
    private String _displayName;
    private String _fieldName;
    private int _width;

    public GridColumn(String displayName, String fieldName, int width)
    {
        _displayName = displayName;
        _fieldName = fieldName;
        _width = width;
    }

    public String DisplayName()
    {
        return _displayName;
    }

    public String FieldName()
    {
        return _fieldName;
    }

    public int Width()
    {
        return _width;
    }
}

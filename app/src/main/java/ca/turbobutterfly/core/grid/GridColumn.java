package ca.turbobutterfly.core.grid;

public class GridColumn
{
    private String _caption;
    private String _fieldName;
    private int _width;

    public GridColumn(String caption, String fieldName, int width)
    {
        _caption = caption;
        _fieldName = fieldName;
        _width = width;
    }

    public String Caption()
    {
        return _caption;
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

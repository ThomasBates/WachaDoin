package ca.turbobutterfly.wachadoin.core.data;

class DataLogEntry implements IDataLogEntry
{
    private String _startTime;
    private String _endTime;
    private String _logText;

    @Override
    public String StartTime()
    {
        return _startTime;
    }

    @Override
    public void StartTime(String startTime)
    {
        _startTime = startTime;
    }

    @Override
    public String EndTime()
    {
        return _endTime;
    }

    @Override
    public void EndTime(String endTime)
    {
        _endTime = endTime;
    }

    @Override
    public String LogText()
    {
        return _logText;
    }

    @Override
    public void LogText(String logText)
    {
        _logText = logText;
    }
}

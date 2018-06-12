package ca.turbobutterfly.wachadoin.core.data;

public interface IDataLogEntry
{
    String StartTime();
    void StartTime(String startTime);

    String EndTime();
    void EndTime(String endTime);

    String LogText();
    void LogText(String logText);
}

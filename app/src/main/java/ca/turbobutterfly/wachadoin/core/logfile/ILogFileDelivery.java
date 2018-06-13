package ca.turbobutterfly.wachadoin.core.logfile;

public interface ILogFileDelivery
{
    Object Extra(String key);
    void Extra(String key, Object value);

    boolean DeliverLogFile(String logFile);
}

package ca.turbobutterfly.wachadoin.core.logfile;

public interface ILogFileDeliveryFactory
{
    ILogFileDelivery GetLogFileDelivery(String type);
}

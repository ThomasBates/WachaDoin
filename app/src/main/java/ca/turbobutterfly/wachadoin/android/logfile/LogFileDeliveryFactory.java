package ca.turbobutterfly.wachadoin.android.logfile;

import android.content.Context;

import ca.turbobutterfly.wachadoin.core.logfile.ILogFileDelivery;
import ca.turbobutterfly.wachadoin.core.logfile.ILogFileDeliveryFactory;

public class LogFileDeliveryFactory implements ILogFileDeliveryFactory
{
    private final Context _context;

    public LogFileDeliveryFactory(Context context)
    {
        _context = context;
    }

    @Override
    public ILogFileDelivery GetLogFileDelivery(String type)
    {
        switch (type)
        {
            case "Email":
                return new LogFileEmailDelivery(_context);
            case "USB":
                return new LogFileUSBDelivery(_context);
        }
        return null;
    }
}

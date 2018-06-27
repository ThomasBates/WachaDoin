package ca.turbobutterfly.wachadoin.android.logfile;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

import ca.turbobutterfly.wachadoin.core.logfile.ILogFileDelivery;

public class LogFileUSBDelivery implements ILogFileDelivery
{
    private final Context _context;

    private String _fileExtension = ".txt";

    public LogFileUSBDelivery(Context context)
    {
        _context = context;
    }

    @Override
    public Object Extra(String key)
    {
        switch (key)
        {
            case "FileExtension":
                return _fileExtension;
            default:
                return null;
        }
    }

    @Override
    public void Extra(String key, Object value)
    {
        switch (key)
        {
            case "FileExtension":
                _fileExtension = value.toString();
                break;
        }
    }

    @Override
    public boolean DeliverLogFile(String logFile)
    {
        try
        {
            //  Ensure external storage is available.
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state))
            {
                //  External storage is not available.
                return false;
            }

            File root = new File(Environment.getExternalStorageDirectory(), "WachaDoin");
            if (!root.exists())
            {
                //noinspection ResultOfMethodCallIgnored
                root.mkdirs();
            }

            //  Find unused file name.
            String filename = "WachaDoinLog" + _fileExtension;
            File file;

            int retry = 0;
            while (true)
            {
                file = new File(root, filename);
                if (!file.exists())
                {
                    break;
                }

                retry++;
                filename = String.format(Locale.getDefault(), "WachaDoinLog (%d)%s", retry, _fileExtension);
            }

            //  Store logFile to external storage.
            FileWriter writer = new FileWriter(file);
            writer.append(logFile);
            writer.flush();
            writer.close();

            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}

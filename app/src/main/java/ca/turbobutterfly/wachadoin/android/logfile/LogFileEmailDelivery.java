package ca.turbobutterfly.wachadoin.android.logfile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileWriter;

import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.core.logfile.ILogFileDelivery;

public class LogFileEmailDelivery implements ILogFileDelivery
{
    private final Context _context;

    private String _emailAddress;
    private String _fileExtension = ".txt";

    public LogFileEmailDelivery(Context context)
    {
        _context = context;
    }

    @Override
    public Object Extra(String key)
    {
        switch (key)
        {
            case "EmailAddress":
                return _emailAddress;
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
            case "EmailAddress":
                _emailAddress = value.toString();
                break;
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
            //  Store logFile to internal storage.
            String filename = "WachaDoinLog" + _fileExtension;

            File file = new File(_context.getCacheDir(), filename);

            if (file.exists())
            {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }

            FileWriter writer = new FileWriter(file);
            writer.append(logFile);
            writer.flush();
            writer.close();

            //  Send logFile to email address.
            String authority = _context.getString(R.string.file_provider_authority);
            Uri fileUri = FileProvider.getUriForFile(_context, authority, file);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{_emailAddress});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WachaDoin Log Export");
            emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

            Intent chosenIntent = Intent.createChooser(emailIntent, "Send mail...");
            _context.startActivity(chosenIntent);

            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            return false;
        }
    }
}

//  https://stackoverflow.com/questions/9974987/how-to-send-an-email-with-a-file-attachment-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

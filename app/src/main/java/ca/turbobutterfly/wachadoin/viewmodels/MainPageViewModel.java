package ca.turbobutterfly.wachadoin.viewmodels;

import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.turbobutterfly.mvvm.Command;
import ca.turbobutterfly.mvvm.CommandListener;
import ca.turbobutterfly.mvvm.ICommand;
import ca.turbobutterfly.viewmodels.ViewModel;
import ca.turbobutterfly.wachadoin.data.IDataProvider;

public class MainPageViewModel extends ViewModel
{
    //  Variables ----------------------------------------------------------------------------------

    //  Injected dependencies
    private IDataProvider _dataProvider;
    private SharedPreferences _preferences;
    private SimpleDateFormat _timeFormat;
    private String _instructionFormat;

    //  Property backers
    private Date _startTime;
    private Date _endTime;
    private String _timeText;
    private String _instructionText;
    private String _logText;
    private String[] _recentLogText;
    private ICommand _saveCommand = new Command(new CommandListener()
    {
        @Override
        public void Execute(Object parameters)
        {
            DoSave();
        }
    });
    private ICommand _pauseCommand = new Command(new CommandListener()
    {
        @Override
        public void Execute(Object parameters)
        {
            DoPause();
        }
    });
    private ICommand _resumeCommand = new Command(new CommandListener()
    {
        @Override
        public void Execute(Object parameters)
        {
            DoResume();
        }
    });

    //  runs without a timer by reposting this handler at the end of the runnable
    private Handler _timerHandler = new Handler();
    private Runnable _timerRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            _endTime = new Date();

            String formattedTime = _timeFormat.format(_endTime);
            TimeText(formattedTime);

            long milliseconds = _endTime.getTime() - _startTime.getTime();
            long minutes = milliseconds / (60 * 1000);
            String plural = ((minutes == 1) ? "" : "s");
            String formattedInstruction = String.format(_instructionFormat, minutes, plural);
            InstructionText(formattedInstruction);

            _timerHandler.postDelayed(this, 500);
        }
    };

    //  Constructors -------------------------------------------------------------------------------

    public MainPageViewModel(
            IDataProvider dataProvider,
            SharedPreferences preferences,
            SimpleDateFormat timeFormat,
            String instructionFormat)
    {
        _dataProvider = dataProvider;
        _preferences = preferences;
        _timeFormat = timeFormat;
        _instructionFormat = instructionFormat;

        _endTime = new Date();

        _recentLogText = _dataProvider.GetRecentLogText(true);
        _startTime = _dataProvider.GetLastEndTime();
        _logText = _dataProvider.GetLastLogText();
    }

    //  Properties ---------------------------------------------------------------------------------

    public String TimeText()
    {
        return _timeText;
    }

    private void TimeText(String timeText)
    {
        if (TextUtils.equals(_timeText, timeText))
        {
            return;
        }
        _timeText = timeText;
        NotifyPropertyChanged("TimeText");
    }

    public String InstructionText()
    {
        return _instructionText;
    }

    private void InstructionText(String instructionText)
    {
        if (TextUtils.equals(_instructionText, instructionText))
        {
            return;
        }
        _instructionText = instructionText;
        NotifyPropertyChanged("InstructionText");
    }

    public String LogText()
    {
        return _logText;
    }

    public void LogText(String logText)
    {
        if (TextUtils.equals(_logText, logText))
        {
            return;
        }
        _logText = logText;
        NotifyPropertyChanged("LogText");
    }

    public String[] RecentLogText()
    {
        return _recentLogText;
    }

    private void RecentLogText(String[] recentLogText)
    {
        if (_recentLogText == recentLogText)
        {
            return;
        }
        _recentLogText = recentLogText;
        NotifyPropertyChanged("RecentLogText");
    }

    public ICommand SaveCommand()
    {
        return _saveCommand;
    }

    public ICommand PauseCommand()
    {
        return _pauseCommand;
    }

    public ICommand ResumeCommand()
    {
        return _resumeCommand;
    }

    //  Methods ------------------------------------------------------------------------------------

    private void DoSave()
    {
        //  If saving the log takes longer than a second,
        //  then _endTime will be different after SaveLogEntry().
        //  Save a snapshot and update _startTime with that.
        Date endTime = _endTime;

        boolean saveOK = _dataProvider.SaveLogEntry(_startTime, endTime, _logText);
        if (saveOK)
        {
            RecentLogText(_dataProvider.GetRecentLogText(true));
            _startTime = endTime;

            boolean autoClose = _preferences.getBoolean("pref_general_auto_close", false);
            if (autoClose)
            {
                NotifyPropertyChanged("AutoClose");
            }
        }
    }

    private void DoPause()
    {
        _timerHandler.removeCallbacks(_timerRunnable);
    }

    private void DoResume()
    {
        _timerHandler.postDelayed(_timerRunnable, 0);

        RecentLogText(_dataProvider.GetRecentLogText(true));
        _startTime = _dataProvider.GetLastEndTime();
    }
}

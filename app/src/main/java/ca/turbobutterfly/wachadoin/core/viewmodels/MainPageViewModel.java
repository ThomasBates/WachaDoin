package ca.turbobutterfly.wachadoin.core.viewmodels;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.turbobutterfly.android.utils.RunnableTimer;

import ca.turbobutterfly.core.events.EventHandler;
import ca.turbobutterfly.core.events.IEventArgs;
import ca.turbobutterfly.core.events.IEventHandler;
import ca.turbobutterfly.core.mvvm.Command;
import ca.turbobutterfly.core.mvvm.CommandListener;
import ca.turbobutterfly.core.mvvm.ICommand;
import ca.turbobutterfly.core.utils.ITimer;
import ca.turbobutterfly.core.mvvm.ViewModel;

import ca.turbobutterfly.wachadoin.core.data.IDataProvider;

import ca.turbobutterfly.wachadoin.core.options.IMainOptions;

public class MainPageViewModel extends ViewModel
{
    //  Variables ----------------------------------------------------------------------------------

    //  Injected dependencies
    private IDataProvider _dataProvider;
    private IMainOptions _mainOptions;
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

    private ITimer _timer;
    private IEventHandler _timerTickEventHandler = new EventHandler(){
                @Override
                public void HandleEvent(Object sender, IEventArgs eventArgs)
                {
                    _endTime = new Date();

                    String formattedTime = _timeFormat.format(_endTime);
                    TimeText(formattedTime);

                    long milliseconds = _endTime.getTime() - _startTime.getTime();
                    long minutes = milliseconds / (60 * 1000);
                    String plural = ((minutes == 1) ? "" : "s");
                    String formattedInstruction = String.format(_instructionFormat, minutes, plural);
                    InstructionText(formattedInstruction);
                }
            };

    //  Constructors -------------------------------------------------------------------------------

    public MainPageViewModel(
            IDataProvider dataProvider,
            IMainOptions mainOptions,
            SimpleDateFormat timeFormat,
            String instructionFormat)
    {
        _dataProvider = dataProvider;
        _mainOptions = mainOptions;
        _timeFormat = timeFormat;
        _instructionFormat = instructionFormat;

        _endTime = new Date();

        _recentLogText = _dataProvider.GetRecentLogText(true);
        _startTime = _dataProvider.GetLastEndTime();
        _logText = _dataProvider.GetLastLogText();

        _timer = new RunnableTimer(500);
        _timer.OnTick().Subscribe(_timerTickEventHandler);
    }

    //  Properties ---------------------------------------------------------------------------------

    public String TimeText()
    {
        return _timeText;
    }

    private void TimeText(String timeText)
    {
        if (_timeText == null ? timeText == null : _timeText.equals(timeText))
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
        if (_instructionText == null ? instructionText == null : _instructionText.equals(instructionText))
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
        if (_logText == null ? logText == null : _logText.equals(logText))
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

            boolean autoClose = _mainOptions.General().auto_close().Value();
            if (autoClose)
            {
                NotifyPropertyChanged("AutoClose");
            }
        }
    }

    private void DoPause()
    {
        _timer.Active(false);
        //_timerHandler.removeCallbacks(_timerRunnable);
    }

    private void DoResume()
    {
        _timer.Active(true);
        //_timerHandler.postDelayed(_timerRunnable, 0);

        RecentLogText(_dataProvider.GetRecentLogText(true));
        _startTime = _dataProvider.GetLastEndTime();
    }
}

package ca.turbobutterfly.wachadoin.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import ca.turbobutterfly.events.EventHandler;
import ca.turbobutterfly.events.IEventArgs;
import ca.turbobutterfly.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.views.FragmentView;

import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.services.NotificationHelper;
import ca.turbobutterfly.wachadoin.support.Bootstrapper;
import ca.turbobutterfly.wachadoin.viewmodels.MainPageViewModel;

public class MainPage extends FragmentView
{
    private MainPageViewModel _dataContext;

    private TextView _timeTextView;
    private TextView _instructionTextView;
    private EditText _logEditText;
    private ListPopupWindow _listPopupWindow;

    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();

            switch (propertyName)
            {
                case "TimeText":
                    if (!TextUtils.equals(_timeTextView.getText().toString(), _dataContext.TimeText()))
                    {
                        _timeTextView.setText(_dataContext.TimeText());
                    }
                    break;
                case "InstructionText":
                    if (!TextUtils.equals(_instructionTextView.getText().toString(), _dataContext.InstructionText()))
                    {
                        _instructionTextView.setText(_dataContext.InstructionText());
                        //_logEditText.setHint(_dataContext.InstructionText());
                    }
                    break;
                case "LogText":
                    if (!TextUtils.equals(_logEditText.getText().toString(), _dataContext.LogText()))
                    {
                        _logEditText.setText(_dataContext.LogText());
                    }
                    break;
                case "RecentLogText":
                    String recentLogText[] = _dataContext.RecentLogText();
                    ArrayAdapter arrayAdapter = new ArrayAdapter(
                            getContext(),
                            R.layout.wachadoin_dropdown,
                            recentLogText);
                    _listPopupWindow.setAdapter(arrayAdapter);
                    break;
            }
        }
    };

    @Override
    public String toString()
    {
        return "Wacha Doin?";
    }

    @Override
    public void DataContext(Object dataContext)
    {
        if (_dataContext != null)
        {
            _dataContext.PropertyChanged().Unsubscribe(_dataContextPropertyChangedEventHandler);
        }

        _dataContext = (MainPageViewModel) dataContext;

        if (_dataContext != null)
        {
            _dataContext.PropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);
        }

        InitializeBindings();
    }

    private void InitializeBindings()
    {
        if (_dataContext == null)
        {
            return;
        }
        if (_timeTextView == null)
        {
            return;
        }

        _timeTextView.setText(_dataContext.TimeText());
        _instructionTextView.setText(_dataContext.InstructionText());
        _logEditText.setText(_dataContext.LogText());

        String recentLogText[] = _dataContext.RecentLogText();
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                getContext(),
                R.layout.wachadoin_dropdown,
                recentLogText);
        _listPopupWindow.setAdapter(arrayAdapter);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.wachadoin_main_page, container, false);

        _timeTextView = view.findViewById(R.id.timeTextView);
        _instructionTextView = view.findViewById(R.id.instructionTextView);
        _logEditText = view.findViewById(R.id.logEditText);

        _listPopupWindow = new ListPopupWindow(getContext());
        _listPopupWindow.setAnchorView(_logEditText);
        _listPopupWindow.setModal(true);

        _logEditText.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    int dWidth = ((EditText) v).getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    if (event.getX() >= (v.getWidth() - dWidth))
                    {
                        _listPopupWindow.show();
                        return true;
                    }
                }
                return false;
            }
        });
        _listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String item = parent.getItemAtPosition(position).toString();
                _logEditText.setText(item);
                _listPopupWindow.dismiss();
            }
        });
        _logEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s)
            {
                if (_dataContext != null)
                {
                    _dataContext.LogText(_logEditText.getText().toString());
                }
            }
        });

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (_dataContext != null)
                {
                    _dataContext.SaveCommand().Execute(null);
                }
            }
        });

        InitializeBindings();

        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (_dataContext != null)
        {
            _dataContext.PauseCommand().Execute(null);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (_dataContext != null)
        {
            _dataContext.ResumeCommand().Execute(null);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        NotificationHelper helper = Bootstrapper.ComposeNotificationHelper(getContext());
        helper.CancelNotification();
        helper.CancelScheduledNotification();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        NotificationHelper helper = Bootstrapper.ComposeNotificationHelper(getContext());
        helper.ScheduleNotification();
    }
}

package ca.turbobutterfly.android.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment
{
    private int _titleId;
    private Date _initialValue;
    private TimePickerDialog.OnTimeSetListener _listener;

    public void TitleID(int titleID)
    {
        _titleId = titleID;
    }

    public void InitialValue(Date initialValue)
    {
        _initialValue = initialValue;
    }

    public void Listener(TimePickerDialog.OnTimeSetListener listener)
    {
        _listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(_initialValue);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        Dialog dialog = new TimePickerDialog(getActivity(), _listener, hour, minute, true);
        dialog.setTitle(_titleId);
        return dialog;
    }
}

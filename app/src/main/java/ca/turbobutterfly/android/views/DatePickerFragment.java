package ca.turbobutterfly.android.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
{
    private int _titleId;
    private Date _initialValue;
    private DatePickerDialog.OnDateSetListener _listener;

    public void TitleID(int titleID)
    {
        _titleId = titleID;
    }

    public void InitialValue(Date initialValue)
    {
        _initialValue = initialValue;
    }

    public void Listener(DatePickerDialog.OnDateSetListener listener)
    {
        _listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(_initialValue);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        Dialog dialog = new DatePickerDialog(getActivity(), _listener, year, month, day);
        dialog.setTitle(_titleId);
        return dialog;
    }
}

package ca.turbobutterfly.android.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import ca.turbobutterfly.android.views.DatePickerFragment;
import ca.turbobutterfly.android.views.TimePickerFragment;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.wachadoin.R;

public class ViewUtils
{
    public static void OpenInputDialog(Fragment fragment, int titleId, String value, final ReturnValueDelegate returnValueDelegate)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(fragment.getContext());
        LayoutInflater inflater = fragment.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.input_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText inputEditText = dialogView.findViewById(R.id.inputEditText);
        inputEditText.setText(value);

        dialogBuilder.setTitle(titleId);
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        returnValueDelegate.HandleReturnValue(inputEditText.getText().toString());
                    }
                });
        dialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //pass
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public static void OpenListDialog(Fragment fragment, int titleId, int entriesId, int valuesId, String initialValue, final ReturnValueDelegate returnValueDelegate)
    {
        Resources res = fragment.getResources();
        final String[] entries = res.getStringArray(entriesId);
        final String[] values = res.getStringArray(valuesId);

        int currentIndex = GetListIndex(fragment, initialValue, valuesId);

        AlertDialog dialog = new AlertDialog.Builder(fragment.getContext())
                .setCancelable(true)
                .setTitle(titleId)
                .setSingleChoiceItems(entries, currentIndex, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick( DialogInterface dialog, int whichButton)
                    {
                        ListView listView = ((AlertDialog)dialog).getListView();
                        int which = listView.getCheckedItemPosition();

                        returnValueDelegate.HandleReturnValue(values[which]);
                    }
                })
                .create();
        dialog.show();
    }

    public static void OpenDatePicker(Fragment fragment, int titleID, Date value, final ReturnValueDelegate returnValueDelegate)
    {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.TitleID(titleID);
        datePicker.InitialValue(value);
        datePicker.Listener(new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth, 0, 0, 0);
                returnValueDelegate.HandleReturnValue(calendar.getTime());
            }
        });
        datePicker.show(fragment.getFragmentManager(), "datePicker");
    }

    public static void OpenTimePicker(Fragment fragment, int titleID, Date value, final ReturnValueDelegate returnValueDelegate)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.TitleID(titleID);
        timePicker.InitialValue(value);
        timePicker.Listener(new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hourOfDay, minute, 0);
                returnValueDelegate.HandleReturnValue(calendar.getTime());
            }
        });
        timePicker.show(fragment.getFragmentManager(), "timePicker");
    }

    private static int GetListIndex(Fragment fragment, String value, int valuesId)
    {
        Resources res = fragment.getResources();
        String[] values = res.getStringArray(valuesId);

        for (int index = 0; index < values.length; index++)
        {
            if (TextUtils.equals(value, values[index]))
            {
                return index;
            }
        }

        return -1;
    }
}

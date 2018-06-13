package ca.turbobutterfly.wachadoin.android.views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import ca.turbobutterfly.core.events.EventHandler;
import ca.turbobutterfly.core.events.IEventArgs;
import ca.turbobutterfly.core.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.android.views.FragmentView;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.core.viewmodels.ExportPageViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExportPage extends FragmentView
{
    private ExportPageViewModel _dataContext;

    private TextView _startDateTextView;
    private TextView _endDateTextView;
    private SwitchCompat _groupByDateSwitch;
    private TextView _logOrderTextView;
    private TextView _snapTimeTextView;
    private TextView _fileFormatTextView;
    private TextView _fileDeliveryTextView;
    private TextView _emailAddressTextView;
    private TextView _statusTextView;

    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();
            String entry;
            switch (propertyName)
            {
                case "StartDateText":
                    _startDateTextView.setText(_dataContext.StartDateText());
                    break;

                case "EndDateText":
                    _endDateTextView.setText(_dataContext.EndDateText());
                    break;

                case "GroupByDate":
                    _groupByDateSwitch.setChecked(_dataContext.GroupByDate());
                    break;

                case "LogOrder":
                    entry = GetListEntry(
                            _dataContext.LogOrder(),
                            R.array.pref_order_list_entries,
                            R.array.pref_order_list_values);
                    _logOrderTextView.setText(entry);
                    break;

                case "SnapTime":
                    entry = GetListEntry(
                            _dataContext.SnapTime().toString(),
                            R.array.pref_time_span_list_entries,
                            R.array.pref_time_span_list_values);
                    _snapTimeTextView.setText(entry);
                    break;

                case "FileFormat":
                    entry = GetListEntry(
                            _dataContext.FileFormat(),
                            R.array.pref_format_list_entries,
                            R.array.pref_format_list_values);
                    _fileFormatTextView.setText(entry);
                    break;

                case "FileDelivery":
                    entry = GetListEntry(
                            _dataContext.FileDelivery(),
                            R.array.pref_delivery_list_entries,
                            R.array.pref_delivery_list_values);
                    _fileDeliveryTextView.setText(entry);
                    break;

                case "EmailAddress":
                    _emailAddressTextView.setText(_dataContext.EmailAddress());
                    break;

                case "StatusColor":
                    _statusTextView.setTextColor(_dataContext.StatusColor());
                    break;

                case "StatusText":
                    _statusTextView.setText(_dataContext.StatusText());
                    break;
            }
        }
    };

    private String GetListEntry(String value, int entriesId, int valuesId)
    {
        Resources res = getResources();
        String[] entries = res.getStringArray(entriesId);

        int index = GetListIndex(value, valuesId);

        if (index < 0)
        {
            return "";
        }

        return entries[index];
    }

    private int GetListIndex(String value, int valuesId)
    {
        Resources res = getResources();
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

    @Override
    public String toString()
    {
        return "Export Log";
    }

    @Override
    public void DataContext(Object dataContext)
    {
        if (_dataContext != null)
        {
            _dataContext.PropertyChanged().Unsubscribe(_dataContextPropertyChangedEventHandler);
        }

        _dataContext = (ExportPageViewModel) dataContext;

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

        if (_startDateTextView == null)
        {
            return;
        }

        _startDateTextView.setText(_dataContext.StartDateText());
        _endDateTextView.setText(_dataContext.EndDateText());
        _groupByDateSwitch.setChecked(_dataContext.GroupByDate());

        String entry = GetListEntry(
                _dataContext.LogOrder(),
                R.array.pref_order_list_entries,
                R.array.pref_order_list_values);
        _logOrderTextView.setText(entry);

        entry = GetListEntry(
                _dataContext.SnapTime().toString(),
                R.array.pref_time_span_list_entries,
                R.array.pref_time_span_list_values);
        _snapTimeTextView.setText(entry);

        entry = GetListEntry(
                _dataContext.FileFormat(),
                R.array.pref_format_list_entries,
                R.array.pref_format_list_values);
        _fileFormatTextView.setText(entry);

        entry = GetListEntry(
                _dataContext.FileDelivery(),
                R.array.pref_delivery_list_entries,
                R.array.pref_delivery_list_values);
        _fileDeliveryTextView.setText(entry);

        _emailAddressTextView.setText(_dataContext.EmailAddress());
        _statusTextView.setTextColor(_dataContext.StatusColor());
        _statusTextView.setText(_dataContext.StatusText());
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.export_page, container, false);

        RelativeLayout layout = view.findViewById(R.id.startDateLayout);
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenStartDatePicker();
            }
        });
        _startDateTextView = view.findViewById(R.id.startDateTextView);

        layout = view.findViewById(R.id.endDateLayout);
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenEndDatePicker();
            }
        });
        _endDateTextView = view.findViewById(R.id.endDateTextView);

        layout = view.findViewById(R.id.groupByDateLayout);
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _dataContext.GroupByDate(!_dataContext.GroupByDate());
            }
        });
        _groupByDateSwitch = view.findViewById(R.id.groupByDateSwitch);
        _groupByDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                _dataContext.GroupByDate(_groupByDateSwitch.isChecked());
            }
        });

        layout = view.findViewById(R.id.logOrderLayout);
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenLogOrderDialog();
            }
        });
        _logOrderTextView = view.findViewById(R.id.logOrderTextView);

        layout = view.findViewById(R.id.snapTimeLayout);
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenSnapTimeDialog();
            }
        });
        _snapTimeTextView = view.findViewById(R.id.snapTimeTextView);

        layout = view.findViewById(R.id.fileFormatLayout);
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenFileFormatDialog();
            }
        });
        _fileFormatTextView = view.findViewById(R.id.fileFormatTextView);

        layout = view.findViewById(R.id.fileDeliveryLayout);
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenFileDeliveryDialog();
            }
        });
        _fileDeliveryTextView = view.findViewById(R.id.fileDeliveryTextView);

        layout = view.findViewById(R.id.emailAddressLayout);
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenEmailAddressDialog();
            }
        });
        _emailAddressTextView = view.findViewById(R.id.emailAddressTextView);

        Button exportButton = view.findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (_dataContext != null)
                {
                    _dataContext.ExportCommand().Execute(null);
                }
            }
        });

        _statusTextView = view.findViewById(R.id.statusTextView);

        InitializeBindings();

        CheckStoragePermission();

        return view;
    }

    private void CheckStoragePermission()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED)
            {
                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permissions, 1);
            }
        }
    }

    private void OpenStartDatePicker()
    {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker._initialDate = _dataContext.StartTime();
        datePicker._listener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth, 0, 0);
                _dataContext.StartTime(c.getTime());
            }
        };
        datePicker.show(getFragmentManager(), "datePicker");
    }

    private void OpenEndDatePicker()
    {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker._initialDate = _dataContext.EndTime();
        datePicker._listener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth, 0, 0);
                _dataContext.EndTime(c.getTime());
            }
        };

        datePicker.show(getFragmentManager(), "datePicker");
    }

    public static interface ReturnValueDelegate
    {
        public void doReturnValue(String value);
    }

    private void OpenLogOrderDialog()
    {
        OpenListDialog(
                R.string.pref_export_order_title,
                R.array.pref_order_list_entries,
                R.array.pref_order_list_values,
                _dataContext.LogOrder(),
                new ReturnValueDelegate()
                {
                    @Override
                    public void doReturnValue(String value)
                    {
                        _dataContext.LogOrder(value);
                    }
                });
    }

    private void OpenSnapTimeDialog()
    {
        OpenListDialog(
                R.string.pref_export_snap_title,
                R.array.pref_time_span_list_entries,
                R.array.pref_time_span_list_values,
                _dataContext.SnapTime().toString(),
                new ReturnValueDelegate()
                {
                    @Override
                    public void doReturnValue(String value)
                    {
                        _dataContext.SnapTime(Integer.parseInt(value));
                    }
                });
    }

    private void OpenFileFormatDialog()
    {
        OpenListDialog(
                R.string.pref_export_format_title,
                R.array.pref_format_list_entries,
                R.array.pref_format_list_values,
                _dataContext.FileFormat(),
                new ReturnValueDelegate()
                {
                    @Override
                    public void doReturnValue(String value)
                    {
                        _dataContext.FileFormat(value);
                    }
                });
    }

    private void OpenFileDeliveryDialog()
    {
        OpenListDialog(
                R.string.pref_export_delivery_title,
                R.array.pref_delivery_list_entries,
                R.array.pref_delivery_list_values,
                _dataContext.FileDelivery(),
                new ReturnValueDelegate()
                {
                    @Override
                    public void doReturnValue(String value)
                    {
                        _dataContext.FileDelivery(value);
                    }
                });
    }

    private void OpenEmailAddressDialog()
    {
        OpenInputDialog(
                R.string.pref_export_email_title,
                _dataContext.EmailAddress(),
                new ReturnValueDelegate()
                {
                    @Override
                    public void doReturnValue(String value)
                    {
                        _dataContext.EmailAddress(value);
                    }
                });
    }

    public static class DatePickerFragment
            extends DialogFragment
    {
        Date _initialDate;
        DatePickerDialog.OnDateSetListener _listener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(_initialDate);

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), _listener, year, month, day);
        }
    }

    private void OpenListDialog(int titleId, int entriesId, int valuesId, String initialValue, final ReturnValueDelegate returnValueDelegate)
    {
        Resources res = getResources();
        final String[] entries = res.getStringArray(entriesId);
        final String[] values = res.getStringArray(valuesId);

        int currentIndex = GetListIndex(initialValue, valuesId);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(titleId)
                .setSingleChoiceItems(entries, currentIndex, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick( DialogInterface dialog, int whichButton)
                    {
                        ListView listView = ((AlertDialog)dialog).getListView();
                        int which = listView.getCheckedItemPosition();

                        returnValueDelegate.doReturnValue(values[which]);
                    }
                })
                .create();
        dialog.show();
    }

    private void OpenInputDialog(int titleId, String value, final ReturnValueDelegate returnValueDelegate)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.input_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText inputEditText = (EditText) dialogView.findViewById(R.id.inputEditText);
        inputEditText.setText(value);

        dialogBuilder.setTitle(titleId);
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        returnValueDelegate.doReturnValue(inputEditText.getText().toString());
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
}

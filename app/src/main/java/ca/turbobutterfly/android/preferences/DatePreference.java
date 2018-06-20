package ca.turbobutterfly.android.preferences;

//  https://github.com/bostonandroid/DatePreference/blob/master/DatePreference/src/org/bostonandroid/datepreference/DatePreference.java
//  edited.

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ca.turbobutterfly.core.utils.DateUtils;

public class DatePreference
        extends DialogPreference
        implements DatePicker.OnDateChangedListener
{
    private Date _defaultDate = new Date();
    private Date _date;
    private Date _changedDate;
    private DatePicker _datePicker;

    @SuppressWarnings("unused")
    public DatePreference(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @SuppressWarnings("unused")
    public DatePreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Produces the date used for the date picker.
     * It is set from {@link #onSetInitialValue(boolean, Object)}.
     *
     * @return the Date for the date picker
     */
    public Date getDate()
    {
        return _date;
    }

    /**
     * Set the selected date to the specified string.
     *
     * @param date The date.
     */
    public void setDate(Date date)
    {
        _date = date;
        persistDate(date);
        showSummary(date);
    }

    private void persistDate(Date date)
    {
        String value = dateToPersistedString(date);
        persistString(value);
    }

    private void showSummary(Date date)
    {
        String summary = dateToSummaryString(date);
        setSummary(summary);
    }

    /**
     * Produces a DatePicker set to the date produced by {@link #getDate()}. When
     * overriding be sure to call the super.
     *
     * @return a DatePicker with the date set
     */
    @Override
    protected View onCreateDialogView()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());

        _datePicker = new DatePicker(getContext());
        _datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                this);
        return _datePicker;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index)
    {
        return a.getString(index);
    }

    /**
     * Called when the date picker is shown or restored. If it's a restore it gets
     * the persisted value, otherwise it persists the value.
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue)
    {
        //  set _defaultDate from defaultValue
        _defaultDate = xmlStringToDate((String)defaultValue, _defaultDate);

        //  set _date from persisted value or _defaultDate.
        if (restorePersistedValue)
        {
            String defaultString = dateToPersistedString(_defaultDate);
            String persistedString = getPersistedString(defaultString);
            Date persistedDate = persistedStringToDate(persistedString, _defaultDate);
            setDate(persistedDate);
        }
        else
        {
            setDate(_defaultDate);
        }
    }

    /**
     * Called when Android pauses the activity.
     */
    @Override
    protected Parcelable onSaveInstanceState()
    {
        if (isPersistent())
        {
            return super.onSaveInstanceState();
        }
        return new SavedState(super.onSaveInstanceState());
    }

    /**
     * Called when Android restores the activity.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state == null || !state.getClass().equals(SavedState.class))
        {
            super.onRestoreInstanceState(state);
            if (state instanceof SavedState)
            {
                SavedState savedState = (SavedState)state;

                Date savedDate = persistedStringToDate(savedState.dateValue, _defaultDate);
                setDate(savedDate);
            }
        }
        else
        {
            SavedState savedState = (SavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());

            Date savedDate = persistedStringToDate(savedState.dateValue, _defaultDate);
            setDate(savedDate);
        }
    }

    /**
     * Called whenever the user clicks on a button. Invokes {@link #onDateChanged(DatePicker, int, int, int)}
     * and {@link #onDialogClosed(boolean)}. Be sure to call the super when overriding.
     */
    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        super.onClick(dialog, which);
        _datePicker.clearFocus();
        onDateChanged(
                _datePicker,
                _datePicker.getYear(),
                _datePicker.getMonth(),
                _datePicker.getDayOfMonth());
        onDialogClosed(which == DialogInterface.BUTTON_POSITIVE); // OK?
    }

    /**
     * Called when the user changes the date.
     */
    public void onDateChanged(DatePicker view, int year, int month, int day)
    {
        Calendar selected = new GregorianCalendar(year, month, day);

        _changedDate = selected.getTime();
    }

    /**
     * Called when the dialog is closed. If the close was by pressing "OK" it
     * saves the value.
     */
    @Override
    protected void onDialogClosed(boolean shouldSave)
    {
        if (shouldSave && _changedDate != null)
        {
            setDate(_changedDate);
            _changedDate = null;
        }
    }

    private static Date xmlStringToDate(String value, Date defaultDate)
    {
        return DateUtils.ShortDate(value, defaultDate);
    }

    private static Date persistedStringToDate(String value, Date defaultDate)
    {
        return DateUtils.ISO8601(value, defaultDate);
    }

    private static String dateToPersistedString(Date date)
    {
        return DateUtils.ISO8601(date);
    }

    private static String dateToSummaryString(Date date)
    {
        return DateUtils.LongDate(date);
    }

    private static class SavedState extends BaseSavedState
    {
        String dateValue;

        SavedState(Parcel source)
        {
            super(source);
            dateValue = source.readString();
        }

        SavedState(Parcelable superState)
        {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags)
        {
            super.writeToParcel(out, flags);
            out.writeString(dateValue);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>()
        {
            public SavedState createFromParcel(Parcel in)
            {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size)
            {
                return new SavedState[size];
            }
        };
    }
}
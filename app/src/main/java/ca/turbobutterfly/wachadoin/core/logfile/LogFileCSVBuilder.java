package ca.turbobutterfly.wachadoin.core.logfile;

import java.util.Date;

import ca.turbobutterfly.core.data.IDataTable;
import ca.turbobutterfly.core.grid.GridColumn;
import ca.turbobutterfly.core.grid.GridDataSource;
import ca.turbobutterfly.core.grid.IGridDataRow;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.wachadoin.core.data.IDataProvider;

public class LogFileCSVBuilder implements ILogFileBuilder
{
    private IDataProvider _dataProvider;

    public LogFileCSVBuilder(IDataProvider dataProvider)
    {
        _dataProvider = dataProvider;
    }

    @Override
    public String BuildLogFile(
            Date startTime,
            Date endTime,
            boolean groupByDate,
            String logOrder,
            int roundTime)
    {
        GridColumn[] gridColumns = new GridColumn[]
                {
                        new GridColumn("Date", "DisplayDate", 0),
                        new GridColumn("Start", "DisplayStartTime", 0),
                        new GridColumn("End", "DisplayEndTime", 0),
                        new GridColumn("Total", "DisplayTotalTime", 0),
                        new GridColumn("Activity", "LogText", 0)
                };

        IDataTable logEntries = _dataProvider.GetLogEntries(startTime, endTime, groupByDate, roundTime);

        GridDataSource dataSource = new GridDataSource(logEntries);
        dataSource.GroupFields(GroupFields(groupByDate));
        dataSource.IndexFields(IndexFields(TextUtils.equals(logOrder, "Descending")));

        StringBuilder builder = new StringBuilder();
        StringBuilder rowBuilder = new StringBuilder();

        for (GridColumn gridColumn : gridColumns)
        {
            if (groupByDate && TextUtils.equals(gridColumn.FieldName(), "DisplayDate"))
            {
                continue;
            }

            if (rowBuilder.length() > 0)
            {
                rowBuilder.append(",");
            }
            rowBuilder.append(gridColumn.Caption());
        }
        builder.append(rowBuilder.toString()).append("\n");

        for (int rowIndex = 0; rowIndex < dataSource.RowCount(); rowIndex++)
        {
            IGridDataRow row = dataSource.GetRow(rowIndex);

            rowBuilder = new StringBuilder();

            if (row.IsGroupRow())
            {
                rowBuilder.append("\n");
                Object value = row.Value("DisplayDate");
                String safeValue = MakeSafeValue(value);
                rowBuilder.append(safeValue);
            }
            else
            {
                for (GridColumn gridColumn : gridColumns)
                {
                    if (groupByDate && TextUtils.equals(gridColumn.FieldName(), "DisplayDate"))
                    {
                        continue;
                    }

                    if (rowBuilder.length() > 0)
                    {
                        rowBuilder.append(",");
                    }
                    Object value = row.Value(gridColumn.FieldName());
                    String safeValue = MakeSafeValue(value);
                    rowBuilder.append(safeValue);
                }
            }

            builder.append(rowBuilder.toString());
            builder.append("\n");
        }

        return builder.toString();
    }

    private String MakeSafeValue(Object value)
    {
        String safeValue = value.toString();

        int commaIndex = safeValue.indexOf(',');
        int quoteIndex = safeValue.indexOf('"');

        if ((quoteIndex < 0) && (commaIndex < 0))
        {
            return safeValue;
        }

        safeValue = safeValue.replace("\"", "\"\"");
        safeValue = "\"" + safeValue + "\"";
        return safeValue;
    }

    private String[] GroupFields(boolean groupByDate)
    {
        if (groupByDate)
        {
            return new String[]{"DisplayDate"};
        }

        return new String[0];
    }

    private String[] IndexFields(boolean descending)
    {
        if (descending)
        {
            return new String[]{"EndTime DESC", "StartTime DESC"};
        }

        return new String[]{"EndTime", "StartTime"};
    }
}

package ca.turbobutterfly.android.grid;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.turbobutterfly.core.grid.GridColumn;
import ca.turbobutterfly.core.grid.IGridDataSource;

public class GridHeaderView extends LinearLayout
{
    public GridHeaderView(
            Context context,
            ArrayList<GridColumn> gridColumns,
            IGridDataSource dataSource)
    {
        super(context);

        setOrientation(HORIZONTAL);

        List<String> groupFields = new ArrayList<>(Arrays.asList(dataSource.GroupFields()));

        for (int i = 0; i < gridColumns.size(); i++)
        {
            GridColumn gridColumn = gridColumns.get(i);

            if (groupFields.contains(gridColumn.FieldName()))
            {
                continue;
            }

            TextView textView = new TextView(context);
            textView.setTextSize(16);
            textView.setPadding(5, 0, 5, 0);
            textView.setBackgroundColor(0xFF3F51B5);
            textView.setTextColor(Color.WHITE);
            textView.setWidth(gridColumn.Width());
            textView.setText(gridColumn.Caption());
            textView.setSingleLine(true);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            addView(textView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView splitterView = new TextView(context);
            addView(splitterView, new LinearLayout.LayoutParams(
                    5,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }
}

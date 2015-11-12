package ru.kir.diary.client.common;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import ru.kir.diary.client.composite.DiaryComposite;

/**
 * Created by Kirill Zhitelev on 04.11.2015.
 */
public class Table {
    private CellTable<Record> table = new CellTable<>();

    public void createTable(final DiaryComposite composite) {

        TextColumn<Record> themeColumn = new TextColumn<Record>() {
            @Override
            public String getValue(Record object) {
                return object.getTheme();
            }
        };

        table.addColumn(themeColumn, "Theme");
        table.setColumnWidth(themeColumn, 140, Style.Unit.PX);

        TextColumn<Record> dateColumn = new TextColumn<Record>() {
            @Override
            public String getValue(Record object) {
                return object.getDate();
            }
        };

        ActionCell<Record> actionCell = new ActionCell<>("View", new ActionCell.Delegate<Record>() {
            @Override
            public void execute(Record object) {
                composite.getTextDiary().setText(object.getText());
                composite.getThemeText().setText(object.getTheme());

                composite.getTextDiary().setReadOnly(true);
                composite.getThemeText().setReadOnly(true);

                composite.getSaveToDB().setEnabled(false);
            }
        });

        Column<Record, Record> actionCol = new Column<Record, Record>(actionCell) {
            @Override
            public Record getValue(Record object) {
                return object;
            }
        };

        table.addColumn(dateColumn, "Date");
        table.setColumnWidth(dateColumn, 60, Style.Unit.PX);

        table.addColumn(actionCol, "Edit");
        table.setColumnWidth(actionCol, 60, Style.Unit.PX);

        table.setRowCount(0);
    }

    public CellTable<Record> getTable() {
        return table;
    }
}

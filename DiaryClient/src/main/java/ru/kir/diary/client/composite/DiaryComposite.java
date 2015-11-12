package ru.kir.diary.client.composite;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;
import ru.kir.diary.client.base.ReceivingRecord;
import ru.kir.diary.client.base.SavingRecord;
import ru.kir.diary.client.common.Record;
import ru.kir.diary.client.common.Table;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kirill Zhitelev on 28.10.2015.
 */
public class DiaryComposite extends Composite {
    private AbsolutePanel absPanel = new AbsolutePanel();
    private Button saveToDB = new Button("Save");
    private Button newRecord = new Button("New record");
    private TextArea textDiary = new TextArea();
    private TextBox searchTextTheme = new TextBox();
    private TextBox themeText = new TextBox();
    private Table table = new Table();
    private List<Record> records = new ArrayList<>();
    private ReceivingRecord get = new ReceivingRecord(this, table.getTable(), records);
    private Button allRecords = new Button("All records");

    public DiaryComposite() {
        initWidget(absPanel);
        addComposite();

        SavingRecord save = new SavingRecord(this);

        table.createTable(this);
        save.saveToBase();
        getDataFromBase();
        makeNewRecord();
    }

    private void addComposite() {
        absPanel.setSize("100%", "80%");
        absPanel.setStyleName("Absolute-Center");
        textDiary.setStyleName("textAreaFontSize");
        Label themeSearch = new Label("Searching");
        themeSearch.setStyleName("labelStyle");
        Label theme = new Label("Theme");
        theme.setStyleName("labelStyle");
        Label yourText = new Label("Text");
        yourText.setStyleName("labelStyle");
        searchTextTheme.getElement().setAttribute("placeholder", "Enter theme or date");
        ScrollPanel scrollPanel = new ScrollPanel(table.getTable());
        scrollPanel.setSize("300px", "200px");
        DecoratorPanel decoratorPanel = new DecoratorPanel();
        decoratorPanel.add(scrollPanel);

        textDiary.setWidth("600px");
        textDiary.setHeight("200px");

        absPanel.add(yourText, 220, 125);
        absPanel.add(theme, 220, 58);
        absPanel.add(themeText, 220, 78);
        absPanel.add(textDiary, 220, 150);
        absPanel.add(saveToDB, 440, 380);
        absPanel.add(newRecord, 510, 380);

        absPanel.add(decoratorPanel, 1000, 120);
        absPanel.add(themeSearch, 1070, 50);
        absPanel.add(searchTextTheme, 1030, 78);
        absPanel.add(allRecords, 1210, 78);
    }

    public Button getSaveToDB() {
        return saveToDB;
    }

    public TextArea getTextDiary() {
        return textDiary;
    }

    public TextBox getThemeText() {
        return themeText;
    }

    private void getDataFromBase() {
        searchTextTheme.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    if (searchTextTheme.getText().matches("(\\d{2}\\.){2}\\d{4}")) {
                        get.getByDate(searchTextTheme.getText());
                    } else {
                        get.getByTheme(searchTextTheme.getText());
                    }
                }
            }
        });
        allRecords.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                searchTextTheme.setText("");
                get.getAllRecords();
            }
        });
    }

    private void makeNewRecord() {
        newRecord.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                themeText.setText("");
                textDiary.setText("");

                themeText.setReadOnly(false);
                textDiary.setReadOnly(false);

                saveToDB.setEnabled(true);
            }
        });
    }
}


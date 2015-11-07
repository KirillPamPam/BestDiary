package ru.kir.diary.client.composite;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.*;
import ru.kir.diary.client.base.DiaryGet;
import ru.kir.diary.client.base.DiarySave;
import ru.kir.diary.client.common.Table;


/**
 * Created by Kirill Zhitelev on 28.10.2015.
 */
public class DiaryComposite extends Composite {
    private AbsolutePanel absPanel = new AbsolutePanel();
    private Button saveToDB = new Button("Save");
    private TextArea textDiary = new TextArea();
    private TextBox searchTextTheme = new TextBox();
    private TextBox themeText = new TextBox();
    private Table table = new Table();
    private DiaryGet get = new DiaryGet(this, table.getTable(), table.getRecords());

    public DiaryComposite() {
        initWidget(absPanel);
        addComposite();

        DiarySave save = new DiarySave(this);

        table.createTable(this);
        save.saveToBase();
        getDataFromBase();
    }

    private void addComposite() {
        absPanel.setSize("100%", "100%");
        absPanel.setStyleName("Absolute-Center");
        textDiary.setStyleName("textAreaFontSize");
        Label themeSearch = new Label("Searching");
        themeSearch.setStyleName("labelStyle");
        Label theme = new Label("Theme");
        theme.setStyleName("labelStyle");
        Label yourText = new Label("Text");
        yourText.setStyleName("labelStyle");
        searchTextTheme.getElement().setAttribute("placeholder", "Enter theme or date");

        textDiary.setWidth("600px");
        textDiary.setHeight("200px");

        absPanel.add(yourText, 220, 125);
        absPanel.add(theme, 220, 58);
        absPanel.add(themeText, 220, 78);
        absPanel.add(textDiary, 220, 150);
        absPanel.add(saveToDB, 490, 380);

        absPanel.add(table.getTable(), 1000, 120);
        absPanel.add(themeSearch, 1070, 50);
        absPanel.add(searchTextTheme, 1030, 78);
    }

    public Button getSaveToDB() {
        return saveToDB;
    }

    public TextArea getTextDiary() {
        return textDiary;
    }

    public TextBox getSearchTextTheme() {
        return searchTextTheme;
    }

    public TextBox getThemeText() {
        return themeText;
    }

    private void getDataFromBase() {
        searchTextTheme.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if(event.getCharCode() == KeyCodes.KEY_ENTER) {
                    if(searchTextTheme.getText().matches("(\\d{2}\\.){2}\\d{4}"))
                        get.getByDate();
                    else get.getByTheme();
                }
            }
        });
    }
}


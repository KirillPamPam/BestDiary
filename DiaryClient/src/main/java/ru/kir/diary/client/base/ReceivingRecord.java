package ru.kir.diary.client.base;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import ru.kir.diary.client.common.Record;
import ru.kir.diary.client.composite.DiaryComposite;

import java.util.List;

import static ru.kir.commons.CommonConstants.*;

/**
 * Created by Kirill Zhitelev on 01.11.2015.
 */
public class ReceivingRecord {
    private DiaryComposite diaryComposite;
    private CellTable<Record> table;
    private List<Record> records;

    public ReceivingRecord(DiaryComposite diaryComposite, CellTable<Record> table, List<Record> records) {
        this.diaryComposite = diaryComposite;
        this.table = table;
        this.records = records;
    }

    public void getByTheme() {
        String url = "/ServerDiary_war/diary/records/theme?" +
                "theme=" + diaryComposite.getSearchTextTheme().getText();
        getData(url);
    }

    public void getByDate() {
        String url = "/ServerDiary_war/diary/records/date?" +
                "date=" + makeBaseDate(diaryComposite.getSearchTextTheme().getText());
        getData(url);
    }

    public void getAllRecords() {
        diaryComposite.getAllRecords().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                diaryComposite.getSearchTextTheme().setText("");

                String url = "/ServerDiary_war/diary/records/all";

                getData(url);
            }
        });
    }

    private void getData(String url) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
        try {
            builder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        records.clear();
                        table.setRowCount(0);
                        diaryComposite.getTextDiary().setText("");
                        diaryComposite.getThemeText().setText("");

                        JSONValue jsonValue = JSONParser.parseStrict(response.getText());

                        boolean ckeckSize = fromJsonToRecord(jsonValue, records, table);

                        if(!ckeckSize) {
                            Window.alert("No results");
                            diaryComposite.getTextDiary().setText("");
                            diaryComposite.getThemeText().setText("");
                        }
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    Window.alert("Couldn't connect to server, please, try later");
                }
            });
        } catch (RequestException e) {
            Window.alert("Sorry, couldn't connect to server, please, try later");
        }
    }

    private String makeBaseDate(String date) {
        StringBuilder builder = new StringBuilder(date);

        builder.deleteCharAt(2);
        builder.deleteCharAt(4);

        return String.valueOf(builder);
    }

    private static String makeDate(String date) {
        StringBuilder builder = new StringBuilder(date);

        builder.insert(2, '.');
        builder.insert(5, '.');

        return String.valueOf(builder);
    }

    private static boolean fromJsonToRecord(JSONValue value, List<Record> records, CellTable<Record> table) {
        JSONObject jsonObject = value.isObject();
        JSONArray jsonArray = jsonObject.get("records").isArray();

        if (jsonArray.size() != 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                records.add(new Record(jsonArray.get(i).isObject().get(THEME).isString().stringValue(),
                        jsonArray.get(i).isObject().get(TEXT).isString().stringValue(),
                        makeDate(jsonArray.get(i).isObject().get(DATE).isString().stringValue())));
            }
            table.setRowData(0, records);
            return true;
        }
        else return false;
    }
}

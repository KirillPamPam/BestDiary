package ru.kir.diary.client.base;

import com.google.gwt.http.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import ru.kir.diary.client.common.Record;
import ru.kir.diary.client.common.RecordsCallback;

import java.util.Date;
import java.util.List;

import static ru.kir.commons.CommonConstants.*;

/**
 * Created by Kirill Zhitelev on 01.11.2015.
 */
public class ClientRecordService {
    private static final String PATH_GET = "/ServerDiary_war/diary/records";
    private static final String PATH_SAVE = "/ServerDiary_war/diary/records/transfer?";
    private CellTable<Record> table;
    private List<Record> records;

    public ClientRecordService(CellTable<Record> table, List<Record> records) {
        this.table = table;
        this.records = records;
    }

    public void getData(String pathToData, final RecordsCallback callback) {
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(PATH_GET + pathToData));
        try {
            builder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        records.clear();
                        table.setRowCount(0);

                        JSONValue jsonValue = JSONParser.parseStrict(response.getText());

                        records = fromJsonToRecord(jsonValue, records, table);

                        callback.call(records);

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

    private static List<Record> fromJsonToRecord(JSONValue value, List<Record> records, CellTable<Record> table) {
        JSONObject jsonObject = value.isObject();
        JSONArray jsonArray = jsonObject.get("records").isArray();

        if (jsonArray.size() != 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                records.add(new Record(jsonArray.get(i).isObject().get(THEME).isString().stringValue(),
                        jsonArray.get(i).isObject().get(TEXT).isString().stringValue(),
                        jsonArray.get(i).isObject().get(DATE).isString().stringValue()));
            }
            table.setRowData(0, records);
        }
        return records;
    }

    public void saveToBase(String theme, String text, final RecordsCallback callback) {
        String url = PATH_SAVE +
                "theme=" + replaceSpacesInText(theme) +
                "&text=" + replaceSpacesInText(text) +
                "&date=" + getCurrentDate();

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));

        try {
            builder.sendRequest(null, new RequestCallback() {
                public void onError(Request request, Throwable exception) {
                    Window.alert("Couldn't connect to server, please, try later");
                }

                public void onResponseReceived(Request request, Response response) {
                    callback.call(null);
                }
            });
        } catch (RequestException e) {
            Window.alert("Sorry, couldn't connect to server, please, try later");
        }

    }

    private String replaceSpacesInText(String text) {
        return text.replaceAll(" ", "+");
    }

    private String getCurrentDate() {
        Date date = new Date();
        DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

}

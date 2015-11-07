package ru.kir.diary.client.base;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import ru.kir.diary.client.composite.DiaryComposite;

import java.util.Date;

/**
 * Created by Kirill Zhitelev on 01.11.2015.
 */
public class DiarySave {
    private DiaryComposite diaryComposite;

    public DiarySave(DiaryComposite diaryComposite) {
        this.diaryComposite = diaryComposite;
    }

    public void saveToBase() {
        diaryComposite.getSaveToDB().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String url = "/ServerDiary_war/diary/send?" +
                        "theme=" + replaceSpacesInText(diaryComposite.getThemeText().getText()) +
                        "&text=" + replaceSpacesInText(diaryComposite.getTextDiary().getText()) +
                        "&date=" + getCurrentDate();

                if (diaryComposite.getThemeText().getText().equals("") || diaryComposite.getTextDiary().getText().equals("")) {
                    Window.alert("Please, enter text and theme");
                }
                else {
                    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));

                    try {
                        builder.sendRequest(null, new RequestCallback() {
                            public void onError(Request request, Throwable exception) {
                                Window.alert("Couldn't connect to server, please, try later");
                            }

                            public void onResponseReceived(Request request, Response response) {
                                Window.alert("Saved");
                                diaryComposite.getTextDiary().setText("");
                                diaryComposite.getThemeText().setText("");
                            }
                        });
                    } catch (RequestException e) {
                        Window.alert("Sorry, couldn't connect to server, please, try later");
                    }
                }
            }
        });

    }

    private String replaceSpacesInText(String text) {
        return text.replaceAll(" ", "+");
    }

    private String getCurrentDate() {
        Date date = new Date();
        DateTimeFormat dateFormat = DateTimeFormat.getFormat("ddMMyyyy");
        return dateFormat.format(date);
    }
}

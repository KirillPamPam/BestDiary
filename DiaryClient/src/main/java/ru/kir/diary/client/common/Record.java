package ru.kir.diary.client.common;

/**
 * Created by Kirill Zhitelev on 04.11.2015.
 */
public class Record {
    private String theme, date, text;

    public Record(String theme, String text, String date) {
        this.theme = theme;
        this.date = date;
        this.text = text;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}

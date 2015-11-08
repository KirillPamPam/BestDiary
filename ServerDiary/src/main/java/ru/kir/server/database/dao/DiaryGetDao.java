package ru.kir.server.database.dao;

/**
 * Created by Kirill Zhitelev on 28.10.2015.
 */
public interface DiaryGetDao {
    String getTextByDate(String date);

    String getTextByTheme(String theme);

    String getAll();
}

package ru.kir.server.database.dao;

/**
 * Created by Kirill Zhitelev on 28.10.2015.
 */
public interface SavingRecordDao {
    void insertRecord(String theme, String text, String currentDate);
}

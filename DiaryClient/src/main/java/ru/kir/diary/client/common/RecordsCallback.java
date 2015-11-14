package ru.kir.diary.client.common;

import java.util.List;

/**
 * Created by Kirill Zhitelev on 14.11.2015.
 */
public interface RecordsCallback {
    void call(List<Record> records);
}

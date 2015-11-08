package ru.kir.diary.client.composite;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Created by Kirill Zhitelev on 28.10.2015.
 */
public class DiaryEntry implements EntryPoint {
    @Override
    public void onModuleLoad() {
        RootPanel.get().add(new DiaryComposite());
    }
}

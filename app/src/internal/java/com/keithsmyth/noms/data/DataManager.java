package com.keithsmyth.noms.data;

import android.util.Log;

import com.keithsmyth.noms.model.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * @author keithsmyth
 */
public class DataManager {

    private final List<Entry> list = new ArrayList<>();

    private static DataManager sInstance;

    public static DataManager get() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    private DataManager() {
        // no public construction
    }

    private void log(String log) {
        Log.i(getClass().getSimpleName(), log);
    }

    public Observable<List<Entry>> getEntryList() {
        log("getEntryList " + list.size());
        return Observable.just(list).delay(1, TimeUnit.SECONDS);
    }

    public Observable<Boolean> saveEntry(Entry entry) {
        log("saveEntry");
        list.add(entry);
        return Observable.just(true).delay(1, TimeUnit.SECONDS);
    }
}

package com.keithsmyth.noms.data;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.keithsmyth.noms.model.Entry;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * @author keithsmyth
 */
public class DataManager implements IDataManager {

    private final Map<String, Entry> entryCache = new ArrayMap<>();

    public DataManager() {
        // create some mock data
        final Entry entry = new Entry();
        entry.setDate(new Date());
        entry.setCategory("Category");
        entry.setAmount(1);
        entry.setDescription("Description");
        entry.setInRules(true);
        entry.setComment("Comment");
        saveEntry(entry);
    }

    private void log(String log) {
        Log.i(getClass().getSimpleName(), log);
    }

    @Override public Observable<Collection<Entry>> getEntryList() {
        log("getEntryList " + entryCache.size());
        return Observable.just(entryCache.values()).delay(1, TimeUnit.SECONDS); // this will break ordering
    }

    @Override public Observable<Entry> getEntry(String objectId) {
        final Entry entry = entryCache.get(objectId);
        return Observable.just(entry).delay(1, TimeUnit.SECONDS);
    }

    @Override public Observable<Entry> saveEntry(Entry entry) {
        // mimic random objectId from Parse
        if (TextUtils.isEmpty(entry.getObjectId())) {
            entry.setObjectId(UUID.randomUUID().toString());
        }
        log("saveEntry " + entry.getObjectId());
        entryCache.put(entry.getObjectId(), entry);
        return Observable.just(entry).delay(1, TimeUnit.SECONDS);
    }
}

package com.keithsmyth.noms.data;

import com.keithsmyth.noms.model.Entry;

import java.util.Collection;

import rx.Observable;

/**
 * @author keithsmyth
 */
public interface IDataManager {
    Observable<Collection<Entry>> getEntryList();

    Observable<Entry> getEntry(String objectId);

    Observable<Entry> saveEntry(Entry entry);
}

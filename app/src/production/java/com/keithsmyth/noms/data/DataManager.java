package com.keithsmyth.noms.data;

import android.text.TextUtils;

import com.keithsmyth.noms.App;
import com.keithsmyth.noms.R;
import com.keithsmyth.noms.model.Entry;

import java.util.Collection;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import rx.Observable;

/**
 * @author keithsmyth
 */
public class DataManager implements IDataManager {

    private final ParseApi parseApi;

    public DataManager() {
        final RequestInterceptor interceptor = new RequestInterceptor() {
            @Override public void intercept(RequestFacade request) {
                request.addHeader("X-Parse-Application-Id", App.inflateString(R.string.app_key));
                request.addHeader("X-Parse-REST-API-Key", App.inflateString(R.string.api_key));
            }
        };

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.parse.com/1")
                .setRequestInterceptor(interceptor)
                .build();

        parseApi = restAdapter.create(ParseApi.class);
    }

    @Override public Observable<Collection<Entry>> getEntryList() {
        return parseApi.listEntries();
    }

    @Override public Observable<Entry> getEntry(String objectId) {
        return parseApi.getEntry(objectId);
    }

    @Override public Observable<Entry> saveEntry(Entry entry) {
        if (TextUtils.isEmpty(entry.getObjectId())) {
            return createEntry(entry);
        }
        return updateEntry(entry);
    }

    private Observable<Entry> createEntry(Entry entry) {
        final CreatedModel createdModel = parseApi.createEntry(entry);
        entry.setObjectId(createdModel.getObjectId());
        return Observable.just(entry);
    }

    private Observable<Entry> updateEntry(Entry entry) {
        parseApi.updateEntry(entry.getObjectId(), entry);
        return Observable.just(entry);
    }
}

package com.keithsmyth.noms.data;

import com.keithsmyth.noms.model.Entry;

import java.util.Collection;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;

/**
 * @author keithsmyth
 */
public interface ParseApi {

    @GET("/classes/Entity") Observable<Collection<Entry>> listEntries();

    @GET("/classes/Entity/{objectId}")
    Observable<Entry> getEntry(@Path("objectId") String objectId);

    @POST("/classes/Entity") CreatedModel createEntry(@Body Entry entry);

    @PUT("/classes/Entity/{objectId}")
    void updateEntry(@Path("objectId") String objectId, @Body Entry entry);
}

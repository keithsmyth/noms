package com.keithsmyth.noms;

import android.app.Application;
import android.support.annotation.StringRes;

import com.keithsmyth.noms.data.DataManager;
import com.keithsmyth.noms.data.IDataManager;

/**
 * @author keithsmyth
 */
public class App extends Application {

    private static App sInstance;

    private IDataManager dataManager;

    @Override public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static IDataManager getDataManager() {
        if (sInstance.dataManager == null) {
            sInstance.dataManager = new DataManager();
        }
        return sInstance.dataManager;
    }

    public static String inflateString(@StringRes int stringResId) {
        return sInstance.getString(stringResId);
    }
}

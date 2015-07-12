package com.keithsmyth.noms.ui;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * @author keithsmyth
 */
public class SubscriptionManager {

    private final List<Subscription> subs = new ArrayList<>();

    public void add(@NonNull Subscription sub) {
        if (subs.contains(sub)) {
            return;
        }
        subs.add(sub);
    }

    public void unsubscribe() {
        for (int i = 0; i < subs.size(); i++) {
            subs.get(i).unsubscribe();
        }
        subs.clear();
    }
}

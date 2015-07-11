package com.keithsmyth.noms.ui;

import android.support.v4.app.Fragment;

/**
 * @author keithsmyth
 */
public interface Navigatable {
    void next(Fragment fragment);

    void previous();
}

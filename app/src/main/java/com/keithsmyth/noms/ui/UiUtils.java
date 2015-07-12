package com.keithsmyth.noms.ui;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author keithsmyth
 */
public final class UiUtils {

    private UiUtils() {
        // Not to be instantiated
    }

    public static void closeKeyboard(Context context, View view) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

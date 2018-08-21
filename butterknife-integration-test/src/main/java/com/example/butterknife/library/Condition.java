package com.example.butterknife.library;

import butterknife.internal.ClickSession;

/**
 * Conditions for ViewController
 *
 *
 * Created by LeoTesla on 2017/7/15.
 */

public interface Condition {

    boolean condition();
    boolean condition(ClickSession session);

}

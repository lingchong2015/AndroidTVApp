package com.dirk41.androidtvapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by lingchong on 16-1-24.
 */
public class ErrorActivity extends Activity {
    private ErrorFragment mErrorFragment;
    private static final String TAG = ErrorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        testError();
    }

    private void testError() {
        mErrorFragment = new ErrorFragment();
        getFragmentManager().beginTransaction().add(R.id.main_main_fragment, mErrorFragment).commit();
    }
}

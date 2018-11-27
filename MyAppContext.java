package ca.gotchasomething.mynance;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class MyAppContext extends Application {

    Context mContext;

    @SuppressLint("MissingSuperCall")
    public void onCreate() {
        mContext = this.getApplicationContext();
    }

    public Context getAppContext() {
        return mContext;
    }
}

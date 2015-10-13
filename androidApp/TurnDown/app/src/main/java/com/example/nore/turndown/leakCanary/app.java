package com.example.nore.turndown.leakCanary;

import android.app.Application;
import android.os.StrictMode;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.squareup.leakcanary.LeakCanary;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;
import static com.joanzapata.iconify.Iconify.with;

/**
 * Created by NORE on 15/08/2015.
 */
public class app extends Application {
    public class ExampleApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            enabledStrictMode();
            //LeakCanary.install(this);
            Iconify.with(new MaterialModule());
        }

        private void enabledStrictMode() {
            if (SDK_INT >= GINGERBREAD) {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                        .detectAll() //
                        .penaltyLog() //
                        .penaltyDeath() //
                        .build());
            }
        }
    }
}

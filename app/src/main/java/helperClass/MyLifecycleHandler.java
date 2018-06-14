package helperClass;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by rio.issac on 5/17/2016.
 */
public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static int resumed;
    private static int stopped;

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
        ++stopped;
    }

    // And add this public static function
    public static boolean isApplicationInForeground() {
        return resumed > stopped + 1;
    }
}


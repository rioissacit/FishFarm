package helperClass;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

/**
 * Created by rio.issac on 9/10/2015.
 */
public class AppController extends Application {


    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private static Context context;
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));
        mInstance = this;
        context=getApplicationContext();
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public static Context getCustomAppContext(){
        return context;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> boolean addToRequestQueue(Request<T> req) {
        try {
            if (!CommonFunctions.isNetworkAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(),
                        helperClass.AppConfig.NO_INTERNET, Toast.LENGTH_SHORT)
                        .show();
            } else if (CommonFunctions.isRefresh(getApplicationContext(), req.getUrl().substring(req.getUrl().lastIndexOf("/")), new String(((JsonObjectRequest) req).getBody(), "UTF-8").toString())) {
                req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                getRequestQueue().add(req);
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}


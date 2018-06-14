package helperClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by aruna.ramakrishnan on 3/5/2018.
 */
public class SessionManager {

    // Shared preferences file name
    private static final String PREF_NAME = "FishFarmLogin";
    private static final String IS_LOGGEDIN = "isLoggedIn";
    private static final String REGISTRATION_ID = "registrationID";
    private static final String DESIGNATION = "designation";
    private static final String USER_ID = "userID";
    private static final String USER_NAME = "userName";
    private static final String USER_EMAIL = "userEmail";
    private static final String DEVICE_ID = "deviceID";
    private static final String NOTIFICATION_COUNT = "notificationCount";
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    SharedPreferences pref;
    Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SessionManager() {
    }

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void clearSessionVariables() {
        setLogin(false);
        setUserId("");
        setUserName("");
        setUserEmail("");
        setNotificationCount("");
        setDesignation("");
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGEDIN, false);
    }

    public String getRegistrationId() {
        return pref.getString(REGISTRATION_ID, "");
    }

    public void setRegistrationId(String registrationId) {

        editor.putString(REGISTRATION_ID, registrationId);

        // commit changes
        editor.commit();

        Log.d("Registration", registrationId);
    }

    public String getDesignation() {
        return pref.getString(DESIGNATION, "");
    }

    public void setDesignation(String designation) {

        editor.putString(DESIGNATION, designation);

        // commit changes
        editor.commit();

    }

    public String getUserId() {
        return pref.getString(USER_ID, "");
    }

    public void setUserId(String userId) {

        editor.putString(USER_ID, userId);

        // commit changes
        editor.commit();

        Log.d("User", userId);
    }

    public String getUserName() {
        return pref.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {

        editor.putString(USER_NAME, userName);

        // commit changes
        editor.commit();

    }

    public String getUserEmail() {
        return pref.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {

        editor.putString(USER_EMAIL, userEmail);

        // commit changes
        editor.commit();

    }

    public String getDeviceId() {
        return pref.getString(DEVICE_ID, "");
    }

    public void setDeviceId(String deviceId) {

        editor.putString(DEVICE_ID, deviceId);

        // commit changes
        editor.commit();

        Log.d("Device", deviceId);
    }
    public String getNotificationCount() {
        return pref.getString(NOTIFICATION_COUNT, "0");
    }

    public void setNotificationCount(String NotificationCount) {

        editor.putString(NOTIFICATION_COUNT, NotificationCount);

        // commit changes
        editor.commit();
    }
}

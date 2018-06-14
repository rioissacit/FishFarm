package helperClass;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by aruna.ramakrishnan on 3/5/2018.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    SessionManager sessionManager;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sessionManager = new SessionManager(getApplicationContext());
//        System.out.println("refreshed token" + refreshedToken);
        sessionManager.setRegistrationId(refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/allUsers");
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/allAndroid");
        // TODO: Implement this method to send any registration to your app's servers.

    }
}

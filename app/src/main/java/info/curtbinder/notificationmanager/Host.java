package info.curtbinder.notificationmanager;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by binder on 9/12/14.
 */
public class Host {

    private static String GET_ALERTS = "http://forum.reefangel.com/status/alerts.aspx?id=";
    private String host;
    private String username;

    public Host() {
        username = "";
    }

    public Host(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d("Host", "Failed to encode username: " + username);
        }
        return GET_ALERTS + encoded;
    }
}

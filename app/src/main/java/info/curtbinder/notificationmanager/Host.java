package info.curtbinder.notificationmanager;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by binder on 9/12/14.
 */
public class Host {

    public static final int GET = 0;
    public static final int ADD = 1;
    public static final int EDIT = 2;
    public static final int DELETE = 3;

    private static final String TAG = Host.class.getSimpleName();
    private static final String BASE = "http://forum.reefangel.com/status/";
    private static final String GET_ALERTS = BASE + "alerts.aspx?id=";
    private static final String UPDATE_ALERT = BASE + "submittriggers.aspx?id=";
    private static final String DELETE_ALERT = BASE + "deletetriggers.aspx?id=";
    private String host;
    private String username;
    private int type;
    private Alert alert;

    public Host() {
        username = "";
        type = GET;
        alert = null;
    }

    public Host(String username) {
        super();
        setUsername(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAlert(Alert a) {
        this.alert = a;
    }

    @Override
    public String toString() {
        String encoded = "";
        String s = "";
        switch (type) {
            default:
            case GET:
                try {
                    encoded = URLEncoder.encode(username, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.d(TAG, "Failed to encode username: " + username);
                }
                s = GET_ALERTS + encoded;
                break;
            case ADD:
                try {
                    String url = username;
                    encoded = URLEncoder.encode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.d(TAG, "Failed to encode");
                }
                s = UPDATE_ALERT + encoded;
                Log.d(TAG, "ADD: " + s);
                break;
            case EDIT:
                break;
            case DELETE:
                try {
                    String url = username + "&triggerid=" + alert.getId();
                    encoded = URLEncoder.encode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.d(TAG, "Failed to encode");
                }
                s = DELETE_ALERT + encoded;
                Log.d(TAG, "DELETE: " + s);
                break;
        }
        return s;
    }
}

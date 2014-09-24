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
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    private static final String TAG = Host.class.getSimpleName();
    private static final String BASE = "http://forum.reefangel.com/status/";
    public static final String TRIGGERS_URL = BASE + "specialtags.aspx";
    private static final String GET_ALERTS = "alerts.aspx?id=";
    private static final String UPDATE_ALERT = "submittriggers.aspx?id=";
    private static final String DELETE_ALERT = "deletetriggers.aspx?id=";
    //    private String host;
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
        String prefix = "";
        String suffix = "";
        switch (type) {
            default:
            case GET:
                prefix = GET_ALERTS;
                break;
            case ADD:
                prefix = UPDATE_ALERT;
                suffix = alert.getAddString();
                break;
            case UPDATE:
                prefix = UPDATE_ALERT;
                suffix = alert.getUpdateString();
                break;
            case DELETE:
                prefix = DELETE_ALERT;
                suffix = alert.getDeleteString();
                break;
        }
        try {
            encoded = URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Failed to encode: " + username);
            return "";
        }
        Log.d(TAG, "Encoded URL: " + BASE + prefix + encoded + suffix);
        return BASE + prefix + encoded + suffix;
    }
}

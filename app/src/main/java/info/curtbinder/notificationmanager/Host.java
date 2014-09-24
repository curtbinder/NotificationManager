/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Curt Binder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

    public int getType() {
        return this.type;
    }

    public String getAlertName() {
        return alert.getAlertName();
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
        //Log.d(TAG, "Encoded URL: " + BASE + prefix + encoded + suffix);
        return BASE + prefix + encoded + suffix;
    }
}

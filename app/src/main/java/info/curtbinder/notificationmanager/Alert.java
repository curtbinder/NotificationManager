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

import android.os.Parcel;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by binder on 9/10/14.
 */
public class Alert implements Parcelable {

    public static Creator<Alert> CREATOR = new Creator<Alert>() {
        @Override
        public Alert createFromParcel(Parcel parcel) {
            return new Alert(parcel);
        }

        @Override
        public Alert[] newArray(int i) {
            return new Alert[i];
        }
    };
    // TODO add in trigger - need to confirm this value...associated with paramName
    private int id;
    private int paramId;
    private String paramName;
    private String paramDescription;
    private int comparison;
    private float value;
    private String lastAlert;
    private String alertName;
    private String alertDescription;

    public Alert() {
        id = -1;
        paramId = 1;
        paramName = "";
        paramDescription = "";
        comparison = 0;
        value = 0;
        lastAlert = "never";
        alertName = "New Alert";
        alertDescription = "Description of the Alert";
    }

    public Alert(Parcel in) {
        id = in.readInt();
        paramId = in.readInt();
        paramName = in.readString();
        paramDescription = in.readString();
        comparison = in.readInt();
        value = in.readFloat();
        lastAlert = in.readString();
        alertName = in.readString();
        alertDescription = in.readString();
    }

    public static String getComparisonString(int comparison) {
        String s = "";
        switch (comparison) {
            case 0:
                s = "<=";
                break;
            case 1:
                s = "<";
                break;
            case 2:
                s = "=";
                break;
            case 3:
                s = ">";
                break;
            case 4:
                s = ">=";
                break;
            default:
                s = "!";
                break;
        }
        return s;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParamId() {
        return this.paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamDescription() {
        return paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription;
    }

    public int getComparison() {
        return comparison;
    }

    public void setComparison(int comparison) {
        this.comparison = comparison;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getLastAlert() {
        return lastAlert;
    }

    public void setLastAlert(String lastAlert) {
        this.lastAlert = lastAlert;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getAlertDescription() {
        return alertDescription;
    }

    public void setAlertDescription(String alertDescription) {
        this.alertDescription = alertDescription;
    }

    private String getAlertStrings() {
        String encodedName, encodedDescription;
        String s = "";
        try {
            encodedName = URLEncoder.encode(alertName, "UTF-8");
            encodedDescription = URLEncoder.encode(alertDescription, "UTF-8");
            s = "&alert_name=" + encodedName + "&alert_description=" + encodedDescription;
        } catch (UnsupportedEncodingException e) {
        }
        return s;
    }

    private String getComparisonAndValueStrings() {
        return "&comparison=" + comparison + "&triggervalue=" + value;
    }

    public String getAddString() {
        return "&trigger=" + paramId + getComparisonAndValueStrings() + getAlertStrings();
    }

    public String getUpdateString() {
        return "&triggerid=" + id + getAddString();
    }

    public String getDeleteString() {
        return "&triggerid=" + id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeInt(paramId);
        out.writeString(paramName);
        out.writeString(paramDescription);
        out.writeInt(comparison);
        out.writeFloat(value);
        out.writeString(lastAlert);
        out.writeString(alertName);
        out.writeString(alertDescription);
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Alert #").append(id);
        sb.append(" - {ParamId: ").append(paramId);
        sb.append(", ParamName: ").append(paramName);
        sb.append(", ParamDescription: ").append(paramDescription);
        sb.append(", Comparision: ").append(comparison).append(", Value: ").append(value);
        sb.append(", LastAlert: ").append(lastAlert).append(", Name: ").append(alertName);
        sb.append(", Description: ").append(alertDescription).append("}");
        return sb.toString();
    }
}

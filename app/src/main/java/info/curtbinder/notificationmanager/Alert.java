package info.curtbinder.notificationmanager;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by binder on 9/10/14.
 */
public class Alert implements Parcelable {

    private int id;
    private String paramName;
    private String paramDescription;
    private int comparison;
    private int value;
    private String lastAlert;
    private String alertName;
    private String alertDescription;

    public Alert(){
        id = 0;
        paramName = "";
        paramDescription = "";
        comparison = 0;
        value = 0;
        lastAlert = "never";
        alertName = "New Alert";
        alertDescription = "Description of the Alert";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static String getComparisonString(int comparison) {
        String s = "";
        switch ( comparison ) {
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

    public int getComparison() {
        return comparison;
    }

    public void setComparison(int comparison) {
        this.comparison = comparison;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeString(paramName);
        out.writeString(paramDescription);
        out.writeInt(comparison);
        out.writeInt(value);
        out.writeString(lastAlert);
        out.writeString(alertName);
        out.writeString(alertDescription);
    }

    public Alert(Parcel in) {
        id = in.readInt();
        paramName = in.readString();
        paramDescription = in.readString();
        comparison = in.readInt();
        value = in.readInt();
        lastAlert = in.readString();
        alertName = in.readString();
        alertDescription = in.readString();
    }

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

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Alert #").append(id);
        sb.append(" - {ParamName: ").append(paramName);
        sb.append(", ParamDescription: ").append(paramDescription);
        sb.append(", Comparision: ").append(comparison).append(", Value: ").append(value);
        sb.append(", LastAlert: ").append(lastAlert).append(", Name: ").append(alertName);
        sb.append(", Description: ").append(alertDescription).append("}");
        return sb.toString();
    }
}

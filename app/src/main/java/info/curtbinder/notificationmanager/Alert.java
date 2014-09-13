package info.curtbinder.notificationmanager;

/**
 * Created by binder on 9/10/14.
 */
public class Alert {

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
}

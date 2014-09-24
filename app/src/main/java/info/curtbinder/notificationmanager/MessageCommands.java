package info.curtbinder.notificationmanager;

/**
 * Created by binder on 9/13/14.
 */
public class MessageCommands {
    public static final String PACKAGE_BASE = MessageCommands.class.getPackage().getName();

    public static final String UPDATE_DISPLAY_ALERTS = PACKAGE_BASE + ".UPDATE_DISPLAY_ALERTS";
    public static final String RELOAD_ALERTS = PACKAGE_BASE + ".RELOAD_ALERTS";
    public static final String ADD_ALERT = PACKAGE_BASE + ".ADD_ALERT";
    public static final String UPDATE_ALERT = PACKAGE_BASE + ".UPDATE_ALERT";
    public static final String DELETE_ALERT = PACKAGE_BASE + ".DELETE_ALERT";
    public static final String SERVER_RESPONSE = PACKAGE_BASE + ".SERVER_RESPONSE";
    public static final String MSG_ALERTS = "ALERTS";
    public static final String MSG_ALERT_DATA = "ALERT";
    public static final String MSG_ALERT_TYPE = "ALERT_TYPE";
    public static final String MSG_RESPONSE = "RESPONSE";
}

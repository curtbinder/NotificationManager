package info.curtbinder.notificationmanager;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by binder on 9/17/14.
 */
public class DialogAddEditTrigger extends DialogFragment
    implements View.OnClickListener {

    private static final String TAG = DialogAddEditTrigger.class.getSimpleName();
    private static final String UPDATE = "update";
    private static final String ALERT = "alert";
    private Spinner spinParam;
    private Spinner spinCond;
    private EditText editName;
    private EditText editDescription;
    private EditText editValue;
    private Button btnCancel;
    private Button btnUpdate;
    private boolean fUpdate;
    private Alert alert;

    public DialogAddEditTrigger() {

    }

    public static DialogAddEditTrigger newInstance() {
        return new DialogAddEditTrigger();
    }

    public static DialogAddEditTrigger newInstance(boolean fEdit, Alert a) {
        DialogAddEditTrigger d = DialogAddEditTrigger.newInstance();
        Bundle args = new Bundle();
        args.putBoolean(UPDATE, fEdit);
        args.putParcelable(ALERT, a);
        d.setArguments(args);
        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dlg_add_edit_trigger, container);
        findViews(v);
        setAdapters();
        Bundle args = getArguments();
        fUpdate = false;  // set to false, which means we are adding a trigger
        alert = null;
        if ( args != null ) {
            fUpdate = args.getBoolean(UPDATE, false);  // get the value, otherwise false
            alert = args.getParcelable(ALERT);  // get the alert, if it exists
        }
        updateDisplay();
        return v;
    }

    private void updateDisplay() {
        // update the display as appropriate
        // set default values and information
        int titleId = R.string.edit_notification;
        int updateId = R.string.update;
        if ( alert == null ) {
            // no alert given, so we are adding an alert
            alert = new Alert();
            titleId = R.string.add_notification;
            updateId = R.string.add;
        }
        getDialog().setTitle(titleId);
        btnUpdate.setText(updateId);
        editName.setText(alert.getAlertName());
        editDescription.setText(alert.getAlertDescription());
        editValue.setText("" + alert.getValue());
    }

    private void setAdapters() {
        // TODO set adapters for the spinners
    }

    private void findViews(View root) {
        editName = (EditText) root.findViewById(R.id.editName);
        editDescription = (EditText) root.findViewById(R.id.editDescription);
        editValue = (EditText) root.findViewById(R.id.editValue);
        spinParam = (Spinner) root.findViewById(R.id.spinParam);
        spinCond = (Spinner) root.findViewById(R.id.spinCond);
        btnCancel = (Button) root.findViewById(R.id.buttonCancel);
        btnUpdate = (Button) root.findViewById(R.id.buttonUpdate);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonUpdate:
                Log.d(TAG, "update button pressed");
                break;
        }
    }
}

package info.curtbinder.notificationmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by binder on 9/17/14.
 */
public class DialogAddEditTrigger extends DialogFragment {

    public static final int DLG_ADDEDIT_CALL = 1;
    public static final String ALERT = "alert";

    private static final String TAG = DialogAddEditTrigger.class.getSimpleName();
    private Spinner spinParam;
    private Spinner spinCond;
    private EditText editName;
    private EditText editDescription;
    private EditText editValue;
    private Alert alert;

    public DialogAddEditTrigger() {
    }

    public static DialogAddEditTrigger newInstance() {
        return new DialogAddEditTrigger();
    }

    public static DialogAddEditTrigger newInstance(Alert a) {
        DialogAddEditTrigger d = DialogAddEditTrigger.newInstance();
        Bundle args = new Bundle();
        args.putParcelable(ALERT, a);
        d.setArguments(args);
        return d;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dlg_add_edit_trigger, null);
        findViews(v);
        Bundle args = getArguments();
        alert = null;
        int titleId = R.string.edit_notification;
        int updateId = R.string.update;
        if ( args == null ) {
            alert = new Alert();
            titleId = R.string.add_notification;
            updateId = R.string.add;
        } else {
            alert = args.getParcelable(ALERT);  // get the alert, if it exists
        }
        setAdapters();
        updateDisplay();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titleId)
                .setView(v)
                .setPositiveButton(updateId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateAlert();
                        Intent i = new Intent();
                        i.putExtra(ALERT, alert);
                        getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK, i);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    private void findViews(View root) {
        editName = (EditText) root.findViewById(R.id.editName);
        editDescription = (EditText) root.findViewById(R.id.editDescription);
        editValue = (EditText) root.findViewById(R.id.editValue);
        spinParam = (Spinner) root.findViewById(R.id.spinParam);
        spinCond = (Spinner) root.findViewById(R.id.spinCond);
    }

    private void updateDisplay() {
        // set default values and information
        editName.setText(alert.getAlertName());
        editDescription.setText(alert.getAlertDescription());
        editValue.setText("" + alert.getValue());
    }

    private void setAdapters() {
        ArrayAdapter<CharSequence> arrayCondition = ArrayAdapter.createFromResource(getActivity(),
                R.array.comparisonText, android.R.layout.simple_spinner_item);
        arrayCondition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCond.setAdapter(arrayCondition);
        ArrayAdapter<CharSequence> arrayParam = ArrayAdapter.createFromResource(getActivity(),
                R.array.paramText, android.R.layout.simple_spinner_item);
        arrayParam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinParam.setAdapter(arrayParam);
    }

    private void updateAlert() {
        alert.setAlertName(editName.getText().toString());
        alert.setAlertDescription(editDescription.getText().toString());
        alert.setValue(Integer.parseInt(editValue.getText().toString()));
        // TODO update the values stored in the spinners
        int pos = spinCond.getSelectedItemPosition();
        Log.d(TAG, "Condition: " + pos);
        alert.setComparison(pos);
    }
}

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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

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
    private ArrayList<String> mTriggerDescription = new ArrayList<String>();
    private Map<String, Integer> mapTriggers = new HashMap<String, Integer>();

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
        try {
            parseTriggers();
        } catch (Exception e) {
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
        spinCond.setSelection(alert.getComparison());
    }

    private void setAdapters() {
        ArrayAdapter<CharSequence> arrayCondition = ArrayAdapter.createFromResource(getActivity(),
                R.array.comparisonText, android.R.layout.simple_spinner_item);
        arrayCondition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCond.setAdapter(arrayCondition);
        ArrayAdapter<String> arrayParam = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, mTriggerDescription);
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

    private void parseTriggers() throws Exception {
        InputSource inputSource = new InputSource(getResources().openRawResource(R.raw.trigger_params));
        XPath xpath = XPathFactory.newInstance().newXPath();
        String exp = "triggers//trigger";
        NodeList nodes = (NodeList) xpath.evaluate(exp, inputSource, XPathConstants.NODESET);
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                NodeList children = n.getChildNodes();
                String desc = "";
                String name = "";
                int id = 0;
                for (int j = 0; j < children.getLength(); j++) {
                    Node nc = children.item(j);
                    String localName = nc.getLocalName();
                    if (localName == null) continue;
                    if ( localName.equals("description") ) {
                        desc = nc.getTextContent();
                    } else if ( localName.equals("id") ) {
                        id = Integer.parseInt(nc.getTextContent());
                    } else if ( localName.equals("name")) {
                        name = nc.getTextContent();
                    }
                }
                mTriggerDescription.add(desc);
                mapTriggers.put(name, id);
            }
        }
    }
}

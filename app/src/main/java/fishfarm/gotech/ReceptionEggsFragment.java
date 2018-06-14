package fishfarm.gotech;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import SQLClasses.MasterDao;
import helperClass.CommonFunctions;
import helperClass.SessionManager;
import helperClass.SpinnerObject;


public class ReceptionEggsFragment extends Fragment implements View.OnClickListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    View view;
    EditText edtRcptDate;
    EditText iedt_Batch;
    EditText edtRcptTime;
    EditText iedt_Ext_Hatchery;
    EditText iedt_Airway_Bill;
    EditText iedt_Eggs;
    EditText iedt_Dead_Eggs;
    Button btnScan;
    private CommonFunctions commonFunctions;
    private SessionManager sessionManager;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private String minutePick;
    PopupWindow popupWindow;
    TextInputLayout itxtLay_Ext_Hatchery;
    MasterDao masterDao;
    int locID = 0;
    private SaveTask mAuthTask = null;
    private ProgressDialog pDialog;

    public ReceptionEggsFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hatcher_manager_rcpt_eggs, container, false);

        commonFunctions = new CommonFunctions(getActivity());
        sessionManager = new SessionManager(getActivity());
        masterDao = new MasterDao(getActivity());
        pDialog = new ProgressDialog(getActivity());

        edtRcptDate = (EditText) view.findViewById(R.id.edtRcptDate);
        iedt_Batch = (EditText) view.findViewById(R.id.iedt_Batch);
        edtRcptTime = (EditText) view.findViewById(R.id.edtRcptTime);
        iedt_Ext_Hatchery = (EditText) view.findViewById(R.id.iedt_Ext_Hatchery);
        iedt_Airway_Bill = (EditText) view.findViewById(R.id.iedt_Airway_Bill);

        iedt_Eggs = (EditText) view.findViewById(R.id.iedt_Ext_Hatchery);
        iedt_Dead_Eggs = (EditText) view.findViewById(R.id.iedt_Airway_Bill);

        itxtLay_Ext_Hatchery = (TextInputLayout) view.findViewById(R.id.itxtLay_Ext_Hatchery);
        btnScan = (Button) view.findViewById(R.id.btnScan);


        edtRcptDate.setText(commonFunctions.getCurrentDateInString("dd-MMM-yyyy"));
        Drawable img = getContext().getResources().getDrawable(R.drawable.date);
        img.setBounds(0, 0, 55, 55);
        edtRcptDate.setCompoundDrawables(null, null, img, null);
        edtRcptDate.setOnClickListener(this);


        edtRcptTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutePick = "00";
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View dialogLayout = inflater.inflate(R.layout.custom_time_picker, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyNumberPickerDialogStyle);
                hourPicker = (NumberPicker) dialogLayout.findViewById(R.id.hourPicker);
                minutePicker = (NumberPicker) dialogLayout.findViewById(R.id.minutePicker);
                builder.setView(dialogLayout);

                hourPicker.setMaxValue(24);
                hourPicker.setMinValue(0);
                minutePicker.setMaxValue(4);
                minutePicker.setMinValue(1);
                minutePicker.setDisplayedValues(new String[]{"00", "15", "30", "45"});
                setCurrentHoursToNumberPicker();

                hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        setHourPickerValue(picker.getValue());
                    }
                });

                minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        setMinutePicker(picker.getValue());
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        edtRcptTime.setText("");
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        String time;
                        String hour = "";
                        if (String.valueOf(hourPicker.getValue()).length() == 1)
                            hour = "0" + String.valueOf(hourPicker.getValue());
                        else
                            hour = String.valueOf(hourPicker.getValue());

                        time = hour + "." + minutePick;
                        edtRcptTime.setText(time);
                        dialog.dismiss();
//                        jobModel.setTime(time);
                    }
                });
                AlertDialog customAlertDialog = builder.create();
                customAlertDialog.show();
            }
        });
        iedt_Ext_Hatchery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFunctions.showHideDialog(pDialog);
                mAuthTask = new SaveTask(edtRcptDate.getText().toString(),edtRcptTime.getText().toString(),
                        iedt_Batch.getText().toString(),iedt_Eggs.getText().toString(),iedt_Dead_Eggs.getText().toString(),
                        String.valueOf(locID),iedt_Airway_Bill.getText().toString());
                mAuthTask.execute((Void) null);
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
//        DialogFragment newFragment = new DatePickerFragment(getApplicationContext(),view, edtDate, true);
//        newFragment.show(getFragmentManager(), "Date Picker");
        String date = commonFunctions.changeDateFormat(edtRcptDate.getText().toString(), new SimpleDateFormat("dd-MMM-yyyy"), new SimpleDateFormat("dd-MM-yyyy"));
        String dates[] = date.split("-");

        showDate(Integer.valueOf(dates[2]), Integer.valueOf(dates[1]) - 1, Integer.valueOf(dates[0]), R.style.DatePickerSpinner);
    }

    private void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(getActivity())
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .showTitle(false)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        edtRcptDate.setText(CommonFunctions.convertGregorianCalendar(calendar));

    }

    private void setCurrentHoursToNumberPicker() {
        String currentHours = edtRcptTime.getText().toString();
        if (currentHours.equals("") || currentHours.equals(null))
            setMinutePicker(1);

        else {
            String[] hourArray = currentHours.split("\\.");
            hourPicker.setValue(Integer.parseInt(hourArray[0]));

            if (hourArray[1].equals("00"))
                setMinutePicker(1);
            else if (hourArray[1].equals("15"))
                setMinutePicker(2);
            else if (hourArray[1].equals("30"))
                setMinutePicker(3);
            else
                setMinutePicker(4);
        }
    }

    private void setMinutePicker(int value) {
        switch (value) {
            case 1:
                minutePicker.setValue(1);
                minutePick = "00";
                break;
            case 2:
                minutePicker.setValue(2);
                minutePick = "15";
                break;
            case 3:
                minutePicker.setValue(3);
                minutePick = "30";
                break;
            case 4:
                minutePicker.setValue(4);
                minutePick = "45";
                break;
            default:
                break;
        }
    }

    private void setHourPickerValue(int value) {
        if (value == 24) {
            minutePicker.setMaxValue(1);
            minutePicker.setMinValue(1);
        } else {
            minutePicker.setMaxValue(4);
            minutePicker.setMinValue(1);
        }
    }

    private void showPopUp() {

        // initialize a pop up window type

        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());

        View mView = mLayoutInflater.inflate(R.layout.pop_up_layout, null);

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);


        popupWindow.setContentView(mView);

        final ArrayList<SpinnerObject> sortList = masterDao.fetchAdminStructure("LOC");


        ArrayAdapter<SpinnerObject> adapter = new ArrayAdapter<SpinnerObject>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                sortList);
        // the drop down list is a list view
        ListView listViewSort = (ListView) mView.findViewById(R.id.lst_popup);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        // set on item selected
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iedt_Ext_Hatchery.setText(sortList.get(position).getValue());
                locID = Integer.parseInt(sortList.get(position).getId());
                popupWindow.dismiss();
                CommonFunctions.hideSoftwareKeyboard(getActivity());

            }
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(iedt_Ext_Hatchery, 0, 0, Gravity.LEFT);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setView(1);
    }

    public class SaveTask extends AsyncTask<Void, Void, JSONObject> {

        private final String date;
        private final String time;
        private final String batch;
        private final String noeggs;
        private final String deadeggs;
        private final String locId;
        private final String airwaybillno;

        SaveTask(String date, String time, String batch, String noeggs, String deadeggs, String locId, String airwaybillno) {
            this.date = date;
            this.time = time;
            this.batch = batch;
            this.noeggs = noeggs;
            this.deadeggs = deadeggs;
            this.locId = locId;
            this.airwaybillno = airwaybillno;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HashMap<String, String> postParams = new HashMap<String, String>();
            postParams.put("user_id", sessionManager.getUserId());
            postParams.put("date", date);
            postParams.put("time", time);
            postParams.put("batch_no", batch);
            postParams.put("no_eggs", noeggs);
            postParams.put("no_dead_eggs", deadeggs);
            postParams.put("loc_id", locId);
            postParams.put("airway_bill_no", airwaybillno);
            postParams.put("opr_type", "INS");
            try {
                return CommonFunctions.performPostCall("http://trinityintellects.com/FishFarm/savereceptionEggs", postParams);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // TODO: register the new account here.
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(final JSONObject jsonResponse) {
            mAuthTask = null;

            ((HomeActivity) getActivity()).displayView(2);

            CommonFunctions.hideSoftwareKeyboard(getActivity());
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            CommonFunctions.showHideDialog(pDialog);
        }
    }

}

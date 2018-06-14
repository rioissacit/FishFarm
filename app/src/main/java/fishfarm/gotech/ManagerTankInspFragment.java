package fishfarm.gotech;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import helperClass.CommonFunctions;
import helperClass.SessionManager;


public class ManagerTankInspFragment extends Fragment  implements  View.OnClickListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener{
    View view;
    EditText edtRcptDate;
    EditText iedt_Batch;
    EditText edtRcptTime;
    EditText iedt_Oxygen_Per;
    EditText iedt_Temp;
    private CommonFunctions commonFunctions;
    private SessionManager sessionManager;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private String minutePick;
    TextView txtTemp;

    public ManagerTankInspFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hatchery_manager_tank_insp, container, false);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        commonFunctions = new CommonFunctions(getActivity());
        sessionManager = new SessionManager(getActivity());

        edtRcptDate = (EditText) view.findViewById(R.id.edtRcptDate);
        edtRcptTime = (EditText) view.findViewById(R.id.edtRcptTime);
        iedt_Oxygen_Per = (EditText) view.findViewById(R.id.iedt_Oxygen_Per);
        iedt_Temp = (EditText) view.findViewById(R.id.iedt_Temp);



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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyNumberPickerDialogStyle);
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
                        if(String.valueOf(hourPicker.getValue()).length() == 1)
                            hour = "0"+String.valueOf(hourPicker.getValue());
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
        return view;
    }
    @Override
    public void onClick(View view) {
//        DialogFragment newFragment = new DatePickerFragment(getApplicationContext(),view, edtDate, true);
//        newFragment.show(getFragmentManager(), "Date Picker");
        String date = commonFunctions.changeDateFormat(edtRcptDate.getText().toString(),new SimpleDateFormat("dd-MMM-yyyy"),new SimpleDateFormat("dd-MM-yyyy"));
        String dates[] = date.split("-");

        showDate(Integer.valueOf(dates[2]), Integer.valueOf(dates[1])-1, Integer.valueOf(dates[0]), R.style.DatePickerSpinner);
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
        if(currentHours.equals("") || currentHours.equals(null))
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
        }  else {
            minutePicker.setMaxValue(4);
            minutePicker.setMinValue(1);
        }
    }


}

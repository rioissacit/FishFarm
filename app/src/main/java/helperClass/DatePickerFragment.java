package helperClass;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fishfarm.gotech.R;

/**
 * Created by aruna.ramakrishnan on 3/7/2018.
 */

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private View mView;
    private boolean isApproved;
    DatePickerDialog dpd;
    EditText edtFromDate;
    EditText edtToDate;
    boolean isDateShown;
    boolean isNextShown;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleYearFormat;
    CommonFunctions commonFunctions;
    Context mContext;
    TextView toolbar;
    ArrayList<Integer> dateArray = new ArrayList<>();
    DatePicker dp;
    String fromDate;
    String outputDate;

    public DatePickerFragment(Context context, View view, EditText from, EditText to, boolean isShown) {
        mContext = context;
        mView = view;
        edtFromDate = from;
        edtToDate = to;
        isDateShown = isShown;
        commonFunctions = new CommonFunctions(mContext);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        long milliseconds = 0;


        dpd = new DatePickerDialog(getActivity(),R.style.DialogTheme, this, year, month, day) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                int month = getContext().getResources().getIdentifier("android:id/month", null, null);
                if (day != 0) {
                    View dayPicker = findViewById(day);
                    if (dayPicker != null && !isDateShown) {
                        //Set Day view visibility Off/Gone
                        dayPicker.setVisibility(View.GONE);
                    }
                }
                if (month != 0) {
                    View monthPicker = findViewById(month);
                    if (monthPicker != null && !isDateShown) {
                        //Set Day view visibility Off/Gone
                        monthPicker.setVisibility(View.GONE);
                    }
                }
            }
        };
        dpd.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    onDateSet(dp, dpd.getDatePicker().getYear(), dpd.getDatePicker().getMonth(), dpd.getDatePicker().getDayOfMonth());
                }
            }
        });


        if(!isDateShown && mView == edtFromDate)
            dpd.setTitle("Pick From year");
        else
            dpd.setTitle("Pick To year");

        dp = dpd.getDatePicker();
        dp.setSpinnersShown(true);


        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        simpleYearFormat = new SimpleDateFormat("yyyy");
        if (mView == edtFromDate){
            dp.setMinDate(commonFunctions.getCurrentDateInMilliSeconds() - 10000);
        }
        else if (mView == edtToDate){
            try {
                Date d = simpleDateFormat.parse("01-Jan-"+edtFromDate.getText().toString());
                long fromMilliseconds = d.getTime();
                dp.setMinDate(fromMilliseconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dpd;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Do something with the date chosen by the user
        String stringFromDate;
        SimpleDateFormat output;
        SimpleDateFormat input;

        if(!isDateShown) {
            outputDate = year + "";
        }
        else {
            stringFromDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            input = new SimpleDateFormat("dd-M-yyyy");
            output = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                Date inputDate = input.parse(stringFromDate);
                outputDate = (output.format(inputDate));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (mView == edtFromDate){
            edtFromDate.setText(outputDate);
            edtFromDate.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            edtFromDate.setHeight(50);
            edtFromDate.requestLayout();
        }
        else if (mView == edtToDate){
            edtToDate.setText(outputDate);
            edtToDate.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            edtToDate.setHeight(50);
            edtToDate.requestLayout();
        }

    }
}


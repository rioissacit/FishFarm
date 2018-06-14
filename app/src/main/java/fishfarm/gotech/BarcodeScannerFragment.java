package fishfarm.gotech;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import helperClass.CommonFunctions;
import helperClass.SessionManager;


public class BarcodeScannerFragment extends Fragment  implements  View.OnClickListener{
    View view;
    TabLayout tab_roles;
    Button btnScan;
    private CommonFunctions commonFunctions;
    private SessionManager sessionManager;

    public BarcodeScannerFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.barcode_scanner, container, false);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        commonFunctions = new CommonFunctions(getActivity());
        sessionManager = new SessionManager(getActivity());

        tab_roles = (TabLayout) view.findViewById(R.id.tab_roles);
        if(sessionManager.getDesignation().equals("MANG"))
            tab_roles.setVisibility(View.GONE);
        btnScan=(Button)view.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View view) {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setResultDisplayDuration(0);
        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).setView(2);
    }




}

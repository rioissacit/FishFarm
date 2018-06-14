package fishfarm.gotech;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import helperClass.CommonFunctions;
import helperClass.SessionManager;
import pl.aprilapps.easyphotopicker.Constants;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class FeederTankInspActivity extends AppCompatActivity implements  View.OnClickListener{
    View view;
    EditText iedt_Loc_Hatchery;
    EditText iedt_Qty;
    EditText edtRcptTime;
    EditText iedt_Top_Count;
    EditText iedt_Bottom_Count;
    EditText iedt_Oxygen_Per;
    EditText iedt_Temp;
    ImageView img_Top_Tank;
    ImageView img_Bottom_Tank;
    PopupWindow popupWindow;
    ListView lstFeederType;
    FeedTypeListAdapter feederTypeListAdapter;
    String fileImageName;
    String filePath;

   int resCode=0;

    private CommonFunctions commonFunctions;
    private SessionManager sessionManager;
    private String beforeFeed;
    private String afterFeed;
    long timer = 600000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hatchery_feeder_tank_insp);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setTitle("");
        commonFunctions = new CommonFunctions(this);
        sessionManager = new SessionManager(this);

        iedt_Loc_Hatchery = (EditText) findViewById(R.id.iedt_Loc_Hatchery);
        iedt_Top_Count = (EditText) findViewById(R.id.iedt_Top_Count);
        iedt_Bottom_Count = (EditText) findViewById(R.id.iedt_Bottom_Count);

        iedt_Oxygen_Per = (EditText) findViewById(R.id.iedt_Oxygen_Per);
        iedt_Temp = (EditText) findViewById(R.id.iedt_Temp);

        iedt_Qty = (EditText) findViewById(R.id.iedt_Qty);
        lstFeederType = (ListView) findViewById(R.id.lstFeedType);
        setViewListner();
        EasyImage.configuration(this)
                .setImagesFolderName("FishFarm")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(false);

        img_Top_Tank=(ImageView) findViewById(R.id.img_Top);
        img_Bottom_Tank=(ImageView) findViewById(R.id.img_Bottom);
        CommonFunctions.hideSoftwareKeyboard(this);
    }

    private void setViewListner()
    {
        ImageView img=(ImageView)findViewById(R.id.imgBefNormal);
        img.setOnClickListener(this);
        img=(ImageView)findViewById(R.id.imgBefModerate);
        img.setOnClickListener(this);
        img=(ImageView)findViewById(R.id.imgBefAggressive);
        img.setOnClickListener(this);
        img=(ImageView)findViewById(R.id.imgAftNormal);
        img.setOnClickListener(this);
        img=(ImageView)findViewById(R.id.imgAftModerate);
        img.setOnClickListener(this);
        img=(ImageView)findViewById(R.id.imgAftAggressive);
        img.setOnClickListener(this);
        img=(ImageView)findViewById(R.id.imgFeedScan);
        img.setOnClickListener(this);
        img=(ImageView)findViewById(R.id.img_Top);
        img.setOnClickListener(this);
        img=(ImageView)findViewById(R.id.img_Bottom);
        img.setOnClickListener(this);
        EditText iedt_Loc_Hatchery =(EditText)findViewById(R.id.iedt_Loc_Hatchery);
        iedt_Loc_Hatchery.setOnClickListener(this);
        Button btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBefNormal: {
                beforeFeed="BNRM";
                selectImage((ImageView)findViewById(R.id.imgBefNormal),true);
                selectImage((ImageView)findViewById(R.id.imgBefModerate),false);
                selectImage((ImageView)findViewById(R.id.imgBefAggressive),false);
                break;
            }
            case R.id.imgBefModerate: {
                beforeFeed="BMOD";
                selectImage((ImageView)findViewById(R.id.imgBefModerate),true);
                selectImage((ImageView)findViewById(R.id.imgBefNormal),false);
                selectImage((ImageView)findViewById(R.id.imgBefAggressive),false);
                break;
            }
            case R.id.imgBefAggressive: {
                beforeFeed="BAGG";
                selectImage((ImageView)findViewById(R.id.imgBefAggressive),true);
                selectImage((ImageView)findViewById(R.id.imgBefModerate),false);
                selectImage((ImageView)findViewById(R.id.imgBefNormal),false);
                break;
            }
            case R.id.imgAftNormal: {
                afterFeed="ANRM";
                selectImage((ImageView)findViewById(R.id.imgAftNormal),true);
                selectImage((ImageView)findViewById(R.id.imgAftModerate),false);
                selectImage((ImageView)findViewById(R.id.imgAftAggressive),false);
                break;
            }
            case R.id.imgAftModerate: {
                afterFeed="AMOD";
                selectImage((ImageView)findViewById(R.id.imgAftModerate),true);
                selectImage((ImageView)findViewById(R.id.imgAftNormal),false);
                selectImage((ImageView)findViewById(R.id.imgAftAggressive),false);
                break;
            }
            case R.id.imgAftAggressive: {
                afterFeed="AAGG";
                selectImage((ImageView)findViewById(R.id.imgAftAggressive),true);
                selectImage((ImageView)findViewById(R.id.imgAftModerate),false);
                selectImage((ImageView)findViewById(R.id.imgAftNormal),false);
                break;
            }
            case R.id.imgFeedScan: {
                resCode=3;
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setResultDisplayDuration(0);
                integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();
                break;
            }
            case R.id.iedt_Loc_Hatchery: {
                showPopUp();
                break;
            }
            case R.id.btnSubmit: {
                CommonFunctions.hideSoftwareKeyboard(this);
                this.finish();
                break;
            }
            case R.id.img_Top:{
                resCode=1;
                EasyImage.openChooserWithGallery(FeederTankInspActivity.this, "Pick source", 0);
                break;
            }
            case R.id.img_Bottom:{
                resCode=2;
                EasyImage.openChooserWithGallery(FeederTankInspActivity.this, "Pick source", 0);
                break;
            }
            default:
                break;

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);

        final MenuItem counter = menu.findItem(R.id.counter);
        new CountDownTimer(timer, 1000) {

            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String  hms = (TimeUnit.MILLISECONDS.toMinutes(millis) -TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                counter.setTitle(hms);
                timer = millis;

            }

            public void onFinish() {
                counter.setTitle("done!");
            }
        }.start();

        return  true;
    }

    private void showPopUp() {

        // initialize a pop up window type

        LayoutInflater mLayoutInflater=LayoutInflater.from(this);

        View mView=mLayoutInflater.inflate(R.layout.pop_up_layout, null);

        popupWindow=new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);


        popupWindow.setContentView(mView);

        final ArrayList<String> sortList = new ArrayList<String>();
        sortList.add("Dubai");
        sortList.add("India");
        sortList.add("Saudi Arabia");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                sortList);
        // the drop down list is a list view
        ListView listViewSort = (ListView)mView.findViewById(R.id.lst_popup);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        // set on item selected
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iedt_Loc_Hatchery.setText(sortList.get(position));
                popupWindow.dismiss();

            }
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(iedt_Loc_Hatchery, 0, 0, Gravity.LEFT);

    }
    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {

        if(resCode!=3){
            EasyImage.handleActivityResult(requestCode, resultCode, intent, this, new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    //Some error handling
                    e.printStackTrace();
                }

                @Override
                public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) throws IOException {
                    if (imageFiles.size() > 0) {
                        Bitmap myBitmap = CommonFunctions.rotateImage(imageFiles.get(0).getAbsolutePath());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap bm = Bitmap.createScaledBitmap(myBitmap, 250, 250, true);
                        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        filePath = saveImageFile(bm);
                        if (!filePath.isEmpty()) {
                            if(resCode==1)
                            img_Top_Tank.setImageBitmap(bm);
                            else
                                img_Bottom_Tank.setImageBitmap(bm);

//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        Bitmap bm = Bitmap.createScaledBitmap(myBitmap, 220, 220, true);
//                        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                        byte[] byteArray = byteArrayOutputStream.toByteArray();
//                        stampBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                        stampedAttachmentId = "";
                        }

                    }
                }

            });
        }
        else{
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            if (scanningResult != null) {
                //we have a result
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();

                addFeederTypeList(new FeedTypeModel("Feeder A",iedt_Qty.getText().toString()));
                iedt_Qty.setText("");

            }else{
                Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }
    private void addFeederTypeList(FeedTypeModel feedTypeModel) {
        ArrayList<FeedTypeModel> feedTypeModelArrayList=new ArrayList<FeedTypeModel>();
        if(feederTypeListAdapter !=null)
        {
            feedTypeModelArrayList=feederTypeListAdapter.feedTypesList;
        }
        else {
            feederTypeListAdapter = new FeedTypeListAdapter(this,feedTypeModelArrayList,"INS");
        }
        feedTypeModelArrayList.add(feedTypeModel);
        feederTypeListAdapter.feedTypesList=feedTypeModelArrayList;

        lstFeederType.setAdapter(feederTypeListAdapter);
        CommonFunctions.setListViewHeightBasedOnChildren(lstFeederType);
        feederTypeListAdapter.notifyDataSetChanged();
    }
    public void removeItem(int position){
        feederTypeListAdapter.feedTypesList.remove(position);
        CommonFunctions.setListViewHeightBasedOnChildren(lstFeederType);
        feederTypeListAdapter.notifyDataSetChanged();
    }
    public String saveImageFile(Bitmap bitmap) {
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }
    private String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "FishFarm");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileImageName = System.currentTimeMillis() + ".jpg";
        String uriSting = (file.getAbsolutePath() + "/"
                + fileImageName);
        return uriSting;
    }

    @Override
    protected void onResume() {
        CommonFunctions.hideSoftwareKeyboard(this);
        super.onResume();
    }

    private void selectImage(ImageView img, boolean isSelected)
    {
        LinearLayout.LayoutParams parms =(LinearLayout.LayoutParams) img.getLayoutParams();
        if(isSelected){
            parms.height=CommonFunctions.pixToDp(this,50);
            parms.width=CommonFunctions.pixToDp(this,75);

        }
        else
        {
            parms.height=CommonFunctions.pixToDp(this,45);
            parms.width=CommonFunctions.pixToDp(this,70);

        }
        img.setLayoutParams(parms);
        img.requestLayout();
    }

}

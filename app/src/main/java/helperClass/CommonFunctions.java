package helperClass;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import fishfarm.gotech.LoginActivity;
import fishfarm.gotech.R;


/**
 * Created by aruna.ramakrishnan on 3/5/2018.
 */
public class CommonFunctions {
    private static Context context;
    private SessionManager session;

    public CommonFunctions() {
    }

    public CommonFunctions(Context context) {
        this.context = context;
        session = new SessionManager(context);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isAuthenticateUser(JSONObject jObj) {
        try {
            if (!jObj.get("CommonEntity").equals(null)) {
                Log.e("notificationDatedown", String.valueOf(jObj.getJSONObject("CommonEntity")));
                session = new SessionManager(this.context);
                JSONObject objCommonEntity = jObj.getJSONObject("CommonEntity");
                if (objCommonEntity.getString("IsAuthourized").toString().matches("N")) {
                    Intent myIntent = new Intent(this.context, LoginActivity.class);
                    context.startActivity(myIntent);
                    session.setLogin(false);
                    Toast.makeText(this.context,
                            R.string.logged_out, Toast.LENGTH_LONG)
                            .show();
                    ((Activity) this.context).finish();
                } else {
                    if (objCommonEntity.getString("TransactionStatus").toString().matches("N") && !objCommonEntity.getString("Message").toString().equals("")) {
                        Toast.makeText(this.context,
                                objCommonEntity.getString("Message").toString(), Toast.LENGTH_LONG)
                                .show();

                        return false;
                    }
                }
            }

        } catch (Exception e) {
            // JSON error
            e.printStackTrace();
        }

        return true;
    }

    public Boolean isUserAuthenticate(JSONObject jObj) {
        try {
            if (!jObj.get("CommonEntity").equals(null)) {
                JSONObject objCommonEntity = jObj.getJSONObject("CommonEntity");
                if (objCommonEntity.getString("IsAuthourized").toString().matches("Y")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void showSnackBar(View view, String text, String buttonText) {
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG).setActionTextColor(context.getResources().getColor(R.color.white));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));


        snackbar.setAction(buttonText, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static ArrayList<Integer> dateConversion(String date, SimpleDateFormat inputFormat) { //inputformat = "dd-MMM-yyyy"
        Date convertedInputDate;
        String outputDate;
        Date convertedOutputDate = null;
        ArrayList<Integer> dateArray = new ArrayList<>();

        try {
            convertedInputDate = inputFormat.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            outputDate = formatter.format(convertedInputDate);
            String separatedDate[] = outputDate.split("-");
            dateArray.add(0, Integer.parseInt(separatedDate[2]));
            dateArray.add(1, Integer.parseInt(separatedDate[1]) - 1);
            dateArray.add(2, Integer.parseInt(separatedDate[0]));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateArray;
    }

    public String changeDateFormat(String date, SimpleDateFormat inputFormat, SimpleDateFormat outPutFormat) { //inputformat = "dd-MMM-yyyy"
        Date convertedInputDate;
        String outputDate = null;
        try {
            convertedInputDate = inputFormat.parse(date);
            outputDate = outPutFormat.format(convertedInputDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputDate;
    }

    public String getCurrentDateInString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return (dateFormat.format(date));
    }


    public static Boolean isRefresh(Context context, String url, String parameters) {
        Boolean status = true;
        long time = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

//        String projection[] = {RequestLogContract.RequestLog.COLUMN_NAME_TIME_STAMP};
//        MasterDbHelper homeDBHelper = new MasterDbHelper(context);
//        SQLiteDatabase db = homeDBHelper.getReadableDatabase();
//        Cursor cursor = db.query(RequestLogContract.RequestLog.TABLE_NAME,
//                projection,
//                RequestLogContract.RequestLog.COLUMN_NAME_URL + " =  '" + url + "' AND " + RequestLogContract.RequestLog.COLUMN_NAME_PARAMETERS + " =  '" + parameters + "'",
//                null,
//                null,
//                null,
//                null);
//
//        if (cursor.moveToFirst()) {
//            time = cursor.getLong(cursor.getColumnIndexOrThrow(RequestLogContract.RequestLog.COLUMN_NAME_TIME_STAMP));
//            if (url.equals(URLs.URL_FETCH_MASTER_LIST.substring(URLs.URL_FETCH_MASTER_LIST.lastIndexOf("/")))) {
//                status = (System.currentTimeMillis() - time) >= MasterDbHelper.TIME_TO_REFRESH_HOME_REQUEST;
//            } else if (url.equals(URLs.URL_FETCH_JOB_LIST.substring(URLs.URL_FETCH_JOB_LIST.lastIndexOf("/")))) {
//                status = true;//(System.currentTimeMillis() - time) >= JobWorkDbHelper.TIME_TO_REFRESH_HOME_REQUEST;
//            } else {
//                status = (System.currentTimeMillis() - time) >= AppConfig.TIME_TO_REFRESH;
//            }
//            if (status) {
//                homeDBHelper.insertRequestLog(url, parameters);
//            }
//        } else {
//            homeDBHelper.insertRequestLog(url, parameters);
//            status = true;
//        }
        return status;
    }

    private static String getDateInString(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    public static int getDaysDifference(Date fromDate, Date toDate) {
        long diff = 0;
        if (fromDate == null || toDate == null)
            return 0;

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        String inputString1 = myFormat.format(fromDate);
        String inputString2 = myFormat.format(toDate);

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            diff = date2.getTime() - date1.getTime();
            //System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        //return (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static Date getCurrentDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        Date date = null;
        try {
            date = df.parse(formattedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

//    public static void deleteRequestLog(String url, Context context) {
//        MasterDbHelper masterDBHelper = new MasterDbHelper(context);
//        SQLiteDatabase db = masterDBHelper.getWritableDatabase();
//        db.execSQL(" Delete from  " + RequestLogContract.RequestLog.TABLE_NAME + " WHERE " + RequestLogContract.RequestLog.COLUMN_NAME_URL + " ='" + url + "'");
//        masterDBHelper.close();
//        db.close();
//
//    }

    public static void showHideDialog(ProgressDialog pDialog) {
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        if (!pDialog.isShowing())
            pDialog.show();
        else
            pDialog.dismiss();
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight +   (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static String convertGregorianCalendar(GregorianCalendar calendar){
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        fmt.setCalendar(calendar);
        String dateFormatted = fmt.format(calendar.getTime());
        return dateFormatted;
    }

    public static long getCurrentDateInMilliSeconds() {
        long time = System.currentTimeMillis();
        return time;
    }

    public static String convertDateInMilliSecondsToString(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static class DownloadPdf extends AsyncTask<String, String, String> {

        String fileURL;
        Context mContext;
        AsyncResponse asyncResponse;
        String pageName;
        String reportName;
        String jobNumber;
        EncodeDecode64 encodeDecode64;

        public DownloadPdf(Context context, String pdfLink, String jobNum, String report, String name) {
            fileURL = pdfLink;
            mContext = context;
            asyncResponse = (AsyncResponse) context;
            pageName = name;
            jobNumber = jobNum;
            reportName = report;
            encodeDecode64 = new EncodeDecode64(mContext);
            File dirDiveMarine = encodeDecode64.getFile(mContext, "/DiveMarine/Reports/"+jobNumber);
            if(!dirDiveMarine.exists())
                dirDiveMarine.mkdirs();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/DiveMarine/Reports/"+jobNumber+"/"+reportName+".pdf"));
                byte[] pdfAsBytes = Base64.decode(fileURL, Base64.DEFAULT);
                fileOutputStream.write(pdfAsBytes);
                fileOutputStream.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return reportName;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            asyncResponse.processFinish(result);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    public void shareIntent(Context mContext, ProgressDialog progressDialog, String fileName, String reportName, String jobNumber) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_SUBJECT, reportName + " Report ");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DiveMarine/Reports/"+jobNumber+"/"+fileName+".pdf")));
        mContext.startActivity(Intent.createChooser(intent, "Send Email"));
        progressDialog.dismiss();
    }

    public void multipleFilesshareIntent(Context mContext, String jobNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Reports");
        intent.setType("application/pdf");

        File f = new File(Environment.getExternalStorageDirectory().toString() + "/DiveMarine/Reports/"+jobNumber);
        File reportFiles[] = f.listFiles();

        ArrayList<Uri> files = new ArrayList<Uri>();
        for (File file : reportFiles) {
            Uri uri = Uri.fromFile(file);
            files.add(uri);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        mContext.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public static boolean checkAndRequestPermissions(Activity activity, String permission, int requestCode) {
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permission.equals(Manifest.permission.CAMERA)) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        } else {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestCode);
            return false;
        }
        return true;
    }

    public void sendCommonPushNotification(String userId, String deviceId) {
        final JSONObject params = new JSONObject();
        try {
            params.put("UserId", userId);
            params.put("DeviceId", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("params",params.toString());
        Log.d("url", URLs.URL_COMMON_PUSH_NOTIFICATION);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                URLs.URL_COMMON_PUSH_NOTIFICATION, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jObj) {
                        try {

                        } catch (Exception e) {
                            // JSON error
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("PANDA", "Error: " + error.getMessage());
                Log.d("PANDA", error.toString());
                Toast.makeText(context,
                        AppConfig.NO_INTERNET, Toast.LENGTH_LONG)
                        .show();
            }
        });

        // Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }

    public boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }
    public static void hideSoftwareKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static Bitmap rotateImage(String photoPath) throws IOException {

        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_ROTATE_90);

        Bitmap rotatedBitmap = null;
        Bitmap myBitmap = BitmapFactory.decodeFile(photoPath);
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap =rotateBitmap(myBitmap,90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateBitmap(myBitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateBitmap(myBitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = myBitmap;


        }
        return rotatedBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static int pixToDp(Context context,int i)
    {
       return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, context.getResources().getDisplayMetrics());
    }

    public static JSONObject  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) throws JSONException {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject(response);
    }
    private static String  getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

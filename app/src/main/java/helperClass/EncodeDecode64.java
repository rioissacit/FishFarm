package helperClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by aruna.ramakrishnan on 5/4/2018.
 */
public class EncodeDecode64 {

    String fullpath;
    String filename;
    Context context;
    File ext;
    File mydir;
    private String TAG = "base64";
    String path;

    public EncodeDecode64(Context context) {
        this.context = context;
    }

    public EncodeDecode64(Context context, String filename) {
        this.context = context;
        this.filename = filename;
        ext = Environment.getExternalStorageDirectory();
        mydir = new File(ext.getAbsolutePath() + "/DiveMarine");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
    }

    public File getFile(Context context, String filename){
        this.context = context;
        this.filename = filename;
        ext = Environment.getExternalStorageDirectory();
        File file = new File(ext.getAbsolutePath(), filename);
        return file;
    }

    //store decoded file to downloads
    public String decodeFile(String strFile) {
        try {
            byte[] data = Base64.decode(strFile, Base64.DEFAULT);
            ext = Environment.getExternalStorageDirectory();
            mydir = new File(ext.getAbsolutePath() + "/DiveMarine");
            if (!mydir.exists()) {
                Log.i(TAG, "directory created");
                mydir.mkdirs();
            }
            fullpath = ext.getAbsolutePath() + "/DiveMarine/" + filename;
            File file = new File(mydir, filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return fullpath;

    }

    public String decodeExternalBitmapFile(Bitmap b, String picName) {
        try {
            ext = Environment.getExternalStorageDirectory();
            mydir = new File(ext + "/DiveMarine");
            if (!mydir.exists()) {
                Log.i(TAG, "directory created");
                mydir.mkdirs();
            }

            fullpath = ext.getAbsolutePath() + picName;
            File file = new File(mydir, picName);

            if (file.exists()) {
                file.delete(); //DELETE existing file
                file = new File(mydir, picName);

            }
            path = mydir + "/" + picName;
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height
                        / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }
}

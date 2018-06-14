package SQLClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fishfarm.gotech.MasterContract;
import helperClass.SpinnerObject;

/**
 * Created by aruna.ramakrishnan on 3/12/2018.
 */

public class MasterDao {

    MasterDbHelper masterDbHelper;
    Context mContext;
    private static final String COMMA_SEP = ",";

    public MasterDao(Context context) {
        masterDbHelper = new MasterDbHelper(context);
        mContext = context;
    }

    public void insertAdminStructureList(JSONObject jObj) {
        SQLiteDatabase db = masterDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("Delete from  " + MasterContract.AdminStructureList.TABLE_NAME);
            ContentValues values = new ContentValues();
            if (!jObj.get(MasterContract.AdminStructureList.TABLE_NAME).equals(null)) {
                JSONArray jsonArray = jObj.getJSONArray(MasterContract.AdminStructureList.TABLE_NAME);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    values.put(MasterContract.AdminStructureList.COLUMN_NAME_ID, jsonObject1.getString(MasterContract.AdminStructureList.COLUMN_NAME_ID).toString());
                    values.put(MasterContract.AdminStructureList.COLUMN_NAME_CODE, jsonObject1.getString(MasterContract.AdminStructureList.COLUMN_NAME_CODE).toString());
                    values.put(MasterContract.AdminStructureList.COLUMN_NAME_NAME, jsonObject1.getString(MasterContract.AdminStructureList.COLUMN_NAME_NAME).toString());
                    values.put(MasterContract.AdminStructureList.COLUMN_NAME_RECORD_TYPE, jsonObject1.getString(MasterContract.AdminStructureList.COLUMN_NAME_RECORD_TYPE).toString());

                    db.insert(
                            MasterContract.AdminStructureList.TABLE_NAME,
                            MasterContract.AdminStructureList.COLUMN_NAME_NULLABLE,
                            values);
                }
                db.setTransactionSuccessful();
            }

        } catch (Exception e) {
            // JSON error
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public ArrayList<SpinnerObject> fetchAdminStructure(String recordType) {
        SQLiteDatabase db = masterDbHelper.getReadableDatabase();
        ArrayList<SpinnerObject> lists = new ArrayList<SpinnerObject>();

        String selectQuery = "SELECT  * FROM " + MasterContract.AdminStructureList.TABLE_NAME + " WHERE "+MasterContract.AdminStructureList.COLUMN_NAME_RECORD_TYPE + " = '" + recordType+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                lists.add(new SpinnerObject(cursor.getString(cursor.getColumnIndexOrThrow(MasterContract.AdminStructureList.COLUMN_NAME_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MasterContract.AdminStructureList.COLUMN_NAME_NAME))));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning vessel list
        return lists;
    }


    public String fetchAdminStructureName(String id, String type) {
        String name = "";
        SQLiteDatabase db = masterDbHelper.getReadableDatabase();
        String selectQuery = "SELECT" +
                " AD." + MasterContract.AdminStructureList.COLUMN_NAME_NAME +

                " FROM " + MasterContract.AdminStructureList.TABLE_NAME + " AD " +

                " WHERE  AD." + MasterContract.AdminStructureList.COLUMN_NAME_ID + " =  '" +id +"'" +
                " AND  AD." + MasterContract.AdminStructureList.COLUMN_NAME_RECORD_TYPE + " =  '" +type+"'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
         name = cursor.getString(cursor.getColumnIndexOrThrow(MasterContract.AdminStructureList.COLUMN_NAME_NAME));
        }

        // closing connection
        cursor.close();
        db.close();
        return name;
    }

    public String fetchAdminStructureId(String name, String type) {
        String id = "";
        SQLiteDatabase db = masterDbHelper.getReadableDatabase();
        String selectQuery = "SELECT" +
                " AD." + MasterContract.AdminStructureList.COLUMN_NAME_ID +

                " FROM " + MasterContract.AdminStructureList.TABLE_NAME + " AD " +

                " WHERE  AD." + MasterContract.AdminStructureList.COLUMN_NAME_NAME + " =  '" +name +"'" +
                " AND  AD." + MasterContract.AdminStructureList.COLUMN_NAME_RECORD_TYPE + " =  '" +type+"'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow(MasterContract.AdminStructureList.COLUMN_NAME_ID));
        }

        // closing connection
        cursor.close();
        db.close();
        return id;
    }


}

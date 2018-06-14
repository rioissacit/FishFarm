package SQLClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fishfarm.gotech.MasterContract;


public class MasterDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public Boolean isDelete = true;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Master.db";
    public static final long TIME_TO_REFRESH_HOME_REQUEST = 1200000;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ADMIN_STRUCTURE_LIST =
            "CREATE TABLE " + MasterContract.AdminStructureList.TABLE_NAME + " (" +
                    MasterContract.AdminStructureList.COLUMN_NAME_ID + " INTEGER ," +
                    MasterContract.AdminStructureList.COLUMN_NAME_CODE + TEXT_TYPE + COMMA_SEP +
                    MasterContract.AdminStructureList.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    MasterContract.AdminStructureList.COLUMN_NAME_RECORD_TYPE + " )";



    private static final String SQL_CREATE_TEAM_ROLE_LIST =
            "CREATE TABLE " + MasterContract.TeamRoleList.TABLE_NAME + " (" +
                    MasterContract.TeamRoleList.COLUMN_NAME_ID + " INTEGER ," +
                    MasterContract.TeamRoleList.COLUMN_NAME_TEAM_ID + TEXT_TYPE + COMMA_SEP +
                    MasterContract.TeamRoleList.COLUMN_NAME_ROLE_ID + TEXT_TYPE + COMMA_SEP +
                    MasterContract.TeamRoleList.COLUMN_NAME_USER_ID + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            " DROP TABLE IF EXISTS " + MasterContract.AdminStructureList.TABLE_NAME +
            " DROP TABLE IF EXISTS " + MasterContract.TeamRoleList.TABLE_NAME;

    private static final String SQL_DELETE_ADMIN_STRUCTURE_LIST =
            "Delete from  " + MasterContract.AdminStructureList.TABLE_NAME;

    private static final String SQL_DELETE_TEAM_ROEL_LIST =
            "Delete from  " + MasterContract.TeamRoleList.TABLE_NAME;


    public MasterDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        isDelete = true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ADMIN_STRUCTURE_LIST);
        db.execSQL(SQL_CREATE_TEAM_ROLE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MasterContract.AdminStructureList.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MasterContract.TeamRoleList.TABLE_NAME);

        onCreate(db);
    }

}

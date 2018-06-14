package fishfarm.gotech;

import android.provider.BaseColumns;

/**
 * Created by aruna.ramakrishnan on 3/12/2018.
 */

public class MasterContract {

    public static abstract class AdminStructureList implements BaseColumns {
        public static final String TABLE_NAME = "adminStructure";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CODE = "code";
        public static final String COLUMN_NAME_NAME = "value";
        public static final String COLUMN_NAME_RECORD_TYPE = "record_type";
        public static final String COLUMN_NAME_NULLABLE = "";
    }



    public static abstract class TeamRoleList implements BaseColumns {
        public static final String TABLE_NAME = "TeamRoleList";
        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_TEAM_ID = "Team_Id";
        public static final String COLUMN_NAME_ROLE_ID = "Role_Id";
        public static final String COLUMN_NAME_USER_ID = "User_Id";
        public static final String COLUMN_NAME_NULLABLE = "";
    }
}

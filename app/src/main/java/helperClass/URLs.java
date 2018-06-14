package helperClass;


import static helperClass.AppConfig.URL_DM_SERVER;

/**
 * Created by rio.issac on 20-03-2018.
 */

public class URLs {
    public static String URL_LOGIN = helperClass.AppConfig.URL_SERVER + "Common/LoginRequest";
    public static String URL_FETCH_JOB_LIST = URL_DM_SERVER +"Home/FetchJobList";
    public static String URL_FETCH_MASTER_LIST = URL_DM_SERVER +"Home/FetchMasterList";
    public static String URL_FETCH_MENU_RIGHTS = URL_DM_SERVER +"Home/FetchMenuRights";
    public static String URL_FETCH_JOB_LIST_BY_ID = URL_DM_SERVER +"Home/FetchJobById";
    public static String URL_FETCH_JOB_LOCK_UNLOCK = URL_DM_SERVER +"Home/LockJobDetails";
    public static String URL_FETCH_NOTIFY_LIST = URL_DM_SERVER +"Home/FetchNotifyList";
    public static String URL_SAVE_NOTIFY_LIST = URL_DM_SERVER +"Home/SaveNotifyListAndPushToMobile";
    public static String URL_SAVE_JOB_DETAILS = URL_DM_SERVER + "Home/SaveDetails";
    public static String URL_FETCH_SERACH_JOB_LIST = URL_DM_SERVER + "Home/SearchJobList";
    public static String URL_FETCH_JOB_DETAILS_REPORT_URL = URL_DM_SERVER + "Home/getJobDetailsRptUrl";
    public static String URL_FETCH_NOTIFICATION = URL_DM_SERVER + "Home/GetNotificationDetails";
    public static String URL_UPDATE_NOTIFICATION = URL_DM_SERVER + "Home/UpdateNotifyStatus";
    public static String URL_FETCH_TEAM_MEMBER_LIST = URL_DM_SERVER + "Team/GetTeamMemberList";
    public static String URL_FETCH_SAVED_TEAM_MEMBER_LIST_BY_JOB = URL_DM_SERVER + "Team/GetTeamListByJob";
    public static String URL_SAVE_TEAM_MEMBER_LIST = URL_DM_SERVER + "Team/SaveTeamDetails";
    public static String URL_LOCK_TEAM = URL_DM_SERVER + "Team/LockTeam";
    public static String URL_SAVE_DIVING_CLEARANCE = URL_DM_SERVER + "DivingClearance/SaveDivingClearanceByJob";
    public static String URL_GET_DIVING_CLEARANCE = URL_DM_SERVER + "DivingClearance/GetDivingClearanceListByJob";
    public static String URL_FETCH_DIVING_ATTACHMENTS = URL_DM_SERVER + "DivingClearance/FetchAttachment";
    public static String URL_COMMON_PUSH_NOTIFICATION = URL_DM_SERVER + "PushNotification/CommonPushNotification";
    public static String URL_FETCH_DIVING_CLEARANCE_REPORT_URL = URL_DM_SERVER + "DivingClearance/getDivingClearanceDetailsRptData";
}

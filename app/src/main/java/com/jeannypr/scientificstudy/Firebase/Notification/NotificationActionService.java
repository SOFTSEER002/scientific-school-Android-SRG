package com.jeannypr.scientificstudy.Firebase.Notification;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Chat.activity.ChatActivity;
import com.jeannypr.scientificstudy.Classwork.activity.CwHwDetailActivity;
import com.jeannypr.scientificstudy.Base.activity.MainActivity;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.Notification.activity.NotificationsActivity;
import com.jeannypr.scientificstudy.leave.activity.LeaveModuleActivity;

public class NotificationActionService extends IntentService {
    Context context;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationActionService(String name) {
        super(name);
    }

    public NotificationActionService() {
        super("");
        context = this;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = null;
        int notificationId;
        if (intent != null) {
            action = intent.getAction();

            if (intent.hasExtra("notificationFor")) {

                String notificationFor = intent.getStringExtra("notificationFor");
                notificationId = intent.getIntExtra("notificationId", 0);

                switch (notificationFor) {
                    case Constants.NotificationFor.CHAT:

                        int chatRoomId = intent.getIntExtra("chatRoomId", 0);
                        String chatRoomName = intent.getStringExtra("chatRoomName");
                        boolean isGroup = intent.getBooleanExtra("isGroup", false);
                        int otherUserId = intent.getIntExtra("classOrOtherUserId", 0);
                        boolean isClass = intent.getBooleanExtra("IsClass", false);
                        boolean isAllTeacherGroup = intent.getBooleanExtra("IsAllTeacherGroup", false);

                        Intent openChatIntent = new Intent(NotificationActionService.this, ChatActivity.class);
                        openChatIntent.putExtra("chatRoomId", chatRoomId);
                        openChatIntent.putExtra("chatRoomName", chatRoomName);
                        openChatIntent.putExtra("isGroup", isGroup);
                        openChatIntent.putExtra("otherUserId", otherUserId);
                        openChatIntent.putExtra("IsAllTeacherGroup", isAllTeacherGroup);
                        openChatIntent.putExtra("IsClass", isClass);

                        if (isClass) {
                            openChatIntent.putExtra("ClassId", otherUserId);
                        }
                        openChatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(openChatIntent);

                        NotificationManagerCompat.from(NotificationActionService.this).cancel(notificationId);
                        break;

                    case Constants.NotificationFor.TRANSPORT:
                        String role = UserPreference.getInstance(getApplicationContext()).getUserData().getRoleTitle();

                        Intent transportIntent;

                        if (role.equals(Constants.Role.PARENT) || role.equals(Constants.Role.DRIVER)) {
                            transportIntent = new Intent(NotificationActionService.this, NotificationsActivity.class);
                        } else if (role.equals(Constants.Role.ADMIN)) {
                            //TODO: check for permission
                           /* ArrayList<DashboardModulesModel> permissionData = UserPreference.getInstance(context).getPermissionData();
                            for (DashboardModulesModel permissionDatum : permissionData) {
                                if(permissionDatum.getModuleId() == )
                            }*/
                            transportIntent = new Intent(NotificationActionService.this, NotificationsActivity.class);

                        } else {
                            transportIntent = new Intent(NotificationActionService.this, MainActivity.class);
                        }

                        transportIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(transportIntent);
                        break;

                    case Constants.NotificationFor.CLASSWORK:
                    case Constants.NotificationFor.HOMEWORK:

                      /*  int senderrId = intent.getIntExtra("senderId", 0);
                        int receiverID = intent.getIntExtra("receiverIds", 0);
                        String notificationDate = intent.getStringExtra("notificationDate");
                        String notificationTime = intent.getStringExtra("notificationTime");
                        String notificationsFor = intent.getStringExtra("notificationFor");*/

                        int diaryId = intent.getIntExtra("diaryId", 0);
                        String className = intent.getStringExtra("className");
                        String subjectName = intent.getStringExtra("subjectName");
                        int diaryType = intent.getIntExtra("diaryType", 0);

                        Intent cwhwIntent = new Intent(NotificationActionService.this, CwHwDetailActivity.class);
                        cwhwIntent.putExtra("activityId", diaryId);
                        cwhwIntent.putExtra("className", className);
                        cwhwIntent.putExtra("subjectName", subjectName);
                        cwhwIntent.putExtra("activityTypeId", diaryType);
                        cwhwIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cwhwIntent);
                        break;


                    case Constants.NotificationFor.LEAVE_APPROVER:
                        /*String fromeDate = intent.getStringExtra("fromDate");
                        String toDate = intent.getStringExtra("toDate");*/

                        int requesterType = intent.getIntExtra("requesterType", 0);

                        if (Constants.LeaveRequesterType.TEACHER_LEAVE == requesterType) {

                            //  int receiverId = intent.getIntExtra("receiverId", 0);


                            Intent leaveIntent = new Intent(NotificationActionService.this, LeaveModuleActivity.class);
                            leaveIntent.putExtra("isApprover", true);
                            startActivity(leaveIntent);

                        }

                        break;

                    default:
                        Intent intent1 = new Intent(NotificationActionService.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                }
            } else {
                Intent intent1 = new Intent(NotificationActionService.this, MainActivity.class);
                startActivity(intent1);
            }
        }
    }
}

package com.jeannypr.scientificstudy.Firebase.Notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.BuildConfig;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.UpdateTokenModel;
import com.jeannypr.scientificstudy.Database.repository.TransportNotificationRepository;
import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.SplashActivity;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.application.SptApp;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    Bitmap bitmap;
    public static MyFirebaseMessagingService instance;
    PendingIntent pendingIntent;
    Intent defaultIntent;
    UserPreference userPref;
    int notificationId;
    NotificationChannel notificationChannel;
    /* private final String CHANNEL_ID = "chat_channel";*/
    final String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("_notification_id");
    final String CHANNEL_NAME = BuildConfig.APPLICATION_ID.concat("_notification_name");
    private NotificationTarget notificationTarget;
    SchoolDetailModel schoolData;
    private String notificationFor;

    public static MyFirebaseMessagingService getInstance(Context context) {
        if (instance != null) {
            return instance;
        } else {
            return instance = new MyFirebaseMessagingService();
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        try {
            storeRegIdInPref(token);

            // Notify UI that registration has completed, so the progress indicator can be hidden.
            Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", token);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        defaultIntent = new Intent(MyFirebaseMessagingService.this, NotificationActionService.class);
        userPref = UserPreference.getInstance(MyFirebaseMessagingService.this);
        schoolData = userPref.getSchoolData();

        if (!userPref.isLoggedIn())
            return;

        notificationId = generateRandom();

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                handleDataMessage(remoteMessage);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        } else if (remoteMessage.getNotification() != null) {
            // Check if message contains a notification payload.
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification("Title", remoteMessage.getNotification().getBody(), defaultIntent);
        }
    }

    private void handleNotification(String title, String message, Intent intent) {

        Log.d("msg", "onMessageReceived: " + message);
        Bitmap largeIc = null;
        int flag = Notification.FLAG_AUTO_CANCEL;
        int notificationImg = 0;
        userPref.SetTotalUnreadMessages(userPref.GetUnreadMessages() + 1);

        switch (intent.getStringExtra("notificationFor")) {
            case Constants.NotificationFor.CHAT:
                notificationImg = R.drawable.messenger;
                break;

            case Constants.NotificationFor.TRANSPORT:
                notificationImg = R.drawable.transport;
                break;

            case Constants.NotificationFor.CLASSWORK:
                notificationImg = R.drawable.classwork;
                break;

            case Constants.NotificationFor.HOMEWORK:
                notificationImg = R.drawable.homework;
                break;

            case Constants.NotificationFor.LEAVE_APPROVER:
                notificationImg = R.drawable.self_attendance;
                break;

            case Constants.NotificationFor.BIOMETERICS:
                notificationImg = R.drawable.attendance;
                break;
        }

        largeIc = BitmapFactory.decodeResource(getResources(), notificationImg);
        long[] vibrate = {500, 500};
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        pendingIntent = PendingIntent.getService(MyFirebaseMessagingService.this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        //Create custom view for notification using remoteView
       /* RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.remote_view_notification);
        remoteViews.setTextViewText(R.id.headline, title);
        remoteViews.setTextViewText(R.id.short_message, message);
        remoteViews.setImageViewResource(R.id.user_image, R.drawable.profile);
        remoteViews.setImageViewResource(R.id.context_icon, R.drawable.messenger);*/

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)

                /* .setCustomContentView(remoteViews)
                 .setSmallIcon(R.mipmap.logo)*/

                .setVibrate(vibrate)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setAutoCancel(false)
                .setSubText(schoolData.SchoolName);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setLargeIcon(largeIc);
            builder.setSmallIcon(R.mipmap.logo);

            // builder.addAction(new NotificationCompat.Action(R.drawable.messenger, Constants.NotificationActionsString.REPLY, pendingIntent));
            //builder.setColor(getResources().getColor(R.color.colorPrimaryDark));

        } else {
            builder.setSmallIcon(R.mipmap.logo);
        }

        Notification notification = builder.build();
        notification.flags = flag;

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                int importance = NotificationManager.IMPORTANCE_NONE;
                NotificationChannel mChannel = manager.getNotificationChannel(CHANNEL_ID);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
                    manager.createNotificationChannel(mChannel);
                }
            }

            try {
//create notification target to pass into glide
               /* if (schoolData != null && schoolData.SchoolLogo != null && !schoolData.SchoolLogo.equals("")) {
                    notificationTarget = new NotificationTarget(getApplicationContext(), R.id.user_image, remoteViews, notification, notificationId);
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Glide
                                    .with(getApplicationContext())
                                    .asBitmap().load(schoolData.SchoolLogo)
                                    .into(notificationTarget);
                        }
                    });
                }*/

                manager.notify(notificationId, notification);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    private void handleDataMessage(RemoteMessage remoteMessage) {
        Log.e(TAG, "push json: " + remoteMessage.toString());

        try {
            String body = "", title = "";
            if (remoteMessage.getNotification() != null) {
                title = remoteMessage.getNotification().getTitle();

                if (remoteMessage.getNotification().getBody() != null) {
                    body = remoteMessage.getNotification().getBody();
                }
            }

            Map<String, String> data = remoteMessage.getData();
            String schoolCode = data.get("schoolCode");
            String notificationFor = data.get("notificationFor");

            if (schoolCode != null && !schoolCode.equals("")) {
                if (!userPref.getSchoolCode().equals(schoolCode)) {
                    return;
                }
            }

            switch (notificationFor) {
                case Constants.NotificationFor.CHAT:

                    int chatRoomId = Integer.parseInt(data.get("chatRoomId"));
                    String chatRoomName = data.get("chatRoomName");
                    Boolean isGroup = Boolean.valueOf(data.get("isGroup"));
                    int classOrOtherUserId = Integer.parseInt(data.get("otherUserId"));
                    boolean isAllTeacherGroup = Boolean.valueOf(data.get("IsAllTeacherGroup"));
                    boolean isClass = Boolean.valueOf(data.get("IsClass"));

                    if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        //In foreground
                        String currentActivityName = "";
                        Activity currentActivity = ((SptApp) MyFirebaseMessagingService.this.getApplicationContext()).getCurrentActivity();
                        if (currentActivity != null) {
                            currentActivityName = currentActivity.getLocalClassName();
                        }

                        int roomId = userPref.GetCurrentChatRoomId();
                        if (currentActivityName.equals("Chat.activity.ChatActivity") && roomId == chatRoomId) {
                            break;
                        }
                    }

                    Intent OpenChatRoomIntent = new Intent(MyFirebaseMessagingService.this,
                            NotificationActionService.class);
                    OpenChatRoomIntent.putExtra("chatRoomId", chatRoomId);
                    OpenChatRoomIntent.putExtra("chatRoomName", chatRoomName);
                    OpenChatRoomIntent.putExtra("isGroup", isGroup);
                    OpenChatRoomIntent.putExtra("classOrOtherUserId", classOrOtherUserId);
                    OpenChatRoomIntent.putExtra("notificationFor", Constants.NotificationFor.CHAT);
                    OpenChatRoomIntent.putExtra("notificationId", notificationId);
                    OpenChatRoomIntent.putExtra("IsAllTeacherGroup", isAllTeacherGroup);
                    OpenChatRoomIntent.putExtra("IsClass", isClass);

                    handleNotification(title, body, OpenChatRoomIntent);
                    break;

                case Constants.NotificationFor.TRANSPORT:
                    Boolean isValidUser = false;

                    String time = data.get("notificationTime");
                    String date = data.get("notificationDate");
                    String receiverIds = data.get("receiverIds");
                    int senderUserId = Integer.parseInt(data.get("senderId"));

                    Date strDate = new SimpleDateFormat("hh:mm a").parse(time);
                    String formattedTime = Utility.GetFormattedTimeHMS(strDate);

                    //Show notification of current date only
                    if (date != null) {
                        Date objDate = Calendar.getInstance().getTime();
                        String currentDate = Utility.GetFormattedDateMDY(objDate,Constants.DATE_FORMAT_MDY);
                        if (!date.equals(currentDate)) {
                            return;
                        }
                    }

                    //Compare user id of login user with key value received from notification
                    List<String> ids = Arrays.asList(receiverIds.split(","));
                    for (String id : ids) {
                        String logggedInUserId = String.valueOf(userPref.getUserData().getUserId());
                        if (id.equals(logggedInUserId)) {
                            isValidUser = true;
                            break;
                        }
                    }

                    if (isValidUser) {
                        Intent transportIntent = new Intent(MyFirebaseMessagingService.this,
                                NotificationActionService.class);
                        transportIntent.putExtra("notificationFor", Constants.NotificationFor.TRANSPORT);
                        handleNotification(title, body, transportIntent);

                        //TODO: no need to save from here. On notification screen, get all notifications from server.
                        //SaveToDb(new TransportNotificationModel(title, body, formattedTime, date, Constants.NotificationFor.TRANSPORT, senderUserId, "," + receiverIds + ",", 0), Constants.NotificationFor.TRANSPORT);
                    }

                    break;

                case Constants.NotificationFor.CLASSWORK:
                case Constants.NotificationFor.HOMEWORK:

                  /*  int senderId = Integer.parseInt(data.get("senderId"));
                    String receiverId = data.get("receiverIds");*/
                    int diaryId = Integer.parseInt(data.get("diaryId"));
                    String subjectName = data.get("subjectName");
                    String className = data.get("className");
                    int diaryType = Integer.parseInt(data.get("diaryType"));

                 /*   String notificationDate = data.get("notificationDate");
                    Date objDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(notificationDate);
                    String formattedtime = Utility.GetFormattedTimeHMS(objDate);
                    String formattedDate = Utility.GetFormattedDateMDY(objDate);*/

                    Intent classworkIntent = new Intent(MyFirebaseMessagingService.this, NotificationActionService.class);
                   /* classworkIntent.putExtra("notificationFor", notificationFor);
                    classworkIntent.putExtra("notificationTime", formattedtime);
                    classworkIntent.putExtra("notificationDate", formattedDate);
                    classworkIntent.putExtra("schoolCode", schoolCode);
                    classworkIntent.putExtra("senderId", senderId);
                    classworkIntent.putExtra("receiverIds", receiverId);*/

                    classworkIntent.putExtra("diaryId", diaryId);
                    classworkIntent.putExtra("subjectName", subjectName);
                    classworkIntent.putExtra("className", className);
                    classworkIntent.putExtra("diaryType", diaryType);
                    handleNotification(title, body, classworkIntent);

                    break;

                case Constants.NotificationFor.LEAVE_APPROVER:

                  /*  String notificationDate = data.get("notificationDate");
                    String notificationTime = data.get("notificationTime");
                    int senderId = Integer.parseInt(data.get("senderId"));
                    int receiverId = Integer.parseInt(data.get("receiverIds"));
                    String leaveFromDate = data.get("leaveFromDate");
                    String leaveToDate = data.get("leaveToDate");
                    int requesterType = Integer.parseInt(data.get("requesterType"));

                    Date leaveTime = new SimpleDateFormat("hh:mm a").parse(notificationTime);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    String requetsTime = dateFormat.format(leaveTime);

                    Intent leaveInetent = new Intent(MyFirebaseMessagingService.this, NotificationActionService.class);
                    leaveInetent.putExtra("notificationFor", notificationFor);
                    *//*leaveInetent.putExtra("fromDate", leaveFromDate);
                    leaveInetent.putExtra("toDate", leaveToDate);*//*
                    leaveInetent.putExtra("receiverId", receiverId);
                    leaveInetent.putExtra("requesterType", requesterType);
                    handleNotification(title, body + "(" + leaveFromDate + " - " + leaveToDate + ")", leaveInetent);*/
                    break;

                case Constants.NotificationFor.BIOMETERICS:
                    //TODO: customize notification.
                    break;

                default:
                    Intent defaultInt = new Intent(MyFirebaseMessagingService.this,
                            NotificationActionService.class);
                    defaultInt.putExtra("notificationFor", "");
                    handleNotification("", body, defaultInt);
                    break;
            }

        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Save all notifications in local db.
     *
     * @param model
     * @param notificationFor
     * @param <T>
     */
    private <T> void SaveToDb(T model, String notificationFor) {
        switch (notificationFor) {
            case Constants.NotificationFor.TRANSPORT:
                new TransportNotificationRepository(getApplication()).insert((TransportNotificationModel) model);
                break;

            default:

                break;
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private int getNotificationIcon() {
        /*boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.icon_silhouette : R.drawable.ic_launcher;*/

        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.logo : R.mipmap.logo;
    }

    public void sendNotification(String messageBody, Bitmap image, boolean TrueOrFalse, Context context) {
        try {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("AnotherActivity", TrueOrFalse);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setLargeIcon(image)/*Notification icon image*/
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(image))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private void sendRegistrationToServer(final String token, String schoolCode) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        UpdateTokenModel model = new UpdateTokenModel();
        model.DeviceToken = token;
        model.Platform = Constants.Platform.ANDROID;
        model.SchoolCode = schoolCode;


        ChatService service = new DataRepo<>(ChatService.class, getInstance(getApplicationContext())).getService();
        service.UpdateTokenOnRefresh(model).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response != null) {
                    Log.i("Update token : ", "Success");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("Update token error: ", t.getMessage());
            }
        });
    }

    private void storeRegIdInPref(String newToken) {
        if (!newToken.equals("")) {
            userPref = UserPreference.getInstance(MyFirebaseMessagingService.this);
            String currentToken = userPref.getDeviceToken();
            userPref.setDeviceToken(currentToken, newToken);
        }

        Log.e("newToken", newToken);
        // sending reg id to your server
        sendRegistrationToServer(newToken, userPref.getSchoolCode());
    }
}
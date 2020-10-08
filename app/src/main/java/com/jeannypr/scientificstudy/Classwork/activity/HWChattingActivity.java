package com.jeannypr.scientificstudy.Classwork.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.camera.CropImageIntentBuilder;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Classwork.adapter.HWInstructionCommentAdapter;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailBean;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailModel;
import com.jeannypr.scientificstudy.Classwork.model.HWCommentModel;
import com.jeannypr.scientificstudy.Classwork.model.HWCommentsBean;
import com.jeannypr.scientificstudy.Classwork.model.HWCommentsModel;
import com.jeannypr.scientificstudy.Login.api.LoginService;
import com.jeannypr.scientificstudy.Login.model.FedratedLoginBean;
import com.jeannypr.scientificstudy.Login.model.FedratedLoginInput;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.RealPathUtil;
import com.jeannypr.scientificstudy.Utilities.SilentLogin;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.ActivityChattingBinding;
import com.jeannypr.scientificstudy.databinding.RowLinkDialogBinding;
import com.varunjohn1990.audio_record_view.AttachmentOption;
import com.varunjohn1990.audio_record_view.AttachmentOptionsListener;
import com.varunjohn1990.audio_record_view.AudioRecordView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HWChattingActivity extends AppCompatActivity implements AudioRecordView.RecordingListener,
        View.OnClickListener, AttachmentOptionsListener, HWInstructionCommentAdapter.MessageInterface {

    Context mContext;
    private UserModel userModel;
    private CwHwService classworkService;
    private ActivityChattingBinding mViewBinding;
    Disposable disposable;
    String studentName;
    private AudioRecordView audioRecordView;
    private RecyclerView recyclerViewMessages;
    private HWInstructionCommentAdapter messageAdapter;

    private long time;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private static final int RECORD_AUDIO = 0;
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};
    private int currentFormat = 0, activityId;

    final int galleryRequestCode = 2;
    final int documentRequestCode = 3;
    String[] sizeArr;
    float sizeInFloat;
    String sizeUnit, currentStatus;
    int studentId;
    private UserPreference userPref;

    private final int REQUEST_CAMERA = 0, REQUEST_CROP_PICTURE = 4;
    private static int CROP_IMAGE_SIZE = 192;
    Bitmap cameraImgBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_chatting);
        mContext = this;

        userPref = UserPreference.getInstance(mContext);
        userModel = userPref.getUserData();
        classworkService = new DataRepo<>(CwHwService.class, mContext).getService();

        setSupportActionBar(mViewBinding.toolbar);
        Utility.SetToolbar(mContext, getString(R.string.homework), studentName);

        getIntents();
        getActivityDetail();
        initView();
        getAllComments();
        mViewBinding.link.setOnClickListener(this);
    }

    private void getIntents() {
        studentName = getIntent().getStringExtra(Constants.STUDENT_NAME);
        activityId = getIntent().getIntExtra(Constants.ACTIVITY_ID, -1);
        studentId = getIntent().getIntExtra(Constants.STUDENT_ID, -1);
        currentStatus = getIntent().getStringExtra(Constants.STATUS);
    }

    @Override
    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getActivityDetail() {
        mViewBinding.pb.setVisibility(View.VISIBLE);

        classworkService.getClassworkDetail(activityId, userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<ActivityDetailBean>() {
            @Override
            public void onResponse(Call<ActivityDetailBean> call, Response<ActivityDetailBean> response) {
                mViewBinding.pb.setVisibility(View.GONE);

                ActivityDetailBean bean = response.body();
                if (bean != null) {
                    if (bean.rcode.equals(Constants.Rcode.OK)) {
                        setHwDetail(bean.data);

                    } else if (bean.rcode == Constants.Rcode.NORECORDS) {
                        showToast(bean.msg);
                    }
                } else
                    showToast("Could not load activity detail. Please try again");
            }

            @Override
            public void onFailure(Call<ActivityDetailBean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                showToast("Could not load activity detail. Please try again");
            }
        });
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);

        if (!Utility.isReadAndWriteStoragePermissionGranted(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }

        if (!Utility.IsCameraPermissionGranted(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    /**
     * initialize whtsapp messenger view
     */
    private void initView() {
        mViewBinding.btnCompleted.setOnClickListener(this);
        mViewBinding.btnRework.setOnClickListener(this);
        setCurrentStatus();

        audioRecordView = new AudioRecordView();
        // this is to make your layout the root of audio record view, root layout supposed to be empty..
        audioRecordView.initView(mViewBinding.layoutMain);
        // this is to provide the container layout to the audio record view..
        View containerView = audioRecordView.setContainerView(R.layout.layout_chatting);
        audioRecordView.setRecordingListener(this);

        recyclerViewMessages = containerView.findViewById(R.id.recyclerViewMessages);
        messageAdapter = new HWInstructionCommentAdapter(this, mContext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layoutManager);

        recyclerViewMessages.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    scrollToBottom();
                }
            }
        });

        recyclerViewMessages.setHasFixedSize(false);

        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());

        setListener();
//        audioRecordView.getMessageView().requestFocus();
        audioRecordView.setAttachmentOptions(AttachmentOption.getDefaultList(), this);
        audioRecordView.removeAttachmentOptionAnimation(false);

        checkPermission();
    }


    private void scrollToBottom() {
        // mMessageRecycler.scrollToPosition(0);
        //mMessageAdapter.getItemCount() - 1
        recyclerViewMessages.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerViewMessages.smoothScrollToPosition(0);
            }
        }, 200);
    }

    private void getAllComments() {
        mViewBinding.commentsPB.setVisibility(View.VISIBLE);

        classworkService.getAllHWComments(activityId, studentId, userModel.getUserId()).enqueue(new Callback<HWCommentsBean>() {
            @Override
            public void onResponse(Call<HWCommentsBean> call, Response<HWCommentsBean> response) {
                HWCommentsBean bean = response.body();

                if (bean != null) {
                    if (bean.rcode.equals(Constants.Rcode.OK)) {
                        messageAdapter.clear();
                        HWCommentsModel comments = bean.getData();

                        for (HWCommentModel item : comments.getComments()) {
                            messageAdapter.add(item);
                        }
                        messageAdapter.notifyDataSetChanged();

                    } else if (bean.rcode == Constants.Rcode.NORECORDS) {
                        //
                    }
                }
                mViewBinding.commentsPB.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<HWCommentsBean> call, Throwable t) {
                mViewBinding.commentsPB.setVisibility(View.GONE);
            }
        });
    }

    private void setHwDetail(ActivityDetailModel data) {
        mViewBinding.submissionDate.setText(data.activityModel.ActivitySubmissionDate);
        mViewBinding.title.setText(data.activityModel.Title);
        mViewBinding.studentName.setText(studentName);
    }

    private void initRecording() {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        outputFile = getFilename();
        myAudioRecorder.setOutputFile(outputFile);
        myAudioRecorder.setAudioSamplingRate(Constants.AUDIO_SAMPLE_RATE);
        myAudioRecorder.setMaxDuration(Constants.AUDIO_MAX_DURATION);

        myAudioRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mediaRecorder, int what, int i1) {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    Log.v("Audio Recording: ", "Maximum Duration Reached");
//                    audioRecordView.stopRecording(AudioRecordView.RecordingBehaviour.RELEASED);
                    audioRecordView.sendMsg();
                    onRecordingCompleted();
                }
            }
        });
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, Constants.Directory.Base + File.separator + AUDIO_RECORDER_FOLDER);

        if (!file.exists())
            file.mkdirs();

        return (file.getAbsolutePath() + Constants.SLASH + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    private void setListener() {
        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }

    /**
     * Create comment input model
     * Call API
     * update UI
     */
    private void sendComment() {
        String msg = audioRecordView.getMessageView().getText().toString().trim();
        audioRecordView.getMessageView().setText("");

        saveComment(new HWCommentModel(0, 0, 0, msg, "", "", Constants.ActivityAttachmentType.TEXT, "",
                "", "", "", "", "", false));
    }

    /**
     * @param input add in adapter to update UI.
     *              Call api to save comment
     */
    private void saveComment(final HWCommentModel input) {
        int commentedBy = 0;
        if (userModel.getRoleTitle().equals(Constants.Role.PARENT)) {
            commentedBy = Constants.HWCommentBy.STUDENT;
        } else
            commentedBy = Constants.HWCommentBy.TEACHER;

        input.setStudentId(studentId);
        input.setTeacherUserId(userModel.getUserId());
        input.setCommentedBy(commentedBy);

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, hh:mm a", Locale.ENGLISH); //Aug 25, 03:28 PM
        String currentDate = df.format(cal.getTime());
        input.setSentOn(currentDate);

        Utility.CloseKeyboard(mContext);
        if (input.getFileType() == Constants.ActivityAttachmentType.FILE) {
            String path = "";
            String ext = "";

            if (input.uri != null) {
                final Uri uri = input.uri;
                path = RealPathUtil.getRealPath(mContext, uri);
                ext = Utility.getMimeType(mContext, uri);
            } else {
                path = input.getFilePath();
                ext = Utility.getMimeType(mContext, input.getFilePath());
            }

            if (path == null || path.equals("")) {
                Toast.makeText(mContext, "Something went wrong. File can not be uploaded.", Toast.LENGTH_SHORT).show();
                return;
            }

            mViewBinding.commentsPB.setVisibility(View.VISIBLE);
            String fileDataType = Utility.getFileDataType(ext);
            File file;
            file = new File(path);

            RequestBody requestBody = RequestBody.create(MediaType.parse(fileDataType), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData(file.getName(), file.getName(), requestBody);
//        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            //TODO what is status here?
            new DataRepo<>(CwHwService.class, mContext)
                    .getService()
                    .saveCommentFile(activityId, studentId, userModel.getUserId(), input.getComment(), currentStatus, input.getFileType(),
                            commentedBy, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Bean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(Bean bean) {
                        }

                        @Override
                        public void onError(Throwable e) {
                            mViewBinding.commentsPB.setVisibility(View.GONE);
                            showToast(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            mViewBinding.commentsPB.setVisibility(View.GONE);
                            Log.i("comment: ", "successfully saved");
//                            messageAdapter.add(input);
                            getAllComments();
                        }
                    });
        } else {

            mViewBinding.commentsPB.setVisibility(View.VISIBLE);
            new DataRepo<>(CwHwService.class, mContext)
                    .getService()
                    .saveComment(activityId, studentId, userModel.getUserId(), input.getFileType(), commentedBy, input.getComment(), currentStatus)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Bean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(Bean bean) {
                        }

                        @Override
                        public void onError(Throwable e) {
                            mViewBinding.commentsPB.setVisibility(View.GONE);
                            showToast(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            mViewBinding.commentsPB.setVisibility(View.GONE);
                            Log.i("comment: ", "successfully saved");
//                            messageAdapter.add(input);
                            getAllComments();
                        }
                    });
        }
    }

    @Override
    public void onRecordingStarted() {
        showToast("started");
        debug("started");

        time = System.currentTimeMillis() / (1000);

        initRecording();
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException ise) {
            // make something ...
        } catch (IOException ioe) {
            // make something
        }
    }

    @Override
    public void onRecordingLocked() {
        showToast("locked");
        debug("locked");
    }

    @Override
    public void onRecordingCompleted() {
        showToast("completed");
        debug("completed");

        stopRecoding();
    }

    private void stopRecoding() {
        try {
            myAudioRecorder.stop();
            myAudioRecorder.reset();
            myAudioRecorder.release();
            myAudioRecorder = null;
        } catch (Exception ex) {
            Log.e("Audio recording: ", "onRecordingCompleted method throwing exception", ex);
        }
        int recordTime = (int) ((System.currentTimeMillis() / (1000)) - time);

        if (recordTime > 1) {//"Aug 25, 03:28 PM"
            String ext = Utility.getMimeType(mContext, outputFile);
            saveComment(new HWCommentModel(0, 0, 0, "", outputFile, "", Constants.ActivityAttachmentType.FILE,
                    String.valueOf(recordTime), "", "", "", ext, "", false));
        }
    }

    @Override
    public void onRecordingCanceled() {
        showToast("canceled");
        debug("canceled");

        /*myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;*/
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void debug(String log) {
        Log.d("Whtsap messenger", log);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRework:
                saveStatus(Constants.HWBulkOperationStatus.REWORK);
                break;

            case R.id.btnCompleted:
                saveStatus(Constants.HWBulkOperationStatus.COMPLETED);
                break;

            case R.id.link: {
                String url =
                        Constants.HTTPS + userPref.getSchoolData().getSubDomain() + Constants.SUBDOMAIN + "/ConceptMap/RenderAssignmentSummary?id=" + activityId + "&studentId=" + studentId;
                getJWTToken(url, false);
            }
            break;
        }
    }

    private void saveStatus(final String status) {
        mViewBinding.pb.setVisibility(View.VISIBLE);
        mViewBinding.btnRework.setVisibility(View.GONE);
        mViewBinding.btnCompleted.setVisibility(View.GONE);

        classworkService.bulkHWOperation(activityId, userModel.getUserId(), String.valueOf(studentId), status, Constants.HWCommentBy.TEACHER)
                .enqueue(new Callback<Bean>() {
                    @Override
                    public void onResponse(Call<Bean> call, Response<Bean> response) {
                        mViewBinding.pb.setVisibility(View.GONE);
                        mViewBinding.btnRework.setVisibility(View.VISIBLE);
                        mViewBinding.btnCompleted.setVisibility(View.VISIBLE);

                        if (response.body() != null) {
                            if (response.body().rcode.equals(Constants.Rcode.OK)) {
                                displayErrorMsg(response.body().msg);
                                currentStatus = status;
                                setCurrentStatus();
                                getAllComments();
                            } else
                                displayErrorMsg(response.body().msg);
                        } else
                            displayErrorMsg(R.string.somethingWrongNoDataMsg);
                    }

                    @Override
                    public void onFailure(Call<Bean> call, Throwable t) {
                        mViewBinding.pb.setVisibility(View.GONE);
                        mViewBinding.btnRework.setVisibility(View.VISIBLE);
                        mViewBinding.btnCompleted.setVisibility(View.VISIBLE);
                        displayErrorMsg(R.string.somethingWrongNoDataMsg);
                    }
                });
    }

    @Override
    public void onClick(AttachmentOption attachmentOption) {
        switch (attachmentOption.getId()) {

            case AttachmentOption.DOCUMENT_ID:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf" + "application/msword" + "application/vnd.openxmlformats-officedocument.wordprocessingml.document" + "text/plain");
                try {
                    startActivityForResult(intent, documentRequestCode);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
                break;

            /*case AttachmentOption.CAMERA_ID:
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(imageIntent, REQUEST_CAMERA);
                break;*/

            case AttachmentOption.GALLERY_ID:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                try {
                    startActivityForResult(galleryIntent, galleryRequestCode);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
                break;

            case AttachmentOption.YOUTUBE_LINK_ID:
                getLinkInput(Constants.ActivityAttachmentType.MEDIA);
                break;

            case AttachmentOption.EXTERNAL_LINK_ID:
                getLinkInput(Constants.ActivityAttachmentType.LINK);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;

        Uri imageUri;
        File croppedImageFile = Utility.CreateFileName(userModel.getUserId(), mContext);
        imageUri = Uri.fromFile(croppedImageFile);

        switch (requestCode) {
            case galleryRequestCode:
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getData();
                    if (uri != null) {
                        try {
                            InflateImageAttachment(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else showToast(getString(R.string.pleaseTryAgain));

                } else if (resultCode == RESULT_CANCELED)
                    showToast(getString(R.string.pleaseTryAgain));
                break;

            case documentRequestCode:
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getData();

                    if (uri != null) {
                        try {
                            InflateDocumentTypeAttachment(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else showToast(getString(R.string.pleaseTryAgain));

                } else if (resultCode == RESULT_CANCELED)
                    showToast(getString(R.string.pleaseTryAgain));
                break;

            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK && imageUri != null) {
                    ShowImageCropView(data, imageUri);
                } else if (resultCode == RESULT_CANCELED)
                    showToast(getString(R.string.pleaseTryAgain));
                break;

            case REQUEST_CROP_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri uri = Utility.GetUriFromIntent(data, mContext);
                    if (uri != null) {
                        try {
                            InflateImageAttachment(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else showToast(getString(R.string.pleaseTryAgain));

                } else if (resultCode == RESULT_CANCELED)
                    showToast(getString(R.string.pleaseTryAgain));

                break;
        }
    }

    private void ShowImageCropView(Intent data, Uri selectedImageUri) {
        try {
            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(CROP_IMAGE_SIZE, CROP_IMAGE_SIZE, selectedImageUri);
            int CROP_IMAGE_HIGHLIGHT_COLOR = 0x6aa746F4;
            cropImage.setOutlineColor(CROP_IMAGE_HIGHLIGHT_COLOR);
            cropImage.setCircleCrop(false);

            Bundle bundle = data.getExtras();

            if (bundle != null) {
                if (bundle.getParcelable("data") != null) {
                    cameraImgBmp = bundle.getParcelable("data");
                    cropImage.setBitmap(cameraImgBmp);
                }
            }

            startActivityForResult(cropImage.getIntent(mContext), REQUEST_CROP_PICTURE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void OpenApplicationForBrowsing(int linkType) {
        switch (linkType) {
            case Constants.ActivityAttachmentType.LINK:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case Constants.ActivityAttachmentType.MEDIA:
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com"));
                browseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browseIntent);
                break;
        }
    }

    private void InflateImageAttachment(Uri uri) throws IOException {

        final String fileName = Utility.GetFileName(uri, mContext);

        sizeArr = Utility.GetFileLength(uri, mContext);
        sizeInFloat = Float.parseFloat(sizeArr[0]);
        sizeUnit = sizeArr[1];

        if (sizeUnit.equals(Constants.MB)) {
            if (sizeInFloat > Constants.IMAGE_MAX_SIZE) {
                Toast.makeText(mContext, R.string.invalidImageSize, Toast.LENGTH_SHORT).show();

        /*if (sizeUnit.equals("MB")) {
            if (sizeInFloat > Constants.ATTACHMENT_SIZE_IN_NEWS_NOTICE_CREATION) {
                Toast.makeText(context, "Size of file can not be greater than 5MB", Toast.LENGTH_SHORT).show();*/
                return;
            }
        }
        String size = sizeInFloat > 0 ? sizeInFloat + sizeUnit : "";

        String ext = Utility.getMimeType(mContext, uri);
       /* final View view = inflater.inflate(R.layout.row_image, mViewBinding.layoutMain, false);

        final RelativeLayout imageRow = view.findViewById(R.id.imageRow);
        final ImageView uploadIc = view.findViewById(R.id.icDownload);
        final TextView txtSize = view.findViewById(R.id.size);
        final ImageView imgView = view.findViewById(R.id.image);
        final LinearLayout uploadIcRow = view.findViewById(R.id.downloadIcRow);
        final ProgressBar pb = view.findViewById(R.id.progressBar);
        final ImageView deleteIc = view.findViewById(R.id.deleteIc);
        deleteIc.setVisibility(View.GONE);
        uploadIc.setVisibility(View.GONE);

        imageRow.setTag(fileName + "img");
        pb.setTag(fileName + "img_pb");
        uploadIcRow.setTag(fileName + "img_uploadic");
        txtSize.setText(size);
        //  uploadIc.setImageDrawable(getResources().getDrawable(android.R.drawable.stat_sys_upload_done));

        uploadIcRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Please click on save button to upload attachments", Toast.LENGTH_SHORT).show();
            }
        });

       *//* deleteIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAttachmentConfirmation(view, fileName);
            }
        });*//*

        mViewBinding.layoutMain.addView(view);

        bmpImage = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
        imgView.setImageBitmap(bmpImage);*/

        /*ActivityItemSaveModel item = new ActivityItemSaveModel();
        item.AttachmentName = fileName;
        item.Title = "";
        item.FileType = Constants.ActivityAttachmentType.FILE;
        item.uri = uri;
        item.Extension = ext;
        item.Path = "";
        item.Id = -1;
        attachments.add(item);*/
        HWCommentModel input = new HWCommentModel(0, 0, 0, "", uri.toString(),
                fileName, Constants.ActivityAttachmentType.FILE, "",
                "", "", "", ext, "", false);
        input.uri = uri;
        saveComment(input);
    }

    public void InflateDocumentTypeAttachment(Uri uri) throws IOException {
        final String fileName = Utility.GetFileName(uri, mContext);

        sizeArr = Utility.GetFileLength(uri, mContext);
        sizeInFloat = Float.parseFloat(sizeArr[0]);
        sizeUnit = sizeArr[1];
        if (sizeUnit.equals("MB")) {
            if (sizeInFloat > Constants.ATTACHMENT_SIZE_IN_NEWS_NOTICE_CREATION) {
                Toast.makeText(mContext, "Size of file can not be greater than 5MB", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String size = sizeInFloat > 0 ? sizeInFloat + sizeUnit : "";
        String ext = Utility.getMimeType(mContext, uri);
/*
        final View view = inflater.inflate(R.layout.row_activity_item_file, mViewBinding.layoutMain, false);

        final ConstraintLayout pdfContainer = view.findViewById(R.id.pdfRow);
        final TextView docFileName = view.findViewById(R.id.fileUrl);
        final ImageView icFile = view.findViewById(R.id.icFile);
        final ImageView icUpload = view.findViewById(R.id.icDownload);
        final ProgressBar pb = view.findViewById(R.id.progressBar);
        final ImageView deleteIc = view.findViewById(R.id.deleteIc);
        deleteIc.setVisibility(View.GONE);

        pdfContainer.setTag(fileName + "doc");
        pb.setTag(fileName + "doc_pb");
        icUpload.setTag(fileName + "doc_uploadic");
        icUpload.setVisibility(View.GONE);

        docFileName.setText(fileName + "(" + size + ")");
       *//* icUpload.setImageDrawable(getResources().getDrawable(android.R.drawable.stat_sys_upload_done));

        icUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please click on save button to upload attachments", Toast.LENGTH_SHORT).show();
            }
        });*//*

         *//*  deleteIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAttachmentConfirmation(view, fileName);
            }
        });*//*

        switch (ext.toLowerCase()) {
            case Constants.FileType.PDF:
                icFile.setImageResource(R.drawable.pdf);
                break;

            case Constants.FileType.DOC:
                icFile.setImageResource(R.drawable.doc);
                break;

            case Constants.FileType.DOCX:
                icFile.setImageResource(R.drawable.docx);
                break;
        }

        mViewBinding.layoutMain.addView(view);*/
       /* ActivityItemSaveModel item = new ActivityItemSaveModel();
        item.AttachmentName = fileName;
        item.Title = "";
        item.FileType = Constants.ActivityAttachmentType.FILE;
        item.uri = uri;
        item.Extension = ext;
        item.Id = -1;
        attachments.add(item);*/

        HWCommentModel input = new HWCommentModel(0, 0, 0, "", uri.toString(), fileName, Constants.ActivityAttachmentType.FILE, "",
                "", "", "", "", "", false);
        input.uri = uri;
        saveComment(input);
    }

    @Override
    public void onClickAudio(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            if (filePath != null && filePath != "") {
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                displayErrorMsg(R.string.playingMediaPlayer);
            } else displayErrorMsg(R.string.fileNotFound);

        } catch (Exception e) {
            // make something
        }
    }

    private void displayErrorMsg(int errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
    }

    private void displayErrorMsg(String errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
    }

    private void getLinkInput(final int linkType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(getString(R.string.enterLink));

        final RowLinkDialogBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.row_link_dialog, mViewBinding.coordinatorLayout, false);
        if (linkType == Constants.ActivityAttachmentType.MEDIA) {
            mBinding.extLinkIc.setImageResource(R.drawable.youtube);
            mBinding.externalLink.setHint(R.string.enterYoutubeLink);
        }

        mBinding.externalLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String link = s.toString();
                Boolean isValidUrl = Utility.isValidUrl(link);

                if (!isValidUrl) {
                    mBinding.externalLink.setError("Invalid url");
                }
            }
        });

        mBinding.browseExtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenApplicationForBrowsing(linkType);
            }
        });

        mBinding.btnLayout.positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = mBinding.externalLink.getText().toString();
                saveComment(new HWCommentModel(0, 0, 0, link, "", "", linkType, "",
                        "", "", "", "", "", false));
                alertDialog.dismiss();
            }
        });

        mBinding.btnLayout.negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(mBinding.getRoot());
        alertDialog.show();
    }

    @Override
    public void setCurrentStatus() {
        mViewBinding.currentStatus.setText(String.format(getString(R.string.status), currentStatus));
       /* if (status.equals("Rework"))
            currentStatus = Constants.HWBulkOperationStatus.REWORK;
        else if (status.equals("Completed"))
            currentStatus = Constants.HWBulkOperationStatus.COMPLETED;
        else currentStatus = "";
        mViewBinding.currentStatus.setText(String.format(getString(R.string.status), status));*/
    }


    /**
     * Call api to get JWT token
     *
     * @param returnUrl redirect to url
     */
    private void getJWTToken(final String returnUrl, final Boolean openLinkInWebView) {
        mViewBinding.commentsPB.setVisibility(View.VISIBLE);
        LoginService service = new DataRepo<>(LoginService.class, mContext, ApiConstants.SILENT_LOGIN_BASE_URL, ApiConstants.AUTHENTICATION_TOKEN).getService();

        service.getJWTToken(new FedratedLoginInput(userModel.getUserId(), userModel.getUserName().trim(), userPref.getSchoolData().getSchoolKey().trim()))
                .enqueue(new Callback<FedratedLoginBean>() {
                    @Override
                    public void onResponse(Call<FedratedLoginBean> call, Response<FedratedLoginBean> response) {

                        FedratedLoginBean bean = response.body();
                        if (bean != null) {
                            if (!bean.getToken().equals(""))
                                redirectToNext(returnUrl, bean.getToken(), openLinkInWebView);
                            else
                                Utility.showAlertDialog(mContext, null, null, getString(R.string.silentLoginErrorMsg));
                        } else
                            Utility.showAlertDialog(mContext, null, null, getString(R.string.somethingWrongMsg));
                        mViewBinding.commentsPB.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<FedratedLoginBean> call, Throwable t) {
                        Utility.showAlertDialog(mContext, null, null, t.getMessage());
                        mViewBinding.commentsPB.setVisibility(View.GONE);
                    }
                });
    }

    public void redirectToNext(String returnUrl, String token, Boolean openLinkInWebView) {
        String url = SilentLogin.HTTPS + userPref.getSchoolData().getSubDomain() + SilentLogin.SCIENTIFICSTUDY_IN + "sso/do?jwt=" + token + "&returnUrl=" + returnUrl;

        if (!openLinkInWebView)
            Utility.openLinkInSystemBrowser(url, R.string.linkNotFoundMsg, mContext);
    }
}

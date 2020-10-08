package com.jeannypr.scientificstudy.Classwork.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Classwork.activity.CreateCwHwActivity;
import com.jeannypr.scientificstudy.Classwork.activity.HWDetailActivity;
import com.jeannypr.scientificstudy.Classwork.adapter.CwHwDetailAdapter;
import com.jeannypr.scientificstudy.Classwork.adapter.HWInstructionCommentAdapter;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailBean;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailModel;
import com.jeannypr.scientificstudy.Classwork.model.ActivityInfoModel;
import com.jeannypr.scientificstudy.Classwork.model.ActivityItemModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;
import com.jeannypr.scientificstudy.databinding.FragmentHwInstructionsBinding;
import com.varunjohn1990.audio_record_view.AttachmentOption;
import com.varunjohn1990.audio_record_view.AttachmentOptionsListener;
import com.varunjohn1990.audio_record_view.AudioRecordView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HWInstructionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HWInstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HWInstructionFragment extends Fragment implements  //AudioRecordView.RecordingListener,
        View.OnClickListener, AttachmentOptionsListener, HWInstructionCommentAdapter.MessageInterface, CwHwDetailAdapter.OnItemClickListner {
    private FragmentHwInstructionsBinding mViewBinding;
    Context mContext;
    private UserPreference userPref;
    private UserModel userModel;
    private HWDetailActivity mListner;
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
    private int currentFormat = 0, activityId, activityTypeId;
    List<ActivityItemModel> activityItemModels;
    private CwHwDetailAdapter adapter;
    //    private RecyclerView activityItems;
    LayoutInflater inflater;
    private CwHwService classworkService;
    final int galleryRequestCode = 2;
    final int documentRequestCode = 3;
    public Bitmap bmpImage;
    String[] sizeArr;
    float sizeInFloat;
    String sizeUnit, activityType;
    String description = "";
    Disposable disposable;
    private final int REQUEST_CAMERA = 0, REQUEST_CROP_PICTURE = 4;
    private static int CROP_IMAGE_SIZE = 192;
    Bitmap cameraImgBmp;

    public HWInstructionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BroadcastMsgFragment.
     */
    public static HWInstructionFragment newInstance() {
        HWInstructionFragment fragment = new HWInstructionFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListner = (HWDetailActivity) context;
            mContext = context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TextClicked");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            activityId = bundle.getInt(Constants.ACTIVITY_ID);
            activityTypeId = bundle.getInt(Constants.ACTIVITY_TYPE_ID);
            activityType = bundle.getString(Constants.ACTIVITY_TYPE);
        }
        userPref = UserPreference.getInstance(mContext);
        userModel = userPref.getUserData();
        classworkService = new DataRepo<>(CwHwService.class, mContext).getService();
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_hw_instructions, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        scrollPage();
        checkPermission();
        initAttachmentsView();
        getActivityDetail();

        if (userModel.getRoleTitle().equals(Constants.Role.PARENT)) {
            mViewBinding.btnCompleted.setOnClickListener(this);
            mViewBinding.btnCompleted.setVisibility(View.VISIBLE);
            mViewBinding.underline3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    private void saveStatus(String status) {
        mViewBinding.pb.setVisibility(View.VISIBLE);
        mViewBinding.btnCompleted.setVisibility(View.GONE);

        classworkService.bulkHWOperation(activityId, userModel.getUserId(), String.valueOf(userPref.getSelectedChild().StudentId), status,
                Constants.HWCommentBy.STUDENT).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                mViewBinding.pb.setVisibility(View.GONE);
                mViewBinding.btnCompleted.setVisibility(View.VISIBLE);

                if (response.body() != null) {
                    if (response.body().rcode.equals(Constants.Rcode.OK))
                        displayErrorMsg(response.body().msg);
                    else
                        displayErrorMsg(response.body().msg);
                } else
                    displayErrorMsg(R.string.somethingWrongNoDataMsg);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                mViewBinding.btnCompleted.setVisibility(View.VISIBLE);
                displayErrorMsg(R.string.somethingWrongNoDataMsg);
            }
        });
    }

   /* private void getAllComments() {
        mViewBinding.underline3.setVisibility(View.VISIBLE);
        mViewBinding.commentsLbl.setVisibility(View.VISIBLE);
        mViewBinding.layoutMain.setVisibility(View.VISIBLE);
        mViewBinding.commentsPB.setVisibility(View.VISIBLE);
//        int studentId = 0;
//        int teacherId = 0;
//        if (userModel.getRoleTitle().equals(Constants.Role.PARENT))
//        studentId = userModel.getUserId();
//        else teacherId = userModel.getUserId();

        classworkService.getAllHWComments(activityId, userPref.getSelectedChild().StudentId, 0).enqueue(new Callback<HWCommentsBean>() {
            @Override
            public void onResponse(Call<HWCommentsBean> call, Response<HWCommentsBean> response) {
                mViewBinding.commentsPB.setVisibility(View.GONE);
                HWCommentsBean bean = response.body();
                messageAdapter.clear();

                if (bean != null) {
                    if (bean.rcode.equals(Constants.Rcode.OK)) {
                        HWCommentsModel comments = bean.getData();

                        for (HWCommentModel item : comments.getComments()) {
                            messageAdapter.add(item);
                        }
                        messageAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFailure(Call<HWCommentsBean> call, Throwable t) {
                mViewBinding.commentsPB.setVisibility(View.GONE);
            }
        });
    }

    */

    /**
     * initialize whtsapp messenger view
     *//*
    private void initChatView() {
        audioRecordView = new AudioRecordView();
        // this is to make your layout the root of audio record view, root layout supposed to be empty..
        audioRecordView.initView(mViewBinding.layoutMain);
        // this is to provide the container layout to the audio record view..
        View containerView = audioRecordView.setContainerView(R.layout.layout_chatting);
        audioRecordView.setRecordingListener(this);

        recyclerViewMessages = containerView.findViewById(R.id.recyclerViewMessages);

        messageAdapter = new HWInstructionCommentAdapter(this, mContext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setHasFixedSize(false);

        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());

        setListener();
//        audioRecordView.getMessageView().requestFocus();
        audioRecordView.setAttachmentOptions(AttachmentOption.getDefaultList(), this);
        audioRecordView.removeAttachmentOptionAnimation(false);

        checkPermission();
    }*/
    private void initAttachmentsView() {
        activityItemModels = new ArrayList<>();
        adapter = new CwHwDetailAdapter(mContext, activityItemModels, Constants.DiaryType.Homework, this);
        mViewBinding.activityItems.setAdapter(adapter);
        mViewBinding.activityItems.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void getActivityDetail() {
        mViewBinding.pb.setVisibility(View.VISIBLE);

        classworkService.getClassworkDetail(activityId, userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<ActivityDetailBean>() {
            @Override
            public void onResponse(Call<ActivityDetailBean> call, Response<ActivityDetailBean> response) {
                mViewBinding.pb.setVisibility(View.GONE);
                activityItemModels.clear();

                ActivityDetailBean classworkDetailBean = response.body();
                if (classworkDetailBean != null) {
                    if (classworkDetailBean.rcode.equals(Constants.Rcode.OK)) {
                        ActivityDetailModel classworkDetailModel = classworkDetailBean.data;
                        setHwDetail(classworkDetailModel.activityModel);

                        for (ActivityItemModel item : classworkDetailModel.activityItemModel) {
                            activityItemModels.add(item);
                        }
                        adapter.notifyDataSetChanged();

                    } else if (classworkDetailBean.rcode == Constants.Rcode.NORECORDS)
                        showToast(classworkDetailBean.msg);
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

    private void setHwDetail(ActivityInfoModel activityModel) {
        mViewBinding.assignedOnDate.setText(activityModel.ActivityDate);
        mViewBinding.submissionDate.setText(activityModel.ActivitySubmissionDate);

        ArrayList clsIDArr = new ArrayList<String>();
        String allIds = "";
        for (ClassModel aClass : activityModel.Classes) {
            clsIDArr.add(aClass.Name);
        }
        if (clsIDArr.size() > 0) allIds = TextUtils.join(",", clsIDArr);
        mViewBinding.grades.setText(allIds);
        mViewBinding.title.setText(activityModel.Title);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);

        if (!Utility.IsCameraPermissionGranted(getActivity()))
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);

        if (!Utility.isReadAndWriteStoragePermissionGranted(getActivity())) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (userModel.getRoleTitle().equals(Constants.Role.PARENT))
            menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.icEdit:
                Intent editCwHwIntent = new Intent(mContext, CreateCwHwActivity.class);
                editCwHwIntent.putExtra("activityId", activityId);
                editCwHwIntent.putExtra("activityType", activityType);
                editCwHwIntent.putExtra("activityTypeId", activityTypeId);
                startActivity(editCwHwIntent);
                break;

            case R.id.icRefresh:
                initView();
                break;

            case R.id.icDelete:
                displayErrorMsg(R.string.comingSoon);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecording() {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        outputFile = getFilename();
        myAudioRecorder.setOutputFile(outputFile);
//        myAudioRecorder.setAudioSamplingRate(16000);
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, Constants.Directory.Base + File.separator + AUDIO_RECORDER_FOLDER);

        if (!file.exists())
            file.mkdirs();

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

   /* private void setListener() {
        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }*/

    /**
     * Create comment input model
     * Call API
     * update UI
     */
  /*  private void sendComment() {
        String msg = audioRecordView.getMessageView().getText().toString().trim();
        audioRecordView.getMessageView().setText("");

        saveComment(new HWCommentModel(0, 0, 0, msg, "", "", Constants.ActivityAttachmentType.TEXT, "",
                "", "", "", "", "", false));
    }*/

    /**
     * @param
     */
 /*   private void saveComment(final HWCommentModel input) {
        int commentedBy = 0;
        int studentId = userPref.getSelectedChild().StudentId;
        if (userModel.getRoleTitle().equals(Constants.Role.PARENT)) {
            commentedBy = Constants.HWCommentBy.STUDENT;
        } else
            commentedBy = Constants.HWCommentBy.TEACHER;

        input.setStudentId(studentId);
        input.setTeacherUserId(0);
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
                    .saveCommentFile(activityId, studentId, 0, input.getComment(), "", input.getFileType(), commentedBy, body)
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
                    .saveComment(activityId, studentId, 0, input.getFileType(), commentedBy, input.getComment(), "")
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
    }*/

   /* @Override
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

        myAudioRecorder.stop();
        myAudioRecorder.reset();
        myAudioRecorder.release();
        myAudioRecorder = null;

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

        *//*myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;*//*
    }*/
    private void showToast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void debug(String log) {
        Log.d("Whtsap messenger", log);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCompleted:
                saveStatus(Constants.HWBulkOperationStatus.SUBMITTED);
                break;
        }

    }

    @Override
    public void onClick(AttachmentOption attachmentOption) {
      /*  switch (attachmentOption.getId()) {

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

            case AttachmentOption.CAMERA_ID:
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(imageIntent, REQUEST_CAMERA);
                break;

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
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (data == null) return;

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
        }*/
    }

    /*private void ShowImageCropView(Intent data, Uri selectedImageUri) {
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
    }*/

   /* private void InflateImageAttachment(Uri uri) throws IOException {

        final String fileName = Utility.GetFileName(uri, mContext);

        sizeArr = Utility.GetFileLength(uri, mContext);
        sizeInFloat = Float.parseFloat(sizeArr[0]);
        sizeUnit = sizeArr[1];

        if (sizeUnit.equals(Constants.MB)) {
            if (sizeInFloat > Constants.IMAGE_MAX_SIZE) {
                Toast.makeText(mContext, R.string.invalidImageSize, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String size = sizeInFloat > 0 ? sizeInFloat + sizeUnit : "";
        String ext = Utility.getMimeType(mContext, uri);

        HWCommentModel input = new HWCommentModel(0, 0, 0, "", uri.toString(), fileName, Constants.ActivityAttachmentType.FILE, "",
                "", "", "", ext, "", false);
        input.uri = uri;
        saveComment(input);
    }

    public void InflateDocumentTypeAttachment(Uri uri) throws IOException {
        final String fileName = Utility.GetFileName(uri, mContext);

        sizeArr = Utility.GetFileLength(uri, mContext);
        sizeInFloat = Float.parseFloat(sizeArr[0]);
        sizeUnit = sizeArr[1];
        if (sizeUnit.equals(Constants.MB)) {
            if (sizeInFloat > Constants.IMAGE_MAX_SIZE) {
                Toast.makeText(mContext, R.string.invalidImageSize, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        HWCommentModel input = new HWCommentModel(0, 0, 0, "", uri.toString(), fileName, Constants.ActivityAttachmentType.FILE, "",
                "", "", "", "", "", false);
        input.uri = uri;
        saveComment(input);
    }*/

    @Override
    public void onClickAudio(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            if (filePath != null && !filePath.equals("")) {
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
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void displayErrorMsg(String errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(String title) {
        if (!title.equals("") && !title.equals("\n")) {
            if (description.equals(""))
                description = title;
            else
                description = description + "\n" + title;
            mViewBinding.desc.setText(description);
        }
    }

  /*  private void getLinkInput(final int linkType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(getString(R.string.enterLink));

        final RowLinkDialogBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.row_link_dialog, mViewBinding.constraintLayout, false);
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
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }

    @Override
    public void setCurrentStatus() {

    }

    private void scrollPage() {
        mViewBinding.scroll.smoothScrollTo(0, 0);
    }
}

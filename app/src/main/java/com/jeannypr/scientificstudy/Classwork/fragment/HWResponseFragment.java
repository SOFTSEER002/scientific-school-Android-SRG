package com.jeannypr.scientificstudy.Classwork.fragment;

import android.Manifest;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.camera.CropImageIntentBuilder;
import com.android.camera.Util;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Classwork.activity.HWDetailActivity;
import com.jeannypr.scientificstudy.Classwork.adapter.CwHwDetailAdapter;
import com.jeannypr.scientificstudy.Classwork.adapter.HWInstructionCommentAdapter;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Classwork.model.ActivityItemModel;
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
import com.jeannypr.scientificstudy.databinding.FragmentHwResponseTabBinding;
import com.jeannypr.scientificstudy.databinding.RowLinkDialogBinding;
import com.varunjohn1990.audio_record_view.AttachmentOption;
import com.varunjohn1990.audio_record_view.AttachmentOptionsListener;
import com.varunjohn1990.audio_record_view.AudioRecordView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HWInstructionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HWInstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HWResponseFragment extends Fragment implements AudioRecordView.RecordingListener,
        View.OnClickListener, AttachmentOptionsListener, HWInstructionCommentAdapter.MessageInterface, CwHwDetailAdapter.OnItemClickListner {
    private FragmentHwResponseTabBinding mViewBinding;
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

    public HWResponseFragment() {
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
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_hw_response_tab, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllComments();
    }

    private void initView() {
        initChatView();
        getAllComments();
        mViewBinding.link.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
        super.onDestroy();
    }

    private void getAllComments() {
        mViewBinding.commentsPB.setVisibility(View.VISIBLE);

        classworkService.getAllHWComments(activityId, userPref.getSelectedChild().StudentId, 0).enqueue(new Callback<HWCommentsBean>() {
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

    /**
     * initialize whtsapp messenger view
     */
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
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

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
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

        String currentStatus = "";
        ArrayList messages = (ArrayList) messageAdapter.getData();
        if (messages.size() > 0)
            currentStatus = ((HWCommentModel) messages.get(0)).getCurrentStatus();

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

            new DataRepo<>(CwHwService.class, mContext)
                    .getService()
                    .saveCommentFile(activityId, studentId, 0, input.getComment(), currentStatus, input.getFileType(), commentedBy, body)
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
                            getAllComments();
                        }
                    });
        } else {
            mViewBinding.commentsPB.setVisibility(View.VISIBLE);

            new DataRepo<>(CwHwService.class, mContext)
                    .getService()
                    .saveComment(activityId, studentId, 0, input.getFileType(), commentedBy, input.getComment(), currentStatus)
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
                            getAllComments();
                        }
                    });
        }
    }

    @Override
    public void onRecordingStarted() {
        showToast("started");
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
    }

    @Override
    public void onRecordingCompleted() {
        showToast("completed");
        stopRecoding();
    }

    private void stopRecoding() {
        try {
            myAudioRecorder.stop();
            myAudioRecorder.reset();
            myAudioRecorder.release();
            myAudioRecorder = null;
        } catch (Exception ex) {

        }

        int recordTime = (int) ((System.currentTimeMillis() / (1000)) - time);

        if (recordTime > 1) {//"Aug 25, 03:28 PM"
//            messageAdapter.add(new HWMessage(recordTime));
            String ext = Utility.getMimeType(mContext, outputFile);
            saveComment(new HWCommentModel(0, 0, 0, "", outputFile, "", Constants.ActivityAttachmentType.FILE,
                    String.valueOf(recordTime), "", "", "", ext, "", false));
        }
    }

    @Override
    public void onRecordingCanceled() {
        showToast("canceled");

        /*myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;*/
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.link:
            {
                String url =
                        Constants.HTTPS + userPref.getSchoolData().getSubDomain() + Constants.SUBDOMAIN + "/ConceptMap/RenderAssignmentSummary?id=" + activityId + "&studentId=" + userPref.getSelectedChild().StudentId;
                getJWTToken(url, false);
            }
                break;
        }
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

          /*  case AttachmentOption.CAMERA_ID:
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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GOOGLE_URL));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case Constants.ActivityAttachmentType.MEDIA:
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_URL));
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
                return;
            }
        }
        String size = sizeInFloat > 0 ? sizeInFloat + sizeUnit : "";
        String ext = Utility.getMimeType(mContext, uri);

        HWCommentModel input = new HWCommentModel(0, 0, 0, "", uri.toString(), fileName, Constants.ActivityAttachmentType.FILE,
                "", "", "", "", ext, "", false);
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
    }

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

    @Override
    public void onItemClick(String title) {
       /* if (description.equals(""))
            description = title;
        else
            description = description + "\n" + title;
        mViewBinding.desc.setText(description);*/
    }

    private void getLinkInput(final int linkType) {
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
    }

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
        //
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

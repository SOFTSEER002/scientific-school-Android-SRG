package com.jeannypr.scientificstudy.Classwork.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailBean;
import com.jeannypr.scientificstudy.Classwork.model.ActivityDetailModel;
import com.jeannypr.scientificstudy.Classwork.model.ActivityInfoModel;
import com.jeannypr.scientificstudy.Classwork.model.ActivityItemModel;
import com.jeannypr.scientificstudy.Classwork.model.ActivityItemSaveModel;
import com.jeannypr.scientificstudy.Classwork.model.CwHwSaveModel;
import com.jeannypr.scientificstudy.Classwork.model.SaveCwHwResponseBean;
import com.jeannypr.scientificstudy.FloatingActionButton.MovableFloatingActionButton;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.RealPathUtil;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

public class CreateCwHwActivity extends AppCompatActivity implements View.OnClickListener {

    MovableFloatingActionButton saveBtn;
    LinearLayout galleryLayout, documentsLayout, audioLayout, youtubeLayout, externalLinkLayout, attachmentsParent, noRecord;
    LinearLayout fabMenuLayout;
    boolean isFABOpen = false;
    Boolean continueToUpload, isAssignedToClass;
    Context context;
    ImageView chooseAudienceBtn, browseExtLink, browseYoutuLink;
    ProgressBar pb;
    CwHwService service;
    private Calendar calendar;
    private SimpleDateFormat df, df2;
    ConstraintLayout content_main2, submissionDateRow, externalLinkRow, youtubeLinkRow;
    TextView txtSubmissionDate, txtSubject, groupLbl, noRecordMsg;
    String submissionDate, topic, desc, activityType, subjectname, externalLink, youtubeLink, schoolCode, sizeUnit;
    String[] sizeArr;
    float sizeInFloat;
    int subjectId, activityTypeId, savedActivityId;
    int totalExtLinks = 0;
    int totalYoutubeLinks = 0;
    final int classRequestCode = 1;
    final int galleryRequestCode = 2;
    final int documentRequestCode = 3;
    int totalTextTypeAttachments = 0;
    List<ClassModel> selectedSections;
    EditText edtExternalLink, txtTopic, txtDesc, edtYoutubeLink;
    public Bitmap bmpImage;
    Disposable disposable;
    List<ActivityItemSaveModel> attachments, items;
    UserPreference userPrefer;
    UserModel userModel;
    CheckBox isAssignedChk;
    LayoutInflater inflater;
    List<ActivityItemModel> activityItemModels;
    ActivityInfoModel activityDetail;
    NestedScrollView scroll_view;
    MenuItem fabMenu;
    FloatingActionButton externalLinkFab, youtubeFab, galleryFab, documentsFab;
    ConstraintLayout parent;
    private BottomSheetDialog bottomSheetDialog;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cw_hw);
        context = this;

        activityType = getIntent().getStringExtra("activityType");
        activityTypeId = getIntent().getIntExtra("activityTypeId", -1);
        savedActivityId = getIntent().getIntExtra("activityId", -1);

        service = new DataRepo<>(CwHwService.class, context).getService();
        inflater = LayoutInflater.from(context);
        userPrefer = UserPreference.getInstance(context);
        userModel = userPrefer.getUserData();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        isAssignedToClass = false;

        continueToUpload = true;
        selectedSections = new ArrayList<>();
        attachments = new ArrayList<>();
        items = new ArrayList<>();
        activityItemModels = new ArrayList<>();
        df = new SimpleDateFormat("dd-MMM-yyyy");
        df2 = new SimpleDateFormat("MM/dd/yyyy");

        content_main2 = findViewById(R.id.content_main2);
        scroll_view = findViewById(R.id.scroll_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        noRecord = findViewById(R.id.noRecord);
        noRecordMsg = findViewById(R.id.noRecordMsg);

        txtTopic = findViewById(R.id.topic);
        txtDesc = findViewById(R.id.desc);

        isAssignedChk = findViewById(R.id.isAssignedChk);
        pb = findViewById(R.id.progressBar);
        groupLbl = findViewById(R.id.groupLbl);

        setSupportActionBar(toolbar);
        String title = savedActivityId != -1 ? "Update " + activityType : "Create " + activityType;
        Utility.SetToolbar(context, title, "");

        if (!Utility.isReadAndWriteStoragePermissionGranted(this)) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }

        attachmentsParent = findViewById(R.id.attachmentsParent);
        saveBtn = findViewById(R.id.saveActivityBtn);
        saveBtn.setOnClickListener(this);

//        fabMenuLayout = findViewById(R.id.fabMenuLayout);
        chooseAudienceBtn = findViewById(R.id.chooseAudienceBtn);
        chooseAudienceBtn.setOnClickListener(this);
        txtSubject = findViewById(R.id.subject);

      /*  externalLinkLayout = findViewById(R.id.externalLinkLayout);
        externalLinkLayout.setOnClickListener(this);
        externalLinkFab = findViewById(R.id.externalLinkFab);
        externalLinkFab.setOnClickListener(this);

        youtubeLayout = findViewById(R.id.youtubeLayout);
        youtubeLayout.setOnClickListener(this);
        youtubeFab = findViewById(R.id.youtubeFab);
        youtubeFab.setOnClickListener(this);

        galleryLayout = findViewById(R.id.galleryLayout);
        galleryLayout.setOnClickListener(this);
        galleryFab = findViewById(R.id.galleryFab);
        galleryFab.setOnClickListener(this);

        documentsLayout = findViewById(R.id.documentsLayout);
        documentsLayout.setOnClickListener(this);
        documentsFab = findViewById(R.id.documentsFab);
        documentsFab.setOnClickListener(this);*/
        submissionDateRow = findViewById(R.id.submissionDateRow);

        if (activityTypeId == Constants.DiaryType.Homework) {
            submissionDate = df.format(calendar.getTime());
            txtSubmissionDate = findViewById(R.id.submissionDate);
            txtSubmissionDate.setText(submissionDate);
            submissionDateRow.setOnClickListener(this);
        } else {
            submissionDateRow.setVisibility(View.INVISIBLE);
            submissionDate = "";
        }

        isAssignedChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAssignedToClass = isChecked;
            }
        });

        if (savedActivityId != -1) {
            GetActivityDetailsToEdit();
        }

        initializeBottomSheet();
    }

    private void initializeBottomSheet() {

//        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.layoutBottomSheet));
        final Context context = this;
        findViewById(R.id.attachmentBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }*/

                View dialogView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
                bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();

                dialogView.findViewById(R.id.imgIc).setOnClickListener((View.OnClickListener) context);
                dialogView.findViewById(R.id.img).setOnClickListener((View.OnClickListener) context);

                dialogView.findViewById(R.id.youtubeIc).setOnClickListener((View.OnClickListener) context);
                dialogView.findViewById(R.id.youtubeLink).setOnClickListener((View.OnClickListener) context);

                dialogView.findViewById(R.id.externalLinkIc).setOnClickListener((View.OnClickListener) context);
                dialogView.findViewById(R.id.externalLink).setOnClickListener((View.OnClickListener) context);

                dialogView.findViewById(R.id.documentIc).setOnClickListener((View.OnClickListener) context);
                dialogView.findViewById(R.id.document).setOnClickListener((View.OnClickListener) context);
            }
        });
    }

    public void GetActivityDetailsToEdit() {

        pb.setVisibility(View.VISIBLE);
        service.getClassworkDetail(savedActivityId, userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<ActivityDetailBean>() {
            @Override
            public void onResponse(Call<ActivityDetailBean> call, Response<ActivityDetailBean> response) {
                pb.setVisibility(View.GONE);
                activityItemModels.clear();

                ActivityDetailBean classworkDetailBean = response.body();
                if (classworkDetailBean != null && classworkDetailBean.rcode.equals(Constants.Rcode.OK)) {
                    ActivityDetailModel classworkDetailModel = classworkDetailBean.data;

                    //Set detail
                    activityDetail = classworkDetailModel.activityModel;
                    SetActivityDetailToEdit(activityDetail);

                    //set attachments
                    try {
                        if (classworkDetailModel.activityItemModel != null && classworkDetailModel.activityItemModel.size() > 0) {
                            StringBuilder sb = new StringBuilder();

                            for (ActivityItemModel item : classworkDetailModel.activityItemModel) {

                                switch (item.FileType) {
                                    case Constants.ActivityAttachmentType.MEDIA:
                                    case Constants.ActivityAttachmentType.LINK:
                                        InflateLinkAsAttachment(item.FileType, item.Title);
                                        break;

                                    case Constants.ActivityAttachmentType.TEXT:
                                        //combine all text type items in single string to show as desc and save it as a single item in the form of desc
                                        AppendTextItemsAsDesc(item, sb);
                                        break;

                                    case Constants.ActivityAttachmentType.FILE:
                                        showImgAndDocAttachments(item);
                                        break;
                                }
                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (classworkDetailBean != null && classworkDetailBean.rcode == Constants.Rcode.NORECORDS) {
                    Toast.makeText(context, "No detail found.Please try again", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context, "Could not load activity detail. Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ActivityDetailBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Could not load activity detail. Please try again", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void SetActivityDetailToEdit(ActivityInfoModel activityDetail) {
        topic = activityDetail.Title != null ? activityDetail.Title : "";
        txtTopic.setText(topic);

        subjectname = activityDetail.SubjectName != null ? activityDetail.SubjectName : "";
        txtSubject.setText("Subject: " + subjectname);
        txtSubject.setTextColor(getResources().getColor(R.color.black));
        subjectId = activityDetail.SubjectId;

        List<ClassModel> classList = activityDetail.Classes;
        selectedSections.clear();
        selectedSections.addAll(classList);
        if (selectedSections != null && selectedSections.size() > 0) {
            InflateSectionsWithUI();
        }

        isAssignedToClass = activityDetail.IsAssignedToClass;
        isAssignedChk.setChecked(isAssignedToClass);

        if (activityTypeId == Constants.DiaryType.Homework) {
            if (activityDetail.ActivitySubmissionDate != null && !activityDetail.ActivitySubmissionDate.equals("")) {

                String[] dateArr = activityDetail.ActivitySubmissionDate.split("-");
                Log.i("Update cw/hw:", dateArr[1]);

                int day = Integer.parseInt(dateArr[0]);
                int year = Integer.parseInt(dateArr[2]);

                int month = 0;
               /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
               //TODO: first get long name(Like : september) of month to use Month.valueOf(), API response returning short name(sep)
                    month = Month.valueOf(dateArr[1]).getValue();
                } else {*/
                SimpleDateFormat inputFormat = new SimpleDateFormat("MMM");
                try {
                    calendar.setTime(inputFormat.parse(dateArr[1]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat outputFormat = new SimpleDateFormat("MM"); // 01-12
                month = Integer.parseInt(outputFormat.format(calendar.getTime()));
                // }
                calendar.set(year, month - 1, day);
                submissionDate = df2.format(calendar.getTime());
                txtSubmissionDate.setText(df.format(calendar.getTime()));
            }
        }
    }

    private void showImgAndDocAttachments(ActivityItemModel item) throws IOException {

        if (item.FileType == Constants.ActivityAttachmentType.FILE) {
            if (item.Path != null) {
                File file = new File(item.Path);
                Uri uri = Uri.fromFile(file);

                switch (item.FileExtension.toLowerCase()) {
                    case Constants.FileType.PDF:
                    case Constants.FileType.DOC:
                    case Constants.FileType.DOCX:
                    case Constants.FileType.TXT:
                        InflateDocumentTypeAttachment(uri, item);
                        break;

                    case Constants.FileType.JPEG:
                    case Constants.FileType.JPG:
                    case Constants.FileType.PNG:
                    case Constants.FileType.BMP:
                    case Constants.FileType.COD:
                    case Constants.FileType.GIF:
                    case Constants.FileType.IEF:
                    case Constants.FileType.JPE:
                    case Constants.FileType.JFIF:
                    case Constants.FileType.SVG:
                    case Constants.FileType.TIF:
                    case Constants.FileType.TIFF:
                    case Constants.FileType.RAS:
                    case Constants.FileType.CMX:
                    case Constants.FileType.ICO:
                    case Constants.FileType.PNM:
                    case Constants.FileType.PBM:
                    case Constants.FileType.PGM:
                    case Constants.FileType.RGB:
                    case Constants.FileType.XBM:
                    case Constants.FileType.XPM:
                    case Constants.FileType.XWD:
                        InflateImageAttachment(uri, item);
                        break;
                }


            }
        }


    }

    private void AppendTextItemsAsDesc(ActivityItemModel item, StringBuilder sb) {

        if (item.Title != null) {
            if (totalTextTypeAttachments == 0) {
                sb.append(item.Title);
                ++totalTextTypeAttachments;
            } else {
                sb.append('\n').append(item.Title);
                ++totalTextTypeAttachments;
            }
        }

        if (totalTextTypeAttachments > 0) {
            txtDesc.setText(sb.toString());
        }
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cw_hw_menu, menu);
        fabMenu = menu.findItem(R.id.fab);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.fab:

                isFABOpen = ShowFabMenu(isFABOpen);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void DisableOtherViews() {
        //saveBtn.setVisibility(View.GONE);
        saveBtn.hide();
        scroll_view.setNestedScrollingEnabled(false);
        scroll_view.setEnabled(false);
        chooseAudienceBtn.setClickable(false);
        submissionDateRow.setEnabled(false);
        txtTopic.setEnabled(false);
        txtDesc.setEnabled(false);
        isAssignedChk.setEnabled(false);

    }

    public void EnableOtherViews() {
        // saveBtn.setVisibility(View.VISIBLE);
        saveBtn.show();
        scroll_view.setNestedScrollingEnabled(true);
        scroll_view.setEnabled(true);
        chooseAudienceBtn.setClickable(true);
        submissionDateRow.setEnabled(true);
        txtTopic.setEnabled(true);
        txtDesc.setEnabled(true);
        isAssignedChk.setEnabled(true);

    }

    /*public Boolean ShowFabMenu(Boolean isFABOpen) {
        if (!isFABOpen) {
            fabMenuLayout.setVisibility(View.VISIBLE);
            fabMenuLayout.bringToFront();
            fabMenu.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
            DisableOtherViews();

            isFABOpen = true;
        } else {

            fabMenuLayout.setVisibility(View.GONE);
            fabMenu.setIcon(R.drawable.attachments3);
            EnableOtherViews();
            isFABOpen = false;
        }
        return isFABOpen;
    }*/

    @Override
    public void onBackPressed() {
      /*  if (isFABOpen) {
            isFABOpen = ShowFabMenu(isFABOpen);
        } else {*/
        super.onBackPressed();
//        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseAudienceBtn:
                Intent chooseAudienceIntent = new Intent(this, SelectClassAndSectionActivity.class);

                try {
                    startActivityForResult(chooseAudienceIntent, classRequestCode);
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
                break;

            case R.id.saveActivityBtn:
                SaveActivity();

                break;

            case R.id.submissionDateRow:
                DatePickerDialog submissionDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
                        submissionDate = df2.format(calendar.getTime());
                        txtSubmissionDate.setText(df.format(calendar.getTime()));

                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                submissionDateDialog.show();
                break;

            case R.id.externalLinkIc:
            case R.id.externalLink:
//                isFABOpen = ShowFabMenu(isFABOpen);
                bottomSheetDialog.dismiss();
                InflateLinkAsAttachment(Constants.ActivityAttachmentType.LINK, "");
                break;

            case R.id.youtubeIc:
            case R.id.youtubeLink:
//                isFABOpen = ShowFabMenu(isFABOpen);
                bottomSheetDialog.dismiss();
                InflateLinkAsAttachment(Constants.ActivityAttachmentType.MEDIA, "");
                break;

           /* case R.id.galleryLayout:
            case R.id.galleryFab:*/
            case R.id.img:
            case R.id.imgIc:
                bottomSheetDialog.dismiss();
                continueToUpload = isAttachmentsExceed();

                if (continueToUpload) {
//                    isFABOpen = ShowFabMenu(isFABOpen);

                   /* Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //  galleryIntent.setType("image/*");
                    String[] mimeTypes = {"image/jpeg", "image/jpg", "image/png", "image/bmp"};
                    galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);*/

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                    String[] mimeTypes = {"image/jpeg", "image/jpg", "image/png", "image/bmp"};
//                    galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                    try {
                        startActivityForResult(galleryIntent, galleryRequestCode);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    break;

                } else {
                    break;
                }

            case R.id.document:
            case R.id.documentIc:
                bottomSheetDialog.dismiss();
                continueToUpload = isAttachmentsExceed();

                if (continueToUpload) {
                    /*'docx' => 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
                    'doc' => 'application/msword'*/

//                    isFABOpen = ShowFabMenu(isFABOpen);

                   /* Intent docIntent = new Intent(Intent.ACTION_PICK);
                    String[] mimeTypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf"};

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        docIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    } else {
                        docIntent.setType("application/*");
                    }

                    try {
                        startActivityForResult(docIntent, documentRequestCode);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }*/

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/pdf" + "application/msword" + "application/vnd.openxmlformats-officedocument.wordprocessingml.document" + "text/plain");
                    try {
                        startActivityForResult(intent, documentRequestCode);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    break;

                } else {
                    break;
                }

        }

    }

    public void SaveActivity() {
        Boolean continueToSave;
        int totalSections;
        int[] classIds;

        if (selectedSections != null) {
            totalSections = selectedSections.size();

            if (totalSections > 0) {
                classIds = new int[totalSections];
                int i = 0;

                for (ClassModel selectedSection : selectedSections) {
                    classIds[i] = selectedSection.Id;
                    ++i;
                }
                continueToSave = true;

            } else {
                Toast.makeText(context, "Please select atleast one class", Toast.LENGTH_SHORT).show();
                continueToSave = false;
                return;
            }
        } else {
            Toast.makeText(context, "Please select atleast one class", Toast.LENGTH_SHORT).show();
            continueToSave = false;
            return;
        }

        if (activityTypeId == Constants.DiaryType.Homework && txtSubmissionDate.getText().equals("")) {
            txtSubmissionDate.setError("Enter Submission date");
        }

        //collect external links and youtube links input to save
        int totalRows = attachmentsParent.getChildCount();
        if (totalRows > 0) {
            int extLinkRowIndex = 1;
            int youtubeRowIndex = 1;

            for (int i = 0; i < totalRows; i++) {

                View v = attachmentsParent.getChildAt(i);
                EditText edtLink = v.findViewById(R.id.externalLink);

                if (edtLink != null) {
                    String link = edtLink.getText().toString();

                    if (!link.equals("")) {
                        String tag = v.getTag().toString();
                        String extTag = "ext" + extLinkRowIndex;
                        String ytTag = "yt" + youtubeRowIndex;

                        if (tag.equals(extTag)) {

                            ActivityItemSaveModel item = new ActivityItemSaveModel();
                            item.Title = link;
                            item.FileType = Constants.ActivityAttachmentType.LINK;
                            item.AttachmentName = "";
                            items.add(item);

                            ++extLinkRowIndex;

                        } else if (tag.equals(ytTag)) {

                            ActivityItemSaveModel itemModel = new ActivityItemSaveModel();
                            itemModel.Title = link;
                            itemModel.FileType = Constants.ActivityAttachmentType.MEDIA;
                            itemModel.AttachmentName = "";
                            items.add(itemModel);

                            ++youtubeRowIndex;
                        }
                    }
                }
            }
        }

        //get desc input by user
        desc = txtDesc.getText().toString();
        if (!desc.equals("")) {
            ActivityItemSaveModel item = new ActivityItemSaveModel();
            item.AttachmentName = "";
            item.Title = desc;
            item.FileType = Constants.ActivityAttachmentType.TEXT;
            items.add(item);
        }

        topic = txtTopic.getText().toString();
        if (topic.equals("")) {
            Toast.makeText(context, "Please enter topic", Toast.LENGTH_SHORT).show();
            continueToSave = false;
            return;
        }

        //if all necessary fields are filled up then continue to save ansd after getting positive res ,upload attachments
        if (continueToSave) {
            saveBtn.hide();
            pb.setVisibility(View.VISIBLE);

            CwHwSaveModel activityModel = new CwHwSaveModel();
            activityModel.Id = savedActivityId == -1 ? 0 : savedActivityId;
            activityModel.AcademicYearId = userModel.getAcademicyearId();
            activityModel.SchoolId = userModel.getSchoolId();
            activityModel.SubmissionDate = submissionDate;
            activityModel.ActivityType = activityType.toLowerCase().equals("classwork") ? Constants.DiaryType.Classwork : Constants.DiaryType.Homework;
            activityModel.ClassIds = new Gson().toJson(classIds);
            activityModel.IsAssignedToClass = isAssignedToClass == null ? false : isAssignedToClass;
            activityModel.Title = topic;
            activityModel.SubjectId = subjectId;
            activityModel.ActivityItemsArr = new Gson().toJson(items);
            activityModel.SubjectName = subjectname;
            activityModel.CreatedBy = userModel.getUserId();

            service.SaveCwHw(activityModel).enqueue(new Callback<SaveCwHwResponseBean>() {
                @Override
                public void onResponse(Call<SaveCwHwResponseBean> call, Response<SaveCwHwResponseBean> response) {
                    if (response.body() != null) {
                        items.clear();
                        SaveCwHwResponseBean bean = response.body();

                        if (bean != null && bean.rcode == Constants.Rcode.OK) {
                            savedActivityId = bean.data.ActivityId;

                            if (attachments.size() > 0) {

                                for (ActivityItemSaveModel attachment : attachments) {
                                    if (attachment.FileType == Constants.ActivityAttachmentType.FILE) {
                                        try {
                                            UploadFile(attachment, savedActivityId);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            Toast.makeText(context, activityType + " created successfully", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context, activityType + " could not be created. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    pb.setVisibility(View.GONE);
                    saveBtn.show();
                }

                @Override
                public void onFailure(Call<SaveCwHwResponseBean> call, Throwable t) {
                    pb.setVisibility(View.GONE);
                    saveBtn.show();
                    items.clear();
                    Toast.makeText(context, activityType + " could not be created. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        switch (requestCode) {
            case classRequestCode:
                if (resultCode == RESULT_OK) {
                    selectedSections.clear();
                    selectedSections = data.getParcelableArrayListExtra("selectedSections");
                    subjectId = data.getIntExtra("subjectId", -1);
                    subjectname = data.getStringExtra("subjectName");

                    if (subjectname != null && !subjectname.equals("")) {
                        txtSubject.setText("Subject: " + subjectname);
                    }
                    if (selectedSections != null && selectedSections.size() > 0) {
                        InflateSectionsWithUI();
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    selectedSections.clear();
                    Toast.makeText(context, "No class is selected. Please select again", Toast.LENGTH_SHORT).show();
                }
                break;

            case galleryRequestCode:
                if (resultCode == RESULT_OK) {

                    final Uri uri = data.getData();
                    if (uri != null) {

                        try {
                            //TODO: check total size of all files before inflating row
                            InflateImageAttachment(uri);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else if (resultCode == RESULT_CANCELED) {

                }
                break;

            case documentRequestCode:

                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getData();

                    if (uri != null) {
                        try {

                            //TODO: check total size of all files before inflating row
                            InflateDocumentTypeAttachment(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else if (resultCode == RESULT_CANCELED) {

                }
                break;
        }
    }

    public void InflateDocumentTypeAttachment(Uri uri) throws IOException {
        final String fileName = Utility.GetFileName(uri, context);

        sizeArr = Utility.GetFileLength(uri, context);
        sizeInFloat = Float.parseFloat(sizeArr[0]);
        sizeUnit = sizeArr[1];
        if (sizeUnit.equals("MB")) {
            if (sizeInFloat > Constants.ATTACHMENT_SIZE_IN_NEWS_NOTICE_CREATION) {
                Toast.makeText(context, "Size of file can not be greater than 5MB", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String size = sizeInFloat > 0 ? sizeInFloat + sizeUnit : "";
        String ext = Utility.getMimeType(context, uri);

        final View view = inflater.inflate(R.layout.row_activity_item_file, attachmentsParent, false);

        final ConstraintLayout pdfContainer = view.findViewById(R.id.pdfRow);
        final TextView docFileName = view.findViewById(R.id.fileUrl);
        final ImageView icFile = view.findViewById(R.id.icFile);
        final ImageView icUpload = view.findViewById(R.id.icDownload);
        final ProgressBar pb = view.findViewById(R.id.progressBar);
        final ImageView deleteIc = view.findViewById(R.id.deleteIc);
        deleteIc.setVisibility(View.VISIBLE);

        pdfContainer.setTag(fileName + "doc");
        pb.setTag(fileName + "doc_pb");
        icUpload.setTag(fileName + "doc_uploadic");
        icUpload.setVisibility(View.GONE);

        docFileName.setText(fileName + "(" + size + ")");
       /* icUpload.setImageDrawable(getResources().getDrawable(android.R.drawable.stat_sys_upload_done));

        icUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please click on save button to upload attachments", Toast.LENGTH_SHORT).show();
            }
        });*/

        deleteIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAttachmentConfirmation(view, fileName);
            }
        });

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

        attachmentsParent.addView(view);
        ActivityItemSaveModel item = new ActivityItemSaveModel();
        item.AttachmentName = fileName;
        item.Title = "";
        item.FileType = Constants.ActivityAttachmentType.FILE;
        item.uri = uri;
        item.Extension = ext;
        item.Id = -1;
        attachments.add(item);
    }

    public void InflateDocumentTypeAttachment(Uri uri, ActivityItemModel item) {

        final String fileName = item.AttachmentName;

        String ext = item.FileExtension;

        final View view = inflater.inflate(R.layout.row_activity_item_file, attachmentsParent, false);

        final ConstraintLayout pdfContainer = view.findViewById(R.id.pdfRow);
        final TextView docFileName = view.findViewById(R.id.fileUrl);
        final ImageView icFile = view.findViewById(R.id.icFile);
        final ImageView icUpload = view.findViewById(R.id.icDownload);
        final ProgressBar pb = view.findViewById(R.id.progressBar);
        final ImageView deleteIc = view.findViewById(R.id.deleteIc);
        deleteIc.setVisibility(View.VISIBLE);

        pdfContainer.setTag(fileName + "doc");
        pb.setTag(fileName + "doc_pb");
        icUpload.setTag(fileName + "doc_uploadic");
        icUpload.setVisibility(View.GONE);

        docFileName.setText(fileName + "(" + item.Size + ")");
      /*  icUpload.setImageDrawable(getResources().getDrawable(android.R.drawable.stat_sys_upload_done));

        icUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please click on save button to upload attachments", Toast.LENGTH_SHORT).show();
            }
        });*/

        deleteIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAttachmentConfirmation(view, fileName);
            }
        });
        //  registerForContextMenu(view);

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

        attachmentsParent.addView(view);
        ActivityItemSaveModel model = new ActivityItemSaveModel();
        model.AttachmentName = fileName;
        model.Title = "";
        model.FileType = Constants.ActivityAttachmentType.FILE;
        model.uri = uri;
        model.Extension = ext;
        model.Path = item.Path;
        model.Id = item.Id;
        attachments.add(model);
    }

    public void InflateImageAttachment(Uri uri) throws IOException {

        final String fileName = Utility.GetFileName(uri, context);

        sizeArr = Utility.GetFileLength(uri, context);
        sizeInFloat = Float.parseFloat(sizeArr[0]);
        sizeUnit = sizeArr[1];

        if (sizeUnit.equals(Constants.MB)) {
            if (sizeInFloat > Constants.IMAGE_MAX_SIZE) {
                Toast.makeText(context, R.string.invalidImageSize, Toast.LENGTH_SHORT).show();

        /*if (sizeUnit.equals("MB")) {
            if (sizeInFloat > Constants.ATTACHMENT_SIZE_IN_NEWS_NOTICE_CREATION) {
                Toast.makeText(context, "Size of file can not be greater than 5MB", Toast.LENGTH_SHORT).show();*/
                return;
            }
        }
        String size = sizeInFloat > 0 ? sizeInFloat + sizeUnit : "";

        String ext = Utility.getMimeType(context, uri);
        final View view = inflater.inflate(R.layout.row_image, attachmentsParent, false);

        final RelativeLayout imageRow = view.findViewById(R.id.imageRow);
        final ImageView uploadIc = view.findViewById(R.id.icDownload);
        final TextView txtSize = view.findViewById(R.id.size);
        final ImageView imgView = view.findViewById(R.id.image);
        final LinearLayout uploadIcRow = view.findViewById(R.id.downloadIcRow);
        final ProgressBar pb = view.findViewById(R.id.progressBar);
        final ImageView deleteIc = view.findViewById(R.id.deleteIc);
        deleteIc.setVisibility(View.VISIBLE);
        uploadIc.setVisibility(View.GONE);

        imageRow.setTag(fileName + "img");
        pb.setTag(fileName + "img_pb");
        uploadIcRow.setTag(fileName + "img_uploadic");
        txtSize.setText(size);
        //  uploadIc.setImageDrawable(getResources().getDrawable(android.R.drawable.stat_sys_upload_done));

        uploadIcRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please click on save button to upload attachments", Toast.LENGTH_SHORT).show();
            }
        });

        deleteIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAttachmentConfirmation(view, fileName);
            }
        });

        attachmentsParent.addView(view);

        bmpImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        imgView.setImageBitmap(bmpImage);

        ActivityItemSaveModel item = new ActivityItemSaveModel();
        item.AttachmentName = fileName;
        item.Title = "";
        item.FileType = Constants.ActivityAttachmentType.FILE;
        item.uri = uri;
        item.Extension = ext;
        item.Path = "";
        item.Id = -1;
        attachments.add(item);
    }

    public void InflateImageAttachment(Uri uri, ActivityItemModel item) throws IOException {

        final String fileName = item.AttachmentName;
        String ext = item.FileExtension;
        final View view = inflater.inflate(R.layout.row_image, attachmentsParent, false);

        final RelativeLayout imageRow = view.findViewById(R.id.imageRow);
        final ImageView uploadIc = view.findViewById(R.id.icDownload);
        final TextView txtSize = view.findViewById(R.id.size);
        final ImageView imgView = view.findViewById(R.id.image);
        final LinearLayout uploadIcRow = view.findViewById(R.id.downloadIcRow);
        final ProgressBar pb = view.findViewById(R.id.progressBar);
        final ImageView deleteIc = view.findViewById(R.id.deleteIc);
        deleteIc.setVisibility(View.VISIBLE);
        uploadIc.setVisibility(View.GONE);

        imageRow.setTag(fileName + "img");
        pb.setTag(fileName + "img_pb");
        uploadIcRow.setTag(fileName + "img_uploadic");
        txtSize.setText(item.Size != null ? item.Size : "");
        // uploadIc.setImageDrawable(getResources().getDrawable(android.R.drawable.stat_sys_upload_done));

        uploadIcRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please click on save button to upload attachments", Toast.LENGTH_SHORT).show();
            }
        });

        deleteIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAttachmentConfirmation(view, fileName);
            }
        });

        attachmentsParent.addView(view);


        if (item.HasThumbnail && !item.ThumbnailPath.equals("")) {
            Glide.with(context).load(item.ThumbnailPath).into(imgView);
        } else {
            Glide.with(context).load(item.Path).into(imgView);
        }

        ActivityItemSaveModel model = new ActivityItemSaveModel();
        model.AttachmentName = fileName;
        model.Title = "";
        model.FileType = Constants.ActivityAttachmentType.FILE;
        model.uri = uri;
        model.Extension = ext;
        model.Path = item.Path;
        model.Id = item.Id;
        attachments.add(model);
    }

    public void InflateLinkAsAttachment(final int linkType, String link) {
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.row_enter_external_link, attachmentsParent, false);

        final ImageView linkIc = view.findViewById(R.id.extLinkIc);
        final EditText edtLink = view.findViewById(R.id.externalLink);
        final ImageView browswIc = view.findViewById(R.id.browseExtLink);
        view.findViewById(R.id.btnLayout).setVisibility(View.GONE);

        int extLinkRowIndex = totalExtLinks + 1;
        int youtubeRowIndex = totalYoutubeLinks + 1;

        switch (linkType) {
            case Constants.ActivityAttachmentType.LINK:
                linkIc.setImageResource(R.drawable.link);
                if (link != null && !link.equals("")) {
                    edtLink.setText(link);
                } else {
                    edtLink.setHint("Enter external link");
                }
                view.setTag("ext" + extLinkRowIndex);
                ++totalExtLinks;
                break;

            case Constants.ActivityAttachmentType.MEDIA:
                linkIc.setImageResource(R.drawable.youtube);

                if (link != null && !link.equals("")) {
                    edtLink.setText(link);
                } else {
                    edtLink.setHint("Enter youtube link");
                }
                view.setTag("yt" + youtubeRowIndex);
                ++totalYoutubeLinks;
                break;

        }

        edtLink.addTextChangedListener(new TextWatcher() {
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
                    edtLink.setError("Invalid url");
                }
            }
        });

        browswIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenApplicationForBrowsing(linkType);
            }
        });
        attachmentsParent.addView(view);

        //  registerForContextMenu(attachmentsParent);
    }

    public void DeleteAttachmentConfirmation(final View view, final String fileName) {
        ConstraintLayout row = (ConstraintLayout) inflater.inflate(R.layout.row_alert_dialog_buttons, parent, false);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();

        dialog.setTitle("Confirmation");
        dialog.setMessage("Do you really want to delete it?");
      /*  builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface bottomSheetDialog, int which) {
                attachmentsParent.removeView(view);

                for (ActivityItemSaveModel attachment : attachments) {
                    if (attachment.AttachmentName.toLowerCase().equals(fileName.toLowerCase())) {
                        attachments.remove(attachment);
                        if (attachment.Id != -1) {
                            DeleteAttachmentFromErp(attachment);

                        }
                        break;
                    }

                }

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface bottomSheetDialog, int which) {
                bottomSheetDialog.cancel();
            }
        });*/

        dialog.setView(row);
        dialog.show();


        Button positiveBtn = row.findViewById(R.id.positiveBtn);
        Button negativeBtn = row.findViewById(R.id.negativeBtn);


        positiveBtn.setText(R.string.dialogPositiveButtonDelete);
        negativeBtn.setText(R.string.dialogNegativeButtonCancel);


        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                attachmentsParent.removeView(view);

                for (ActivityItemSaveModel attachment : attachments) {
                    if (attachment.AttachmentName.toLowerCase().equals(fileName.toLowerCase())) {
                        attachments.remove(attachment);
                        if (attachment.Id != -1) {
                            DeleteAttachmentFromErp(attachment);

                        }
                        break;
                    }

                }
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void DeleteAttachmentFromErp(ActivityItemSaveModel attachment) {
        service.DeleteAttachment(savedActivityId, userModel.getSchoolId(), userModel.getAcademicyearId(), attachment.AttachmentName, activityTypeId).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                Bean res = response.body();
                if (res != null) {
                    if (res.rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to delete.Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                Toast.makeText(context, "Failed to delete.Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void OpenApplicationForBrowsing(int linkType) {
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

    private void UploadFile(final ActivityItemSaveModel attachment, int savedActivityId) throws FileNotFoundException {
        File file = null;
        final Uri uri = attachment.uri;

        if (attachment.Path != null && !attachment.Path.equals("")) {
            file = new File(attachment.Path);
        } else {
            String path = "";
            path = RealPathUtil.getRealPath(context, uri);

            if (path == null || path.equals("")) {
                Toast.makeText(context, "Something went wrong. File can not be uploaded.", Toast.LENGTH_SHORT).show();
                return;
            }
            file = new File(path);
        }

        String ext = Utility.getMimeType(context, uri);
        String fileDataType = Utility.getFileDataType(ext);
        ProgressBar progressBar = null;

        switch (attachment.Extension.toLowerCase()) {
            case Constants.FileType.JPEG:
            case Constants.FileType.JPG:
            case Constants.FileType.BMP:
            case Constants.FileType.PNG:
            case Constants.FileType.COD:
            case Constants.FileType.GIF:
            case Constants.FileType.IEF:
            case Constants.FileType.JPE:
            case Constants.FileType.JFIF:
            case Constants.FileType.SVG:
            case Constants.FileType.TIF:
            case Constants.FileType.TIFF:
            case Constants.FileType.RAS:
            case Constants.FileType.CMX:
            case Constants.FileType.ICO:
            case Constants.FileType.PNM:
            case Constants.FileType.PBM:
            case Constants.FileType.PGM:
            case Constants.FileType.RGB:
            case Constants.FileType.XBM:
            case Constants.FileType.XPM:
            case Constants.FileType.XWD:

                RelativeLayout imgRow = attachmentsParent.findViewWithTag(attachment.AttachmentName + "img");
                progressBar = imgRow.findViewWithTag(attachment.AttachmentName + "img_pb");
                // final LinearLayout uploadIcRow = imgRow.findViewWithTag(attachment.AttachmentName + "img_uploadic");

                progressBar.setVisibility(View.VISIBLE);
                //    uploadIcRow.setVisibility(View.GONE);

               /* new UploadFileUsingMultipart().UploadFile(fileDataType, file, savedActivityId, activityType, activityTypeId, context, new UploadListner() {

                    @Override
                    public void onUploadComplete(Integer res) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, attachment.AttachmentName + " saved successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onUploadStart() {

                    }
                });*/
                break;

            case Constants.FileType.PDF:
            case Constants.FileType.DOC:
            case Constants.FileType.DOCX:
            case Constants.FileType.TXT:
                ConstraintLayout docRow = attachmentsParent.findViewWithTag(attachment.AttachmentName + "doc");
                progressBar = docRow.findViewWithTag(attachment.AttachmentName + "doc_pb");
                // final ImageView uploadIc = docRow.findViewWithTag(attachment.AttachmentName + "doc_uploadic");

                progressBar.setVisibility(View.VISIBLE);
                //   uploadIc.setVisibility(View.GONE);

              /*  new UploadFileUsingMultipart().UploadFile(fileDataType, file, savedActivityId, activityType, activityTypeId, context, new UploadListner() {

                    @Override
                    public void onUploadComplete(Integer res) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, attachment.AttachmentName + " saved successfully", Toast.LENGTH_LONG).show();

                       *//* switch (res) {
                            case "ok":
                                Toast.makeText(context, fileName + "saved successfully", Toast.LENGTH_SHORT).show();
                                break;

                            case "null":
                            case "server_error":
                            case "exception":
                                uploadIc.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Failed to upload.Try again later", Toast.LENGTH_SHORT).show();
                                break;
                        }*//*
                    }

                    @Override
                    public void onUploadStart() {

                    }
                });*/

                break;
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse(fileDataType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(file.getName(), file.getName(), requestBody);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        final ProgressBar finalProgressBar = progressBar;
        new DataRepo<>(CwHwService.class, context)
                .getService()
                .postImage(userModel.getSchoolId(), savedActivityId, activityTypeId, body, name)
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
                    }

                    @Override
                    public void onComplete() {
                        if (finalProgressBar != null) {
                            finalProgressBar.setVisibility(View.GONE);
                        }
                        Toast.makeText(context, attachment.AttachmentName + " saved successfully", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void InflateSectionsWithUI() {
        final ConstraintLayout parent = findViewById(R.id.sectionsParent);
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) parent.getLayoutParams();
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        parent.setLayoutParams(params);

        parent.removeAllViews();
        int index = 0;
        int viewId = 1;

        for (final ClassModel selectedSection : selectedSections) {
            final View view = inflater.inflate(R.layout.selected_class_tab, parent, false);
            final TextView txtSectionName = view.findViewById(R.id.section);
            final ImageView removeSectionBtn = view.findViewById(R.id.removeSectionBtn);
            view.setId(viewId);

            txtSectionName.setText(selectedSection.Name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedSections.remove(selectedSection);
                    ((ViewGroup) view.getParent()).removeView(view);

                    if (selectedSections.size() < 1) {
                        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) parent.getLayoutParams();
                        params2.height = 0;
                        parent.setLayoutParams(params2);

                    } else {
                        //reset all remaining tabs after deleting a tab
                        InflateSectionsWithUI();
                    }

                }
            });

            parent.addView(view);

            // set constraint of section row(Relative layout) according to parent(Constraint layout)
            int expressionVal = index % 2;
            Boolean isEven = expressionVal != 1;
            ConstraintSet set = new ConstraintSet();
            set.clone(parent);

            View leftSibling = parent.getChildAt(index - 1);
            View topSibling = parent.getChildAt(index - 2);

            if (!isEven) {
                //set constraint of right side section view
                set.connect(viewId, ConstraintSet.LEFT, leftSibling.getId(), ConstraintSet.RIGHT, 30);
                if (index == 1 || index == 0) {
                    set.connect(viewId, ConstraintSet.TOP, parent.getId(), ConstraintSet.TOP, 4);
                } else {
                    set.connect(viewId, ConstraintSet.TOP, topSibling.getId(), ConstraintSet.BOTTOM, 4);
                }


            } else {
                //set constraint of left side section view
                set.connect(viewId, ConstraintSet.LEFT, parent.getId(), ConstraintSet.LEFT, 30);
                if (index == 1 || index == 0) {
                    set.connect(viewId, ConstraintSet.TOP, parent.getId(), ConstraintSet.TOP, 4);
                } else {
                    set.connect(viewId, ConstraintSet.TOP, topSibling.getId(), ConstraintSet.BOTTOM, 4);
                }
            }

            set.applyTo(parent);

            ++index;
            ++viewId;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PermissionRequestCode.RREAD_WRITE_STORAGE) {
            String msg = "Without these permissions you won't be able to download the attachments.Click OK to grant permission else Cancel.";
            Utility.onRequestPermissionresult(grantResults, context, Constants.ContextTypeForPermissionResult.CW_HW, msg);
        }

    }

    public boolean isAttachmentsExceed() {
        if (attachments != null) {

            if (attachments.size() == 3) {
                Toast.makeText(context, "Can't upload more than 3 attachments", Toast.LENGTH_SHORT).show();
//                isFABOpen = ShowFabMenu(isFABOpen);
                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
package com.jeannypr.scientificstudy.SptWall.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Classwork.model.ActivityItemSaveModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.SptWall.api.SptWallService;
import com.jeannypr.scientificstudy.SptWall.model.EventInputModel;
import com.jeannypr.scientificstudy.SptWall.model.EventTypeBean;
import com.jeannypr.scientificstudy.SptWall.model.EventTypeModel;
import com.jeannypr.scientificstudy.SptWall.model.NewsNoticeInputModel;
import com.jeannypr.scientificstudy.SptWall.model.SavePostResponseBean;
import com.jeannypr.scientificstudy.Utilities.RealPathUtil;
import com.jeannypr.scientificstudy.Utilities.UploadFileUsingMultipart;
import com.jeannypr.scientificstudy.Utilities.UploadListner;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    UserModel userData;
    Calendar calendar;
    SptWallService newsNoticeService;
    CwHwService cwHwService;
    private SimpleDateFormat df, df2, timeFormat;
    boolean isFABOpen, isPublished, continueToUpload, is24HoursFormat;
    MenuItem fabMenu;
    LinearLayout fabMenuLayout, newsLayout, noticeLayout, galleryLayout, docLayout, attachmentsParent, eventLayout;
    FloatingActionButton saveBtn;
    FrameLayout frame;
    NestedScrollView scroll_view;
    Boolean isLayoutInflated;
    String postType, startDate, topic, desc, selectedAudienceName, endDate, time, selectedELevelName, selectedETypeName;
    int selectedAudienceId, savedPostId, postTypeId, selectedELevelId, selectedETypeId;
    DropDownAdapter adapter;
    SptWallService service;
    final int galleryRequestCode = 2;
    final int documentRequestCode = 3;
    final int audienceRequestCode = 1;
    List<ActivityItemSaveModel> attachments;
    ProgressBar pb;
    ArrayList<DropDownModel> eventTypes, eventLevels;
    DropDownAdapter eTypeAdapter, eLevelAdapter;
    EventInputModel eventModel;
    NewsNoticeInputModel newsNoticeModel;
    String[] mimeTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_create_post);

        isLayoutInflated = false;
        isFABOpen = false;
        continueToUpload = true;
        is24HoursFormat = false;
        savedPostId = 0;
        postType = Constants.PostType.NEWS;
        postTypeId = Constants.TypeOfCreatedPost.NEWS;
        selectedAudienceId = Constants.PostAudienceValues.SCHOOL;
        selectedAudienceName = Constants.PostAudienceNames.SCHOOL;
        selectedELevelId = 0;
        selectedELevelName = "";
        selectedETypeId = 0;
        selectedETypeName = "";
        mimeTypes = new String[]{"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf"};

        userData = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(SptWallService.class, context).getService();
        cwHwService = new DataRepo<>(CwHwService.class, context).getService();
        inflater = LayoutInflater.from(context);

        calendar = Calendar.getInstance(TimeZone.getDefault());
        df = new SimpleDateFormat("dd-MMM-yyyy");
        df2 = new SimpleDateFormat("MM/dd/yyyy");
        timeFormat = new SimpleDateFormat("hh:mm a");

        attachments = new ArrayList<ActivityItemSaveModel>();
        eventModel = new EventInputModel();
        newsNoticeModel = new NewsNoticeInputModel();

        eventLevels = new ArrayList<DropDownModel>();
        eventTypes = new ArrayList<DropDownModel>();
        eTypeAdapter = new DropDownAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_dropdown_item, eventTypes);
        eLevelAdapter = new DropDownAdapter(CreatePostActivity.this, android.R.layout.simple_spinner_dropdown_item, eventLevels);

        DropDownModel levelModel = new DropDownModel();
        levelModel.setText("Event Level");
        levelModel.setId(-1);
        eventLevels.add(levelModel);

        DropDownModel typeModel = new DropDownModel();
        typeModel.setText("Event Type");
        typeModel.setId(-1);
        eventTypes.add(typeModel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle("Create Post");
            getSupportActionBar().setSubtitle("News, Notice, Events...");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        frame = findViewById(R.id.frame);
        fabMenuLayout = findViewById(R.id.fabMenuLayout);
        scroll_view = findViewById(R.id.scroll_view);
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);

        newsLayout = findViewById(R.id.newsLayout);
        newsLayout.setOnClickListener(this);
        noticeLayout = findViewById(R.id.noticeLayout);
        noticeLayout.setOnClickListener(this);
        eventLayout = findViewById(R.id.eventLayout);
        eventLayout.setOnClickListener(this);

        galleryLayout = findViewById(R.id.galleryLayout);
        galleryLayout.setOnClickListener(this);
        docLayout = findViewById(R.id.documentsLayout);
        docLayout.setOnClickListener(this);
        pb = findViewById(R.id.progressBar);

        ShowAttachment(Constants.PostType.NEWS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                SavePost();
                break;

            case R.id.noticeLayout:
                isFABOpen = ShowFabMenu(isFABOpen);
                ShowAttachment(Constants.PostType.NOTICE);
                break;

            case R.id.newsLayout:
                isFABOpen = ShowFabMenu(isFABOpen);
                ShowAttachment(Constants.PostType.NEWS);
                break;

            case R.id.eventLayout:
                isFABOpen = ShowFabMenu(isFABOpen);
                ShowAttachment(Constants.PostType.EVENT);
                break;

            case R.id.galleryLayout:

                continueToUpload = isAttachmentsExceed(Constants.TOTAL_ATTACHMENTS_IN_NEWS_NOTICE_CREATION);

                if (continueToUpload) {
                    isFABOpen = ShowFabMenu(isFABOpen);

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    /*String[] mimeTypes = {"image/jpeg", "image/jpg", "image/png", "image/bmp"};
                    galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);*/

                    galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                    try {
                        startActivityForResult(galleryIntent, galleryRequestCode);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    break;

                } else {
                    break;
                }

            case R.id.documentsLayout:
                if (postType.equals(Constants.PostType.EVENT)) {
                    isFABOpen = ShowFabMenu(isFABOpen);
                    Toast.makeText(context, "Document can not be attached to event", Toast.LENGTH_SHORT).show();
                    break;
                }

                continueToUpload = isAttachmentsExceed(Constants.TOTAL_ATTACHMENTS_IN_NEWS_NOTICE_CREATION);

                if (continueToUpload) {
                    /*'docx' => 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
                    'doc' => 'application/msword'*/

                    isFABOpen = ShowFabMenu(isFABOpen);

                    Intent docIntent = new Intent(Intent.ACTION_GET_CONTENT);

                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        docIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    } else {*/
                    docIntent.setType("application/*");
                    /* }*/

                    try {
                        startActivityForResult(docIntent, documentRequestCode);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    break;

                } else {
                    break;
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

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
                    }

                } else if (resultCode == RESULT_CANCELED) {

                }
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
                    }

                } else if (resultCode == RESULT_CANCELED) {

                }
                break;

            case audienceRequestCode:
                if (resultCode == RESULT_OK) {

                    selectedAudienceId = data.getIntExtra("audienceId", -1);
                    selectedAudienceName = data.getStringExtra("audienceName");

                    if (selectedAudienceName != null && !selectedAudienceName.equals("")) {
                        SetDataInUI();
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    selectedAudienceId = -1;
                    selectedAudienceName = "";
                    Toast.makeText(context, "Audience is not selected.Please select again", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void SetDataInUI() {
        ConstraintLayout row = (ConstraintLayout) scroll_view.getChildAt(0);
        final TextView txtAudience = row.findViewById(R.id.audience);
        txtAudience.setText(selectedAudienceName);
    }

    @Override
    public void onBackPressed() {
        if (isFABOpen) {
            isFABOpen = ShowFabMenu(isFABOpen);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
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
    }

    public Boolean ShowFabMenu(Boolean isFABOpen) {
        if (!isFABOpen) {
            fabMenuLayout.setVisibility(View.VISIBLE);
            frame.bringChildToFront(fabMenuLayout);
            fabMenu.setIcon(R.drawable.ic_cross2);

            saveBtn.hide();

            isFABOpen = true;

        } else {
            fabMenuLayout.setVisibility(View.GONE);
            fabMenu.setIcon(R.drawable.attachments3);

            saveBtn.show();

            isFABOpen = false;
        }
        return isFABOpen;
    }

    private void ShowAttachment(String typeOfSelectedAttachment) {
        if (!isLayoutInflated) {
            isLayoutInflated = true;
            postType = typeOfSelectedAttachment.toLowerCase();
            InflateLayout(typeOfSelectedAttachment);

        } else {
            //confirmation for discard changes?
            //if yes, inflate layout accordingly
            //otherwise no action
            if (!typeOfSelectedAttachment.toLowerCase().equals(postType)) {
                DiscardChangesConfirmation(typeOfSelectedAttachment);
            }
        }

    }

    private void InflateLayout(String layoutTypeToBeInflated) {
        ConstraintLayout view = null;

        if (isLayoutInflated) {
            scroll_view.removeAllViews();
        }
        switch (layoutTypeToBeInflated.toLowerCase()) {
            case Constants.PostType.NEWS:
            case Constants.PostType.NOTICE:

                view = (ConstraintLayout) inflater.inflate(R.layout.row_news_notice_creation, scroll_view, false);
                attachmentsParent = view.findViewById(R.id.attachmentsParent);

                final ConstraintLayout dateRow = view.findViewById(R.id.dateRow);
                final View divider = view.findViewById(R.id.dividerOfAudience);
                //  divider.setVisibility(View.GONE);

                final TextView txtDate = view.findViewById(R.id.newsNoticeDate);
                txtDate.setText(df.format(calendar.getTime()));
                startDate = df2.format(calendar.getTime());

                dateRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog submissionDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                calendar.set(year, month, dayOfMonth);
                                startDate = df2.format(calendar.getTime());
                                txtDate.setText(df.format(calendar.getTime()));

                            }
                        },
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));

                        submissionDateDialog.show();
                    }
                });

                /*final CheckBox isPublishChk = view.findViewById(R.id.isAssignedChk);
                switch (postType) {
                    case Constants.PostType.NEWS:
                        isPublishChk.setText("Publish News");
                        break;
                    case Constants.PostType.NOTICE:
                        isPublishChk.setText("Publish Notice");
                        break;
                }

                isPublishChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isPublished = isChecked;
                    }
                });
*/

                final ConstraintLayout audienceRow = view.findViewById(R.id.audienceRow);
                final TextView audience = view.findViewById(R.id.audience);
                audienceRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Start another activity for result
                        SelectAudienceIntent();
                    }
                });
                break;

            case Constants.PostType.EVENT:
                view = (ConstraintLayout) inflater.inflate(R.layout.row_event_creation, scroll_view, false);
                attachmentsParent = view.findViewById(R.id.attachmentsParent);

                final ConstraintLayout sDateRow = view.findViewById(R.id.sDateRow);
                final ConstraintLayout eDateRow = view.findViewById(R.id.eDateRow);
                final ConstraintLayout timeRow = view.findViewById(R.id.timeRow);
                final Spinner eventTypeList = view.findViewById(R.id.eventTypeList);
                final Spinner eventLevelList = view.findViewById(R.id.eventLevelList);

                eventLevelList.setAdapter(eLevelAdapter);
                eventTypeList.setAdapter(eTypeAdapter);

                eventLevelList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DropDownModel model = eLevelAdapter.getItem(position);
                        if (model != null) {
                            if (model.getId() == -1) {
                                selectedELevelId = 0;
                                selectedELevelName = "";
                            } else {
                                selectedELevelId = model.getId();
                                selectedELevelName = model.getText();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                eventTypeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DropDownModel model = eTypeAdapter.getItem(position);
                        if (model != null) {
                            if (model.getId() == -1) {
                                selectedETypeId = 0;
                                selectedETypeName = "";
                            } else {
                                selectedETypeId = model.getId();
                                selectedETypeName = model.getText();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final TextView txtStartDate = view.findViewById(R.id.startDate);
                txtStartDate.setText(df.format(calendar.getTime()));
                startDate = df2.format(calendar.getTime());

                final TextView txtEndDate = view.findViewById(R.id.endDate);
                txtEndDate.setText(df.format(calendar.getTime()));
                endDate = df2.format(calendar.getTime());

                final TextView txtTime = view.findViewById(R.id.time);
                time = timeFormat.format(calendar.getTime());
                txtTime.setText(time);

                sDateRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog submissionDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                calendar.set(year, month, dayOfMonth);
                                startDate = df2.format(calendar.getTime());
                                txtStartDate.setText(df.format(calendar.getTime()));

                            }
                        },
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));

                        submissionDateDialog.show();
                    }
                });

                eDateRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog submissionDateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                calendar.set(year, month, dayOfMonth);
                                endDate = df2.format(calendar.getTime());
                                txtEndDate.setText(df.format(calendar.getTime()));

                            }
                        },
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));

                        submissionDateDialog.show();
                    }
                });

                timeRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                time = timeFormat.format(calendar.getTime());
                                txtTime.setText(time);

                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HoursFormat);

                        timePickerDialog.show();
                    }
                });

                final ConstraintLayout audienceRow2 = view.findViewById(R.id.audienceRow);
                audienceRow2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Start another activity for result
                        SelectAudienceIntent();
                    }
                });


                GetEventLevelList();
                GetEventTypeList();
                break;
        }
        scroll_view.addView(view);
        SetDataInUI();
    }

    private void GetEventTypeList() {
        service.GetEventTypes().enqueue(new Callback<EventTypeBean>() {
            @Override
            public void onResponse(Call<EventTypeBean> call, Response<EventTypeBean> response) {

                EventTypeBean bean = response.body();
                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {
                        for (EventTypeModel event : bean.data) {
                            DropDownModel model = new DropDownModel();
                            model.setId(event.getId());
                            model.setText(event.getName());
                            eventTypes.add(model);
                        }
                        eTypeAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Something went wrong.Event type list is not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EventTypeBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetEventLevelList() {
        service.GetEventLevels().enqueue(new Callback<EventTypeBean>() {
            @Override
            public void onResponse(Call<EventTypeBean> call, Response<EventTypeBean> response) {

                EventTypeBean bean = response.body();
                if (bean != null) {
                    if (bean.rcode == Constants.Rcode.OK) {
                        for (EventTypeModel event : bean.data) {
                            DropDownModel model = new DropDownModel();
                            model.setId(event.getId());
                            model.setText(event.getName());
                            eventLevels.add(model);
                        }
                        eLevelAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Something went wrong.Event level list is not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EventTypeBean> call, Throwable t) {
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SelectAudienceIntent() {
        Intent i = new Intent(context, SelectAudience.class);
        try {
            startActivityForResult(i, audienceRequestCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void InflateImageAttachment(Uri uri) throws IOException {

        ConstraintLayout rowPost = (ConstraintLayout) scroll_view.getChildAt(0);
        final LinearLayout attachmentsParent = rowPost.findViewById(R.id.attachmentsParent);

        final String fileName = Utility.GetFileName(uri, context);

        String[] sizeArr = Utility.GetFileLength(uri, context);
        float sizeInFloat = Float.parseFloat(sizeArr[0]);
        String sizeUnit = sizeArr[1];
        if (sizeUnit.equals("MB")) {
            if (sizeInFloat > Constants.ATTACHMENT_SIZE_IN_NEWS_NOTICE_CREATION) {
                Toast.makeText(context, "Size of file can not be greater than 5 MB", Toast.LENGTH_SHORT).show();
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

        Bitmap bmpImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        imgView.setImageBitmap(bmpImage);

        ActivityItemSaveModel item = new ActivityItemSaveModel();
        item.AttachmentName = fileName;
        item.Title = "";
        item.FileType = -1;
        item.uri = uri;
        item.Extension = ext;
        item.Path = "";
        item.Id = -1;
        attachments.add(item);
    }

    public void InflateDocumentTypeAttachment(Uri uri) throws IOException {
        ConstraintLayout rowPost = (ConstraintLayout) scroll_view.getChildAt(0);
        final LinearLayout attachmentsParent = rowPost.findViewById(R.id.attachmentsParent);

        final String fileName = Utility.GetFileName(uri, context);

        String[] sizeArr = Utility.GetFileLength(uri, context);
        float sizeInFloat = Float.parseFloat(sizeArr[0]);
        String sizeUnit = sizeArr[1];
        if (sizeUnit.equals("MB")) {
            if (sizeInFloat > 5.0) {
                Toast.makeText(context, "Size of file can not be greater than 5MB", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String size = sizeInFloat > 0 ? sizeInFloat + sizeUnit : "";
        String ext = Utility.getMimeType(context, uri);
        if (!isExtAllowed(ext)) {
            Toast.makeText(context, "Only pdf,doc and docx files are allowed", Toast.LENGTH_SHORT).show();
            return;
        }

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
        item.FileType = -1;
        item.uri = uri;
        item.Extension = ext;
        item.Path = "";
        item.Id = -1;
        attachments.add(item);
    }

    private Boolean isExtAllowed(String ext) {
        return ext.toLowerCase().equals("pdf") ||
                ext.toLowerCase().equals("doc") ||
                ext.toLowerCase().equals("docx");
    }

    public void DiscardChangesConfirmation(final String typeOfSelectedAttachment) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirmation");
        builder.setMessage("Discard changes?");

        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postType = typeOfSelectedAttachment;
                InflateLayout(typeOfSelectedAttachment);

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void SavePost() {
        pb.setVisibility(View.VISIBLE);

        switch (postType) {
            case Constants.PostType.NEWS:
            case Constants.PostType.NOTICE:
                SaveNewsNotice();
                break;

            case Constants.PostType.EVENT:
                SaveEvent();
                break;
        }

    }

    private void SaveNewsNotice() {

        ConstraintLayout view = scroll_view.findViewById(R.id.content_main2);

        final TextView txtTopic = view.findViewById(R.id.topic);
        topic = txtTopic.getText().toString();
        final TextView txtDesc = view.findViewById(R.id.desc);
        desc = txtDesc.getText().toString();

        if (topic == null || topic.equals("")) {
            Toast.makeText(context, "Please enter title", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
            return;
        }

        if (startDate == null || startDate.equals("")) {
            Toast.makeText(context, "Please select date", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
            return;
        }

        newsNoticeModel.Title = topic != null ? topic : "";
        newsNoticeModel.Description = desc != null ? desc : "";
        newsNoticeModel.Id = 0;
        newsNoticeModel.NewsDate = startDate;
        newsNoticeModel.Audience = selectedAudienceId != -1 ? selectedAudienceId : 0;
        newsNoticeModel.CreatedBy = userData.getUserId();
        newsNoticeModel.IsPublished = true;
        newsNoticeModel.SchoolId = userData.getSchoolId();
        newsNoticeModel.AcademicYearId = userData.getAcademicyearId();

        switch (postType) {
            case Constants.PostType.NEWS:
                newsNoticeModel.NewsType = Constants.TypeOfCreatedPost.NEWS;

                break;
            case Constants.PostType.NOTICE:
                newsNoticeModel.NewsType = Constants.TypeOfCreatedPost.NOTICE;

                break;
        }

        CallServiceToSave();
    }

    private void SaveEvent() {
        ConstraintLayout view = scroll_view.findViewById(R.id.content_main2);
        final TextView txtTopic = view.findViewById(R.id.topic);
        topic = txtTopic.getText().toString();
        final TextView txtDesc = view.findViewById(R.id.desc);
        desc = txtDesc.getText().toString();

        if (topic == null || topic.equals("")) {
            Toast.makeText(context, "Please enter title", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
            return;
        }

        if (startDate == null || startDate.equals("")) {
            Toast.makeText(context, "Please select start date", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
            return;
        }

        if (endDate == null || endDate.equals("")) {
            Toast.makeText(context, "Please select end date", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
            return;
        }


        eventModel.Budget = 0;
        eventModel.CreatedBy = userData.getUserId();
        eventModel.Description = desc != null ? desc : "";
        eventModel.EndDate = endDate;
        eventModel.StartDate = startDate;
        eventModel.Title = topic;
        eventModel.EventLevel = selectedELevelId;
        eventModel.EventType = selectedETypeId;
        eventModel.EventVenue = "";
        eventModel.StartTime = time != null ? time : "";
        eventModel.SchoolId = userData.getSchoolId();
        eventModel.AcademicYearId = userData.getAcademicyearId();

        CallServiceToSave();

    }

    private void CallServiceToSave() {
        switch (postType) {
            case Constants.PostType.NEWS:
            case Constants.PostType.NOTICE:
                service.CreateNewsOrNotice(newsNoticeModel).enqueue(new Callback<SavePostResponseBean>() {
                    @Override
                    public void onResponse(Call<SavePostResponseBean> call, Response<SavePostResponseBean> response) {
                        try {
                            SavePostResponseBean bean = response.body();
                            if (bean != null) {
                                if (bean.rcode == Constants.Rcode.OK) {
                                    savedPostId = bean.data.Id;
                                    Toast.makeText(context, postType + " successfully created", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(View.GONE);

                                    //now upload attachment
                                    if (attachments.size() > 0) {

                                        for (ActivityItemSaveModel attachment : attachments) {
                                            try {
                                                UploadFile(attachment, savedPostId);
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Failed to create " + postType, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<SavePostResponseBean> call, Throwable t) {
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case Constants.PostType.EVENT:
                service.CreateEvents(eventModel).enqueue(new Callback<SavePostResponseBean>() {
                    @Override
                    public void onResponse(Call<SavePostResponseBean> call, Response<SavePostResponseBean> response) {
                        try {
                            SavePostResponseBean bean = response.body();
                            if (bean != null) {
                                if (bean.rcode == Constants.Rcode.OK) {
                                    savedPostId = bean.data.Id;
                                    Toast.makeText(context, postType + " successfully created", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(View.GONE);

                                    //now upload attachment
                                    if (attachments.size() > 0) {

                                        for (ActivityItemSaveModel attachment : attachments) {
                                            try {
                                                UploadFile(attachment, savedPostId);
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Failed to create " + postType, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<SavePostResponseBean> call, Throwable t) {
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                    }
                });
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
                Toast.makeText(context, "Something went wrong.File can not be upploaded.", Toast.LENGTH_SHORT).show();
                return;
            }
            file = new File(path);
        }

        String ext = Utility.getMimeType(context, uri);
        String fileDataType = Utility.getFileDataType(ext);
        final ProgressBar progressBar;

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

                progressBar.setVisibility(View.VISIBLE);

                new UploadFileUsingMultipart().UploadFile(fileDataType, file, savedPostId,
                        postType, postTypeId, context, new UploadListner() {

                            @Override
                            public void onUploadComplete(Integer res) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, attachment.AttachmentName + " saved successfully", Toast.LENGTH_LONG).show();
                                //  ClearForm();

                            }

                            @Override
                            public void onUploadStart() {

                            }
                        });
                break;

            case Constants.FileType.PDF:
            case Constants.FileType.DOC:
            case Constants.FileType.DOCX:
                ConstraintLayout docRow = attachmentsParent.findViewWithTag(attachment.AttachmentName + "doc");
                progressBar = docRow.findViewWithTag(attachment.AttachmentName + "doc_pb");

                progressBar.setVisibility(View.VISIBLE);

                new UploadFileUsingMultipart().UploadFile(fileDataType, file, savedPostId, postType, postTypeId, context, new UploadListner() {

                    @Override
                    public void onUploadComplete(Integer res) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, attachment.AttachmentName + " saved successfully", Toast.LENGTH_LONG).show();
                        //  ClearForm();


                        /*if (res != null) {
                            switch (res) {
                                case Constants.UploadAttachmentRcode.OK:
                                    Toast.makeText(context, attachment.AttachmentName + "saved successfully", Toast.LENGTH_SHORT).show();
                                    break;

                                case Constants.UploadAttachmentRcode.NULL:
                                case Constants.UploadAttachmentRcode.SERVER_ERROR:
                                case Constants.UploadAttachmentRcode.EXCEPTION:
                                    // uploadIc.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, "Failed to upload.Try again later", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }*/
                    }

                    @Override
                    public void onUploadStart() {

                    }
                });

                break;
        }
    }

    public void DeleteAttachmentConfirmation(final View view, final String fileName) {
        ConstraintLayout rowPost = (ConstraintLayout) scroll_view.getChildAt(0);
        final LinearLayout attachmentsParent = rowPost.findViewById(R.id.attachmentsParent);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirmation");
        builder.setMessage("Do you really want to delete it?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void DeleteAttachmentFromErp(ActivityItemSaveModel attachment) {
        cwHwService.DeleteAttachment(savedPostId, userData.getSchoolId(), userData.getAcademicyearId(), attachment.AttachmentName, postTypeId).enqueue(new Callback<Bean>() {
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

    private boolean isAttachmentsExceed(int allowedAttachments) {
        if (attachments != null) {

            if (attachments.size() == Constants.TOTAL_ATTACHMENTS_IN_NEWS_NOTICE_CREATION) {
                Toast.makeText(context, "Can't upload more than " + allowedAttachments + " attachment", Toast.LENGTH_SHORT).show();
                isFABOpen = ShowFabMenu(isFABOpen);
                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void ClearForm() {
        InflateLayout(postType);
    }
}

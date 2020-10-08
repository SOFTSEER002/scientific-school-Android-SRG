package com.jeannypr.scientificstudy.Student.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.camera.CropImageIntentBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.ApiConstants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.activity.BaseActivity;
import com.jeannypr.scientificstudy.Chat.activity.ChatActivity;
import com.jeannypr.scientificstudy.Chat.api.ChatService;
import com.jeannypr.scientificstudy.Chat.model.ChatRoomModel;
import com.jeannypr.scientificstudy.Chat.model.ChatStartBean;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.fragment.StudentProfileDetailTabFragment;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

public class StudentProfileActivity extends AppCompatActivity implements
        StudentProfileDetailTabFragment.OnDetailTabFragmentInteractionListener, View.OnClickListener {

    private int studentId, classId, mLongAnimationDuration, parentUserId;
    private Context context;
    private TextView studentNameTxt, classNameTxt, txtStudentName, txtRoll, txtAdmNo, txtBloodGroup, txtEmail, txtDob, txtStudentMobile, txtFatherName, txtFatherMobile, txtMotherName, txtMotherMobile,
            txtReligion, txtCaste, txtAddress, txtClassName;
    ProgressBar progressBar;
    FloatingActionButton cameraFab;
    private final int REQUEST_CAMERA = 0, SELECT_FILE = 1, REQUEST_CROP_PICTURE = 2;
    private static int CROP_IMAGE_SIZE = 192;

    UserModel userModel;
    String imageTakenFrom, imgPath, profilePhotoOf, className, studentName, studentImgPath, admNo;
    Bitmap cameraImgBmp;
    Disposable disposable;
    TabLayout tabLayout;
    ViewPager pager;
    ImageView mStudentImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        context = this;

        userModel = UserPreference.getInstance(context).getUserData();

        className = getIntent().getStringExtra("className");
        studentName = getIntent().getStringExtra("studentName");
        classId = getIntent().getIntExtra("classId", 0);
        studentId = getIntent().getIntExtra("studentId", 0);
        studentImgPath = getIntent().getStringExtra("imgPath");
        admNo = getIntent().getStringExtra("admNo");
        parentUserId = getIntent().getIntExtra("parentUserId", 0);

        pager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);
        mStudentImg = findViewById(R.id.studentImg);
        studentNameTxt = findViewById(R.id.title);
        classNameTxt = findViewById(R.id.subtitle);
        progressBar = findViewById(R.id.progressBar);

        SetUpViewPager();
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();
        SetToolbar(studentName, className);
    }

    private void SetUpViewPager() {
        StudentProfileActivity.LeaveVPagerAdapter adapter = new StudentProfileActivity.LeaveVPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putInt("studentId", studentId);
        bundle.putInt("classId", classId);

        StudentProfileDetailTabFragment detailFrag = new StudentProfileDetailTabFragment();
        detailFrag.setArguments(bundle);
        adapter.addFragment(detailFrag, "Detail");
        pager.setAdapter(adapter);

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_info2_sm);
    }

    private void SetToolbar(final String title, String subtitle) {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.getContext().setTheme(R.style.ExpandedToolbarTheme);
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
        AppBarLayout appBar = findViewById(R.id.appBar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //expanded
                    collapsingToolbar.setTitle("");
                    getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back_arrow_sm));
                    //  toolbar.getContext().setTheme(R.style.ExpandedToolbarTheme);
                } else {

                    //collapsed
                    collapsingToolbar.setTitle(title);
                    getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back_arrow_sm));
                    // toolbar.getContext().setTheme(R.style.CollapsedToolbarTheme);
                }
            }
        });

        studentNameTxt.setText(studentName.substring(0, 1).toUpperCase() + studentName.substring(1).toLowerCase());
        classNameTxt.setText("Student of " + className);

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true);

        try {
            if (!studentImgPath.equals("")) {
                Glide.with(context).load(Constants.IMAGE_BASE_URL + studentImgPath).apply(requestOptions).into(mStudentImg);
            } else {
                //TODO : set default one
            }
        } catch (Exception ex) {
            Log.d("Upload image", ex.getMessage());
        }
    }

    class LeaveVPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fmList = new ArrayList<>();
        private final List<String> fmTitle = new ArrayList<>();
        private Fragment mCurrentFragment;

        public LeaveVPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }

            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return fmList.get(position);
        }

        @Override
        public int getCount() {
            return fmList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fmTitle.get(position);
           /* Drawable image = ContextCompat.getDrawable(StudentProfileActivity.this, R.drawable.ic_action_info);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" " + fmTitle.get(position));
            ImageSpan imageSpan = new ImageSpan(image);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;*/
        }

        public void addFragment(Fragment fm, String title) {
            fmList.add(fm);
            fmTitle.add(title);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

/* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        context = this;

        userModel = UserPreference.getInstance(context).getUserData();
        studentService = new DataRepo<>(StudentService.class, this).getService();
        studentId = getIntent().getIntExtra("studentId", -1);
        isFullScreen = false;

        if (!Utility.IsCameraPermissionGranted(context)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

       if (!Utility.isReadAndWriteStoragePermissionGranted(this)) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
            //return false;
        }

        scrollView = findViewById(R.id.scroll);
        header = findViewById(R.id.header);
        container = findViewById(R.id.container);
        parentsPicSection = findViewById(R.id.parentsPicSection);

        imageViewer = findViewById(R.id.imageViewer);
        imageViewer.setOnClickListener(this);
        mLongAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);

        txtStudentName = findViewById(R.id.studentName);
        txtRoll = findViewById(R.id.rollNo);
        txtAdmNo = findViewById(R.id.admNo);
        txtBloodGroup = findViewById(R.id.bloodGroup);
        txtEmail = findViewById(R.id.email);
        txtDob = findViewById(R.id.dob);
        txtStudentMobile = findViewById(R.id.studentMobile);

        txtFatherName = findViewById(R.id.fatherName);
        txtFatherMobile = findViewById(R.id.fatherMobile);
        txtMotherName = findViewById(R.id.motherName);
        txtMotherMobile = findViewById(R.id.motherMobile);
        txtCaste = findViewById(R.id.caste);
        txtReligion = findViewById(R.id.religion);

        txtAddress = findViewById(R.id.address);
        txtClassName = findViewById(R.id.className);
        progressBar = findViewById(R.id.progressBar);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);

        ImgStudentProfile = findViewById(R.id.studentProfilePic);
        fathersPic = findViewById(R.id.fathersPic);
        mothersPic = findViewById(R.id.mothersPic);

        mobileRow = findViewById(R.id.mobileRow);
        fatherMobileRow = findViewById(R.id.fatherMobileRow);
        motherMobileRow = findViewById(R.id.motherMobileRow);
        emailRow = findViewById(R.id.emailRow);

        mobileRow.setOnClickListener(this);
        fatherMobileRow.setOnClickListener(this);
        motherMobileRow.setOnClickListener(this);
        emailRow.setOnClickListener(this);



        ShowUploadPhotoOption();
        GetStudentProfileInfo();
    }*/

   /* private void ShowUploadPhotoOption() {
        cameraFab = findViewById(R.id.camera);
        cameraFab.setOnClickListener(this);
        if (userModel.getRoleTitle().equals(Constants.Role.ADMIN) || (userModel.getRoleTitle().equals(Constants.Role.TEACHER) && userModel.ClassId != null)) {
            cameraFab.show();
            fathersPic.setOnClickListener(this);
            mothersPic.setOnClickListener(this);
            ImgStudentProfile.setOnClickListener(this);
        } else {
            cameraFab.hide();
        }
    }
*/
 /*   private void GetStudentProfileInfo() {
        if (studentId == -1) {
            Toast.makeText(this, "Please go back and click on student to view its profile.", Toast.LENGTH_SHORT).show();
        } else {

            progressBar.setVisibility(View.VISIBLE);
            studentService.getStudentProfile(studentId, userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<StudentProfileBean>() {
                @Override
                public void onResponse(Call<StudentProfileBean> call, Response<StudentProfileBean> response) {

                    progressBar.setVisibility(View.GONE);
                    StudentProfileBean resp = response.body();

                    if (resp != null) {
                        if (resp.rcode == Constants.Rcode.OK) {
                            if (resp.data != null) {
                                StudentProfileModel data = resp.data;

                                txtStudentName.setText(data.Name);
                                txtRoll.setText(data.RollNo);
                                txtAdmNo.setText(data.AdmissionNo);
                                txtClassName.setText(data.ClassName);
                                txtBloodGroup.setText(data.BloodGroup);
                                txtDob.setText(data.DateOfBirth);
                                txtStudentMobile.setText(data.MobileNumber);
                                txtEmail.setText(data.Email);
                                txtAddress.setText(data.Address);
                                txtCaste.setText(data.Category);
                                txtReligion.setText(data.Religion);
                                txtFatherName.setText(data.FatherName);
                                txtFatherMobile.setText(data.FatherMobile);
                                txtMotherMobile.setText(data.MotherMobile);
                                txtMotherName.setText(data.MotherName);


                                RequestOptions requestOptions = new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                                        .skipMemoryCache(true);

                                try {
                                    if (!data.getImagePath().equals("")) {
                                        Glide.with(context).load(Constants.IMAGE_BASE_URL + data.getImagePath()).apply(requestOptions).into(ImgStudentProfile);
                                    }
                                    if (!data.getFatherImagePath().equals("")) {
                                        Glide.with(context).load(Constants.IMAGE_BASE_URL + data.getFatherImagePath()).apply(requestOptions).into(fathersPic);
                                    }
                                    if (!data.getMotherImagePath().equals("")) {
                                        Glide.with(context).load(Constants.IMAGE_BASE_URL + data.getMotherImagePath()).apply(requestOptions).into(mothersPic);
                                    }
                                } catch (Exception ex) {
                                    Log.d("Upload image", ex.getMessage());
                                }
                            }

                        } else if (resp.rcode == Constants.Rcode.NORECORDS)

                        {
                            Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();

                        } else

                        {
                            Toast.makeText(context, "Could not load student profile. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<StudentProfileBean> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("studentList", "server call error");
                    Toast.makeText(context, "Could not load student profile. Please try again.", Toast.LENGTH_LONG).show();
                }
            });
        }

    }*/

    private void UploadPhoto() {

        if (!Utility.IsCameraPermissionGranted(context)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (!Utility.isReadAndWriteStoragePermissionGranted(context)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }
        if (Utility.IsCameraPermissionGranted(context) && Utility.isReadAndWriteStoragePermissionGranted(this)) {
//            progressBar.setVisibility(View.VISIBLE);
            ShowDialogForProfileImageEdit();
        }
    }

    @Override
    public void onClick(View v) {
    }

    private void ShowDialogForProfileImageEdit() {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_from_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.select_option));

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //Open Camera
                if (options[item].equals(getString(R.string.take_photo))) {
                    imageTakenFrom = Constants.ImageTakenFrom.CAMERA;

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }

                //Open Gallery
                else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    imageTakenFrom = Constants.ImageTakenFrom.GALLERY;

                    Intent intent;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    } else {
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                    }

                    intent.setType("image/*");
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, SELECT_FILE);
                }
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;

        File croppedImageFile = Utility.CreateFileName(studentId, context);
        imageUri = Uri.fromFile(croppedImageFile);

        if (imageUri != null && resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_CROP_PICTURE:

                    try {
                        switch (imageTakenFrom) {
                            case Constants.ImageTakenFrom.CAMERA:
                                Uri uri = Utility.GetUriFromIntent(data, context);
                                IsUriExists(uri);
                                break;

                            case Constants.ImageTakenFrom.GALLERY:
                                IsUriExists(imageUri);
                                break;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                    break;

                case REQUEST_CAMERA:
                    ShowImageCropView(data, imageUri);
                    break;

                case SELECT_FILE:
                    //  if (GetUriReadWritePermission(data, imageUri, context)) {
                    //  if (CheckImageResolution(imageUri)) {
                    ShowImageCropView(data, imageUri);
                    //   }
                    // }
                    break;
            }
        }
    }


//    implementation 'me.villani.lorenzo.android:android-cropimage:1.1.+'
    private void ShowImageCropView(Intent data, Uri selectedImageUri) {
        try {
            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(CROP_IMAGE_SIZE, CROP_IMAGE_SIZE, selectedImageUri);
            int CROP_IMAGE_HIGHLIGHT_COLOR = 0x6aa746F4;
            cropImage.setOutlineColor(CROP_IMAGE_HIGHLIGHT_COLOR);
            cropImage.setCircleCrop(false);

            if (imageTakenFrom.equals(Constants.ImageTakenFrom.CAMERA)) {
                Bundle bundle = data.getExtras();

                if (bundle != null) {
                    if (bundle.getParcelable("data") != null) {
                        cameraImgBmp = bundle.getParcelable("data");
                        cropImage.setBitmap(cameraImgBmp);
                    }
                }
            } else {
                cropImage.setSourceImage(data.getData());
            }

            startActivityForResult(cropImage.getIntent(this), REQUEST_CROP_PICTURE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void UploadCroppedPicture(final Uri croppedImageUri) {

        final String mode = Constants.UploadImageContext.STUDENT;
        String ext = Utility.getMimeType(context, croppedImageUri);
        String fileDataType = Utility.getFileDataType(ext);
        File file = Utility.GetFileFromPath(croppedImageUri, context);

        if (file == null) {
            Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
            return;
        }

        //  if (profilePhotoOf.equals(Constants.ProfilePhoto.FATHER)) {
        RequestBody reqFile = RequestBody.create(MediaType.parse(fileDataType), file);
        String fname = file.getName();
        MultipartBody.Part body = MultipartBody.Part.createFormData(fname, file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        new DataRepo<>(StudentService.class, context)
                .getService()
                .UploadStudentphoto(userModel.getSchoolId(), studentId, profilePhotoOf, body, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("On subscribe", "");
                        disposable = d;
                    }

                    @Override
                    public void onNext(Bean bean) {
                        Log.i("On next", bean.msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("On error", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("On complete", "");
                        Toast.makeText(context, "Saved successfully", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                        if (imageTakenFrom.equals(Constants.ImageTakenFrom.CAMERA)) {
                            switch (profilePhotoOf) {
                                case Constants.ProfilePhoto.FATHER:
                                    UpdateParentSectionInDetailFrag(null, Constants.ImageTakenFrom.CAMERA, Constants.ProfilePhoto.FATHER, cameraImgBmp);
                                    break;

                                case Constants.ProfilePhoto.MOTHER:
                                    UpdateParentSectionInDetailFrag(null, Constants.ImageTakenFrom.CAMERA, Constants.ProfilePhoto.MOTHER, cameraImgBmp);
                                    break;

                                case Constants.ProfilePhoto.STUDENT:
                                    mStudentImg.setImageBitmap(cameraImgBmp);
                                    break;
                            }

                        } else {
                            switch (profilePhotoOf) {
                                case Constants.ProfilePhoto.FATHER:
                                    UpdateParentSectionInDetailFrag(croppedImageUri, Constants.ImageTakenFrom.GALLERY, Constants.ProfilePhoto.FATHER, null);
                                    break;

                                case Constants.ProfilePhoto.MOTHER:
                                    UpdateParentSectionInDetailFrag(croppedImageUri, Constants.ImageTakenFrom.GALLERY, Constants.ProfilePhoto.MOTHER, null);
                                    break;

                                case Constants.ProfilePhoto.STUDENT:
                                    mStudentImg.setImageURI(croppedImageUri);
                                    break;
                            }
                        }
                    }
                });
/*    } else {

        new UploadFileUsingMultipart().UploadFile(fileDataType, file, studentId, mode, userModel.getSchoolId(), context, new UploadListner() {
            @Override
            public void onUploadComplete(Integer res) {

                Toast.makeText(context, "Saved successfully", Toast.LENGTH_LONG).show();
                if (imageTakenFrom.equals(Constants.ImageTakenFrom.CAMERA)) {
                    ImgStudentProfile.setImageBitmap(cameraImgBmp);
                } else {
                    ImgStudentProfile.setImageURI(croppedImageUri);
                }
            }

            @Override
            public void onUploadStart() {
            }
        });
    }*/
    }

    private void UpdateParentSectionInDetailFrag(Uri imgUri, String imageTakenFrom, String profilePhotoOf, Bitmap cameraImgBmp) {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + pager.getCurrentItem());
        if (page != null) {
            switch (pager.getCurrentItem()) {
                case 0:
                    StudentProfileDetailTabFragment fatherImgFrag = (StudentProfileDetailTabFragment) page;
                    if (fatherImgFrag != null) {
                        fatherImgFrag.UpdateParentSection(imgUri, imageTakenFrom, profilePhotoOf, cameraImgBmp);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_profile, menu);

        if (userModel.getRoleTitle().equals(Constants.Role.ADMIN)) {
            menu.findItem(R.id.editDetails).setVisible(true);
            menu.findItem(R.id.editDetailsIc).setVisible(true);
        } else {
            menu.findItem(R.id.editDetailsIc).setVisible(false);
            menu.findItem(R.id.editDetails).setVisible(false);
        }

        if (userModel.getRoleTitle().equals(Constants.Role.ADMIN) || (userModel.getRoleTitle().equals(Constants.Role.TEACHER) && userModel.getClassId() != null))
            menu.findItem(R.id.uploadPhoto).setVisible(true);
        else
            menu.findItem(R.id.uploadPhoto).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.uploadPhoto:
                profilePhotoOf = Constants.ProfilePhoto.STUDENT;
                UploadPhoto();
                return true;

            case R.id.editDetails:
            case R.id.editDetailsIc:
                redirectToEditProfile();
                return true;

            default:
                return false;
        }
    }

    private void redirectToEditProfile() {
        Intent i = new Intent(this, AddEditStudentActivity.class);
        i.putExtra(Constants.STUDENT_ID, studentId);
        i.putExtra(Constants.CLASS_ID, classId);
        startActivity(i);
    }

    private void IsUriExists(Uri croppedImageUri) {
        if (croppedImageUri != null) {
            UploadCroppedPicture(croppedImageUri);

        } else {
            switch (imageTakenFrom) {
                case Constants.ImageTakenFrom.CAMERA:
                    Toast.makeText(context, "Image not captured.Please try again...", Toast.LENGTH_SHORT).show();
                    break;

                case Constants.ImageTakenFrom.GALLERY:
                    Toast.makeText(context, "Image not selected.Please try again...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public boolean GetUriReadWritePermission(Intent data, Uri uri, Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            try {
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                context.getContentResolver().takePersistableUriPermission(uri, takeFlags);
                return true;
            } catch (SecurityException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean CheckImageResolution(Uri imageUri) {
        int[] resolutionArr;
        try {
            resolutionArr = Utility.GetResolutionOfImage(context, imageUri);
            if (resolutionArr[0] > CROP_IMAGE_SIZE && resolutionArr[1] > CROP_IMAGE_SIZE) {
                return true;

            } else {
                Toast.makeText(context, "This photo is too small.Please select a photo with height and weight of atleast 192 pixels.", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void uploadFathersPhoto() {
        profilePhotoOf = Constants.ProfilePhoto.FATHER;
        UploadPhoto();
    }

    @Override
    public void uploadMothersPhoto() {
        profilePhotoOf = Constants.ProfilePhoto.MOTHER;
        UploadPhoto();
    }

    @Override
    public void onClickChat() {
        String chatRoomOwnerName = userModel.getFirstName() + " " + userModel.getLastName();
        int teacherUserId = userModel.getUserId();
        String chatRoomName = studentName + "-" + admNo;

        final ChatRoomModel chatRoom = new ChatRoomModel();
        chatRoom.Type = Constants.ChatRoomType.INDIVIDUAL;
        chatRoom.RoomName = chatRoomName;
        chatRoom.CreatedBy = teacherUserId;
        chatRoom.IsClass = false;
        chatRoom.IsAllTeacherGroup = false;
        chatRoom.setLastUserName("");
        chatRoom.setLastMessage("");
        chatRoom.OwnerName = chatRoomOwnerName;
        chatRoom.UserId = parentUserId;

        enableChat(chatRoom, UserPreference.getInstance(this).getSchoolCode(), context);
    }

    private void enableChat(ChatRoomModel chatRoom, String schoolCode, Context mContext) {
        startIndividualChatRoom(chatRoom, schoolCode, mContext);
    }

    private void startIndividualChatRoom(final ChatRoomModel chatRoom, String schoolCode, final Context mContext) {
        ChatService mChatService = new DataRepo<>(ChatService.class, mContext, ApiConstants.CHAT_BASE_URL).getService();
        mChatService.StartIndividualChatRoom(chatRoom, schoolCode).enqueue(new Callback<ChatStartBean>() {
            @Override
            public void onResponse(Call<ChatStartBean> call, Response<ChatStartBean> response) {
                if (call.isExecuted()) {
                    ChatStartBean data = response.body();
                    int userId = UserPreference.getInstance(mContext).getUserData().getUserId();

                    if (data != null) {
                        if (data.Id != 0) {

                            Intent intent = new Intent(mContext, ChatActivity.class);
                            intent.putExtra("chatRoomId", data.Id);
                            intent.putExtra("chatRoomName", chatRoom.RoomName);

                            if (chatRoom.Type != Constants.ChatRoomType.GROUP) {
                                int otherUserId = 0;
                                if (chatRoom.CreatedBy == userId) otherUserId = chatRoom.UserId;
                                else otherUserId = chatRoom.CreatedBy;

                                intent.putExtra("otherUserId", otherUserId);
                            }
                            startActivity(intent);

                        } else
                            Toast.makeText(mContext, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ChatStartBean> call, Throwable t) {
                Toast.makeText(mContext, "Unable to start the chat. Please try again!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
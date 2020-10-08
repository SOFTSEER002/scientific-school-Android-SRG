package com.jeannypr.scientificstudy.Teacher.activity;

import android.Manifest;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.tabs.TabLayout;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.fragment.TeacherProfileDetailTabFragment;
import com.jeannypr.scientificstudy.Utilities.UploadFileUsingMultipart;
import com.jeannypr.scientificstudy.Utilities.UploadListner;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TeacherProfileActivity extends AppCompatActivity {

    private Context context;
    ProgressBar progressBar;
    private final int REQUEST_CAMERA = 0, SELECT_FILE = 1, REQUEST_CROP_PICTURE = 2;
    private static int CROP_IMAGE_SIZE = 192;
    UserModel userModel;
    String imageTakenFrom;
    Bitmap cameraImgBmp;
    int teacherUserId, teacherId;
    String imagePath, teacherName;
    TabLayout tabLayout;
    ViewPager pager;
    ImageView mStudentImg;
    TextView txtTeacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        context = this;

        teacherId = getIntent().getIntExtra(Constants.TEACHER_ID, -1);
        teacherUserId = getIntent().getIntExtra(Constants.TEACHER_USER_ID, -1);
        imagePath = getIntent().getStringExtra("profileImage");
        teacherName = getIntent().getStringExtra("teacherName");
        userModel = UserPreference.getInstance(context).getUserData();

        pager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);
        mStudentImg = findViewById(R.id.studentImg);
        txtTeacherName = findViewById(R.id.title);
        progressBar = findViewById(R.id.progressBar);

        SetUpViewPager();
        tabLayout.setupWithViewPager(pager);
        setupTabIcons();
        SetToolbar(teacherName, "");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void SetUpViewPager() {
        TeacherProfileActivity.LeaveVPagerAdapter adapter = new TeacherProfileActivity.LeaveVPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TEACHER_USER_ID, teacherUserId);

        TeacherProfileDetailTabFragment detailFrag = new TeacherProfileDetailTabFragment();
        detailFrag.setArguments(bundle);
        adapter.addFragment(detailFrag, "Detail");

        // adapter.addFragment(new StudentProfileDetailTabFragment(), "Timetable");
        pager.setAdapter(adapter);

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_info2_sm);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_time_table_sm);
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

        txtTeacherName.setText(teacherName.substring(0, 1).toUpperCase() + teacherName.substring(1).toLowerCase());

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true);

        try {
            if (!imagePath.equals("")) {
                Glide.with(context).load(Constants.IMAGE_BASE_URL + imagePath).apply(requestOptions).into(mStudentImg);
            } else {
                //TODO : set default one
            }
        } catch (Exception ex) {
            Log.d("Upload image", ex.getMessage());
        }
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_profile, menu);

        if (userModel.getRoleTitle().equals(Constants.Role.ADMIN)) {
            menu.findItem(R.id.editDetails).setVisible(true);
            menu.findItem(R.id.editDetailsIc).setVisible(true);
        } else {
            menu.findItem(R.id.editDetailsIc).setVisible(false);
            menu.findItem(R.id.editDetails).setVisible(false);
        }

        if (userModel.getRoleTitle().equals(Constants.Role.ADMIN) || (userModel.getRoleTitle().equals(Constants.Role.TEACHER) && userModel.getClassId() != null)) {
            if (userModel.getUserId() == teacherUserId)
                menu.findItem(R.id.uploadPhoto).setVisible(true);
            else menu.findItem(R.id.uploadPhoto).setVisible(false);

        } else {
            menu.findItem(R.id.uploadPhoto).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.uploadPhoto:
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
        Intent i = new Intent(this, AddEditStaffActivity.class);
        i.putExtra(Constants.TEACHER_ID, teacherId);
        startActivity(i);
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

        File croppedImageFile = Utility.CreateFileName(teacherId, context);
        imageUri = Uri.fromFile(croppedImageFile);

        if (imageUri != null && resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_CROP_PICTURE:

                    switch (imageTakenFrom) {
                        case Constants.ImageTakenFrom.CAMERA:
                            Uri uri = Utility.GetUriFromIntent(data, context);
                            IsUriExists(uri);
                            break;

                        case Constants.ImageTakenFrom.GALLERY:
                            IsUriExists(imageUri);
                            break;
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

    private void ShowImageCropView(Intent data, Uri selectedImageUri) {

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
    }

    private void UploadCroppedPicture(final Uri croppedImageUri) {

        String mode = Constants.UploadImageContext.TEACHER;
        String ext = Utility.getMimeType(context, croppedImageUri);
        String fileDataType = Utility.getFileDataType(ext);
        File file = Utility.GetFileFromPath(croppedImageUri, context);

        new UploadFileUsingMultipart().UploadFile(fileDataType, file, teacherId, mode, userModel.getSchoolId(), context, new UploadListner() {
            @Override
            public void onUploadComplete(Integer res) {

                Toast.makeText(context, "Saved successfully", Toast.LENGTH_LONG).show();
              /*  if (imageTakenFrom.equals(Constants.ImageTakenFrom.CAMERA)) {
//                    ImgTeacherProfile.setImageBitmap(cameraImgBmp);
                } else {
                    ImgTeacherProfile.setImageURI(croppedImageUri);
                }*/
            }

            @Override
            public void onUploadStart() {
            }
        });
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


}

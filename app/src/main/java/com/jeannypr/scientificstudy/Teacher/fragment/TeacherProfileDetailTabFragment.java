package com.jeannypr.scientificstudy.Teacher.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.adapter.ClassTeachersInStaffDetailAdapter;
import com.jeannypr.scientificstudy.Teacher.api.TeacherService;
import com.jeannypr.scientificstudy.Teacher.model.StaffProfileBean;
import com.jeannypr.scientificstudy.Teacher.model.StaffProfileModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherProfileDetailTabFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View view;
    private int teacherUserId;
    private CircleImageView mFathersPic, mMothersPic;
    private ImageView mCallBtn, mEmailBtn, mChatBtn;
    private Context mContext;
    private String phoneNo, emailId;
    private ProgressBar pb;

    public TeacherProfileDetailTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle bundle = getArguments();

        if (bundle != null)
            teacherUserId = bundle.getInt(Constants.TEACHER_USER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_teacher_profile_detail_tab, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        mCallBtn = view.findViewById(R.id.callIc);
        mChatBtn = view.findViewById(R.id.chatIc);
        mEmailBtn = view.findViewById(R.id.emailIc);
        pb = view.findViewById(R.id.progressBar);

        GetTeacherInfo();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void GetTeacherInfo() {
        pb.setVisibility(View.VISIBLE);
        TeacherService teacherService = new DataRepo<>(TeacherService.class, context).getService();
        UserModel userData = UserPreference.getInstance(context).getUserData();

        teacherService.getTeacherDetails(teacherUserId, userData.getSchoolId(), userData.getAcademicyearId()).enqueue(new Callback<StaffProfileBean>() {
            @Override
            public void onResponse(Call<StaffProfileBean> call, Response<StaffProfileBean> response) {

                StaffProfileBean resp = response.body();

                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
                            SetSubjectsAndTeachers(resp.data.classSubjects);
                            SetProfessionalDetail(resp.data.ProffessionalDetails);
                            SetPersonalDetail(resp.data.PersonalDetail);
                            CommunicateByCall(resp.data.PersonalDetail);
                            CommunicateByEmail(resp.data.PersonalDetail);
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Teacher list could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StaffProfileBean> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(context, "Teacher's profile could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetSubjectsAndTeachers(ArrayList<StaffProfileModel> list) {
        ConstraintLayout subTeacherSection = view.findViewById(R.id.subTeacherSection);
        View divider = view.findViewById(R.id.div3);
        if (list.size() < 1) {
            divider.setVisibility(View.GONE);
            subTeacherSection.setVisibility(View.GONE);
        } else {

            divider.setVisibility(View.VISIBLE);
            subTeacherSection.setVisibility(View.VISIBLE);
            RecyclerView subjectTeacherList = view.findViewById(R.id.subjectTeacherList);

            ArrayList<StaffProfileModel> staffProfileModels = new ArrayList<>();
            ClassTeachersInStaffDetailAdapter subjectsAdapter = new ClassTeachersInStaffDetailAdapter(context, staffProfileModels);
            subjectTeacherList.setAdapter(subjectsAdapter);

            for (StaffProfileModel subject : list) {
                staffProfileModels.add(subject);
            }
            subjectsAdapter.notifyDataSetChanged();
        }
    }

    private void SetProfessionalDetail(StaffProfileModel professionalDetail) {
        TextView txtQualification = view.findViewById(R.id.qualification);
        TextView txtDesignation = view.findViewById(R.id.designation);

        txtQualification.setText(professionalDetail.getQualification());
        txtDesignation.setText(professionalDetail.getDesignation());
    }

    private void SetPersonalDetail(StaffProfileModel personalDetail) {
        TextView txtClassName = view.findViewById(R.id.classTxt);
        TextView txtDob = view.findViewById(R.id.dob);
        TextView txtclassTeacher = view.findViewById(R.id.classteacher);
        TextView txtAddress = view.findViewById(R.id.address);

        txtClassName.setText(personalDetail.getClassName());
        txtDob.setText(personalDetail.getDateOfBrith());

        if (personalDetail.IsClassTeacher)
            txtclassTeacher.setText("Yes");
        else
            txtclassTeacher.setText("No");

        txtAddress.setText(personalDetail.getAddress());
    }

    private void CommunicateByCall(StaffProfileModel contact) {
        phoneNo = contact.getPhone();
        if (phoneNo != null && !phoneNo.equals("")) {
            mCallBtn.setVisibility(View.VISIBLE);
            mCallBtn.setOnClickListener(this);

        } else {
            mCallBtn.setVisibility(View.GONE);
        }
    }

    private void CommunicateByEmail(StaffProfileModel email) {
        emailId = email.getEmail();
        if (emailId != null && !emailId.equals("")) {
            mEmailBtn.setVisibility(View.VISIBLE);
            mEmailBtn.setOnClickListener(this);

        } else {
            mEmailBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callIc:
                Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
                dialPadIntent.setData(Uri.parse("tel:" + phoneNo));
                mContext.startActivity(dialPadIntent);
                break;

            case R.id.emailIc:
                Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + emailId));
                mContext.startActivity(emailIntent);
                break;

            case R.id.chatIc:
                //TODO: open chat screen
                break;

        }
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
        void uploadFathersPhoto();

        void uploadMothersPhoto();

    }

    public void UpdateParentSection(Uri imgUri, String imageTakenFrom, String profilePhotoOf, Bitmap cameraImgBmp) {
        if (imageTakenFrom.equals(Constants.ImageTakenFrom.CAMERA)) {
            switch (profilePhotoOf) {
                case Constants.ProfilePhoto.FATHER:
                    mFathersPic.setImageBitmap(cameraImgBmp);
                    break;

                case Constants.ProfilePhoto.MOTHER:
                    mMothersPic.setImageBitmap(cameraImgBmp);
                    break;
            }
        } else {
            switch (profilePhotoOf) {
                case Constants.ProfilePhoto.FATHER:
                    mFathersPic.setImageURI(imgUri);
                    break;

                case Constants.ProfilePhoto.MOTHER:
                    mMothersPic.setImageURI(imgUri);
                    break;
            }
        }
    }
}
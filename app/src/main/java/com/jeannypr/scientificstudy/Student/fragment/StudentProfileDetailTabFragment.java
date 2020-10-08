package com.jeannypr.scientificstudy.Student.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.adapter.SubjectTeachersInStudentDetailAdapter;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.StudentProfileBean;
import com.jeannypr.scientificstudy.Student.model.StudentProfileModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileDetailTabFragment extends Fragment implements View.OnClickListener {
    private StudentService service;
    private Context context;
    UserModel userModel;
    View view;
    int classId, studentId;
    CircleImageView mFathersPic, mMothersPic;
    FrameLayout mothersPicRow, fatherPicRow;
    ImageView mCallBtn, mEmailBtn, mChatBtn;
    Context mContext;
    String contact, email;
    TextView txtFathersPhoto, txtMothersPhoto;

    private OnDetailTabFragmentInteractionListener mListener;

    public StudentProfileDetailTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        Bundle bundle = getArguments();

        if (bundle != null) {
            studentId = bundle.getInt("studentId");
            classId = bundle.getInt("classId");
        }

        userModel = UserPreference.getInstance(context).getUserData();
        service = new DataRepo<>(StudentService.class, context).getService();
        GetStudentDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_profile_detail_tab, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        mFathersPic = view.findViewById(R.id.fathersPic);
        mMothersPic = view.findViewById(R.id.mothersPic);
        fatherPicRow = view.findViewById(R.id.fatherPicRow);
        mothersPicRow = view.findViewById(R.id.mothersPicRow);

        mCallBtn = view.findViewById(R.id.callIc);
        mChatBtn = view.findViewById(R.id.chatIc);
        mEmailBtn = view.findViewById(R.id.emailIc);
        txtFathersPhoto = view.findViewById(R.id.fathersPicLbl);
        txtMothersPhoto = view.findViewById(R.id.mothersPicLbl);

        mChatBtn.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailTabFragmentInteractionListener) {
            mListener = (OnDetailTabFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void GetStudentDetails() {
        service.GetStudentDetails(userModel.getSchoolId(), userModel.getAcademicyearId(), studentId, classId).enqueue(new Callback<StudentProfileBean>() {
            @Override
            public void onResponse(Call<StudentProfileBean> call, Response<StudentProfileBean> response) {
                StudentProfileBean resp = response.body();
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {

                        if (resp.data != null) {
                            SchoolDetailModel detailModel = UserPreference.getInstance(context).getSchoolData();
                            Boolean canSeeContactNumber = resp.data.getPersonalDetails().getCanSeeContactNumber();
                            if (userModel.getRoleTitle().equals(Constants.Role.ADMIN)) {
                                canSeeContactNumber = true;
                            }
                            detailModel.setCanSeeContactNumber(canSeeContactNumber);

                            ShowParentSection(resp.data.getPersonalDetails().getFatherImgPath(), resp.data.getPersonalDetails().getMotherImgPath());
                            SetAcademicSection(resp.data.getPersonalDetails().getAdmissionNo(), resp.data.getPersonalDetails().getRollNo());
                            SetPersonalDetail(resp.data.PersonalDetails, detailModel.getCanSeeContactNumber());

                            CommunicateByCall(resp.data.getPersonalDetails().MobileNo, detailModel.getCanSeeContactNumber());
                            CommunicateByEmail(resp.data.getPersonalDetails().EmailId);
                            SetSubjectsAndTeachers(resp.data.SubjectAndTeacher);
                        }

                    } else if (resp.rcode == Constants.Rcode.NORECORDS) {
                        Toast.makeText(context, "No record found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Could not load student profile. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StudentProfileBean> call, Throwable t) {
                Log.d("student", "server call error");
                Toast.makeText(context, "Could not load student profile. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetPersonalDetail(final StudentProfileModel personalDetail, Boolean canSeeContactNumber) {
        TextView txtFatherName = view.findViewById(R.id.fatherName);
        TextView txtDob = view.findViewById(R.id.dob);
        TextView txtGender = view.findViewById(R.id.gender);
        TextView txtAddress = view.findViewById(R.id.address);
        TextView txtMotherMobile = view.findViewById(R.id.motherMobile);
        TextView txtMotherEmail = view.findViewById(R.id.motherEmail);

        txtDob.setText(personalDetail.getDateOfBirth());
        txtAddress.setText(personalDetail.getAddress());
        txtGender.setText(personalDetail.getSex());
        txtFatherName.setText(personalDetail.getFatherName());
        txtMotherMobile.setText(personalDetail.getMotherMobileNo());
        txtMotherEmail.setText(personalDetail.getMotherEmail());

        if (!personalDetail.getMotherMobileNo().equals("") && canSeeContactNumber)
            txtMotherMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeACall(personalDetail.getMotherMobileNo());
                }
            });

        if (!personalDetail.getMotherEmail().equals(""))
            txtMotherEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMail(personalDetail.getMotherEmail());
                }
            });
    }

    private void SetAcademicSection(String adm, String roll) {
        TextView txtAdmLbl = view.findViewById(R.id.admNo);
        TextView txtRollNo = view.findViewById(R.id.rollNo);

        txtAdmLbl.setText(adm);
        txtRollNo.setText(roll);
    }

    private void SetSubjectsAndTeachers(ArrayList<StudentProfileModel> list) {
        ConstraintLayout subTeacherSection = view.findViewById(R.id.subTeacherSection);
        View divider = view.findViewById(R.id.div3);
        if (list.size() < 1) {
            divider.setVisibility(View.GONE);
            subTeacherSection.setVisibility(View.GONE);
        } else {

            divider.setVisibility(View.VISIBLE);
            subTeacherSection.setVisibility(View.VISIBLE);
            RecyclerView subjectTeacherList = view.findViewById(R.id.subjectTeacherList);

            ArrayList<StudentProfileModel> subjects = new ArrayList<>();
            SubjectTeachersInStudentDetailAdapter subjectsAdapter = new SubjectTeachersInStudentDetailAdapter(context, subjects);
            subjectTeacherList.setAdapter(subjectsAdapter);

            for (StudentProfileModel subject : list) {
                subjects.add(subject);
            }
            subjectsAdapter.notifyDataSetChanged();
        }
    }

    private void CommunicateByCall(final String contact, Boolean canSeeContactNumber) {
        /*SchoolDetailModel detailModel = UserPreference.getInstance(context).getSchoolData();

        if (userModel.getRoleTitle().equals(Constants.Role.ADMIN)) {
            canSeeContactNumber = true;
        }
        detailModel.setCanSeeContactNumber(canSeeContactNumber);*/
        if (contact != null && !contact.equals("") && canSeeContactNumber) {
            mCallBtn.setVisibility(View.VISIBLE);
            mCallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeACall(contact);
                }
            });
//            this.contact = contact;
        } else {
            mCallBtn.setVisibility(View.GONE);
        }
    }

    private void CommunicateByEmail(final String email) {
        if (email != null && !email.equals("")) {
            mEmailBtn.setVisibility(View.VISIBLE);
            mEmailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMail(email);
                }
            });
//            this.email = email;
        } else {
            mEmailBtn.setVisibility(View.GONE);
        }
    }

    private void sendMail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
        mContext.startActivity(emailIntent);
    }

    private void ShowParentSection(String fatherImgPath, String motherImgPath) {
        fatherPicRow.setOnClickListener(this);
        mothersPicRow.setOnClickListener(this);
        txtFathersPhoto.setOnClickListener(this);
        txtMothersPhoto.setOnClickListener(this);

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true);

        try {
            if (!fatherImgPath.equals("")) {
                Glide.with(context).load(Constants.IMAGE_BASE_URL + fatherImgPath).apply(requestOptions).into(mFathersPic);
            } else {
                //TODO : set default one
            }

            if (!motherImgPath.equals("")) {
                Glide.with(context).load(Constants.IMAGE_BASE_URL + motherImgPath).apply(requestOptions).into(mMothersPic);
            } else {
                //TODO : set default one
            }
        } catch (Exception ex) {
            Log.d("Upload image", ex.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.callIc:
               makeACall();
                break;*/

          /*  case R.id.emailIc:
                Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
                mContext.startActivity(emailIntent);
                break;*/

            case R.id.chatIc:
                mListener.onClickChat();
                break;

            case R.id.fathersPicLbl:
            case R.id.fatherPicRow:
                if (mListener != null) {
                    mListener.uploadFathersPhoto();
                }
                break;

            case R.id.mothersPicLbl:
            case R.id.mothersPicRow:
                if (mListener != null) {
                    mListener.uploadMothersPhoto();
                }
                break;
        }
    }

    private void makeACall(String contact) {
        Intent dialPadIntent = new Intent(Intent.ACTION_DIAL);
        dialPadIntent.setData(Uri.parse("tel:" + contact));
        mContext.startActivity(dialPadIntent);
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
    public interface OnDetailTabFragmentInteractionListener {
        void uploadFathersPhoto();

        void uploadMothersPhoto();

        void onClickChat();
    }
}
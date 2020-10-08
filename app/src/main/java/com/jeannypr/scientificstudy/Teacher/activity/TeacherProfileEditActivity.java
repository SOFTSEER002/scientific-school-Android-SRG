package com.jeannypr.scientificstudy.Teacher.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.adapter.TeacherListAdapter;
import com.jeannypr.scientificstudy.Teacher.model.TeacherProfileModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

public class TeacherProfileEditActivity extends AppCompatActivity implements View.OnClickListener {


    private TeacherListAdapter adapter;
    private Context context;
    private ProgressDialog p_dialog;
    private TeacherProfileModel profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        context = this;

        profile = getIntent().getParcelableExtra("teacher_profile");

        //TODO set the data to profile form

        /*

        TeacherService teacherService = new DataRepo<>(TeacherService.class,context).getService();

        UserPreference userPref = UserPreference.getInstance(context);
        UserModel userData = userPref.getUserData();
        showLoader();
        teacherService.getTeachers(userData.getSchoolId()).enqueue(new Callback<TeacherBean>() {
            @Override
            public void onResponse(Call<TeacherBean> call, Response<TeacherBean> response) {
                TeacherBean resp = response.body();
                if(resp.rcode == Constants.Rcode.OK){
                    List<TeacherModel> allTeachers = resp.data;
                    for(TeacherModel teacher:allTeachers){
                        teachers.add(teacher);
                        adapter.notifyDataSetChanged();
                    }
                    //students = allStudents;

                }else{
                    Toast.makeText(context,"Teacher's data could not updated!",Toast.LENGTH_LONG).show();
                }
                hideLoader();

            }

            @Override
            public void onFailure(Call<TeacherBean> call, Throwable t) {
                Log.d("teacherList","server call error");
                hideLoader();
                Toast.makeText(context,"Teacher list could not be loaded. Please try again.",Toast.LENGTH_LONG).show();
            }
        });
        */
    }

    private void showLoader(){
        p_dialog = Utility.showProgressDialog(context,"Getting teacher list. Please wait...");

    }

    private void hideLoader(){
        p_dialog.dismiss();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btnBack:
//                this.finish();
//                break;
        }

    }


}

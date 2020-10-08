package com.jeannypr.scientificstudy.Student.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.adapter.StudentListAdapter;
import com.jeannypr.scientificstudy.Student.api.StudentService;
import com.jeannypr.scientificstudy.Student.model.StudentBean;
import com.jeannypr.scientificstudy.Student.model.StudentModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileEditActivity extends AppCompatActivity implements View.OnClickListener {


    private int classId;
    private String className;
    private StudentListAdapter adapter;
    private Context context;
    private ProgressDialog p_dialog;
    private ArrayList<StudentModel> students;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        context = this;

        classId = getIntent().getIntExtra("Id",-1);
        className = getIntent().getStringExtra("ClassName");
        TextView classTextView= findViewById(R.id.className);
        classTextView.setText(className);
//        ImageView backbutton = findViewById(R.id.btnBack);
//        backbutton.setOnClickListener(this);



        if(classId == -1){
            Toast.makeText(this, "Please select a class to view students.",Toast.LENGTH_SHORT).show();
        }else{
            ListView studentList = findViewById(R.id.student_list);

            students = new ArrayList<StudentModel>();
            adapter = new StudentListAdapter(context, students);


            studentList.setAdapter(adapter);

            studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView,
                                        View view, int position, long rowId) {

                    StudentModel studentModel = adapter.getItem(position);

                    // Use the message to create a Snackbar
                    Snackbar.make(adapterView, studentModel.Name, Snackbar.LENGTH_LONG)
                            .show(); // Show the Snackbar
                }
            });

            StudentService studentService = new DataRepo<>(StudentService.class,context).getService();

            showLoader();
            studentService.getStudents(classId).enqueue(new Callback<StudentBean>() {
                @Override
                public void onResponse(Call<StudentBean> call, Response<StudentBean> response) {
                    StudentBean resp = response.body();
                    if(resp.rcode == Constants.Rcode.OK){
                        List<StudentModel> allStudents = resp.data;
                        for(StudentModel student:allStudents){
                            students.add(student);
                            adapter.notifyDataSetChanged();
                        }
                        //students = allStudents;

                    }else{
                        Toast.makeText(context,"Student list could not be loaded. Please try again.",Toast.LENGTH_LONG).show();
                    }
                    hideLoader();

                }

                @Override
                public void onFailure(Call<StudentBean> call, Throwable t) {
                    Log.d("studentList","server call error");
                    hideLoader();
                    Toast.makeText(context,"Student list could not be loaded. Please try again.",Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private void showLoader(){
        p_dialog = Utility.showProgressDialog(context,"Getting student list. Please wait...");

    }

    private void hideLoader(){
        p_dialog.dismiss();

    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btnBack:
//                this.finish();
//                break;
//        }

    }


}

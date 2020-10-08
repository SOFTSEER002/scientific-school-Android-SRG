package com.jeannypr.scientificstudy.Exam.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Exam.model.ExamMarkDetailModel;
import com.jeannypr.scientificstudy.Exam.model.GradeModel;
import com.jeannypr.scientificstudy.Exam.model.StudentMarkModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class EnterMarksAdapter extends RecyclerView.Adapter {

    List<StudentMarkModel> data;
    ExamMarkDetailModel examDetail;
    List<GradeModel> grades;
    Context mContext;
    EnterMarksAdapter.OnItemClickListener listener;
    ArrayList<DropDownModel> gradeList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_enter_mark, parent, false);
        return new EnterMarksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StudentMarkModel studentModel = data.get(position);
        //TODO: Check for issue while scrolling.
        holder.setIsRecyclable(false);
        ((EnterMarksAdapter.ViewHolder) holder).bind(studentModel, listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public EnterMarksAdapter(Context context, List<StudentMarkModel> data, ExamMarkDetailModel examDetail, List<GradeModel> grades, ArrayList<DropDownModel> gradeList,
                             EnterMarksAdapter.OnItemClickListener listener) {
        super();
        this.data = data;
        this.mContext = context;
        this.listener = listener;
        this.examDetail = examDetail;
        this.grades = grades;
        this.gradeList = gradeList;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        EditText edMarks;
        Spinner ddlGradeList;
        DropDownAdapter adapter;
        TextView roll, tvStudentName, absentReason, firstLetter;
        CheckBox btnPresent;
        ConstraintLayout absentRow;
        ImageView studentImg;

        ViewHolder(View view) {
            super(view);
            edMarks = view.findViewById(R.id.marks);
            ddlGradeList = view.findViewById(R.id.ddlGradeList);
            roll = view.findViewById(R.id.rollInEnterMarksModule);

            tvStudentName = view.findViewById(R.id.studentName);
            absentReason = view.findViewById(R.id.absentReason);
            btnPresent = view.findViewById(R.id.btnPresent);
            firstLetter = view.findViewById(R.id.firstLetter);
            studentImg = view.findViewById(R.id.studentImg);
            absentRow = view.findViewById(R.id.absentRow);
        }

        public void bind(final StudentMarkModel studentMark, final EnterMarksAdapter.OnItemClickListener listener) {
            if (studentMark.Name != null && !studentMark.Name.equals("")) {
                SetImg(String.valueOf(studentMark.Name.charAt(0)));
            } else {
                SetImg("");
            }

            adapter = new DropDownAdapter(mContext,
                    R.layout.row_spinner_grades,
                    gradeList);

            if (!examDetail.getMarking()) {
                edMarks.setVisibility(View.INVISIBLE);
                ddlGradeList.setAdapter(adapter);
                ddlGradeList.setVisibility(View.VISIBLE);

                //   ddlGradeList.setSelection(GetIndex(studentMark.Grade));
                for (int i = 0; i < grades.size(); i++) {
                    if (grades.get(i).Grade.equals(studentMark.Grade)) {
                        ddlGradeList.setSelection(i+1);
                        break;
                    }
                }

                ddlGradeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {

                        DropDownModel selectedItem = adapter.getItem(position);
                        if (selectedItem != null) {
                            studentMark.GradeId = selectedItem.getId();
                            studentMark.Grade = selectedItem.getText();
                            if (selectedItem.getId() == -1) {
                                studentMark.GradeId = null;
                                studentMark.Grade = "";
                            }

                        } else {
                            studentMark.GradeId = null;
                            studentMark.Grade = "";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {
                        studentMark.GradeId = null;
                        studentMark.Grade = "";
                    }
                });

            } else {

                edMarks.setVisibility(View.VISIBLE);
                ddlGradeList.setVisibility(View.GONE);

                studentMark.GradeId = null;
                studentMark.Grade = "";
            }

            tvStudentName.setText(studentMark.Name == null ? "" : studentMark.Name.substring(0, 1).toUpperCase() + studentMark.Name.substring(1).toLowerCase());
            roll.setText("Roll No.- " + String.valueOf(studentMark.Roll == -1 ? "" : studentMark.Roll));

            if (studentMark.IsPresent != null && !studentMark.IsPresent) {
                btnPresent.setChecked(false);
                absentRow.setVisibility(View.VISIBLE);
                absentReason.setText(studentMark.Notes == null ? "AB" : studentMark.Notes);
                edMarks.setVisibility(View.GONE);
                ddlGradeList.setVisibility(View.GONE);

            } else {
                btnPresent.setChecked(true);
                absentRow.setVisibility(View.GONE);

                if (examDetail.getMarking()) {
                    edMarks.setVisibility(View.VISIBLE);
                    edMarks.setText(String.valueOf(studentMark.Marks == null ? "" : studentMark.Marks));
                    ddlGradeList.setVisibility(View.GONE);

                } else {
                    ddlGradeList.setVisibility(View.VISIBLE);
                    edMarks.setVisibility(View.INVISIBLE);

                  /*  if (studentMark.GradeId != null) {
                        for (int i = 0; i < grades.size(); i++) {
                            if (grades.get(i).Grade.equals(studentMark.Grade)) {
                                ddlGradeList.setSelection(i+1);
                                break;
                            }
                        }
                    } else {
                        ddlGradeList.setSelection(0);
                    }*/
                }

            }

            edMarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if (s.length() > 0) {
                        try {
                            studentMark.Marks = Float.parseFloat(s.toString());
                            if (studentMark.Marks > examDetail.FullMarks) {
                                edMarks.setError("Marks obtained can not be greater than full marks!");

                            } else {
                                edMarks.setError(null);
                            }

                        } catch (NumberFormatException ex) {
                            Log.d("EnterMarksFormatError", ex.getMessage());
                        }

                    } else {
                        studentMark.Marks = null;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            btnPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    studentMark.IsPresent = isChecked;
                    if (studentMark.IsPresent) {
                        btnPresent.setChecked(true);
                        absentRow.setVisibility(View.GONE);

                        if (!examDetail.getMarking()) {
                            // gradeContainer.setVisibility(View.VISIBLE);
                            ddlGradeList.setVisibility(View.VISIBLE);
                            edMarks.setVisibility(View.INVISIBLE);
                        } else {
                            edMarks.setVisibility(View.VISIBLE);
                            ddlGradeList.setVisibility(View.GONE);
                        }
                        edMarks.setText("");

                    } else {
                        btnPresent.setChecked(false);
                        studentMark.Marks = null;
                        studentMark.Grade = "";
                        studentMark.GradeId = null;

                        absentReason.setText("AB");
                        absentRow.setVisibility(View.VISIBLE);
                        edMarks.setVisibility(View.GONE);
                        // if (!examDetail.getMarking()) {
                        //   gradeContainer.setVisibility(View.INVISIBLE);
                        ddlGradeList.setVisibility(View.GONE);
                        // }
                    }
                }
            });

            absentRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popup = new PopupMenu(mContext, absentReason);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.absent_reason_menu, popup.getMenu());


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            studentMark.Notes = item.getTitle().toString();
                            absentReason.setText(studentMark.Notes);
                            return true;
                        }
                    });
                    popup.show();
                }
            });

        }

        private void SetImg(String letter) {
            firstLetter.setText(letter.toUpperCase());
            Drawable d = studentImg.getBackground();
            int colorId = Utility.GetRandomMaterialColor("materialColor", mContext);

            if (d instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable) d;
                shapeDrawable.getPaint().setColor(colorId);

            } else if (d instanceof GradientDrawable) {

                GradientDrawable gradientDrawable = (GradientDrawable) d;
                gradientDrawable.setColor(colorId);

            } else if (d instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) d;
                colorDrawable.setColor(colorId);
            }
        }

        private int GetIndex(String gradeVal) {
            int index = 1;
            boolean match = false;
            for (GradeModel grade : grades) {
                if (grade.Grade.equals(gradeVal)) {
                    match = true;
                    break;
                }
                index++;
            }
            return match ? index : 0;
        }
    }

    public interface OnItemClickListener {
        void onStudentClick(StudentMarkModel studentModel);
    }
}
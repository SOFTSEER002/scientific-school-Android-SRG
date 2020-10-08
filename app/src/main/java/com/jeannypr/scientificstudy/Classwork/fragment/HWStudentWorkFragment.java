package com.jeannypr.scientificstudy.Classwork.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Classwork.activity.HWChattingActivity;
import com.jeannypr.scientificstudy.Classwork.activity.HWDetailActivity;
import com.jeannypr.scientificstudy.Classwork.adapter.HwStudentListAdapter;
import com.jeannypr.scientificstudy.Classwork.api.CwHwService;
import com.jeannypr.scientificstudy.Classwork.model.AssignmentBean;
import com.jeannypr.scientificstudy.Classwork.model.AssignmentModel;
import com.jeannypr.scientificstudy.Classwork.model.AssignmentStudentsModel;
import com.jeannypr.scientificstudy.Classwork.model.AssignmentSummaryModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.FragmentHwStudentWorkTabBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HWStudentWorkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HWStudentWorkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HWStudentWorkFragment extends Fragment implements View.OnClickListener, HwStudentListAdapter.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private FragmentHwStudentWorkTabBinding mViewBinding;
    Context mContext;
    private UserPreference userPref;
    private UserModel userModel;
    private HWDetailActivity mListner;
    private CwHwService classworkService;
    private int activityId;
    private ArrayList<AssignmentStudentsModel> students;
    private HwStudentListAdapter adapter;
    private String studentIds;

    public HWStudentWorkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BroadcastMsgFragment.
     */
    public static HWStudentWorkFragment newInstance() {
        HWStudentWorkFragment fragment = new HWStudentWorkFragment();
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
        if (bundle != null)
            activityId = bundle.getInt(Constants.ACTIVITY_ID);

        userPref = UserPreference.getInstance(mContext);
        userModel = userPref.getUserData();
        classworkService = new DataRepo<>(CwHwService.class, mContext).getService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_hw_student_work_tab, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        students = new ArrayList<>();
        adapter = new HwStudentListAdapter(mContext, students, this);
        mViewBinding.studentRV.setAdapter(adapter);

        getStudents();

        mViewBinding.btnCompleted.setOnClickListener(this);
        mViewBinding.btnRework.setOnClickListener(this);
        mViewBinding.checkbox.setOnCheckedChangeListener(this);

        mViewBinding.assignedLbl.setOnClickListener(this);
        mViewBinding.inProgressLbl.setOnClickListener(this);
        mViewBinding.completedLbl.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getStudents();
    }

    private void getStudents() {
        mViewBinding.progressBar.setVisibility(View.VISIBLE);
        classworkService.getStudents(activityId).enqueue(new Callback<AssignmentBean>() {
            @Override
            public void onResponse(Call<AssignmentBean> call, Response<AssignmentBean> response) {
                mViewBinding.progressBar.setVisibility(View.GONE);
                students.clear();

                AssignmentBean bean = response.body();
                if (bean != null) {
                    if (bean.rcode.equals(Constants.Rcode.OK)) {
                        AssignmentModel assignment = bean.getData();

                        Collections.sort(assignment.getStudents(), new Comparator<AssignmentStudentsModel>() {
                            @Override
                            public int compare(AssignmentStudentsModel lhs, AssignmentStudentsModel rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });

                        setMISData(assignment.getSummary());
                        for (AssignmentStudentsModel item : assignment.getStudents()) {
                            students.add(item);
                        }

                        adapter.notifyDataSetChanged();

                    } else if (bean.rcode == Constants.Rcode.NORECORDS) {
                        mViewBinding.studentRV.setVisibility(View.GONE);
                        mViewBinding.noRecordLayout.noRecord.setVisibility(View.VISIBLE);
                        mViewBinding.noRecordLayout.noRecordMsg.setText(getString(R.string.noRecordMsg));

                    }
                } else
                    Toast.makeText(mContext, bean.msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<AssignmentBean> call, Throwable t) {
                mViewBinding.progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, R.string.noStudentFound, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setMISData(AssignmentSummaryModel summary) {
        mViewBinding.assignedMIS.setText(String.valueOf(summary.getAssigned()));
        mViewBinding.inProgressMIS.setText(String.valueOf(summary.getInProgress()));
        mViewBinding.completedMIS.setText(String.valueOf(summary.getCompleted()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRework:
                studentIds = getStudentIds();
                if (!studentIds.isEmpty())
                    performBulkOperation(Constants.HWBulkOperationStatus.REWORK);
                else displayMsgInToast(R.string.pleaseSelectStudent);
                break;

            case R.id.btnCompleted:
                studentIds = getStudentIds();
                if (!studentIds.isEmpty())
                    performBulkOperation(Constants.HWBulkOperationStatus.COMPLETED);
                else displayMsgInToast(R.string.pleaseSelectStudent);
                break;

            case R.id.completedLbl:
                sortList(Constants.HWCurrentStatus.COMPLETED);
                break;

            case R.id.inProgressLbl:
                sortList(Constants.HWCurrentStatus.INPROGRESS);
                break;

            case R.id.assignedLbl:
                sortList("");
                break;
        }
    }

    private void sortList(final String status) {
        if (students.size() > 0) {
            mViewBinding.progressBar.setVisibility(View.VISIBLE);

            if (!status.equals("")) {
                ArrayList<AssignmentStudentsModel> matchingStatus = new ArrayList<>();
                ArrayList<AssignmentStudentsModel> nonMatchingStatus = new ArrayList<>();

                for (AssignmentStudentsModel student : students) {
                    if (student.getHomeworkCurrentStatus().equals(status))
                        matchingStatus.add(student);
                    else nonMatchingStatus.add(student);
                }
                if (matchingStatus.size() > 0) {
                    students.clear();
                    students.addAll(matchingStatus);
                    students.addAll(nonMatchingStatus);
                    adapter.notifyDataSetChanged();
                }

            } else {
                Collections.sort(students, new Comparator<AssignmentStudentsModel>() {
                    @Override
                    public int compare(AssignmentStudentsModel lhs, AssignmentStudentsModel rhs) {
                        return lhs.getName().compareToIgnoreCase(rhs.getName());
                    }
                });
                adapter.notifyDataSetChanged();
            }

            mViewBinding.progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * created to perform bulk operation
     * call API to save status
     *
     * @param isWorkCompleted
     */
    private void performBulkOperation(String isWorkCompleted) {
        mViewBinding.progressBar.setVisibility(View.VISIBLE);
        mViewBinding.btnRework.setVisibility(View.GONE);
        mViewBinding.btnCompleted.setVisibility(View.GONE);

        classworkService.bulkHWOperation(activityId, userModel.getUserId(), studentIds, isWorkCompleted, Constants.HWCommentBy.TEACHER).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                mViewBinding.progressBar.setVisibility(View.GONE);
                mViewBinding.btnRework.setVisibility(View.VISIBLE);
                mViewBinding.btnCompleted.setVisibility(View.VISIBLE);

                if (response.body() != null) {
                    if (response.body().rcode.equals(Constants.Rcode.OK)) {
                        displayMsgInToast(response.body().msg);
                        mViewBinding.checkbox.setChecked(false);
                        getStudents();

                    } else
                        displayMsgInToast(response.body().msg);
                } else
                    displayMsgInToast(R.string.somethingWrongNoDataMsg);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                mViewBinding.progressBar.setVisibility(View.GONE);
                mViewBinding.btnRework.setVisibility(View.VISIBLE);
                mViewBinding.btnCompleted.setVisibility(View.VISIBLE);
                displayMsgInToast(R.string.somethingWrongNoDataMsg);
            }
        });
    }

    private String getStudentIds() {
        ArrayList<String> studentArr = new ArrayList<>();
        for (AssignmentStudentsModel student : students) {
//            studentIds = studentIds + "," + String.valueOf(student.getStudentId());
            if (student.isChecked)
                studentArr.add(String.valueOf(student.getStudentId()));
        }
        if (studentArr.size() > 0)
            return TextUtils.join(",", studentArr);
        return "";
    }

    private void displayMsgInToast(int errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void displayMsgInToast(String errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AssignmentStudentsModel dataModel) {
        Intent i = new Intent(mContext, HWChattingActivity.class);
        i.putExtra(Constants.STUDENT_NAME, dataModel.getName());
        i.putExtra(Constants.ACTIVITY_ID, activityId);
        i.putExtra(Constants.STATUS, dataModel.getHomeworkCurrentStatus());
        i.putExtra(Constants.STUDENT_ID, dataModel.getStudentId());
        startActivity(i);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            for (AssignmentStudentsModel student : students) {
                student.isChecked = true;
            }
        } else {
            for (AssignmentStudentsModel student : students) {
                student.isChecked = false;
            }
        }
        adapter.notifyDataSetChanged();
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
}

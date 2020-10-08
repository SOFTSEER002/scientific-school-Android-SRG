package com.jeannypr.scientificstudy.Dashboard.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Base.Model.Bean;
import com.jeannypr.scientificstudy.Base.Model.ClassBean;
import com.jeannypr.scientificstudy.Base.Model.ClassModel;
import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Base.Repo.BaseService;
import com.jeannypr.scientificstudy.Base.Repo.DataRepo;
import com.jeannypr.scientificstudy.Base.adapter.CheckableSpinnerAdapter;
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter;
import com.jeannypr.scientificstudy.Dashboard.api.AppSettingService;
import com.jeannypr.scientificstudy.Dashboard.model.BroadCastRequest;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.FragmentSmsBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SMSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SMSFragment extends Fragment implements View.OnClickListener {
    ArrayList<DropDownModel> classes;
    DropDownAdapter classAdapter;
    private FragmentSmsBinding mViewBinding;
    Context mContext;
    String selectedClasses = "";
    String className = "";
    String audience;
    private UserPreference userPref;
    private SchoolDetailModel schoolData;
    private UserModel userModel;
    private final List<CheckableSpinnerAdapter.SpinnerItem<DropDownModel>> spinner_items = new ArrayList<>();
    private final Set<DropDownModel> selected_items = new HashSet<>();
    CheckableSpinnerAdapter adapter;
    int totalClassesSelected = 0;

    public SMSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BroadcastMsgFragment.
     */
    public static SMSFragment newInstance() {
        SMSFragment fragment = new SMSFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        userPref = UserPreference.getInstance(mContext);
        schoolData = userPref.getSchoolData();
        userModel = userPref.getUserData();

        classes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sms, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        selected_items.clear();
        totalClassesSelected = 0;
        selectedClasses = "";
        adapter = new CheckableSpinnerAdapter<>(mContext, Constants.DEFAULT_CLASS, spinner_items, selected_items);
        mViewBinding.classSpinner.setAdapter(adapter);

        adapter.setOnItemClickListener(new CheckableSpinnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int Count, View view) {
                setSelectedClassIndex();
            }
        });

        getClasses();
        audience = Constants.SmsAudience.PARENT;
        setAudienceListner();
        mViewBinding.sendMsgBtn.setOnClickListener(this);
        mViewBinding.classSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    setSelectedClassIndex();
                }
            }
        });
    }

    private void setSelectedClassIndex() {
        ArrayList<String> selectedClassesArr = new ArrayList<String>();
        if (selected_items != null && selected_items.size() > 0) {
            mViewBinding.totalClasses.setVisibility(View.VISIBLE);
            for (DropDownModel selectedItem : selected_items) {
                selectedClassesArr.add(String.valueOf(selectedItem.getId()));
            }
            totalClassesSelected = selectedClassesArr.size();
            String totalClasses = "";

            if (totalClassesSelected > 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    selectedClasses = String.join(",", selectedClassesArr);
                } else {
                    selectedClasses = TextUtils.join(",", selectedClassesArr);
                }
                totalClasses = totalClassesSelected + " classes selected";

            } else {
                selectedClasses = String.valueOf(selectedClassesArr.get(0));
                totalClasses = totalClassesSelected + " class selected";
            }

            mViewBinding.totalClasses.setText(totalClasses);
        }else {
            mViewBinding.totalClasses.setText("");
            mViewBinding.totalClasses.setVisibility(View.GONE);
        }
    }

    /**
     * Set item click listner.
     */
    private void setAudienceListner() {
        mViewBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.parentRadioBtn:
                        audience = Constants.SmsAudience.PARENT;
                        mViewBinding.classSpinner.setVisibility(View.VISIBLE);
                        mViewBinding.totalClasses.setVisibility(View.VISIBLE);
                        break;
                    case R.id.staffRadioBtn:
                        audience = Constants.SmsAudience.STAFF;
                        mViewBinding.classSpinner.setVisibility(View.INVISIBLE);
                        mViewBinding.totalClasses.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    /**
     * Call api to get list of classes.
     * Notify adapter.
     */
    public void getClasses() {
        mViewBinding.pb.setVisibility(View.VISIBLE);

        BaseService baseService = new DataRepo<>(BaseService.class, mContext).getService();
        baseService.getClasses(userModel.getSchoolId(), userModel.getAcademicyearId()).enqueue(new Callback<ClassBean>() {
            @Override
            public void onResponse(Call<ClassBean> call, Response<ClassBean> response) {
                ClassBean resp = response.body();
                spinner_items.clear();
                if (resp != null) {

                    if (resp.rcode == Constants.Rcode.OK) {
                        List<ClassModel> allClasses = resp.data;

                        for (ClassModel cls : allClasses) {
                            DropDownModel dropDownModel = new DropDownModel();
                            dropDownModel.setId(cls.Id);
                            dropDownModel.setText(cls.Name);
                            dropDownModel.setObject(cls);
                            spinner_items.add(new CheckableSpinnerAdapter.SpinnerItem<>(dropDownModel, cls.Name));
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(mContext, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                mViewBinding.pb.setVisibility(View.GONE);
                userModel.setIsLoading(false);
            }

            @Override
            public void onFailure(Call<ClassBean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                userModel.setIsLoading(false);
                Toast.makeText(mContext, "Classes could not be loaded. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendMsgBtn:
                performValidation();
                break;
        }
    }

    private void performValidation() {
        if (audience.equals(Constants.BroadcastAudience.PARENT)) {
            setSelectedClassIndex();
            if (totalClassesSelected < 1) {
                displayErrorMsg(R.string.selectClass);
                return;
            }
        }

        String msg = mViewBinding.edMsg1.getText().toString();
        if (msg.equals(""))
            displayErrorMsg(R.string.enterMsg);
        else sendSMS(msg);
    }

    private void displayErrorMsg(int errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(String msg) {
        mViewBinding.pb.setVisibility(View.VISIBLE);
        UserModel userModel = UserPreference.getInstance(mContext).getUserData();

        BroadCastRequest request = new BroadCastRequest();
        request.setAcademicYearId(userModel.getAcademicyearId());
        request.setUserId(userModel.getUserId());
        request.setSchoolId(userModel.getSchoolId());
        request.setAudience(audience);
        request.setClassIds(selectedClasses);
        request.setMessage(msg);
        request.setMessageSendType(Constants.MessageType.BULKSMS);

        /*Log.e("Broadcast", "<<In api>> "+userModel.getAcademicyearId()+"\n"+userModel.getUserId()+"\n"+
                userModel.getSchoolId()+"\n"+audience+"\n"+selectedClasses+"\n"+msg+"\n"+Constants.MessageType.BULKSMS);*/

        AppSettingService service = new DataRepo<>(AppSettingService.class, mContext).getService();
        service.BroadcastMsg(request).enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                //Log.e("Broadcast", "<<pi response>> "+response.body().rcode);
                if (response.body() != null) {
                    if (response.body().rcode == Constants.Rcode.OK) {
                        Toast.makeText(mContext, ""+response.body().msg, Toast.LENGTH_LONG).show();
                        //Toast.makeText(mContext, getString(R.string.smsSentSuccessMsg), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, ""+response.body().msg, Toast.LENGTH_LONG).show();
                        //Toast.makeText(mContext, response.body().msg != null ? response.body().msg : mContext.getString(R.string.somethingWrongMsg), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.somethingWrongMsg), Toast.LENGTH_SHORT).show();
                }
                mViewBinding.pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                mViewBinding.pb.setVisibility(View.GONE);
                Toast.makeText(mContext, mContext.getString(R.string.somethingWrongMsg), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
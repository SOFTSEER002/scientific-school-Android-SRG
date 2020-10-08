package com.jeannypr.scientificstudy.Student

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.Model.Bean
import com.jeannypr.scientificstudy.Base.Model.DropDownModel
import com.jeannypr.scientificstudy.Base.Model.UserModel
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter
import com.jeannypr.scientificstudy.Login.api.LoginService
import com.jeannypr.scientificstudy.Login.model.FeedbackInput
import com.jeannypr.scientificstudy.Preference.UserPreference
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.Utilities.Utility
import com.jeannypr.scientificstudy.databinding.ActivityFeedbackBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FeedbackActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mViewBinding: ActivityFeedbackBinding
    lateinit var userModel: UserModel
    lateinit var userPref: UserPreference
    lateinit var service: LoginService
    private lateinit var feedbackTypeAdapter: DropDownAdapter
    lateinit var feedbackTypes: ArrayList<DropDownModel>
    private lateinit var context: Context
    private var inputModel = FeedbackInput()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_feedback)

        userPref = UserPreference.getInstance(this)
        userModel = userPref.userData
        service = DataRepo(LoginService::class.java, this).getService()

        setToolbar()
        initializeFeedbackTypes()
        mViewBinding.saveBtn.setOnClickListener(this)
    }

    /**
     * Initialize toolbar.
     * Set title and back button.
     */
    private fun setToolbar() {
        setSupportActionBar(mViewBinding.customToolbar!!.toolbar)
        Utility.SetToolbar(this, getString(R.string.feedback), "")
        mViewBinding.customToolbar!!.studentImg.setImageDrawable(getDrawable(R.drawable.feedback_header_bg))
        mViewBinding.customToolbar!!.title.setText(R.string.feedback)
        mViewBinding.customToolbar!!.title.gradientStartColor = resources.getColor(R.color.red10)
        mViewBinding.customToolbar!!.title.gradientEndColor = resources.getColor(R.color.red9)
        mViewBinding.customToolbar!!.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) //expanded
                supportActionBar!!.setHomeAsUpIndicator(context.resources.getDrawable(R.drawable.ic_back_arrow_red)) else  //collapsed
                supportActionBar!!.setHomeAsUpIndicator(context.resources.getDrawable(R.drawable.ic_back_arrow_sm))
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.saveBtn -> performValidation()
        }
    }

    private fun initializeFeedbackTypes() {
        feedbackTypes = ArrayList()
        val defaultOption = DropDownModel()
        defaultOption.text = Constants.SELECT
        defaultOption.id = -1
        feedbackTypes.add(defaultOption)

        feedbackTypeAdapter = DropDownAdapter(this, R.layout.row_spinner, feedbackTypes)
        mViewBinding.spinner.adapter = feedbackTypeAdapter
        mViewBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = feedbackTypeAdapter.getItem(position)
                if (selectedItem != null) inputModel.FeedbackType = selectedItem.id
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {}
        }
        getFeedbackTypes()
    }

    private fun getFeedbackTypes() {
        feedbackTypes.add(DropDownModel(Constants.FeedbackTypeId.SUGGESTION, Constants.FeedbackType.SUGGESTION))
        feedbackTypes.add(DropDownModel(Constants.FeedbackTypeId.FEEDBACK, Constants.FeedbackType.FEEDBACK))
        feedbackTypes.add(DropDownModel(Constants.FeedbackTypeId.COMPLAINT, Constants.FeedbackType.COMPLAINT))
        feedbackTypeAdapter.notifyDataSetChanged()
    }

    /**
     * Perform validation for required fields and display error if empty.
     */
    private fun performValidation() {
        clearAllErrors()
        var isValid = true
        if (inputModel.FeedbackType == -1) {
            isValid = false
            Toast.makeText(context, R.string.selectFeedbackType, Toast.LENGTH_SHORT).show()
        }
        if (mViewBinding.descEd.text.toString().isEmpty()) {
            showDescError()
            isValid = false
        } else {
            inputModel.FeedbackDetail = mViewBinding.descEd.text.toString()
            removeDescError()
        }

        inputModel.AcademicYearId = userModel.AcademicyearId
        inputModel.SchoolId = userModel.SchoolId
        inputModel.CreatedBy = userModel.UserId
        if (isValid) saveFeedback(inputModel)
    }

    private fun removeDescError() {
        mViewBinding.desc.isErrorEnabled = false
    }

    private fun clearAllErrors() {
        mViewBinding.desc.isErrorEnabled = false
    }

    /*
     *Call api and show response/error message.
     * @param detailModel
     */
    private fun saveFeedback(input: FeedbackInput?) {
        mViewBinding.progressBar.visibility = View.VISIBLE
        mViewBinding.saveBtn.visibility = View.GONE
        service.sendFeedback(input).enqueue(object : Callback<Bean?> {
            override fun onResponse(call: Call<Bean?>, response: Response<Bean?>) {
                if (response.body() != null) {
                    if (response.body()!!.rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_LONG).show()
                        clearForm()
                    } else Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                } else Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
                mViewBinding.progressBar.visibility = View.GONE
                mViewBinding.saveBtn.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<Bean?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                mViewBinding.progressBar.visibility = View.GONE
                mViewBinding.saveBtn.visibility = View.VISIBLE
            }
        })
    }

    /**
     * Clear form data after calling api.
     * Get new admission no for next transaction.
     * if it is editing mode, make Adm no. field enable as new student will be added now
     */
    private fun clearForm() {
        inputModel = FeedbackInput()
        mViewBinding.spinner.setSelection(0)
        mViewBinding.viewModel = inputModel
        mViewBinding.executePendingBindings()
        scrollPage()
    }

    private fun scrollPage() {
        mViewBinding.scroll.smoothScrollTo(0, 0)
    }

    /**
     * Show error for required field.
     */
    private fun showDescError() {
        mViewBinding.desc.isErrorEnabled = true
        mViewBinding.desc.error = getString(R.string.enterDesc)
    }
}
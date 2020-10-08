package com.jeannypr.scientificstudy.Events.activity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.Model.Bean
import com.jeannypr.scientificstudy.Base.Model.DropDownModel
import com.jeannypr.scientificstudy.Base.Model.UserModel
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Base.activity.BaseActivity
import com.jeannypr.scientificstudy.Base.adapter.DropDownAdapter
import com.jeannypr.scientificstudy.Base.viewmodel.ViewModelProviderFactory
import com.jeannypr.scientificstudy.Classwork.model.ActivityItemSaveModel
import com.jeannypr.scientificstudy.Events.api.EventService
import com.jeannypr.scientificstudy.Events.model.EventDetailBean
import com.jeannypr.scientificstudy.Events.model.EventDetailModel
import com.jeannypr.scientificstudy.Events.model.EventInputModel
import com.jeannypr.scientificstudy.Events.model.EventResponseBean
import com.jeannypr.scientificstudy.Events.navigator.EventNavigator
import com.jeannypr.scientificstudy.Events.viewmodel.EventViewModel
import com.jeannypr.scientificstudy.Preference.UserPreference
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.SptWall.model.EventTypeBean
import com.jeannypr.scientificstudy.Utilities.RealPathUtil
import com.jeannypr.scientificstudy.Utilities.Utility
import com.jeannypr.scientificstudy.databinding.ActivityAddEventPtmBinding
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.ParseException
import java.util.*

class AddEventPtmActivity : BaseActivity<ActivityAddEventPtmBinding, EventViewModel>(), View.OnClickListener, EventNavigator {
    private var isAttachmentChanged: Boolean = false
    lateinit var mViewBinding: ActivityAddEventPtmBinding
    lateinit var mViewModel: EventViewModel
    var mUserModel = UserModel()
    lateinit var service: EventService
    var selectedEventTypeId = -1
    var selectedEventLevelId = -1
    var mPostId = -1
    private lateinit var context: Context
    var mEventType: String? = null
    //    var isEventPublished = false
    var disposable: Disposable? = null
    //    var postInfoModel = EventInputModel()
    var attachmentInfo = ActivityItemSaveModel()
    var eventTypes: ArrayList<DropDownModel>? = null
    var eventLevels: ArrayList<DropDownModel>? = null
    private var eventTypeAdapter: DropDownAdapter? = null
    private var eventLevelAdapter: DropDownAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mEventType = intent.getStringExtra(Constants.POST_TYPE)
        if (intent.hasExtra(Constants.POST_ID))
            mPostId = intent.getIntExtra(Constants.POST_ID, -1)
        super.onCreate(savedInstanceState)
        context = this

        mViewBinding = getBinding()
        mViewBinding.viewModel = mViewModel
        mViewModel.navigator = this

        mUserModel = UserPreference.getInstance(this).userData
        service = DataRepo(EventService::class.java, this).getService()
//        postInfoModel = EventInputModel()
        initializeViews()
    }

    override fun getViewModel(): EventViewModel {
        mViewModel = EventViewModel()
        val factory = ViewModelProviderFactory(mViewModel)
        mViewModel = ViewModelProviders.of(this).get(EventViewModel::class.java)
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_event_ptm
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
        super.onDestroy()
    }

    /**
     * Initiaize all views
     * set click listners
     * get permissions
     * set toolbar
     * get intents
     */
    private fun initializeViews() {
        getStoragePermissions()
        setToolbar()
//        setCheckboxListner()
        initializeEventType()
        initializeEventLevel()

//        mViewBinding.saveBtn.setOnClickListener(this)
        mViewBinding.startDateEd.setOnClickListener(this)
        mViewBinding.endDateEd.setOnClickListener(this)
        mViewBinding.timeEd.setOnClickListener(this)
        mViewBinding.browseBtn.setOnClickListener(this)

        if (mPostId != -1 && mPostId != 0) {
            getPostDetails()
            supportActionBar?.setTitle(R.string.editEvent)
            mViewBinding.customToolbar?.title?.setText(R.string.editEvent)
        }
        setDecsScrollListner()
    }

    private fun getStoragePermissions() {
        if (!Utility.isReadAndWriteStoragePermissionGranted(this)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 3)
        }
    }

    /**
     * Initialize toolbar.
     * Set title and back button.
     */
    private fun setToolbar() {
        setSupportActionBar(mViewBinding.customToolbar?.toolbar)
        Utility.SetToolbar(this, getString(R.string.addEvent), "")
        mViewBinding.customToolbar?.studentImg?.setImageDrawable(getDrawable(R.drawable.ic_event_bg))

        mViewBinding.customToolbar?.title?.setText(R.string.event)
        mViewBinding.customToolbar?.title?.gradientStartColor = resources.getColor(R.color.blue26)
        mViewBinding.customToolbar?.title?.gradientEndColor = resources.getColor(R.color.blue25)
        mViewBinding.customToolbar?.appBar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->

            if (verticalOffset == 0)  //when toolbar is expanded
                supportActionBar?.setHomeAsUpIndicator(context.resources.getDrawable(R.drawable.ic_back_arrow_blue2))
            else  //collapsed
                supportActionBar?.setHomeAsUpIndicator(context.resources.getDrawable(R.drawable.ic_back_arrow_sm))
        })
    }

    /**
     * set checkbox listner
     */
    /*  private fun setCheckboxListner() {
          mViewBinding.checkbox.setOnCheckedChangeListener { _, isChecked -> isEventPublished = isChecked }
      }*/

    private fun initializeEventType() {
        eventTypes = ArrayList()
        val defaultOption = DropDownModel()
        defaultOption.text = Constants.DEFAULT_EVENT_TYPE
        defaultOption.id = -1
        eventTypes!!.add(defaultOption)

        eventTypeAdapter = DropDownAdapter(this, R.layout.row_spinner, eventTypes)
        mViewBinding.eventTypeSpinner.adapter = eventTypeAdapter

        mViewBinding.eventTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View,
                                        position: Int, id: Long) {
                val selectedItem = eventTypeAdapter!!.getItem(position)
//                selectedEventTypeId = selectedItem?.id ?: -1
                mViewModel.selectedEventType.set(selectedItem?.id)
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {}
        }
        getEventType()
    }

    private fun getEventType() {
        service.eventTypes.enqueue(object : Callback<EventTypeBean?> {
            override fun onResponse(call: Call<EventTypeBean?>, response: Response<EventTypeBean?>) {
                val resp = response.body()
                if (resp != null) {
                    if (resp.rcode == Constants.Rcode.OK) {
                        if (resp.data != null) {
                            val items = resp.data
                            for (datam in items) {
                                val dropDownModel = DropDownModel()
                                dropDownModel.id = datam.id
                                dropDownModel.text = datam.name
                                eventTypes!!.add(dropDownModel)
                            }
                            eventTypeAdapter!!.notifyDataSetChanged()
                        } else Toast.makeText(context, resp.msg, Toast.LENGTH_LONG).show()
                    } else Toast.makeText(context, resp.msg, Toast.LENGTH_LONG).show()
                } else Toast.makeText(context, getString(R.string.eventTypesNotFound), Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<EventTypeBean?>, t: Throwable) {
                Log.d("Event type: ", t.message)
            }
        })
    }

    /**
     * Set index of selected item in Spinner.
     *
     * @param selectedItemId
     */
    override fun setSelectedEventType(selectedItemId: Int) {
        mViewBinding.eventTypeSpinner.setSelection(getEventTypeIndex(selectedItemId))
    }

    private fun getEventTypeIndex(selectedItemId: Int): Int {
        var index = 0
        var match = false
        for (model in eventTypes!!) {
            if (model.id == selectedItemId) {
                match = true
                break
            }
            index++
        }
        return if (match) index else 0
    }

    private fun initializeEventLevel() {
        eventLevels = ArrayList()
        val defaultOption = DropDownModel()
        defaultOption.text = Constants.DEFAULT_EVENT_LEVEL
        defaultOption.id = -1
        eventLevels!!.add(defaultOption)

        eventLevelAdapter = DropDownAdapter(this, R.layout.row_spinner, eventLevels)
        mViewBinding.eventLevelSpinner.adapter = eventLevelAdapter

        mViewBinding.eventLevelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                val selectedItem = eventLevelAdapter!!.getItem(position)
//                selectedEventLevelId = selectedItem?.id ?: -1
                mViewModel.selectedEventLevel.set(selectedItem?.id)
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {}
        }
        getEventLevel()
    }

    private fun getEventLevel() {
        eventLevels!!.add(DropDownModel(Constants.EventLevelId.SCHOOL, Constants.EventLevel.SCHOOL))
        eventLevels!!.add(DropDownModel(Constants.EventLevelId.INTER_SCHOOL, Constants.EventLevel.INTER_SCHOOL))
        eventLevels!!.add(DropDownModel(Constants.EventLevelId.DISTRICT, Constants.EventLevel.DISTRICT))
        eventLevels!!.add(DropDownModel(Constants.EventLevelId.STATE, Constants.EventLevel.STATE))
        eventLevels!!.add(DropDownModel(Constants.EventLevelId.NATIONAL, Constants.EventLevel.NATIONAL))
        eventLevelAdapter!!.notifyDataSetChanged()
    }
    //  postInfoModel.setStartTime(Utility.GetFormattedDateDMY(details.getEventTime(), Constants.TIME_FORMAT_hms, Constants.TIME_FORMAT));
    //  postInfoModel.setBudget(details.getb);

    /**
     * Set index of selected item in Spinner.
     *
     * @param selectedItemId
     */
    override fun setSelectedEventLevel(selectedItemId: Int) {
        mViewBinding.eventLevelSpinner.setSelection(getEventLevelIndex(selectedItemId))
    }

    private fun getEventLevelIndex(selectedItemId: Int): Int {
        var index = 0
        var match = false
        for (model in eventLevels!!) {
            if (model.id == selectedItemId) {
                match = true
                break
            }
            index++
        }
        return if (match) index else 0
    }

    private fun setDecsScrollListner() {
        mViewBinding.descEd.setOnTouchListener { v, event ->
            //Register a callback to be invoked when a touch event is sent to this view.
            //Called when a touch event is dispatched to a view. This allows listeners to get a chance to respond before the target view.
            mViewBinding.scroll.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    /**
     * Get teacher details by ID and update model to bind data to view.
     */
    private fun getPostDetails() {
        mViewBinding.progressBar.visibility = View.VISIBLE
        service.getEventDetails(mPostId).enqueue(object : Callback<EventDetailBean?> {
            override fun onResponse(call: Call<EventDetailBean?>, response: Response<EventDetailBean?>) {
                if (response.body() != null) {
                    if (response.body()!!.rcode == Constants.Rcode.OK) {
                        mViewModel.setEventDetail(response.body()!!.data)

//                        val details = response.body()!!.data
//                        postInfoModel.id = mPostId
//                        postInfoModel.title = details.title
//                        postInfoModel.eventType = details.eventType
//                        setEventTypeIndex(details.eventType)
//                        postInfoModel.eventLevel = details.eventLevel
//                        setEventLevelIndex(details.eventLevel)
//                        postInfoModel.eventVenue = details.eventVenue
//                        postInfoModel.attachment = details.attachmentName
//                        setImageData(details)
                        /*  try {
                              postInfoModel.startDate = Utility.GetFormattedDateDMY(details.startDate, Constants.DATE_FORMAT_DMY5, Constants.DATE_FORMAT_DMY4)
                              postInfoModel!!.endDate = Utility.GetFormattedDateDMY(details.enddate, Constants.DATE_FORMAT_DMY5, Constants.DATE_FORMAT_DMY4)
                              //                            postInfoModel.setStartTime(Utility.GetFormattedDateDMY(details.getEventTime(), Constants.TIME_FORMAT_hms, Constants.TIME_FORMAT));
                              postInfoModel!!.startTime = details.eventTime
                          } catch (e: ParseException) {
                              e.printStackTrace()
                          }*/
                        //                        postInfoModel.setBudget(details.getb);
//                        postInfoModel!!.isPublished = details.isPublished
//                        mViewBinding!!.checkbox.isChecked = details.isPublished
//                        postInfoModel!!.description = details.description
//                        mViewBinding!!.viewModel = postInfoModel
//                        mViewBinding!!.executePendingBindings()

                    } else Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                } else Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
                mViewBinding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<EventDetailBean?>, t: Throwable) {
                mViewBinding.progressBar.visibility = View.GONE
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun setImageData(newsInfo: EventDetailModel) {
        if (newsInfo.fileUrl != "") {
            val file = File(newsInfo.fileUrl)
            val uri = Uri.fromFile(file)
            attachmentInfo = ActivityItemSaveModel()
            attachmentInfo.AttachmentName = newsInfo.attachmentName
            attachmentInfo.Title = ""
            attachmentInfo.FileType = Constants.ActivityAttachmentType.FILE
            attachmentInfo.uri = uri
            attachmentInfo.Extension = Utility.getMimeType(context, uri)
            attachmentInfo.Path = newsInfo.fileUrl
            attachmentInfo.Id = 0
            mViewModel.attachmentName.set(newsInfo.attachmentName)
//            mViewBinding.viewModel = postInfoModel
//            mViewBinding.executePendingBindings()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
//            R.id.saveBtn -> performValidation()
            R.id.startDateEd -> initStartDOBPicker()
            R.id.endDateEd -> initEndDOBPicker()
            R.id.timeEd -> initTimePicker()
            R.id.browseBtn -> {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                try {
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                } catch (ex: ActivityNotFoundException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            GALLERY_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                val uri = data.data
                if (uri != null) {
                    try { //TODO: check total size of all files before inflating row
                        isAttachmentChanged = true
                        inflateImageAttachment(uri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, R.string.noImageSelected, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Throws(IOException::class)
    override fun inflateImageAttachment(uri: Uri?) {
        val fileName = Utility.GetFileName(uri, context)
        val sizeArr = Utility.GetFileLength(uri, context)
        val sizeInFloat = sizeArr[0].toFloat()
        val sizeUnit = sizeArr[1]
        if (sizeUnit == Constants.MB) {
            if (sizeInFloat > Constants.IMAGE_MAX_SIZE) {
                Toast.makeText(context, R.string.invalidImageSize, Toast.LENGTH_SHORT).show()
                return
            }
        }
        val size = if (sizeInFloat > 0) sizeInFloat.toString() + sizeUnit else ""
        val ext = Utility.getMimeType(context, uri)
        attachmentInfo = ActivityItemSaveModel()
        attachmentInfo.AttachmentName = fileName
        attachmentInfo.Title = ""
        attachmentInfo.FileType = Constants.ActivityAttachmentType.FILE
        attachmentInfo.uri = uri
        attachmentInfo.Extension = ext
        attachmentInfo.Path = ""
        attachmentInfo.Id = -1
        mViewModel.attachmentName.set(fileName)
    }

    /**
     * Initialize event date
     * set date change listner
     * set selected date on date change.
     */
    private fun initStartDOBPicker() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar[year, month] = dayOfMonth
//            postInfoModel!!.startDate = Utility.GetFormattedDateMDY(calendar.time, Constants.DATE_FORMAT_MDY)
//            mViewBinding!!.startDateEd.setText(Utility.GetFormattedDateMDY(calendar.time, Constants.DATE_FORMAT_DMY))
            mViewModel.startDate.set(Utility.GetFormattedDateMDY(calendar.time, Constants.DATE_FORMAT_DMY))
        },
                calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }

    /**
     * Initialize event time picker
     * set time change listner
     * set selected time on change.
     */
    private fun initTimePicker() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val timePickerDialog = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar[Calendar.HOUR_OF_DAY] = hourOfDay
            calendar[Calendar.MINUTE] = minute
//            postInfoModel!!.startTime = Utility.GetFormattedDateMDY(calendar.time, Constants.TIME_FORMAT)
//            mViewBinding!!.timeEd.setText(postInfoModel!!.startTime)
            mViewModel.startTime.set(Utility.GetFormattedDateMDY(calendar.time, Constants.TIME_FORMAT))
        }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], false)
        timePickerDialog.show()
    }

    /**
     * Initialize event date
     * set date change listner
     * set selected date on date change.
     */
    private fun initEndDOBPicker() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar[year, month] = dayOfMonth
//            postInfoModel!!.endDate = Utility.GetFormattedDateMDY(calendar.time, Constants.DATE_FORMAT_MDY)
//            mViewBinding!!.endDateEd.setText(Utility.GetFormattedDateMDY(calendar.time, Constants.DATE_FORMAT_DMY))
            mViewModel.endDate.set(Utility.GetFormattedDateMDY(calendar.time, Constants.DATE_FORMAT_DMY))
        },
                calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }

    /**
     * Perform validation for required fields and display error if empty.
     */
    /*  private fun performValidation() {
          var isValid = true
          if (mViewBinding!!.titleEd.text.toString() == "") {
              showTitleError()
              isValid = false
          } else postInfoModel!!.title = mViewBinding!!.titleEd.text.toString()

          if (mViewBinding!!.startDateEd.text.toString() == "") {
              showStartDateError()
              isValid = false
          }
          //        else postInfoModel.setStartDate(startDate);
          if (mViewBinding!!.endDateEd.text.toString() == "") {
              showEndDateError()
              isValid = false
          }
          //        else postInfoModel.setEndDate(startDate);
          if (selectedEventLevelId == -1) {
              Toast.makeText(context, R.string.selectEventLevel, Toast.LENGTH_SHORT).show()
              isValid = false
          } else postInfoModel!!.eventLevel = selectedEventLevelId

          if (selectedEventTypeId == -1) {
              Toast.makeText(context, R.string.selectEventType, Toast.LENGTH_SHORT).show()
              isValid = false
          } else postInfoModel!!.eventType = selectedEventTypeId

          if (mViewBinding!!.venueEd.text.toString() != "") postInfoModel!!.eventVenue = mViewBinding!!.venueEd.text.toString()
          //        if (!viewBinding.timeEd.getText().toString().equals("")) postInfoModel.setStartTime(time);
  //        if (!viewBinding.budgetEd.getText().toString().equals(""))
          postInfoModel!!.budget = ""
          if (mViewBinding!!.descEd.text.toString() != "") postInfoModel!!.description = mViewBinding!!.descEd.text.toString()
          postInfoModel!!.isPublished = isEventPublished
          postInfoModel!!.schoolId = mUserModel!!.SchoolId
          postInfoModel!!.academicYearId = mUserModel!!.AcademicyearId
          postInfoModel!!.createdBy = mUserModel!!.UserId
          if (isValid) saveNewsNotice(postInfoModel)
      }*/

    /*
     *Call api and show response/error message.
     * @param input
     */
    override fun saveNewsNotice(input: EventInputModel) {
        mViewBinding.progressBar.visibility = View.VISIBLE
        mViewBinding.saveBtn.visibility = View.GONE
        disableUserInteraction()

        service.addEventsAndPtm(input).enqueue(object : Callback<EventResponseBean?> {
            override fun onResponse(call: Call<EventResponseBean?>, response: Response<EventResponseBean?>) {
                if (response.body() != null) {
                    if (response.body()!!.rcode == Constants.Rcode.OK) {
                        Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_LONG).show()

                        try {
                            if (isAttachmentChanged)
                                uploadFile(attachmentInfo, response.body()!!.data.Id)

                            else {
                                mViewBinding.progressBar.visibility = View.GONE
                                mViewBinding.saveBtn.visibility = View.VISIBLE
                                enableUserInteraction()
                                clearForm()
                            }
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(context, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        mViewBinding.progressBar.visibility = View.GONE
                        mViewBinding.saveBtn.visibility = View.VISIBLE
                        enableUserInteraction()
                        clearForm()
                    }
                } else {
                    Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
                    mViewBinding.progressBar.visibility = View.GONE
                    mViewBinding.saveBtn.visibility = View.VISIBLE
                    enableUserInteraction()
                    clearForm()
                }
            }

            override fun onFailure(call: Call<EventResponseBean?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                mViewBinding.progressBar.visibility = View.GONE
                mViewBinding.saveBtn.visibility = View.VISIBLE
                enableUserInteraction()
            }
        })
    }

    /**
     * Upload attachment.
     *
     * @param attachment
     * @param savedActivityId
     * @throws FileNotFoundException
     */
    @Throws(FileNotFoundException::class)
    private fun uploadFile(attachment: ActivityItemSaveModel, savedActivityId: Int) {
        mViewBinding.progressBar.visibility = View.VISIBLE

        var file: File? = null
        val uri = attachment.uri
        if (attachment.Path != null && attachment.Path != "") {
            file = File(attachment.Path)
        } else {
            var path: String? = ""
            path = RealPathUtil.getRealPath(context, uri)
            if (path == null || path == "") {
                Toast.makeText(context, R.string.fileUploadError, Toast.LENGTH_SHORT).show()
                return
            }
            file = File(path)
        }

        val ext = Utility.getMimeType(context, uri)
        val fileDataType = Utility.getFileDataType(ext)
        val requestBody = RequestBody.create(MediaType.parse(fileDataType), file)
        val body = MultipartBody.Part.createFormData(file.name, file.name, requestBody)
        val name = RequestBody.create(MediaType.parse("text/plain"), file.name)

        DataRepo(EventService::class.java, context)
                .getService()
                .uploadAttachment(mUserModel.SchoolId, savedActivityId, Constants.PostTypeForAttachment.EVENT, body, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Bean?> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(bean: Bean) {}

                    override fun onError(e: Throwable) {
                        mViewBinding.progressBar.visibility = View.GONE
                        mViewBinding.saveBtn.visibility = View.VISIBLE
                        Toast.makeText(context, getString(R.string.failedToUpload) + " " + attachment.AttachmentName, Toast.LENGTH_LONG).show()
                        clearForm()
                        enableUserInteraction()
                    }

                    override fun onComplete() {
                        mViewBinding.progressBar.visibility = View.GONE
                        mViewBinding.saveBtn.visibility = View.VISIBLE
                        Toast.makeText(context, attachment.AttachmentName + " " + getString(R.string.savedSuccessfully), Toast.LENGTH_LONG).show()
                        clearForm()
                        enableUserInteraction()
                    }
                })
    }

    private fun clearForm() {
        if (mPostId == -1) {
            attachmentInfo = ActivityItemSaveModel()
            mViewBinding.eventLevelSpinner.setSelection(0)
            mViewBinding.eventTypeSpinner.setSelection(0)
            mViewModel.clearForm()
        }
        scrollPage()
    }

    private fun scrollPage() {
        mViewBinding.scroll.smoothScrollTo(0, 0)
    }

    /**
     * Show error for required field.
     */
    override fun showTitleError() {
        mViewBinding.titleTv.isErrorEnabled = true
        mViewBinding.titleTv.error = " "
    }

    /**
     * Show error for required field.
     */
    override fun showStartDateError() {
        mViewBinding.startDateTv.isErrorEnabled = true
        mViewBinding.startDateTv.error = " "
    }

    /**
     * Show error for required field.
     */
    override fun showEndDateError() {
        mViewBinding.endDateTv.isErrorEnabled = true
        mViewBinding.endDateTv.error = " "
    }

    override fun enableUserInteraction() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun disableUserInteraction() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    override fun getSelectedEventLevel(): Int {
        return selectedEventLevelId
    }

    override fun getSelectedEventType(): Int {
        return selectedEventTypeId
    }

    override fun showAlertError(msg: Int) {
        Utility.showAlertDialog(context, null, "", getString(msg))
    }

    override fun getUserModel(): UserModel {
        return mUserModel
    }

    override fun getPostId(): Int {
        return mPostId
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 200
    }
}
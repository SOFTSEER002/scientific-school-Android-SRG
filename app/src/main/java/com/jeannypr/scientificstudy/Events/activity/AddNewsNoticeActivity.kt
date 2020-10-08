package com.jeannypr.scientificstudy.Events.activity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Base.Model.Bean
import com.jeannypr.scientificstudy.Base.Model.UserModel
import com.jeannypr.scientificstudy.Base.Repo.DataRepo
import com.jeannypr.scientificstudy.Base.activity.BaseActivity
import com.jeannypr.scientificstudy.Base.viewmodel.ViewModelProviderFactory
import com.jeannypr.scientificstudy.Classwork.model.ActivityItemSaveModel
import com.jeannypr.scientificstudy.Events.api.EventService
import com.jeannypr.scientificstudy.Events.model.*
import com.jeannypr.scientificstudy.Events.navigator.NewsNoticeNavigator
import com.jeannypr.scientificstudy.Events.viewmodel.NewsNoticeViewModel
import com.jeannypr.scientificstudy.Preference.UserPreference
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.Utilities.RealPathUtil
import com.jeannypr.scientificstudy.Utilities.Utility
import com.jeannypr.scientificstudy.databinding.ActivityAddNewsNoticeBinding
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
import java.util.*

class AddNewsNoticeActivity : BaseActivity<ActivityAddNewsNoticeBinding, NewsNoticeViewModel>(), View.OnClickListener, NewsNoticeNavigator {
    lateinit var mViewBinding: ActivityAddNewsNoticeBinding
    private lateinit var mViewModel: NewsNoticeViewModel

    private var mUserModel = UserModel()
    lateinit var service: EventService
    var mSelectedAudience = -1
    var mPostId = 0
    private lateinit var context: Context
    private var mEventType: String? = null
    var attachmentInfo = ActivityItemSaveModel()
    var disposable: Disposable? = null
    private var isAttachmentChanged: Boolean = false
//    private var postInfoModel = NewsInputModel()

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

//        postInfoModel = NewsInputModel()
        initializeViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_news_notice
    }

    override fun getViewModel(): NewsNoticeViewModel {
        /*mViewModel = NewsNoticeViewModel(eventType, mPostId)*/
        mViewModel = NewsNoticeViewModel()
        val factory = ViewModelProviderFactory(mViewModel)
        mViewModel = ViewModelProviders.of(this).get(NewsNoticeViewModel::class.java)
        return mViewModel
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
        setCheckboxListner()
        initializeAudience()

        mViewBinding.newsDateEd.setOnClickListener(this)
        mViewBinding.browseBtn.setOnClickListener(this)

        if (mEventType == Constants.PostType.NEWS) mViewBinding.desc.hint = getString(R.string.news) else mViewBinding.desc.hint = getString(R.string.notice)
        if (mPostId != -1 && mPostId != 0) {
            val title: Int = if (mEventType == Constants.PostType.NEWS) R.string.editNews else R.string.editNotice
            getPostDetails()
            supportActionBar?.setTitle(title)
            mViewBinding.customToolbar?.title?.setText(title)
        }

        setDecsScrollListner()
    }

    private fun getStoragePermissions() {
        if (!Utility.isReadAndWriteStoragePermissionGranted(this)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 3)
        }
    }

    private fun setDecsScrollListner() {
        mViewBinding!!.descEd.setOnTouchListener { v, event ->
            //Register a callback to be invoked when a touch event is sent to this view.
            //Called when a touch event is dispatched to a view. This allows listeners to get a chance to respond before the target view.
            mViewBinding!!.scroll.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    /**
     * set checkbox listner
     */
    private fun setCheckboxListner() {
        mViewBinding.checkbox.setOnCheckedChangeListener { _, isChecked -> mViewModel.isPublished.set(isChecked) }
    }

    /**
     * Initialize toolbar.
     * Set title and back button.
     */
    private fun setToolbar() {
        setSupportActionBar(mViewBinding.customToolbar?.toolbar)
        val title: Int = if (mEventType == Constants.PostType.NEWS) R.string.addNews else R.string.addNotice
        Utility.SetToolbar(this, getString(title), "")

        mViewBinding.customToolbar?.studentImg?.setImageDrawable(getDrawable(R.drawable.ic_news_bg))
        mViewBinding.customToolbar?.title?.setText(title)
        mViewBinding.customToolbar?.title?.gradientStartColor = resources.getColor(R.color.purple12)
        mViewBinding.customToolbar?.title?.gradientEndColor = resources.getColor(R.color.purple11)
        mViewBinding.customToolbar?.appBar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset == 0) //when toolbar is expanded
                supportActionBar?.setHomeAsUpIndicator(context.resources.getDrawable(R.drawable.ic_back_arrow_purple)) else  //collapsed
                supportActionBar?.setHomeAsUpIndicator(context.resources.getDrawable(R.drawable.ic_back_arrow_sm))
        })
    }

    /**
     * Get teacher details by ID and update model to bind data to view.
     */
    private fun getPostDetails() {
        mViewBinding.progressBar.visibility = View.VISIBLE

        service.getNewsNoticeDetails(mPostId).enqueue(object : Callback<NewsNoticeDetailBean?> {
            override fun onResponse(call: Call<NewsNoticeDetailBean?>, response: Response<NewsNoticeDetailBean?>) {
                if (response.body() != null) {
                    if (response.body()!!.rcode == Constants.Rcode.OK) {
                        mViewModel.setEventDetail(response.body()!!.data)

                    } else Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
                } else Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
                mViewBinding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<NewsNoticeDetailBean?>, t: Throwable) {
                mViewBinding.progressBar.visibility = View.GONE
                Toast.makeText(context, R.string.somethingWrongMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun setImageData(newsInfo: NewsNoticeDetailModel) {
        if (newsInfo.fileUrl != "") {
            val file = File(newsInfo.fileUrl)
            val uri = Uri.fromFile(file)
            attachmentInfo = ActivityItemSaveModel()
            attachmentInfo!!.AttachmentName = newsInfo.attachmentName
            attachmentInfo!!.Title = ""
            attachmentInfo!!.FileType = Constants.ActivityAttachmentType.FILE
            attachmentInfo!!.uri = uri
            attachmentInfo!!.Extension = Utility.getMimeType(context, uri)
            attachmentInfo!!.Path = newsInfo.fileUrl
            attachmentInfo!!.Id = 0
            mViewModel.attachmentName.set(newsInfo.attachmentName)
        }
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
     * populate audience.
     */
    private fun initializeAudience() {
        initializeChipGroup(Constants.PostAudienceValues.STUDENT, Constants.PostAudienceNames.STUDENT, Utility.GetRandomMaterialColor("materialColor", context))
        initializeChipGroup(Constants.PostAudienceValues.TEACHER, Constants.PostAudienceNames.TEACHER, Utility.GetRandomMaterialColor("materialColor", context))
        initializeChipGroup(Constants.PostAudienceValues.PARENT, Constants.PostAudienceNames.PARENT, Utility.GetRandomMaterialColor("materialColor", context))
        initializeChipGroup(Constants.PostAudienceValues.SCHOOL, Constants.PostAudienceNames.SCHOOL, Utility.GetRandomMaterialColor("materialColor", context))
        initializeChipGroup(Constants.PostAudienceValues.ALUMNI, Constants.PostAudienceNames.ALUMNI, Utility.GetRandomMaterialColor("materialColor", context))
    }

    /**
     * initialize chip group.
     * set checked listner.
     *
     * @param id
     * @param label
     * @param colorId
     */
    private fun initializeChipGroup(id: Int, label: String, colorId: Int) {
        val chip = Chip(context)
        chip.tag = id
        chip.text = label
        chip.setTextColor(colorId)
        chip.isCheckable = true
        chip.minimumWidth = 100
        chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        chip.chipCornerRadius = 8f
        chip.chipStrokeColor = ColorStateList.valueOf(colorId)
        chip.chipStrokeWidth = 2f

        mViewBinding.chipGroup.addView(chip)

        chip.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                val tag = compoundButton.tag as Int
                mSelectedAudience = tag
            }
        }
    }

    /**
     * Set index of selected item in Spinner.
     * @param audience
     */
    override fun setAudienceIndex(audience: Int) {
        for (i in 0 until mViewBinding.chipGroup.childCount) {
            val v = mViewBinding.chipGroup.getChildAt(i)
            if (v is Chip) {
                val tag = v.getTag() as Int
                if (tag == audience) {
                    if (getAudienceIndex(tag) != -1) {
                        v.isChecked = true
                        break
                    }
                } else v.isChecked = false
            }
        }
    }

    private fun getAudienceIndex(id: Int): Int {
        when (id) {
            Constants.PostAudienceValues.STUDENT -> return 0
            Constants.PostAudienceValues.TEACHER -> return 1
            Constants.PostAudienceValues.PARENT -> return 2
            Constants.PostAudienceValues.SCHOOL -> return 3
            Constants.PostAudienceValues.ALUMNI -> return 4
        }
        return -1
    }

    override fun onClick(view: View) {
        when (view.id) {
//            R.id.saveBtn -> performValidation()
            R.id.newsDateEd -> initDOBPicker()
            R.id.browseBtn -> {
                /*  String[] mimeTypes = {"image/jpeg", "image/jpg", "image/png", "image/bmp"};

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);*/
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
        attachmentInfo.Id = 0
        mViewModel.attachmentName.set(fileName)

    }

    /**
     * Initialize event date
     * set date change listner
     * set selected date on date change.
     */
    private fun initDOBPicker() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar[year, month] = dayOfMonth
            mViewModel.newsDate.set(Utility.GetFormattedDateMDY(calendar.time, Constants.DATE_FORMAT_DMY))
        },
                calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }

    /*
     *Call api and show response/error message.
     * @param input
     */
    override fun saveNewsNotice(input: NewsInputModel) {
        mViewBinding.progressBar.visibility = View.VISIBLE
        mViewBinding.saveBtn.visibility = View.GONE
        disableUserInteraction()

        service.addNewsNotice(input).enqueue(object : Callback<EventResponseBean?> {
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
        if (attachment.Path != null && attachment.Path != "") file = File(attachment.Path) else {
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

        var postType = ""
        postType = if (mEventType == Constants.PostType.NEWS) Constants.PostTypeForAttachment.NEWS else Constants.PostTypeForAttachment.NOTICE
        //TODO: set api to upload attachment

        DataRepo(EventService::class.java, context)
                .getService()
                .uploadAttachment(mUserModel.SchoolId, savedActivityId, postType, body, name)
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
        if (mPostId == 0) {
            attachmentInfo = ActivityItemSaveModel()
            setAudienceIndex(0)
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
    override fun showDateError() {
        mViewBinding.newsDateTv.isErrorEnabled = true
        mViewBinding.newsDateTv.error = " "
    }

    override fun showAudienceError() {
        Toast.makeText(context, R.string.selectAudience, Toast.LENGTH_SHORT).show()
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

    override fun getSelectedAudience(): Int {
        return mSelectedAudience
    }

    override fun getUserModel(): UserModel {
        return mUserModel
    }

    override fun getEventType(): String? {
        return mEventType
    }

    override fun getPostId(): Int {
        return mPostId
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 200
    }
}
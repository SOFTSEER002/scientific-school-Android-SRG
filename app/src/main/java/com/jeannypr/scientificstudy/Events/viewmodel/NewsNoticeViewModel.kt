package com.jeannypr.scientificstudy.Events.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Events.model.NewsInputModel
import com.jeannypr.scientificstudy.Events.model.NewsNoticeDetailModel
import com.jeannypr.scientificstudy.Events.navigator.NewsNoticeNavigator
import com.jeannypr.scientificstudy.Login.viewmodel.BaseViewModel
import com.jeannypr.scientificstudy.Utilities.Utility

/**
 *Created by Kannu
 **/
class NewsNoticeViewModel() : BaseViewModel<NewsNoticeNavigator>() { //val eventType: String?, private val mPostId: Int
    var title = ObservableField<String>("")
    var desc = ObservableField<String>("")
    var newsDate = ObservableField<String>("")
    var attachmentName = ObservableField<String>("")
    var isPublished = ObservableBoolean(false)

    fun NewsNoticeViewModel() {

    }

    fun setEventDetail(newsInfo: NewsNoticeDetailModel?) {
        title.set(newsInfo?.title)
        desc.set(newsInfo?.description)
        newsDate.set(Utility.GetFormattedDateDMY(newsInfo?.newsdate, Constants.DATE_FORMAT_MDY, Constants.DATE_FORMAT_DMY))
        isPublished.set(newsInfo?.isPublished == 1)
        attachmentName.set(newsInfo?.attachmentName)
        newsInfo?.audience?.let { navigator!!.setAudienceIndex(it) }
//        newsInfo?.let { navigator!!.setImageData(it) }
        newsInfo?.let { navigator?.inflateImageAttachment(Utility.getUriFromFileUrl(it.fileUrl)) }
    }

    fun clearForm() {
        title.set("")
        desc.set("")
        attachmentName.set("")
        newsDate.set("")
        isPublished.set(false)
    }

    fun performValidation() {
        var isValid = true
        if (title.get().isNullOrEmpty()) {
            navigator!!.showTitleError()
            isValid = false
        }

        if (newsDate.get().isNullOrEmpty()) {
            navigator!!.showDateError()
            isValid = false
        }

        if (navigator!!.getSelectedAudience() == -1) {
            navigator!!.showAudienceError()
            isValid = false
        }

        if (isValid) setInput()
    }

    private fun setInput() {
        val postInfoModel = NewsInputModel()
        postInfoModel.title = title.get()
        postInfoModel.audience = navigator!!.getSelectedAudience()

        if (!desc.get().isNullOrEmpty()) postInfoModel.description = desc.get()
        postInfoModel.newsDate = Utility.GetFormattedDateDMY(newsDate.get(), Constants.DATE_FORMAT_DMY, Constants.DATE_FORMAT_MDY)
        // Utility.GetFormattedDateDMY(newsDate.get(), Constants.DATE_FORMAT_MDY, Constants.DATE_FORMAT_DMY)

        postInfoModel.isPublished = isPublished.get()
        postInfoModel.id = navigator!!.getPostId()
        postInfoModel.schoolId = navigator!!.getUserModel().SchoolId
        postInfoModel.academicYearId = navigator!!.getUserModel().AcademicyearId
        postInfoModel.createdBy = navigator!!.getUserModel().UserId

        if (navigator!!.getEventType() == Constants.PostType.NEWS) postInfoModel.newsType = Constants.TypeOfCreatedPost.NEWS else postInfoModel.newsType = Constants.TypeOfCreatedPost.NOTICE
        navigator!!.saveNewsNotice(postInfoModel)
    }

    /**
     * on click button, redirect to next screen.
     */
    fun redirectToNext() {
        setIsLoading(true)
        navigator!!.disableUserInteraction()


    }

}
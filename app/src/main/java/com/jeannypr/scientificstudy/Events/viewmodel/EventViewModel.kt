package com.jeannypr.scientificstudy.Events.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Events.model.EventDetailModel
import com.jeannypr.scientificstudy.Events.model.EventInputModel
import com.jeannypr.scientificstudy.Events.navigator.EventNavigator
import com.jeannypr.scientificstudy.Login.viewmodel.BaseViewModel
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.Utilities.Utility

/**
 *Created by Kannu
 **/
class EventViewModel : BaseViewModel<EventNavigator>() { //val eventType: String?, private val mPostId: Int
    var title = ObservableField<String>("")
    var eventVenue = ObservableField<String>("")
    var desc = ObservableField<String>("")
    var startDate = ObservableField<String>("")
    var endDate = ObservableField<String>("")
    var startTime = ObservableField<String>("")
    var attachmentName = ObservableField<String>("")
    var isPublished = ObservableBoolean(false)
    var selectedEventType = ObservableField<Int>(-1)
    var selectedEventLevel = ObservableField<Int>(-1)

    fun EventViewModel() {}

    fun setEventDetail(newsInfo: EventDetailModel?) {
        title.set(newsInfo?.title)
        eventVenue.set(newsInfo?.eventVenue)
        desc.set(newsInfo?.description)
        startDate.set(Utility.GetFormattedDateDMY(newsInfo?.startDate, Constants.DATE_FORMAT_DMY5, Constants.DATE_FORMAT_DMY))
        endDate.set(Utility.GetFormattedDateDMY(newsInfo?.enddate, Constants.DATE_FORMAT_DMY5, Constants.DATE_FORMAT_DMY))
        startTime.set(newsInfo?.eventTime)
        newsInfo?.isPublished?.let { isPublished.set(it) }
        attachmentName.set(newsInfo?.attachmentName)

        newsInfo?.eventLevel?.let { navigator?.setSelectedEventLevel(it) }
        newsInfo?.eventType?.let { navigator?.setSelectedEventType(it) }
//        newsInfo?.let { navigator!!.setImageData(it) }
//        newsInfo?.let { navigator!!.inflateImageAttachment(Utility.GetUriFromPath(it.fileUrl)) }
        newsInfo?.let { navigator?.inflateImageAttachment(Utility.getUriFromFileUrl(it.fileUrl)) }
    }

    fun clearForm() {
        title.set("")
        eventVenue.set("")
        desc.set("")
        attachmentName.set("")
        startDate.set("")
        endDate.set("")
        startTime.set("")
        isPublished.set(false)
    }

    fun performValidation() {
        var isValid = true
        if (title.get().isNullOrEmpty()) {
            navigator!!.showTitleError()
            isValid = false
        }

        if (startDate.get().isNullOrEmpty()) {
            navigator!!.showStartDateError()
            isValid = false
        }

        if (endDate.get().isNullOrEmpty()) {
            navigator!!.showEndDateError()
            isValid = false
        }

        if (selectedEventLevel.get() == -1) {
            navigator!!.showAlertError(R.string.selectEventLevel)
            isValid = false
        }

        if (selectedEventType.get() == -1) {
            navigator!!.showAlertError(R.string.selectEventType)
            isValid = false
        }

        if (isValid) setInput()
    }

    private fun setInput() {
        val inputModel = EventInputModel()
        inputModel.title = title.get()
        inputModel.startDate = Utility.GetFormattedDateDMY(startDate.get(), Constants.DATE_FORMAT_DMY, Constants.DATE_FORMAT_MDY)
        inputModel.endDate = Utility.GetFormattedDateDMY(endDate.get(), Constants.DATE_FORMAT_DMY, Constants.DATE_FORMAT_MDY)
        inputModel.startTime = startTime.get()
        inputModel.eventLevel = selectedEventLevel.get()!!
        inputModel.eventType = selectedEventType.get()!!

        if (!eventVenue.get().isNullOrEmpty()) inputModel.eventVenue = eventVenue.get()
        inputModel.budget = ""
        if (!desc.get().isNullOrEmpty()) inputModel.description = desc.get()

        inputModel.isPublished = isPublished.get()
        inputModel.id = navigator!!.getPostId()
        inputModel.schoolId = navigator!!.getUserModel().SchoolId
        inputModel.academicYearId = navigator!!.getUserModel().AcademicyearId
        inputModel.createdBy = navigator!!.getUserModel().UserId

        navigator!!.saveNewsNotice(inputModel)
    }

    /**
     * on click button, redirect to next screen.
     */
    fun redirectToNext() {
        setIsLoading(true)
        navigator!!.disableUserInteraction()
    }
}
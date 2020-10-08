package com.jeannypr.scientificstudy.Events.navigator

import android.net.Uri
import com.jeannypr.scientificstudy.Base.Model.UserModel
import com.jeannypr.scientificstudy.Events.model.EventDetailModel
import com.jeannypr.scientificstudy.Events.model.EventInputModel
import com.jeannypr.scientificstudy.Events.model.NewsInputModel
import com.jeannypr.scientificstudy.Events.model.NewsNoticeDetailModel

interface EventNavigator {
    fun enableUserInteraction()
    fun disableUserInteraction()

    fun getSelectedEventLevel(): Int
    fun getSelectedEventType(): Int

    fun setSelectedEventLevel(id: Int)
    fun setSelectedEventType(id: Int)

    fun showTitleError()
    fun showStartDateError()
    fun showEndDateError()

    fun showAlertError(msg: Int)
    fun getUserModel(): UserModel

    fun setImageData(newsInfo: EventDetailModel)
    fun inflateImageAttachment(uri: Uri?)
    fun getPostId(): Int

    fun saveNewsNotice(input: EventInputModel)
}

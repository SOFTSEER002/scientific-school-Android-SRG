package com.jeannypr.scientificstudy.Events.navigator

import android.net.Uri
import com.jeannypr.scientificstudy.Base.Model.UserModel
import com.jeannypr.scientificstudy.Events.model.EventDetailModel
import com.jeannypr.scientificstudy.Events.model.NewsInputModel
import com.jeannypr.scientificstudy.Events.model.NewsNoticeDetailModel

interface NewsNoticeNavigator {
    fun enableUserInteraction()
    fun disableUserInteraction()

    fun getSelectedAudience(): Int

    fun showTitleError()
    fun showDateError()

    fun showAudienceError()
    fun getUserModel():UserModel
    fun setAudienceIndex(audience: Int)
    fun setImageData(newsInfo: NewsNoticeDetailModel)
    fun inflateImageAttachment(uri: Uri?)
    fun getEventType(): String?
    fun getPostId(): Int

    fun saveNewsNotice(input: NewsInputModel)
}

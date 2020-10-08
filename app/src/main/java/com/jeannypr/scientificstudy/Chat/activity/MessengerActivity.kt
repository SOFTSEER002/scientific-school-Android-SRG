package com.jeannypr.scientificstudy.Chat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jeannypr.scientificstudy.Base.Constants
import com.jeannypr.scientificstudy.Chat.navigator.MessengerNavigator
import com.jeannypr.scientificstudy.Dashboard.fragment.BroadcastMsgFragment
import com.jeannypr.scientificstudy.Dashboard.fragment.FeeReminderFragment
import com.jeannypr.scientificstudy.Dashboard.fragment.SMSFragment
import com.jeannypr.scientificstudy.R
import com.jeannypr.scientificstudy.databinding.ActivityMessengerBinding
import java.util.ArrayList

class MessengerActivity : AppCompatActivity(), MessengerNavigator {
    private lateinit var mContext: Context
    private lateinit var mViewBinding: ActivityMessengerBinding
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_messenger)

        setupViewPager(mViewBinding.viewpager)
        mViewBinding.messengerTabs.setupWithViewPager(mViewBinding.viewpager)

        redirectToStaffForChat()
        redirectToGroupForChat()
        redirectToPrivateInboxForChat()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(BroadcastMsgFragment.newInstance(), mContext.getString(R.string.broadcast))
        adapter.addFragment(SMSFragment(), mContext.getString(R.string.sms_tab))
        adapter.addFragment(FeeReminderFragment(), mContext.getString(R.string.fee_reminder_tab))
        viewPager.adapter = adapter
        mViewBinding.pb.visibility = View.GONE
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }

    }

    private fun redirectToPrivateInboxForChat() {
        mViewBinding.inboxChatBtn.setOnClickListener {
            val intent = Intent(mContext, ChatGroupActivity::class.java)
            mContext.startActivity(intent)
        }
    }

    private fun redirectToGroupForChat() {
        mViewBinding.classChatBtn.setOnClickListener {
            val intent = Intent(mContext, ChatGroupActivity::class.java)
            intent.putExtra(Constants.SELECTED_TAB, Constants.ChatGroupTab.CLASS)
            mContext.startActivity(intent)
        }
    }

    private fun redirectToStaffForChat() {
        mViewBinding.staffChatBtn.setOnClickListener {
            val intent = Intent(mContext, ChatGroupActivity::class.java)
            intent.putExtra(Constants.SELECTED_TAB, Constants.ChatGroupTab.STAFF)
            mContext.startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Disable user interaction.
     */
    override fun disableUserInteraction() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    /**
     * Enable user interaction.
     */
    override fun enableUserInteraction() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onError(errorMsg: Int) {

    }

    override fun onError(errorMsg: String) {

    }
}
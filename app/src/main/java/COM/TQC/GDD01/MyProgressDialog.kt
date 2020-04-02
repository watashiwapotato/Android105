package COM.TQC.GDD01

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.lang.invoke.MethodHandle
import java.util.zip.Inflater
import kotlin.coroutines.coroutineContext

class MyProgressDialog : DialogFragment {

    private var mProgressBar: ProgressBar? = null
    var isShowing: Boolean = false
        private set
    private var mStartMillisecond: Long = 0
    private var mStopMillisecond: Long = 0
    private var mHandler: Handler? = null

    constructor(mHandler: Handler) : super() {
        this.mHandler = mHandler
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // TO
        return AlertDialog.Builder(activity)
                .setView(LayoutInflater.from(context).inflate(R.layout.progress_dialog, null, false))
                .setCancelable(false)
                .create()
    }

    override fun onStart() {
        // 初始化mProgressBar
        isCancelable = false
        mProgressBar = ProgressBar(context)
        if (dialog.window != null) {
            val px = (PROGRESS_SIZE_DP * resources.displayMetrics.density).toInt()
            dialog.window!!.setLayout(px, px)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        super.onStart()
    }

    override fun show(fm: FragmentManager, tag: String) {
        mStartMillisecond = System.currentTimeMillis()
        isShowing = false
        mStopMillisecond = java.lang.Long.MAX_VALUE

        val handler = Handler()
        handler.postDelayed({
            if (mStopMillisecond > System.currentTimeMillis()) {
                showDialogFragmentAfterDelay(fm, tag)
            }
        }, DELAY_MILLISECOND.toLong())
    }

    private fun showDialogFragmentAfterDelay(fm: FragmentManager, tag: String) {
        // TO DO
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.add(MyProgressDialog(this.mHandler!!), tag)
        fragmentTransaction.commit()
        val handler = Handler()
        handler.postDelayed({
            var msg = Message()
            msg.what = 0x1001
            msg.obj = fm.fragments[0].activity!!.resources.getString(R.string.str_done)

            val fragmentTransaction = fm.beginTransaction()
            fragmentTransaction.remove(fm.fragments[0])
            fragmentTransaction.commit()
            this.mHandler!!.sendMessage(msg)

        }, 2000)
    }

    override fun dismiss() {
        mStopMillisecond = System.currentTimeMillis()

        if (isShowing) {
            if (mProgressBar != null) {
                cancelWhenShowing()
            } else {
                cancelWhenNotShowing()
            }
        }
        isShowing = false
    }

    private fun cancelWhenShowing() {
        if (mStopMillisecond < mStartMillisecond + DELAY_MILLISECOND.toLong() + SHOW_MIN_MILLISECOND.toLong()) {
            val handler = Handler()
            handler.postDelayed({ dismissAllowingStateLoss() }, SHOW_MIN_MILLISECOND.toLong())
        } else {
            dismissAllowingStateLoss()
        }
    }

    private fun cancelWhenNotShowing() {
        val handler = Handler()
        handler.postDelayed({ dismissAllowingStateLoss() }, DELAY_MILLISECOND.toLong())
    }

    companion object {
        private val DELAY_MILLISECOND = 250
        private val SHOW_MIN_MILLISECOND = 150
        private val PROGRESS_SIZE_DP = 40
    }
}
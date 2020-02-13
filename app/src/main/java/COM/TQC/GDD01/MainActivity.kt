package COM.TQC.GDD01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val TAG = "HIPPO_DEBUG"
    private var tv: TextView? = null
    private var btn: Button? = null
    private var progressDialog: MyProgressDialog? = null

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (progressDialog != null)
                if (progressDialog!!.isShowing)
                    progressDialog!!.dismiss()
            when (msg.what) {
                HANDLER_MSG_OK -> if (msg.obj != null) {
                    tv!!.text = msg.obj.toString()
                }
            }
            super.handleMessage(msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        init()
    }

    private fun init()
    {
        // 初始化tv與btn
        tv = findViewById(R.id.main_textView1)
        btn = findViewById(R.id.main_button1)

        btn!!.setOnClickListener {
            tv!!.text = resources.getString(R.string.str_star_processing)
            // TO DO

        }
    }

    companion object {
        private val HANDLER_MSG_OK = 0x1001
    }
}

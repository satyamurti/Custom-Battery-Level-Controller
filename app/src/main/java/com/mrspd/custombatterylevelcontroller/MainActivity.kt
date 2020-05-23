package com.mrspd.custombatterylevelcontroller
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var textView: TextView? = null
    var seekBAr: SeekBar? = null
    var toggleButton: ToggleButton? = null
    var button1: Button? = null
    var button2: Button? = null
    var button3: Button? = null
    var i = 0
    var Level = 0
    var L = 0
    var textView1: TextView? = null
    var imageView: ImageView? = null
    private val b: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.registerReceiver(b, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        textView = findViewById<View>(R.id.textView) as TextView
        seekBAr = findViewById<View>(R.id.seekBar) as SeekBar
        toggleButton = findViewById<View>(R.id.toggleButton) as ToggleButton
        button1 = findViewById<View>(R.id.button) as Button
        button2 = findViewById<View>(R.id.button2) as Button
        button3 = findViewById<View>(R.id.button3) as Button
        imageView = findViewById<View>(R.id.imageView) as ImageView
        textView1 = findViewById<View>(R.id.textView3) as TextView
        seekBAr!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, P: Int, b: Boolean) {
                textView!!.text = "$P%"
                L = P
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                textView1!!.visibility = View.INVISIBLE
                toggleButton!!.visibility = View.VISIBLE
                button1!!.visibility = View.VISIBLE
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        try {
            unregisterReceiver(b)
        }
        catch (e:Exception){

        }

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(b)
    }

    fun ON(view: View?) {
        val `in` = Intent()
        `in`.action = Intent.ACTION_MAIN
        `in`.addCategory(Intent.CATEGORY_HOME)
        this.startActivity(`in`)
        i = 1
        Toast.makeText(this@MainActivity, "ALarm Will Start At " + textView!!.text + " %", Toast.LENGTH_SHORT).show()
        val notificationBuilder = NotificationCompat.Builder(this@MainActivity)
                .setSmallIcon(R.drawable.power)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.power))
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
        val notificationManager = NotificationManagerCompat.from(this@MainActivity)
        button1!!.visibility = View.INVISIBLE
        button2!!.visibility = View.VISIBLE
        val thread: Thread = object : Thread() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(500)
                        runOnUiThread {
                            if (i == 1 && Level >= L && !toggleButton!!.isChecked) {
                                notificationBuilder.setContentText("Battery Level Exceeding")
                                notificationManager.notify(1, notificationBuilder.build())
                                notificationBuilder.priority = Notification.PRIORITY_HIGH
                                textView!!.visibility = View.INVISIBLE
                                seekBAr!!.visibility = View.INVISIBLE
                                toggleButton!!.visibility = View.INVISIBLE
                                imageView!!.visibility = View.VISIBLE
                                button3!!.visibility = View.VISIBLE
                                button2!!.text = "Stop"
                                button2!!.visibility = View.VISIBLE
                                button2!!.setBackgroundColor(Color.parseColor("#00FF00"))
                            }
                            if (i == 1 && Level <= L && toggleButton!!.isChecked) {
                                notificationBuilder.setContentText("Battery Level Depleting")
                                notificationManager.notify(1, notificationBuilder.build())
                                notificationBuilder.priority = Notification.PRIORITY_HIGH
                                textView!!.visibility = View.INVISIBLE
                                seekBAr!!.visibility = View.INVISIBLE
                                toggleButton!!.visibility = View.INVISIBLE
                                imageView!!.visibility = View.VISIBLE
                                button3!!.visibility = View.VISIBLE
                                button2!!.text = "Stop"
                                button2!!.visibility = View.VISIBLE
                                button2!!.setBackgroundColor(Color.parseColor("#00FF00"))
                            }
                        }
                        sleep(10000)
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        thread.start()
    }

    fun OFF(view: View?) {
        i = 0
        button3!!.visibility = View.INVISIBLE
        button1!!.visibility = View.VISIBLE
        imageView!!.visibility = View.INVISIBLE
        textView!!.visibility = View.VISIBLE
        seekBAr!!.visibility = View.VISIBLE
        toggleButton!!.visibility = View.VISIBLE
        button2!!.text = "OFF"
        button2!!.setBackgroundColor(Color.parseColor("#00BCD4"))
        button2!!.visibility = View.INVISIBLE
        seekBAr!!.visibility = View.VISIBLE
        toggleButton!!.visibility = View.INVISIBLE
        button1!!.visibility = View.INVISIBLE
        textView1!!.visibility = View.VISIBLE
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun Exit(view: View?) {
        i = 0
        finish()
    }
}
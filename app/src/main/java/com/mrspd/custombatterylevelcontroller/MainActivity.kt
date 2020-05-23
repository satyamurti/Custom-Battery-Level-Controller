package com.mrspd.custombatterylevelcontroller

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.lang.Exception


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {


    lateinit var TV: TextView
    lateinit var S: SeekBar
    lateinit var TB: ToggleButton
    lateinit var B1: Button
    var B2: android.widget.Button? = null
    var B3: android.widget.Button? = null
    var i = 0
    var Level: Int = 0
    var L: Int = 0
    lateinit var T3: TextView
    var I: ImageView? = null

    private val b: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.registerReceiver(b, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        TV = findViewById<TextView>(R.id.textView)
        S = findViewById<SeekBar>(R.id.seekBar)
        TB = findViewById<ToggleButton>(R.id.toggleButton)
        B1 = findViewById<Button>(R.id.button)
        B2 = this.findViewById<View>(R.id.button2) as Button
        B3 = findViewById<View>(R.id.button3) as Button
        I = findViewById<ImageView>(R.id.imageView)
        T3 = findViewById<TextView>(R.id.textView3)
        S.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, P: Int, b: Boolean) {
                TV.text = "$P%"
                L = P
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                T3.visibility = View.INVISIBLE
                TB.visibility = View.VISIBLE
                B1.visibility = View.VISIBLE
            }
        })
    } //end of on create
    override fun onRestart() {
        super.onRestart()
        try {
            unregisterReceiver(b)
        }
        catch (e : Exception){
            d("GG","Exception Found")
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
        Toast.makeText(this, "ALarm Will Start At " + TV.text + " %", Toast.LENGTH_SHORT).show()
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.power)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.power))
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
        val notificationManager = NotificationManagerCompat.from(this)
        B1.visibility = View.INVISIBLE
        B2!!.visibility = View.VISIBLE
        val t: Thread = object : Thread() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(500)
                        runOnUiThread {
                            if (i == 1 && Level >= L && !TB.isChecked) {
                                notificationBuilder.setContentText("Battery Level Exceeding")
                                notificationManager.notify(1, notificationBuilder.build())
                                notificationBuilder.priority = Notification.PRIORITY_HIGH
                                TV.visibility = View.INVISIBLE
                                S.visibility = View.INVISIBLE
                                TB.visibility = View.INVISIBLE
                                I!!.visibility = View.VISIBLE
                                B3!!.visibility = View.VISIBLE
                                B2!!.text = "Stop"
                                B2!!.visibility = View.VISIBLE
                                B2!!.setBackgroundColor(Color.parseColor("#00FF00"))
                            }
                            if (i == 1 && Level <= L && TB.isChecked) {
                                notificationBuilder.setContentText("Battery Level Depleting")
                                notificationManager.notify(1, notificationBuilder.build())
                                notificationBuilder.priority = Notification.PRIORITY_HIGH
                                TV.visibility = View.INVISIBLE
                                S.visibility = View.INVISIBLE
                                TB.visibility = View.INVISIBLE
                                I!!.visibility = View.VISIBLE
                                B3!!.visibility = View.VISIBLE
                                B2!!.text = "Stop"
                                B2!!.visibility = View.VISIBLE
                                B2!!.setBackgroundColor(Color.parseColor("#00FF00"))
                            }
                        }
                        sleep(10000)
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        t.start()
    }


    fun OFF(view: View?) {
        i = 0
        B3!!.visibility = View.INVISIBLE
        B1.visibility = View.VISIBLE
        I!!.visibility = View.INVISIBLE
        TV.visibility = View.VISIBLE
        S.visibility = View.VISIBLE
        TB.visibility = View.VISIBLE
        B2!!.text = "OFF"
        B2!!.setBackgroundColor(Color.parseColor("#00BCD4"))
        B2!!.visibility = View.INVISIBLE
        S.visibility = View.VISIBLE
        TB.visibility = View.INVISIBLE
        B1.visibility = View.INVISIBLE
        T3.visibility = View.VISIBLE
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun Exit(view: View?) {
        i = 0
        finish()
    }
}
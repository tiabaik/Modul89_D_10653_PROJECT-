package com.tia_0653.ugd89_d_10653_project1

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // On below line we are initializing our proximity sensor variable
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        try {
            mCamera = Camera.open()
        }catch (e: Exception){
            Log.d("Error", "Failed to get Camera" + e.message)
        }

        // On below line we are checking if the proximity sensor is null
        if(proximitySensor == null){
            // On below line we are displaying a toast if no sensor is available
            Toast.makeText(this, "No proximity sensor found in device..", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            // On below line we are registering
            // Our sensor with sensor manager
            sensorManager.registerListener(
                proximitySensorEventLister,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        if(mCamera!=null){
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }

        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener{view: View? -> System.exit(0)}
    }

    var proximitySensorEventLister: SensorEventListener? = object: SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Method to check accuracy changed in sensor.
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event!!.sensor.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    mCamera?.release();
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)

                    if(mCamera!=null){
                        mCameraView = CameraView(this@MainActivity, mCamera!!)
                        val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                        camera_view.addView(mCameraView)
                    }
                }
            }
        }
    }
}
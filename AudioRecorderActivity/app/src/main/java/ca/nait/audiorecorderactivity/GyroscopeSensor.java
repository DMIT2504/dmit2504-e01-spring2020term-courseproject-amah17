package ca.nait.audiorecorderactivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GyroscopeSensor {
    public interface Listener{
        void onRotation(float rx, float ry, float rz);
    }

    private Listener mListener;
    public void setListener(Listener l){
        mListener = l;
    }

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;

    GyroscopeSensor (Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (mListener != null)
                {
                    mListener.onRotation(sensorEvent.values[0], sensorEvent.values[1],
                            sensorEvent.values[2]);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }
    public void register(){
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void unregister(){
        mSensorManager.unregisterListener(mSensorEventListener);
    }
}

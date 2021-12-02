package dk.au.mad21fall.appproject.group3.ui.notifications;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private ImageView compasspointer;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private Sensor sensoraccelerometer;
    private Sensor sensormagneticfield;
    private Context mContext;

    private float[] Gravity = new float[3];
    private float[] GeoMagnetic = new float[3];
    private float[] Orientation = new float[3];
    private float[] Rotation = new float[3];

    public NotificationsFragment(Context mContext) {
        this.mContext = mContext;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        compasspointer.findViewById(R.id.compasspointer);
        sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        locationManager = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);

        sensoraccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensormagneticfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        View root = binding.getRoot();

        SensorEventListener sensorEventListeneraccelerometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Gravity = sensorEvent.values;
                SensorManager.getRotationMatrix(Rotation,null,Gravity,GeoMagnetic);
                SensorManager.getOrientation(Rotation,Orientation);
                compasspointer.setRotation((float) (-Orientation[0]*180/3.14159));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        SensorEventListener sensorEventListenermagneticfield = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
            GeoMagnetic = sensorEvent.values;
            SensorManager.getRotationMatrix(Rotation,null,Gravity,GeoMagnetic);
            SensorManager.getOrientation(Rotation,Orientation);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(sensorEventListeneraccelerometer,sensoraccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenermagneticfield,sensormagneticfield,SensorManager.SENSOR_DELAY_NORMAL);

        //final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String direction)
            {
                //textView.setText(direction);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}
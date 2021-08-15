package com.zoomalert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import ch.milosz.reactnative.AndroidNative;

public class HepUstte extends Service {
    View mView;
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;
    private Boolean isDialogShowing = false;

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getBaseContext(), "onCreate start Service", Toast.LENGTH_LONG).show();

        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        ViewGroup valetModeWindow = (ViewGroup) View.inflate(this, R.layout.layout_hud, null);
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY :
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
        layoutParams.setTitle("Load Average");

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.layout_hud, null);
        mView.findViewById(R.id.button1).setOnTouchListener((v, event) -> {
            Log.d("TAG", "touch me");
            removeDialog();
            return true;
        });


        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            //Write whatever to want to do after delay specified (1 sec)
            Log.d("Handler", "Running Handler");
            showDialog();
        }, 30000);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void showDialog() {
        if (AndroidNative.AppConstants.isMeetingStarted && !isDialogShowing) {
            windowManager.addView(mView, layoutParams);
            isDialogShowing = true;
        } else {
            Toast.makeText(this, "Meeting not started or already dialog is showing", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeDialog() {
        if (isDialogShowing) {
            windowManager.removeViewImmediate(mView);
            synchronized (windowManager){
                windowManager.notify();
            }
            isDialogShowing = false;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removeDialog();
    }
}

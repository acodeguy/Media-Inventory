package com.reptilesoft.mediainventory;

import java.io.IOException;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ScanBarcode extends Activity 
	implements SurfaceHolder.Callback, OnTouchListener {

	/* variables */
	final static String TAG="ScanBarcode";
	SurfaceView sView;
	SurfaceHolder sHolder;
	Camera scanner;
	boolean mPreviewRunning = false;
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camerasurface);
		sView=(SurfaceView)findViewById(R.id.camerasurface);
		sHolder=sView.getHolder();
		sHolder.addCallback(this);
		sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		if (mPreviewRunning) {
			scanner.stopPreview();
		}
		
		Camera.Parameters params = scanner.getParameters();
		params.setPreviewSize(width, height);
		scanner.setParameters(params);
		
		try{
			scanner.setPreviewDisplay(holder);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		scanner.startPreview();
		mPreviewRunning=true;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		scanner=Camera.open();
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
		mPreviewRunning = false;
		scanner.release();
	}

	public boolean onTouch(View v, MotionEvent event) {
		
		int action=event.getAction();
		
		if(action==MotionEvent.ACTION_DOWN){
				Toast.makeText(this, "down", Toast.LENGTH_SHORT).show();
			}
		return false;
	}
}

package gui.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import capture.image.R;

public class ScanLEDCode extends Activity implements SurfaceHolder.Callback {

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Camera camera;
	private LayoutInflater inflater;
	private Button btnTakePicture;
	private byte[] tempdata;
	private boolean mPreviewRunning = false;

	public void onCreate(Bundle scanLedCode) {
		super.onCreate(scanLedCode);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// Sets full screen camera and no title bar.
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.camera);

		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();

		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		inflater = LayoutInflater.from(this);

		View overView = inflater.inflate(R.layout.camera, null);
		this.addContentView(overView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
		btnTakePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				camera.takePicture(mShutterCallback, mPictureCallback, mjpeg);
			}
		});
	}

	// Sets the shutter for the camera - this is a constant parameter for the
	// hardware to use
	ShutterCallback mShutterCallback = new ShutterCallback() {
		@Override
		public void onShutter() {
		}
	};

	// Sets the Picture for the camera - this is a constant parameter for the
	// hardware to use
	PictureCallback mPictureCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
		}
	};

	// This is the jpeg file type
	PictureCallback mjpeg = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if (data != null) {
				tempdata = data;
				done();
			}
		}
	};

	/**
	 * Rap-up the jit! This is for when the picture is needs to be save or
	 * something like that. We use this for the image processing - probably :)
	 */
	private void done() {
		Bitmap bm = BitmapFactory.decodeByteArray(tempdata, 0, tempdata.length);
		String url = Images.Media.insertImage(getContentResolver(), bm, null,
				null);

		bm.recycle();
		Bundle bundle = new Bundle();
		if (url != null) {
			bundle.putString("url", url);
		} else {
			Toast.makeText(this, "picture cannot be saved", Toast.LENGTH_LONG)
					.show();
		}
		finish();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try {
			if (mPreviewRunning) {
				camera.stopPreview();
				mPreviewRunning = false;
			}
			
			Camera.Parameters p = camera.getParameters();
			camera.setParameters(p);
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			mPreviewRunning = true;
		} catch (Exception e) {
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		mPreviewRunning = false;
		camera.release();
		camera = null;
	}
}
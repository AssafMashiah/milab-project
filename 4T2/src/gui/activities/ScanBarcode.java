package gui.activities;

import java.io.IOException;
import java.util.List;

import capture.image.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ScanBarcode extends Activity implements SurfaceHolder.Callback {
	private static final int CROP_X = 180;
	private static final int CROP_Y = 200;
	private static final int CROP_WIDTH = 380;
	private static final int CROP_HEIGHT = 50;
	public static String CALCULATED_NUMBER = "ImgProcessing";
	public int calculated_number;

	private Camera camera;
	private boolean isPreviewRunning = false;

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Uri target = Media.EXTERNAL_CONTENT_URI;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.e(getClass().getSimpleName(), "onCreate");
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.camera);

		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();

		LinearLayout circlesView = (LinearLayout) findViewById(R.id.circleView);
		circlesView.addView(new CircleView(this));

		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		ImageButton btnTakePicture = (ImageButton) findViewById(R.id.btnTakePicture);
		btnTakePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnTakePicture_click();
			}
		});
	}

	protected void btnTakePicture_click() {
		debug_SavePicture = true;
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuItem item = menu.add(0, 0, 0, "goto gallery");
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(Intent.ACTION_VIEW, target);
				startActivity(intent);
				return true;
			}
		});
		return true;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	Camera.PictureCallback mPictureCallbackRaw = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera c) {
			Log.e(getClass().getSimpleName(), "PICTURE CALLBACK RAW: " + data);
			camera.startPreview();
		}
	};

	Camera.PictureCallback mPictureCallbackJpeg = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera c) {
			Log.e(getClass().getSimpleName(),
					"PICTURE CALLBACK JPEG: data.length = " + data);
		}
	};

	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			Log.e(getClass().getSimpleName(), "SHUTTER CALLBACK");
		}
	};

	boolean debug_SavePicture = false;
	Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			try {
				if (debug_SavePicture) {
					camera.setPreviewCallback(null);

					Size previewSize = camera.getParameters().getPreviewSize();
					int[] rgb = new int[previewSize.width * previewSize.height];
					decodeYUV420SP(rgb, data, previewSize.width,
							previewSize.height);

					int[] croppedImage = cropRgbImage(rgb, previewSize.width,
							previewSize.height);

						        Intent browseIntent = new Intent(Intent.ACTION_VIEW);						        
						        String url = "https://s3.amazonaws.com/milab-bucket/56.jpg";
						        browseIntent.setData(Uri.parse(url));
						        startActivity(browseIntent);
					// Bitmap bitmap = Bitmap.createBitmap(croppedImage,
					// CROP_WIDTH, CROP_HEIGHT, Config.RGB_565);

					int photoNum;

					switch (calculated_number) {
					case 1:
						photoNum = 1;
						break;
					case 2:
						photoNum = 2;
						break;
					case 3:
						photoNum = 3;
						break;
					case 4:
						photoNum = 4;
						break;
					case 5:
						photoNum = 5;
						break;
					default:
						photoNum = calculated_number;
						break;
					}

					getIntent().putExtra(CALCULATED_NUMBER, photoNum);

					debug_SavePicture = false;

					setResult(RESULT_OK, getIntent());
					finish();
				}
			} catch (Exception e) {
				Log.e(getClass().getSimpleName(),
						"Exception : " + e.getMessage());
			}
		}
	};

	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(getClass().getSimpleName(), "surfaceCreated");
		camera = Camera.open();

		int rotation = this.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 90;
			break;
		case Surface.ROTATION_90:
			degrees = 0;
			break;
		case Surface.ROTATION_180:
			degrees = 270;
			break;
		case Surface.ROTATION_270:
			degrees = 180;
			break;
		}

		camera.setDisplayOrientation(degrees);

		Camera.Parameters p = camera.getParameters();
		p.set("jpeg-quality", 100);
		p.setPictureFormat(PixelFormat.JPEG);
		camera.setParameters(p);

		camera.setPreviewCallback(previewCallback);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.e(getClass().getSimpleName(), "surfaceChanged");
		if (isPreviewRunning) {
			camera.stopPreview();
		}
		Camera.Parameters p = camera.getParameters();

		List<Size> sizes = p.getSupportedPreviewSizes();
		Size optimalSize = getOptimalPreviewSize(sizes, w, h);
		p.setPreviewSize(optimalSize.width, optimalSize.height);
		p.setExposureCompensation(p.getMinExposureCompensation());

		camera.setParameters(p);
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}

		camera.startPreview();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(getClass().getSimpleName(), "surfaceDestroyed");
		camera.stopPreview();
		isPreviewRunning = false;
		camera.release();
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.2;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the
		// requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width,
			int height) {
		final int frameSize = width * height;

		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)
					r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0)
					g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0)
					b = 0;
				else if (b > 262143)
					b = 262143;

				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
						| ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}
	}

	private int[] cropRgbImage(int[] rgb, int originalWidth, int originalHeight) {
		int[][] image = new int[CROP_WIDTH][CROP_HEIGHT];

		for (int col = CROP_X; col < CROP_X + CROP_WIDTH; col++) {
			for (int row = CROP_Y; row < CROP_Y + CROP_HEIGHT; row++) {
				image[col - CROP_X][row - CROP_Y] = rgb[(row * originalWidth)
						+ col];
			}
		}

		// Get the number from the picture
		calculated_number = android.Utils.Utils.scanToNumber(image);

		int[] resImage = new int[CROP_WIDTH * CROP_HEIGHT];

		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				resImage[j * CROP_WIDTH + i] = image[i][j];
			}
		}
		
		
//		Toast.makeText(this, calculated_number + "", Toast.LENGTH_LONG).show();

		return resImage;
	}

}

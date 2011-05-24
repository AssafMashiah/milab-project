package activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import capture.image.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
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
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class ScanBarcode extends Activity implements SurfaceHolder.Callback
{
	private static final int CROP_X = 80;
	private static final int CROP_Y = 70;
	private static final int CROP_WIDTH = 200;
	private static final int CROP_HEIGHT = 110;
	public static final String BUNDLE_EXTRA_BITMAP = "Bitmap";

	private Toast toast;
	private Camera camera;
	private boolean isPreviewRunning = false;
	private SimpleDateFormat timeStampFormat = new SimpleDateFormat(
			"yyyyMMddHHmmssSS");

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Uri target = Media.EXTERNAL_CONTENT_URI;

	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		Log.e(getClass().getSimpleName(), "onCreate");
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		setContentView(R.layout.camera);

		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceHolder = surfaceView.getHolder();

		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		ImageButton btnTakePicture = (ImageButton) findViewById(R.id.btnTakePicture);
		btnTakePicture.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				btnTakePicture_click();
			}
		});
	}

	protected void btnTakePicture_click()
	{
		debug_SavePicture = true;
		// ImageCaptureCallback iccb = null;
		// try
		// {
		// String filename = timeStampFormat.format(new Date());
		// ContentValues values = new ContentValues();
		// values.put(Media.TITLE, filename);
		// values.put(Media.DESCRIPTION, "Image capture by camera");
		// Uri uri = getContentResolver().insert(
		// Media.EXTERNAL_CONTENT_URI, values);
		// // String filename = timeStampFormat.format(new Date());
		// iccb = new ImageCaptureCallback(getContentResolver()
		// .openOutputStream(uri));
		// } catch (Exception ex)
		// {
		// ex.printStackTrace();
		// Log.e(getClass().getSimpleName(), ex.getMessage(), ex);
		// }
		//
		// camera.takePicture(mShutterCallback, mPictureCallbackRaw, iccb);

	}

	public boolean onCreateOptionsMenu(android.view.Menu menu)
	{
		MenuItem item = menu.add(0, 0, 0, "goto gallery");
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
		{
			public boolean onMenuItemClick(MenuItem item)
			{
				Intent intent = new Intent(Intent.ACTION_VIEW, target);
				startActivity(intent);
				return true;
			}
		});
		return true;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}

	Camera.PictureCallback mPictureCallbackRaw = new Camera.PictureCallback()
	{
		public void onPictureTaken(byte[] data, Camera c)
		{
			Log.e(getClass().getSimpleName(), "PICTURE CALLBACK RAW: " + data);
			camera.startPreview();
		}
	};

	Camera.PictureCallback mPictureCallbackJpeg = new Camera.PictureCallback()
	{
		public void onPictureTaken(byte[] data, Camera c)
		{
			Log.e(getClass().getSimpleName(),
					"PICTURE CALLBACK JPEG: data.length = " + data);
		}
	};

	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback()
	{
		public void onShutter()
		{
			Log.e(getClass().getSimpleName(), "SHUTTER CALLBACK");
		}
	};

	boolean debug_SavePicture = false;
	Camera.PreviewCallback previewCallback = new Camera.PreviewCallback()
	{
		public void onPreviewFrame(byte[] data, Camera camera)
		{
			try
			{
				if (debug_SavePicture)
				{
					camera.setPreviewCallback(null);

					Size previewSize = camera.getParameters().getPreviewSize();
					int[] rgb = new int[previewSize.width * previewSize.height];
					decodeYUV420SP(rgb, data, previewSize.width,
							previewSize.height);

					int[] croppedImage = cropRgbImage(rgb, previewSize.width, previewSize.height);

					Bitmap bitmap = Bitmap.createBitmap(croppedImage, CROP_WIDTH, CROP_HEIGHT, Config.RGB_565);
					
					// Bitmap bitmap = Bitmap.createBitmap(rgb,
					// previewSize.width, previewSize.height,
					// Bitmap.Config.RGB_565);

					getIntent().putExtra(BUNDLE_EXTRA_BITMAP, bitmap);

					// ---- Start Image Processing Here ---- //

					// // This saves the entire RGB image data to a JPEG file
					// FileOutputStream baos = new
					// FileOutputStream("/mnt/sdcard/code.jpg");
					// Bitmap bitmap = Bitmap.createBitmap(rgb,
					// previewSize.width, previewSize.height,
					// Bitmap.Config.RGB_565);
					// bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);

					// // This saves part of the image to file using a rectangle
					// YuvImage yuvimage=new YuvImage(data, ImageFormat.NV21,
					// previewSize.width, previewSize.height, null);
					// ByteArrayOutputStream baos = new ByteArrayOutputStream();
					// yuvimage.compressToJpeg(new Rect(previewSize.width / 3,
					// previewSize.height / 3, 2 * previewSize.width / 3, 2 *
					// previewSize.height / 3), 80, baos);

					debug_SavePicture = false;

					setResult(RESULT_OK, getIntent());
					finish();
				}
			} catch (Exception e)
			{
				Log.e(getClass().getSimpleName(),
						"Exception : " + e.getMessage());
			}
		}
	};

	protected void onResume()
	{
		Log.e(getClass().getSimpleName(), "onResume");
		super.onResume();
	}

	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}

	protected void onStop()
	{
		Log.e(getClass().getSimpleName(), "onStop");
		super.onStop();
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		Log.e(getClass().getSimpleName(), "surfaceCreated");
		camera = Camera.open();

		int rotation = this.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation)
		{
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

		// if (this.getResources().getConfiguration().orientation !=
		// Configuration.ORIENTATION_LANDSCAPE)
		// {
		// camera.setDisplayOrientation(90);
		// }
		// else
		// {
		// camera.setDisplayOrientation(0);
		// }

		Camera.Parameters p = camera.getParameters();
		p.set("jpeg-quality", 100);
		// p.set("orientation", "landscape");
		// p.set("rotation", 90);
		p.setPictureFormat(PixelFormat.JPEG);
		camera.setParameters(p);

		camera.setPreviewCallback(previewCallback);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
	{
		Log.e(getClass().getSimpleName(), "surfaceChanged");
		if (isPreviewRunning)
		{
			camera.stopPreview();
			// isPreviewRunning = false;
		}
		Camera.Parameters p = camera.getParameters();
		// p.set("rotation", 90);

		List<Size> sizes = p.getSupportedPreviewSizes();
		Size optimalSize = getOptimalPreviewSize(sizes, w, h);
		Log.d(getClass().getSimpleName(), "Surface size is " + w + "w " + h
				+ "h");
		Log.d(getClass().getSimpleName(), "Optimal size is "
				+ optimalSize.width + "w " + optimalSize.height + "h");
		p.setPreviewSize(optimalSize.width, optimalSize.height);

		// p.setPreviewSize(w, h);

		camera.setParameters(p);
		try
		{
			camera.setPreviewDisplay(holder);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		// if (!isPreviewRunning)
		{
			camera.startPreview();
			// isPreviewRunning = true;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.e(getClass().getSimpleName(), "surfaceDestroyed");
		camera.stopPreview();
		isPreviewRunning = false;
		camera.release();
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h)
	{
		final double ASPECT_TOLERANCE = 0.2;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes)
		{
			Log.d(getClass().getSimpleName(), "Checking size " + size.width
					+ "w " + size.height + "h");
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff)
			{
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the
		// requirement
		if (optimalSize == null)
		{
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes)
			{
				if (Math.abs(size.height - targetHeight) < minDiff)
				{
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width,
			int height)
	{
		final int frameSize = width * height;

		for (int j = 0, yp = 0; j < height; j++)
		{
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++)
			{
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0)
				{
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

	private int[] cropRgbImage(int[] rgb, int originalWidth, int originalHeight)
	{
		int[][] image = new int[CROP_WIDTH][CROP_HEIGHT];

		for (int col = CROP_X; col < CROP_X + CROP_WIDTH; col++)
		{
			for (int row = CROP_Y; row < CROP_Y + CROP_HEIGHT; row++)
			{
				image[col - CROP_X][row - CROP_Y] = rgb[(row * originalWidth)
						+ col];
			}
		}

		// Get the number from the picture
		scanToNumber(image);

		int[] resImage = new int[CROP_WIDTH * CROP_HEIGHT];

		for (int i = 0; i < image.length; i++)
		{
			for (int j = 0; j < image[0].length; j++)
			{
				resImage[j * CROP_WIDTH + i] = image[i][j];
			}
		}

		return resImage;
	}

	private void scanToNumber(int[][] image)
	{
		float areaWidth = image.length / 8;

		double[][] grayImage = new double[image.length][image[0].length];
		// gray scale
		for (int i = 0; i < grayImage.length; i++)
		{
			for (int j = 0; j < grayImage[0].length; j++)
			{
				try
				{
					int x = image[i][j];
					int red = Color.red(x);
					int green = Color.green(x);
					int blue = Color.blue(x);
					grayImage[i][j] = (red + green + blue) / 3;
				}
				catch (Exception e)
				{
					Toast toast = Toast.makeText(this, "i=" + i + "j=" + j, Toast.LENGTH_LONG);
				}		
			}
		}

		// Resulted numbers
		double[] finalArray = new double[8];
		int sizeDiv3 = grayImage[0].length / 3;

		int blocker = sizeDiv3;
		for (int ledNumber = 0; ledNumber < 8; ledNumber++)
		{
			int counter = 0;
			for (int i = (int) (ledNumber * areaWidth); i < ledNumber
					* areaWidth + areaWidth; i++)
			{
				for (int j = sizeDiv3; j < blocker * 2; j++)
				{
					finalArray[ledNumber] += grayImage[i][j];
					counter++;
				}
			}
			finalArray[ledNumber] = finalArray[ledNumber] / counter;
		}

		int result = 0;

		for (int i = 0; i < finalArray.length; i++)
		{
			if (finalArray[i] > 0 && finalArray[i] <= 1)
			{
				finalArray[i] = 0;
			}

			if (finalArray[i] > 1 && finalArray[i] <= 30)
			{
				finalArray[i] = 1;
			}

			if (finalArray[i] > 30)
			{
				finalArray[i] = 2;
			}
			result += finalArray[i] * Math.pow(3, i);
		}

//		System.out.println("The image result: " + result);
		Log.d("ANDRO_CAMERA", "The image resault: " + result);
		toast = Toast.makeText(this, "The image resault: " + result, Toast.LENGTH_LONG);
	}
}

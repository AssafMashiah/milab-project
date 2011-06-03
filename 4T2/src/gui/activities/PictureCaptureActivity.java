package gui.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Random;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.amazon.aws.demo.s3.S3;

public class PictureCaptureActivity extends Activity
{
	private static Hashtable<Integer, Integer> ImagesAndBarcodes = new Hashtable<Integer, Integer>();
	public static final String FILE_LAST_DIR = "/MediaLab/";
	public static String FILE_DIR = "";
	public static String FILE_NAME = "";
	public static String FILE_PATH = "";
	public static final int MAX_RANDOM_NAME = 63; 
	public static final int MIN_RANDOM_NAME = 0; 
	public static int PHOTO_NUMBER = -1;
	private static final int IMAGE_CAPTURE = 0;
	private Uri imageUri;

	/**
	 * Called when the activity is first created. sets the content and gets the
	 * references to the basic widgets on the screen like {@code Button} or
	 * {@link ImageView}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		startCamera();
	}

	public void startCamera()
	{
		Log.d("ANDRO_CAMERA", "Starting camera on the phone...");
		
		//randomize filename
		Random random = new Random();
		
		// ----Randomize photo number name---- 
		PHOTO_NUMBER = random.nextInt(MAX_RANDOM_NAME - MIN_RANDOM_NAME) + MIN_RANDOM_NAME;
		FILE_NAME = PHOTO_NUMBER + ".jpg";
		FILE_DIR = Environment.getExternalStorageDirectory() + FILE_LAST_DIR;
		FILE_PATH = FILE_DIR + FILE_NAME;
		
		File imageFile = new File(FILE_PATH);
		
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, FILE_PATH);
		values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		imageUri = Uri.fromFile(imageFile);
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		
		startActivityForResult(intent, IMAGE_CAPTURE);
		
		
		
		
		
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == IMAGE_CAPTURE)
		{
			if (resultCode == RESULT_OK)
			{
				setResult(RESULT_OK, data);
				finish();
				
			}
		}
	}

	  
	
	public static Hashtable<Integer, Integer> getHashtable()
	{
		return ImagesAndBarcodes;
	}
}

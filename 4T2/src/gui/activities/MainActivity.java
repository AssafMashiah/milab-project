package gui.activities;

//import picasa.api.PicasaWeb;
//import com.google.gdata.client.photos.PicasawebService;
import capture.image.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
//import arduino.bluetooth.Comm;

public class MainActivity extends Activity
{
	//*************Picture Camera members*********************
	
	public static final int UPLOAD_SEEN_CODE_REQUEST = 5;
	private static final int AFTER_SCAN_REQUEST = 20;
	private static final int PICTURE_TAKEN_REQUEST = 10;
    
	// *************BlueTooth Members*********************
	
	
	/*
	 * TODO: change the address to the address of your Bluetooth module and
	 * ensure your device is added to Amarino
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// On "create code" click ( "pic to code" button)
		ImageButton btnCreateCode = (ImageButton) findViewById(R.id.btnPicToCode);
		btnCreateCode.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				btnCreateCodeClick();
			}
		});

		// On "scan a code" click ( "scan code" button )
		ImageButton btnScanCode = (ImageButton) findViewById(R.id.btnScanCode);
		btnScanCode.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
		       btnScanCodeClick();
			}
		});
		
	}
	
	// On "create code" click ( "pic to code" button)
	protected void btnCreateCodeClick()
	{
		Intent intent = new Intent(MainActivity.this, CameraOrSurfActivity.class);
		startActivityForResult(intent, PICTURE_TAKEN_REQUEST);
	}

	// On "scan a code" click ( "scan code" button )
	private void btnScanCodeClick()
	{
		Intent intent = new Intent(MainActivity.this, ScanBarcode.class);
		startActivityForResult(intent, AFTER_SCAN_REQUEST);	
	}
		
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// Get the picture from the internal memory, save it into Bitmap
//		this.m_Bitmap = getBitmapFromImageFile(this);

		// Show the bitmap on the screen
//		ImageView imageView = (ImageView) this.findViewById(R.id.imageView);
//		imageView.setImageBitmap(this.m_Bitmap);
//		
//		//[DEMO ONLY CODE] - DELETE LATER!!! *************************************
//		ImageButton logoImg = (ImageButton) this.findViewById(R.id.LogoImg);
//		logoImg.setImageBitmap(this.m_Bitmap);
		
		if (requestCode == PICTURE_TAKEN_REQUEST) 
		{
		      if (resultCode == RESULT_OK) 
		      {
		    	  	Intent myIntent = new Intent(this, SelectColorAndUploadActivity.class);
					startActivityForResult(myIntent, 4);
		      }
		   }

		if (requestCode == AFTER_SCAN_REQUEST) 
		{
		      if (resultCode == RESULT_OK) 
		      {
					Intent afterScanintent =  new Intent(MainActivity.this, AfterScanActivity.class);
					afterScanintent.putExtra(ScanBarcode.CALCULATED_NUMBER,
							data.getExtras().getInt(ScanBarcode.CALCULATED_NUMBER));
					startActivityForResult(afterScanintent, 2);
		      }
		}
		
		if (requestCode == UPLOAD_SEEN_CODE_REQUEST) 
		{
		      if (resultCode == RESULT_OK) 
		      {
					Intent afterScanintent =  new Intent(MainActivity.this, SelectColorAndUploadActivity.class);
					startActivityForResult(afterScanintent, 3);
		      }
		}
		//END OF ------> [DEMO ONLY CODE] - DELETE LATER!!! *************************************
		
		// convert Bitmap to a bytes array
//		byte[] bitmapInBytes = convertBitmapToBytesArray(this.m_Bitmap);

		
		// Sent bytes array through bluetooth
//		sendImageInBytesToArduino(bitmapInBytes);
//		
	}
//
//	private Bitmap getBitmapFromImageFile(Context context)
//	{
//		String path = Environment.getExternalStorageDirectory() + FILE_PATH;
//		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();  
//        bitmapOptions.inSampleSize = 2; //64 
//		Bitmap bMap = BitmapFactory.decodeFile(path, bitmapOptions);
//		return bMap;
//	}

	private byte[] convertBitmapToBytesArray(Bitmap bitmap)
	{
		int rowsNum = bitmap.getHeight();
		int colsNum = bitmap.getWidth();
		byte[] bytesBuffer = new byte[rowsNum * colsNum * 3]; // keeps the data of each pixel color
		int pixelColor;
		int i = 0;

		for (int x = 0; x < rowsNum; x++)
		{
			for (int y = 0; y < colsNum; y++)
			{
				pixelColor = bitmap.getPixel(y, x);

				bytesBuffer[i++] = (byte) (pixelColor & 0x000000FF); // B // returns least significant byte 24 zeros + 8 ones
				bytesBuffer[i++] = (byte) ((pixelColor >>> 8) & 0x000000FF); // G // returns least significant byte 24 zeros + 8 ones
				bytesBuffer[i++] = (byte) ((pixelColor >>> 16) & 0x000000FF); // R // returns least significant byte 24 zeros + 8 ones
			}
		}
		return bytesBuffer;
	}

	private void sendImageInBytesToArduino(byte[] bitmapInBytes)
	{
//		int color = 100;
//		Comm comm = new Comm(this);
//		comm.connect(Comm.DEVICE_ADDRESS);
//		comm.sendData(color);
//		comm.closeComm();
		
		// I have chosen random small letters for the flag 'o' for red, 'p' for green and 'q' for blue
		// you could select any small letter you want however be sure to match the character you register a function for
		// your in Arduino sketch
		
//		switch (bitmapInBytes
//		{
//		}
//		blue * 0x10000 + green * 0x100 + red 
//		0xABCDEF
	}

}
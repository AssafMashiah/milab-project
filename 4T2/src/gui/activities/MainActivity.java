package gui.activities;

import capture.image.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import at.abraxas.amarino.Amarino;

public class MainActivity extends Activity
{
	//*************Picture Camera members*********************
	public static final int UPLOAD_SEEN_CODE_REQUEST = 5;
	private static final int AFTER_SCAN_REQUEST = 20;
	private static final int PICTURE_TAKEN_REQUEST = 10;
	private static final String BLUETOOTH = "00:06:66:06:BE:89";
    public static final String PREFS = "multicolorlamp";
    public static final String PREF_DEVICE_ADDRESS = "device_address";
	String deviceAddress;
    SharedPreferences prefs;
    
	/**
	 * TODO: change the address to the address of your Bluetooth module and
	 * ensure your device is added to Amarino
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        deviceAddress = prefs.getString(PREF_DEVICE_ADDRESS, BLUETOOTH);


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
	@Override
	protected void onStart() {
        super.onStart();
        Amarino.connect(this, deviceAddress);
	Amarino.sendDataToArduino(this,deviceAddress,'g',35350);
	}
	
	@Override
	protected void onStop() {

	Amarino.disconnect(this, BLUETOOTH);
	super.onStop();
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
	}
}
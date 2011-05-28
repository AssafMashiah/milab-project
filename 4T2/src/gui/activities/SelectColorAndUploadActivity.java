package gui.activities;

import capture.image.R;
import gui.activities.ColorPickerDialog.ColorPickerView;
import gui.activities.ColorPickerDialog.OnColorChangedListener;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectColorAndUploadActivity extends Activity implements ColorPickerDialog.OnColorChangedListener
{
	private static final String BRIGHTNESS_PREFERENCE_KEY = "brightness";
	private static final String COLOR_PREFERENCE_KEY = "color";
	TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		
		OnColorChangedListener l = new OnColorChangedListener()
		{
			@Override
			public void colorChanged(String key, int color)
			{
				
			}
		};
		
		setContentView(R.layout.color_and_upload);
		
		LinearLayout colorDialog = (LinearLayout) findViewById(R.id.colorDialogLayout);
		colorDialog.addView(new ColorPickerView(SelectColorAndUploadActivity.this, l, 0, 0));
		
		ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				setResult(RESULT_OK, null);
				finish();
			}
		});

		ImageButton btnUpload = (ImageButton) findViewById(R.id.btnUpload);
		btnUpload.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// add color - amerino code
//				long dataToSend = PictureCaptureActivity.PHOTO_NUMBER;
				setResult(RESULT_OK, null);
				finish();
			}
		});
	}
	
	public void colorChanged(String key, int color)
	{
		PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(COLOR_PREFERENCE_KEY, color).commit();
		tv.setTextColor(color);
	}

}

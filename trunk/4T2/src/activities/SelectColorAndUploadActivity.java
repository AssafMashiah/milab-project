package activities;

import capture.image.R;
import activities.ColorPickerDialog.ColorPickerView;
import activities.ColorPickerDialog.OnColorChangedListener;
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
//		setContentView(new ColorPickerView(SelectColorAndUploadActivity.this, l, 0, 0));
		setContentView(R.layout.color_and_upload);
		
		LinearLayout colorDialog = (LinearLayout) findViewById(R.id.colorDialogLayout);
		colorDialog.addView(new ColorPickerView(SelectColorAndUploadActivity.this, l, 0, 0));
		
//		tv = (TextView) findViewById(R.id.LblTitle);
//		int color = PreferenceManager.getDefaultSharedPreferences(SelectColorAndUploadActivity.this)
//											.getInt(COLOR_PREFERENCE_KEY, Color.WHITE);
//		
		ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				setResult(RESULT_OK, null);
				finish();
			}
		});

//		ColorPickerDialog d = new ColorPickerDialog(SelectColorAndUploadActivity.this, SelectColorAndUploadActivity.this, "", color, color);
//		d.show();
		
	}
	
	public void colorChanged(String key, int color)
	{
		PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(COLOR_PREFERENCE_KEY, color).commit();
		tv.setTextColor(color);
	}

}

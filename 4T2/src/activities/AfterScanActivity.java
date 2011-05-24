package activities;

import capture.image.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AfterScanActivity extends Activity
{
	public static final String BUNDLE_EXTRA_BITMAP = "Bitmap";
	
	private Bitmap m_Bitmap;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.after_scan);
		
		m_Bitmap = getIntent().getExtras().getParcelable(BUNDLE_EXTRA_BITMAP);
		
		RelativeLayout afterScanLayout = (RelativeLayout)findViewById(R.id.afterScanLayout);
//		afterScanLayout.setBackgroundResource(R.drawable.cameralayer);
		
		// Take a picture with the camera, save it to internal memory
		ImageView imageView = (ImageView) this.findViewById(R.id.imageView);
		imageView.setImageBitmap(this.m_Bitmap);
//		LayoutParams layoutParams = new LayoutParams(Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL);
//		imageView.setLayoutParams(layoutParams);
		
		ImageButton btnUpload = (ImageButton) findViewById(R.id.btnUpload1);
		btnUpload.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(AfterScanActivity.this, SelectColorAndUploadActivity.class);
				startActivityForResult(intent, MainActivity.UPLOAD_SEEN_CODE_REQUEST);
			}
		});
		
		ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack1);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(AfterScanActivity.this, MainActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}
}

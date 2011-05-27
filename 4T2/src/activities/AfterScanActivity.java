package activities;

import capture.image.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AfterScanActivity extends Activity
{
	public static final String BUNDLE_EXTRA_BITMAP = "Bitmap";
	
	private Bitmap m_Bitmap;
	private int imgProcFileName = -1;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.after_scan);
		
		imgProcFileName = getIntent().getExtras().getInt(ScanBarcode.CALCULATED_NUMBER);
		
//		PictureCaptureActivity.getHashtable().put(PictureCaptureActivity.PHOTO_NUMBER, imgProcFileName);

		m_Bitmap = BitmapFactory.decodeFile(PictureCaptureActivity.FILE_DIR + PictureCaptureActivity.PHOTO_NUMBER + ".jpg");
		
//		m_Bitmap = getIntent().getExtras().getParcelable(BUNDLE_EXTRA_BITMAP);
		
//		RelativeLayout afterScanLayout = (RelativeLayout)findViewById(R.id.afterScanLayout);
//		afterScanLayout.setBackgroundResource(R.drawable.cameralayer);
		
		// Take a picture with the camera, save it to internal memory
		ImageView imageView = (ImageView) this.findViewById(R.id.imageView);
		imageView.setImageBitmap(this.m_Bitmap);
//		imageView.setBackgroundResource(R.drawable.medstock);
		
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

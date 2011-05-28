package gui.activities;

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

	private static final int ASSAF_PHOTO_ID = 51;
	private static final int KEREN_PHOTO_ID = 2;
	private static final int ORR_PHOTO_ID = 3;
	private static final int RONI_PHOTO_ID = 4;
	private static final int SHACHR_PHOTO_ID = 5;
	
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
		
		int resourceId = R.drawable.logo;
		
		switch (imgProcFileName)
		{
			case ASSAF_PHOTO_ID:
				resourceId = R.drawable.assaf;
				break;
			case KEREN_PHOTO_ID:
				resourceId = R.drawable.keren;
				break;
			case ORR_PHOTO_ID:
				resourceId = R.drawable.orr;
				break;
			case RONI_PHOTO_ID:
				resourceId = R.drawable.roni;
				break;
			case SHACHR_PHOTO_ID:
				resourceId = R.drawable.shachar;
				break;
			default:
				resourceId = R.drawable.logo;
				m_Bitmap = BitmapFactory.decodeFile(PictureCaptureActivity.FILE_DIR + PictureCaptureActivity.PHOTO_NUMBER + ".jpg");
				break;
		}
		
		m_Bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
		
		// Take a picture with the camera, save it to internal memory
		ImageView imageView = (ImageView) this.findViewById(R.id.imageView);
		imageView.setImageBitmap(this.m_Bitmap);
		
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

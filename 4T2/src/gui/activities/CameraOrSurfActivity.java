package gui.activities;

import capture.image.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CameraOrSurfActivity extends Activity {
	// *************Picture Camera members*********************
	public static final int UPLOAD_SEEN_CODE_REQUEST = 5;
	private static final int PICTURE_TAKEN_REQUEST = 10;

	public void onCreate(Bundle savedInstanceState) {

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_pic_or_surf);

		// On "create code" click ( "pic to code" button)
		ImageButton btnTakeAPic = (ImageButton) findViewById(R.id.btntakepicture);
		btnTakeAPic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(CameraOrSurfActivity.this,
						PictureCaptureActivity.class);
				startActivityForResult(intent, PICTURE_TAKEN_REQUEST);
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICTURE_TAKEN_REQUEST) {
			if (resultCode == RESULT_OK) {
				Intent myIntent = new Intent(this,
						SelectColorAndUploadActivity.class);
				startActivityForResult(myIntent, 4);
			}
		}
	}
}

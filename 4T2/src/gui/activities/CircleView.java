package gui.activities;

import capture.image.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;

public class CircleView extends View
{
	private static final float RADIUS = 15;
	private static final float STARTING_CIRCLE_LINE_X = 135;
	private static float STARTING_CIRCLE_LINE_Y = 150;
	private Paint mPaint;

	// Constructor
	public CircleView(Context c)
	{
		super(c);

		ImageView cameraLayerImg = (ImageView) ((Activity)c).findViewById(R.id.cameraLayer);
//		cameraLayerImg.layout(l, t, r, b)
	
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setTextSize(12);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// drawing the leds on the
		int inc = 44;

		for (int j = 0; j < 6; j++)
		{
			mPaint.setColor(Color.WHITE);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(1.5f);
			canvas.drawCircle(STARTING_CIRCLE_LINE_X + (j * inc),
					STARTING_CIRCLE_LINE_Y, RADIUS, mPaint);
		}

	}
}

package activities;

import capture.image.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class CirclesView extends Dialog
{
	private ImageView imageView;
	
	public CirclesView(Context context)
	{
		super(context);
		
		imageView = (ImageView)findViewById(R.id.circleView);
//		IMAGE_HEIGHT = imageView.
//		IMAGE_WIDTH = imagesView.
	}
	
	public static class circlesView extends View
	{
		private static final float RADIUS = 10;
		private static final float STARTING_CIRCLE_LINE_X = 45;
		private static float STARTING_CIRCLE_LINE_Y;
		private int imgW;
		private int imgH;
		private Paint mPaint;

		//Constructor
		circlesView(Context c, int imgH, int imgW)
		{
			super(c);

			imgW = imgW;
			imgH = imgH;
			
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setTextAlign(Paint.Align.CENTER);
			mPaint.setTextSize(12);
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			//drawing the leds on the
			
			int inc = 44; 
			
			for (int j = 0; j < 6; j += inc) 
			{
				mPaint.setColor(Color.GRAY);
				mPaint.setStyle(Paint.Style.STROKE);
				canvas.drawCircle(STARTING_CIRCLE_LINE_X, STARTING_CIRCLE_LINE_Y, RADIUS, mPaint);
			}
			
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(new circlesView(getContext(), 1,1));
	}
	
}

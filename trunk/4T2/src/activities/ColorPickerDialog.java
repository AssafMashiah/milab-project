package activities;

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

public class ColorPickerDialog extends Dialog
{
	public static int ledStartX = 30;
	public static int ledStartY = 20;
	public static int hueBarYstart = 140;
	public static int hueBarYend = 170;
	static int delta = 30;
	
	public ColorPickerDialog(Context context, OnColorChangedListener listener,
			String key, int initialColor, int defaultColor)
	{
		super(context);

		mListener = listener;
		mKey = key;
		mInitialColor = initialColor;
		mDefaultColor = defaultColor;
	}
	
	public interface OnColorChangedListener
	{
		void colorChanged(String key, int color);
	}

	private OnColorChangedListener mListener;
	private int mInitialColor, mDefaultColor;
	private String mKey;

	public static class ColorPickerView extends View
	{
		private Paint mPaint;
		private float mCurrentHue = 0;
		private int mCurrentColor, mDefaultColor;
		private final int[] mHueBarColors = new int[258];
		private int[] mMainColors = new int[65536];
		private OnColorChangedListener mListener;

		//Constructor
		
		ColorPickerView(Context c, OnColorChangedListener l, int color,
				int defaultColor)
		{
			super(c);
			mListener = l;
			mDefaultColor = defaultColor;

			// Get the current hue from the current color and update the main
			// color field
			float[] hsv = new float[3];
			Color.colorToHSV(color, hsv);
			mCurrentHue = hsv[0];
			mCurrentColor = color;

			// Initialize the colors of the hue slider bar
			int index = 0;
			for (float i = 0; i < 256; i += 256 / 42) // Red (#f00) to pink
														// (#f0f)
			{
				mHueBarColors[index] = Color.rgb(255, 0, (int) i);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Pink (#f0f) to blue
														// (#00f)
			{
				mHueBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Blue (#00f) to light
														// blue (#0ff)
			{
				mHueBarColors[index] = Color.rgb(0, (int) i, 255);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Light blue (#0ff) to
														// green (#0f0)
			{
				mHueBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Green (#0f0) to yellow
														// (#ff0)
			{
				mHueBarColors[index] = Color.rgb((int) i, 255, 0);
				index++;
			}
			for (float i = 0; i < 256; i += 256 / 42) // Yellow (#ff0) to red
														// (#f00)
			{
				mHueBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
				index++;
			}

			// Initializes the Paint that will draw the View
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setTextAlign(Paint.Align.CENTER);
			mPaint.setTextSize(12);
		}
		// Update the main field colors depending on the current selected hue
		private int getTranslatedHue()
		{
			return 255 - (int) (mCurrentHue * 255 / 360);
		}
		
		//this method returns the Color in rgb not the HueColor
		private int getCurrentHueColor()
		{
			int color = getTranslatedHue();
			if (color < 0)
			{
				color = 0;
			}
			else if (color >= mHueBarColors.length)
			{
				color = mHueBarColors.length - 1;
			}
			return mHueBarColors[color];
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			int translatedHue = getTranslatedHue();
			// Display all the colors of the hue bar with lines
			for (int x = 0; x < 256; x++)
			{
				// If this is not the current selected hue, display the actual
				// color
				if (translatedHue != x)
				{
					mPaint.setColor(mHueBarColors[x]);
					mPaint.setStrokeWidth(1);
				}
				canvas.drawLine(x + 10, hueBarYstart, x + 10, hueBarYend, mPaint);
			}

			// Display the main field colors using LinearGradient
			for (int x = 0; x < 256; x++)
			{
				int[] colors = new int[2];
				colors[0] = mMainColors[x];
				colors[1] = Color.BLACK;
				Shader shader = new LinearGradient(0, 50, 0, 306, colors, null,	Shader.TileMode.REPEAT);
				mPaint.setShader(shader);
			}
			mPaint.setShader(null);
			

			//this array is the saturation of each led according to the number it is given
			float[] ledBright = android.Utils.colorCalculation.setLedBrightness(
					android.Utils.colorCalculation.getBrightness(5012));
			
			

			//drawing the leds on the
			for (int j = 0; j < 6; j++) {
				
				float[] hsv = new float[3];
				
				int rgb = getCurrentHueColor();

				Color.RGBToHSV(Color.red(rgb), Color.green(rgb),
						Color.blue(rgb), hsv);

				hsv[1] = ledBright[j];
				hsv[2] = 1;
				int color = Color.HSVToColor(hsv);
				mPaint.setColor(color);
				mPaint.setStyle(Paint.Style.FILL);
				canvas.drawCircle(ledStartX + delta * j, ledStartY + delta,	12, mPaint);
			}
			
		}
		//this method set the size of the dialog
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
			setMeasuredDimension(275, 265);
		}
		
		//this method sets the touch event for the color picker
		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			float x = event.getX();
			float y = event.getY();

			// If the touch event is located in the hue bar
			if (x > 10 && x < 266 && y > hueBarYstart && y < hueBarYend)
			{
				// Update the main field colors
				mCurrentHue = (255 - x) * 360 / 255;
				// Update the current selected color
				mCurrentColor = getCurrentHueColor();
				
				// Force the redraw of the dialog
				invalidate();
			}
			return true;
		}
	}



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		OnColorChangedListener l = new OnColorChangedListener()
		{
			public void colorChanged(String key, int color)
			{
				mListener.colorChanged(mKey, color);
				dismiss();
			}
		};

		setContentView(new ColorPickerView(getContext(), l, mInitialColor,
				mDefaultColor));
		setTitle("swipe");
	}
	
	

	
	
}

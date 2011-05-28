package android.Utils;

import android.graphics.Color;

public class colorCalculation {
	public static int[] getBrightness(int number) {
		int[] brightness = new int[8];
		for (int i = 0; i < 8; i++) {
			brightness[i] = number % 3;
			number = number / 3;
		}

		return brightness;
	}

	public static float[] setLedBrightness(int[] number) {
		float[] ledbrightness = new float[8];
		for (int i = 0; i < 8; i++) {
			switch (number[i]) {
			case 0:
				ledbrightness[i] = 0;
				break;
			case 1:
				ledbrightness[i] = 0.1f;
				break;
			case 2:
				ledbrightness[i] = 1f;
				break;
			}

		}
		return ledbrightness;
	}


	  public static String getHexName (int color)
	  {
	    int r = Color.red(color);
	    int g = Color.green(color);
	    int b = Color.blue(color);
	    
	    String rHex = Integer.toString (r, 16);
	    String gHex = Integer.toString (g, 16);
	    String bHex = Integer.toString (b, 16);        

	    return (rHex.length() == 2 ? "" + rHex : "0" + rHex) +
	           (gHex.length() == 2 ? "" + gHex : "0" + gHex) +
	           (bHex.length() == 2 ? "" + bHex : "0" + bHex);
	  }
}

package android.Utils;

import android.graphics.Color;

public class colorCalculation {
	public static int[] getBrightness(int number) {
		int[] brightness = new int[6];
		for (int i = 5; i >= 0; i--) {
			brightness[i] = number % 2;
			number = number / 2;
		}

		return brightness;
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

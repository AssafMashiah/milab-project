package android.Utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Color;

public class Utils {
	
	public static int scanToNumber(int[][] image)
	{
		int heightDiv = image[0].length/3;
		
		float areaWidth = image.length / 6;

		double[][] grayImage = new double[image.length][image[0].length];
		// gray scale
		for (int i = 0; i < grayImage.length; i++)
		{
			for (int j = 0; j < grayImage[0].length; j++)
			{	
					int x = image[i][j];
					float red = Color.red(x);
					float green = Color.green(x);
					float blue = Color.blue(x);
					double currentCalc =  (red + green + blue)/3;
					grayImage[i][j] = (int) currentCalc;
			
			}
		}

		int[] brightPixels = new int[6];
		// Resulted numbers
		for (int ledNumber = 0; ledNumber < 6; ledNumber++)
		{
			brightPixels[ledNumber] = 0;
			for (int i = (int) (ledNumber * areaWidth); i < ledNumber
					* areaWidth + areaWidth; i++)
			{
				
				for (int j = heightDiv; j < heightDiv*2; j++)
				{
					if(grayImage[i][j] >= 100){
						brightPixels[ledNumber]++;
					}
					
				}
			}
		}

		int result = 0;

		for (int i = 0; i < brightPixels.length; i++)
		{
			System.out.println("led " + i + " number of bright " + brightPixels[i]);
			if (brightPixels[i] < 10)	
			{
				brightPixels[i] = 0;	
			}
			if (brightPixels[i] > 10)
			{
				brightPixels[i] = 1;
			}

			
			
			result += brightPixels[i] * Math.pow(2, brightPixels.length - 1 - i);

		}
		
		return result;
	}
	
	 public static String read( InputStream stream ) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream( 8196 );
				byte[] buffer = new byte[1024];
				int length = 0;
				while ( ( length = stream.read( buffer ) ) > 0 ) {
					baos.write( buffer, 0, length );
				}
				
				return baos.toString();
			}
			catch ( Exception exception ) {
				return exception.getMessage();
			}
		}
}

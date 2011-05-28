package android.Utils;

import android.graphics.Color;

public class Utils {
	private static void scanToNumber(int[][] image) {
		float areaWidth = image.length / 8;

		double[][] grayImage = new double[image.length][image[0].length];
		
		// gray scale
		for (int i = 0; i < grayImage.length; i++) {
			for (int j = 0; j < grayImage[0].length; j++) {
				int currentColor = image[i][j];
				int red = Color.red(currentColor);
				int green = Color.green(currentColor);
				int blue = Color.blue(currentColor);
				
				grayImage[i][j] = (red + green + blue) / 3;
			}
		}

		// Resulted numbers
		double[] finalArray = new double[8];
		int sizeDiv3 = grayImage[0].length / 3;

		int blocker = sizeDiv3;
		
		for (int ledNumber = 0; ledNumber < 8; ledNumber++) {
			int counter = 0;
			for (int i = (int) (ledNumber * areaWidth); i < ledNumber
					* areaWidth + areaWidth; i++) {
				for (int j = sizeDiv3; j < blocker * 2; j++) {
					finalArray[ledNumber] += grayImage[i][j];
					counter++;
				}
			}
			finalArray[ledNumber] = finalArray[ledNumber] / counter;
		}

		int resault = 0;

		for (int i = 0; i < finalArray.length; i++) {
			if (finalArray[i] > 0 && finalArray[i] <= 1) {
				finalArray[i] = 0;
			}

			if (finalArray[i] > 1 && finalArray[i] <= 30) {
				finalArray[i] = 1;
			}

			if (finalArray[i] > 30) {
				finalArray[i] = 2;
			}
			
			resault += finalArray[i] * Math.pow(3, i);
		}

		System.out.println("The image resault: " + resault);
	}
}

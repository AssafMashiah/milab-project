/*
 * Copyright 2010-2011 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazon.aws.demo.s3;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class S3Menu extends Activity {
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        S3.createObjectForBucket(bucketName, objectName, data)
//      Intent browseIntent = new Intent(Intent.ACTION_VIEW);
//		 String url = "https://s3.amazonaws.com/milab-bucket/4t2.jpg";
//		 browseIntent.setData(Uri.parse(url));
//		 this.startActivity(browseIntent);
//		 
//		 FileInputStream f=null;
//			try {
//				f = new FileInputStream("/mnt/sdcard/Master Chef.apk");
//	                        //file is stored /mnt/sdcard/audio/file-4555555.mp3
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			
//			S3.createObjectForBucket("milab-bucket","Master Chef.apk",read(f));
		 
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

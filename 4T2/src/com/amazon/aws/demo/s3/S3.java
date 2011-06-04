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
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.amazon.aws.demo.AWSDemo;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class S3 {

	private static AmazonS3 s3 = null;
	private static ObjectListing objListing = null;
	public static final String BUCKET_NAME = "_bucket_name";
	public static final String OBJECT_NAME = "_object_name";
	
	static {
		System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
	}
		
	public static AmazonS3 getInstance() {
        if ( s3 == null ) {
		    s3 = new AmazonS3Client( AWSDemo.credentials );
        }
        return s3;
	}


	public static List<String> getMoreObjectNamesForBucket() {
		try{
			ObjectListing objects = getInstance().listNextBatchOfObjects(objListing);
			objListing = objects;
			List<String> objectNames = new ArrayList<String>( objects.getObjectSummaries().size());
			return objectNames;
		} catch (NullPointerException e){
			return new ArrayList<String>();
		}
	}	

	public static void createBucket( String bucketName ) {
		getInstance().createBucket( bucketName );
	}		
	
	public static void deleteBucket( String bucketName ) {
		getInstance().deleteBucket(  bucketName );
	}

	public static void createObjectForBucket( String bucketName, String objectName, String fileLocation ) {
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			File myFile=new File(fileLocation);
			metadata.setContentLength(myFile.length());
			getInstance().putObject(bucketName,objectName, myFile);
		}
		catch ( Exception exception ) {
		}
	}
	
	public static void deleteObject( String bucketName, String objectName ) {
		getInstance().deleteObject( bucketName, objectName );
	}

	public static String getDataForObject( String bucketName, String objectName ) {
		return read( getInstance().getObject( bucketName, objectName ).getObjectContent() );
	}
	
	protected static String read( InputStream stream ) {
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

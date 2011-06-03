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
package com.amazon.aws.demo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import com.amazonaws.auth.BasicAWSCredentials;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AWSDemo extends Activity {
    
	
	public static BasicAWSCredentials credentials = null;
	protected Button snsButton;
	protected Button sqsButton;
	protected Button s3Button;
	protected Button sdbButton;
	protected TextView welcomeText;
	
	private boolean credentials_found;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGetCredentials();

    }
      
    
    private void startGetCredentials() {
    	Thread t = new Thread() {
    		@Override
    		public void run(){
    	        try {            
    	            Properties properties = new Properties();
    	            properties.load( getClass().getResourceAsStream( "AwsCredentials.properties" ) );
    	            
    	            String accessKeyId = properties.getProperty( "accessKey" );
    	            String secretKey = properties.getProperty( "secretKey" );
    	            
    	            if ( ( accessKeyId == null ) || ( accessKeyId.equals( "" ) ) ||
    	            	 ( accessKeyId.equals( "CHANGEME" ) ) ||( secretKey == null )   || 
    	                 ( secretKey.equals( "" ) ) || ( secretKey.equals( "CHANGEME" ) ) ) {
    	                Log.e( "AWS", "Aws Credentials not configured correctly." );                                    
        	            credentials_found = false;
    	            } else {
    	            credentials = new BasicAWSCredentials( properties.getProperty( "accessKey" ), properties.getProperty( "secretKey" ) );
        	        credentials_found = true;
    	            }

    	        }
    	        catch ( Exception exception ) {
    	            Log.e( "Loading AWS Credentials", exception.getMessage() );
    	            credentials_found = false;
    	        }
    		}
    	};
    	t.start();
    }
    
    protected void displayCredentialsIssueAndExit() {
        AlertDialog.Builder confirm = new AlertDialog.Builder( this );
        confirm.setTitle("Credential Problem!");
        confirm.setMessage( "AWS Credentials not configured correctly.  Please review the README file." );
        confirm.setNegativeButton( "OK", new DialogInterface.OnClickListener() {
                public void onClick( DialogInterface dialog, int which ) {
        AWSDemo.this.finish();
                }
        } );
        confirm.show().show();                
    }
    


}

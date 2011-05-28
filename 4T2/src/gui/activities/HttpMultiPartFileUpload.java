//package gui.activities;
//
//import java.io.File;
//import java.io.IOException;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.MultipartPostMethod;
//
//public class HttpMultiPartFileUpload 
//{
//    private static String url =  "http://localhost:8080/HttpServerSideApp/ProcessFileUpload.jsp";
//
//    public static void main(String[] args) throws IOException 
//    {
//        HttpClient client = new HttpClient();
//        MultipartPostMethod mPost = new MultipartPostMethod(url);
//        client.setConnectionTimeout(8000);
//
//        // Send any XML file as the body of the POST request
//        File f1 = new File("students.xml");
//        File f2 = new File("academy.xml");
//        File f3 = new File("academyRules.xml");
//
//        System.out.println("File1 Length = " + f1.length());
//        System.out.println("File2 Length = " + f2.length());
//        System.out.println("File3 Length = " + f3.length());
//
//        mPost.addParameter(f1.getName(), f1);
//        mPost.addParameter(f2.getName(), f2);
//        mPost.addParameter(f3.getName(), f3);
//
//        int statusCode1 = client.executeMethod(mPost);
//
//        System.out.println("statusLine>>>" + mPost.getStatusLine());
//        mPost.releaseConnection();
//    }
//}

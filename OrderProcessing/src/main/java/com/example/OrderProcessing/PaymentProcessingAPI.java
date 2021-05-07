package com.example.OrderProcessing;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;



public class PaymentProcessingAPI {
    private boolean DEBUG = false ;

    private static String requestHost;


    public static void setHost( String h ) { requestHost = h ; }

    private final String USER_AGENT = "Mozilla/5.0";
    public static String postRequestTarget = "REQUEST_TARGET_PALCEHOLDER";
    public static String APINAME = "APINAME_PLACEHOLDER";
    public static String resource = "resource_PLACEHOLDER";
    public static String payload = null;


    public PostResponse sendPost(String url){
        HttpURLConnection con = null ;
        int responseCode = 0 ;
        try{
            /* HTTP connection */
            URL obj = new URL(url); // my url contains all info
            con = (HttpURLConnection)obj.openConnection();

            /* Add Request Header
            * "Host" Name of the host to send the request to.
            */
           con.setRequestProperty("Host", requestHost);

           /* HTTP Method POST */
           con.setRequestMethod("POST");

           /* Additional Request Headers */
           con.setRequestProperty("User-Agent", USER_AGENT);
           con.setRequestProperty("Content-Type", "application/json");

           // Send POST request
           con.setDoOutput(true);
           con.setDoInput(true);
          
            /* Establishing HTTP connection*/
            responseCode = con.getResponseCode();


            String responseHeader = con.getHeaderField("v-c-correlation-id");
            System.out.println("\n -- RequestURL -- ");
            System.out.println("\tURL : " + url);
            System.out.println("\n -- HTTP Headers -- ");
            System.out.println("\tContent-Type : " + "application/json");

            System.out.println("\n -- Response Message -- " );
            System.out.println("\tResponse Code :" + responseCode);
            System.out.println("\tv-c-correlation-id :" + responseHeader);


            /* Reading Response Message */
            BufferedReader in  = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            System.out.println("Response Payload : " + response.toString());
            PostResponse res = new PostResponse() ;
            res.setCode(responseCode) ;
            res.setResponse(response.toString());
            return res;
        }
        catch(Exception exception){
             // Output unexpected IOExceptions
             BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
             String inputLine;
             StringBuffer response = new StringBuffer();
 
             try {
                 while ((inputLine = in.readLine()) != null) {
                     response.append(inputLine);
                 }
                 in.close();
             } catch( Exception e ) { }

             /* print Response */
            System.out.println("Response Payload : " + response.toString());
            System.out.println(exception);
            PostResponse res = new PostResponse() ;
            res.setCode(responseCode) ;
            res.setException(exception.toString()) ;
            res.setResponse(response.toString());
            return res ;     
        }
    }

    
}

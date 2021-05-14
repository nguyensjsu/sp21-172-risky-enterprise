package com.example.CashierApp;

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

public class CyberSourceAPI {
    private final String USER_AGENT = "Mozilla/5.0";
    public static String gmtDateTime = "DATE_PLACEHOLDER";
    public static String postRequestTarget = "REQUEST_TARGET_PALCEHOLDER";
    public static String APINAME = "APINAME_PLACEHOLDER";
    public static String resource = "resource_PLACEHOLDER";
    public static String  payload = null;

    public OrderResponse autOrder(Order req, String resource){

        payload = "{\n\"drink\": \"" + req.getDrink() + "\",\n\"milk\": \"" + req.getMilk() +
         "\",\n\"size\": " + req.getSize() + "\"";

        OrderResponse response = new OrderResponse();
        PostResponse res = sendPost("https://" + requestHost + resource) ;

    }

    // HTTP POST request
    class PostResponse {
        int code ;
        String exception ;
        String response ;
    }
    private PostResponse sendPost(String url) {

        HttpURLConnection con = null ;
        int responseCode = 0 ;
        String requestHost = "localhost";

        try {

            /* HTTP connection */
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            responseCode = con.getResponseCode();

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
            
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(getPayload().getBytes("UTF-8"));
            wr.flush();
            wr.close();

            /* Establishing HTTP connection*/
            responseCode = con.getResponseCode();

            System.out.println("\tResponse Code :" + responseCode);

            /* Reading Response Message */
            BufferedReader in  = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            
            in.close();
            
            /* print Response */
            //System.out.println("\tResponse Payload :\n" + response.toString());
            PostResponse res = new PostResponse() ;
            res.code = responseCode ;
            res.response = response.toString() ;
            return res ;

        }
        catch (Exception exception) {

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
            //System.out.println("Response Payload : " + response.toString());
            System.out.println(exception);
            PostResponse res = new PostResponse() ;
            res.code = responseCode ;
            res.exception =  exception.toString() ;
            res.response = response.toString() ;
            return res ;            
        }
    }
}

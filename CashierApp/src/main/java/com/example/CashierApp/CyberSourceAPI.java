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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CyberSourceAPI {

    private static String APIKEY = "";

    private final String USER_AGENT = "Mozilla/5.0";
    public static String postRequestTarget = "REQUEST_TARGET_PALCEHOLDER";
    public static String APINAME = "APINAME_PLACEHOLDER";
    public static String resource = "resource_PLACEHOLDER";

    private boolean DEBUG = false;
    public static String payload = null;

    public OrderResponse authorize(Order req, String resource, String action) throws Exception{
        OrderResponse response = new OrderResponse();
        if (!action.equals("")){
            System.out.println("Requesting Orders");
            payload = "{\n}";
            log.info("https://localhost:80/" + resource);
            String res = sendGet("http://localhost:80/" + resource, action) ;
            response.reply = res;
        }
        else{
            payload = "{\n\"drink\": \"" + req.getDrink() + "\",\n\"milk\": \"" + req.getMilk() +
            "\",\n\"size\": \"" + req.getSize() + "\"\n}";
            log.info("https://localhost:80/" + resource);
            PostResponse res = sendPost("http://localhost:80/" + resource) ;
            response.code = res.code;
            
            if ( res.exception != null ) {
                response.status = "ERROR" ;
                response.message = res.exception ;
                System.out.println("Exception" + res.exception);
            } else {
                String authResult = res.response ;
                response.reply = authResult;   
            }
        }

        return response ;
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

            /* Add Request Header
             * "Host" Name of the host to send the request to.
             */
            con.setRequestProperty("Host", requestHost);

            /* HTTP Method POST */
            con.setRequestMethod("POST");

            /* Additional Request Headers */
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json");

            /* Add Request Header
            * Secret api key for connecting to apis
            */
            con.setRequestProperty("apikey", APIKEY);
            // Send POST request
            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(getPayload().getBytes("UTF-8"));
            wr.flush();
            wr.close();

            /* Establishing HTTP connection*/
            responseCode = con.getResponseCode();

            System.out.println("\nResponse Code :" + responseCode);

            /* Reading Response Message */
            BufferedReader in  = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            
            in.close();
            
            /* print Response */
            System.out.println("\nResponse Payload :\n" + response.toString());
            PostResponse res = new PostResponse();
            res.code = responseCode ;
            res.response = response.toString();
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
            System.out.println("Response Payload : " + response.toString());
            System.out.println(exception);
            PostResponse res = new PostResponse() ;
            res.code = responseCode ;
            res.exception =  exception.toString() ;
            res.response = response.toString() ;
            return res ;            
        }

    }

    private String getPayload() throws IOException {
        String messageBody = payload;
        return messageBody;
    }

    // HTTP request
    private String sendGet(String url, String action) throws Exception {
        /* HTTP connection */
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        /* Add Request Header
         * "Host" Host to send the request to.
         */
        con.setRequestProperty("Host", "localhost");

        if (action.equals("ClearOrder")){
            /* HTTP Method DELETE */
            con.setRequestMethod("DELETE");
        } else if (action.equals("GetOrder")){
            /* HTTP Method GET */
            con.setRequestMethod("GET");
        } else if (action.equals("PayOrder")){
            /* HTTP Method POST */
            con.setRequestMethod("POST");
        }

        /* Establishing HTTP connection*/
        int responseCode = con.getResponseCode();
        String responseHeader = con.getHeaderField("v-c-correlation-id");

        /* Reading Response Message */
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        
        in.close();

        /* print Response */
        System.out.println("\tResponse Payload :\n" + response.toString());

        return response.toString() ;
    }
}

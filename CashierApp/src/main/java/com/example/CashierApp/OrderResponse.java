package com.example.CashierApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class OrderResponse extends Payload {
    public String id ;
    public int code ;
    public String status ;
    public String reconciliationId ;
    public String reason ;
    public String message ;
    public String reply;

    public static OrderResponse fromJson( String json ) {
        /* 
            https://www.baeldung.com/jackson-object-mapper-tutorial
        */
        ObjectMapper mapper = new ObjectMapper() ;
        OrderResponse response = new OrderResponse() ;
        try { 
            JsonNode jsonNode = mapper.readTree(json);
            if ( jsonNode.get("response") != null ) {
                response.status = "ERROR" ;
                response.message = jsonNode.get("response").get("rmsg").asText() ;
            } else {
                response.id = jsonNode.get("id").asText() ;
                response.status = jsonNode.get("status").asText() ;
                response.code = Integer.parseInt(jsonNode.get("code").asText());
                if ( !response.status.equals("READY_FOR_PAYMENT") && !response.message.equals("{\"message\":\"FAILURE\"}") && !response.message.equals("{\"message\":\"SUCCESS\"}")) { //CHANGE
                    response.status = "ERROR" ;
                    response.reason = jsonNode.get("errorInformation").get("reason").asText() ;
                    response.message = jsonNode.get("errorInformation").get("message").asText() ;
                } else { 
                    response.reconciliationId = jsonNode.get("reconciliationId").asText() ;   
                }                
            }

        } catch ( Exception e ) { System.out.println( e ) ; }	
        return response ;
    }
}

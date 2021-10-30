package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.serverless.dynamodb.CovidScreening;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;

public class DeleteCovidScreeningHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

    try {
        // get the 'pathParameters' from input
        Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
        String covidScreeningId = pathParameters.get("id");

        // get the CovidScreening by id
        Boolean success = new CovidScreening().delete(covidScreeningId);

        // send the response back
        if (success) {
          return ApiGatewayResponse.builder()
      				.setStatusCode(204)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();
        } else {
          return ApiGatewayResponse.builder()
      				.setStatusCode(404)
      				.setObjectBody("CovidScreening with id: '" + covidScreeningId + "' not found.")
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();
        }
    } catch (Exception ex) {
        logger.error("Error in deleting covidScreening: " + ex);

        // send the error response back
  			Response responseBody = new Response("Error in deleting covidScreening: ", input);
  			return ApiGatewayResponse.builder()
  					.setStatusCode(500)
  					.setObjectBody(responseBody)
  					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
  					.build();
    }
	}
}

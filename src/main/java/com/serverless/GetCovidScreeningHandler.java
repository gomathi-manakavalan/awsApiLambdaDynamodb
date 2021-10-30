package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.serverless.dynamodb.CovidScreening;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;

public class GetCovidScreeningHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

    try {
        // get the 'pathParameters' from input
        Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
        String covidScreeningId = pathParameters.get("id");

        // get the CovidScreening by id
        CovidScreening covidScreening = new CovidScreening().get(covidScreeningId);

        // send the response back
        if (covidScreening != null) {
          return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(covidScreening)
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
        logger.error("Error in retrieving covidScreening: " + ex);

        // send the error response back
  			Response responseBody = new Response("Error in retrieving covidScreening: ", input);
  			return ApiGatewayResponse.builder()
  					.setStatusCode(500)
  					.setObjectBody(responseBody)
  					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
  					.build();
    }
	}
}

package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.dynamodb.CovidScreening;
import org.apache.http.util.ExceptionUtils;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

public class CreateCovidScreeningHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

      try {
          // get the 'body' from input
          JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

          // create the CovidScreening object for post
          CovidScreening covidScreening = new CovidScreening();
          // covidScreening.setId(body.get("id").asText());
          covidScreening.setName(body.get("name").asText());
          covidScreening.setPhone(body.get("phone").asText());
		  covidScreening.setDate(body.get("date").asText());
		  covidScreening.setAnswer1(Boolean.valueOf(body.get("answer1").asText()));
		  covidScreening.setAnswer2(Boolean.valueOf(body.get("answer2").asText()));
			logger.info("quest and answer "+body.findValuesAsText("questionandAnswers"));
          covidScreening.save(covidScreening);

          // send the response back
      		return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(covidScreening)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();

      } catch (Exception ex) {
		  StringWriter stringWriter= new StringWriter();
		  PrintWriter printWriter= new PrintWriter(stringWriter);
		  ex.printStackTrace(printWriter);
		  String stackTraceAsString= stringWriter.toString();
		  logger.error("Error in saving covidScreening3: " + stackTraceAsString);

          // send the error response back
    			Response responseBody = new Response("Error in saving covidScreening3: ", input);
    			return ApiGatewayResponse.builder()
    					.setStatusCode(500)
    					.setObjectBody(responseBody)
    					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
    					.build();
      }
	}
}

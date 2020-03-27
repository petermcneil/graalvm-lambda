package mcneil.peter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {
    public Handler(){}
    @Override
    public ApiGatewayResponse handleRequest(final ApiGatewayRequest input, final Context context) {
        return ApiGatewayResponse.builder().statusCode(200).rawBody("Hello").build();
    }
}

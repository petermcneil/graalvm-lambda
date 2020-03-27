package mcneil.peter;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Application {
    private final static String requestIdHeaderName = "lambda-runtime-aws-request-id";
    private final static String runtimeApiEndpointVariableName = "AWS_LAMBDA_RUNTIME_API";
    private final static String handlerVariableName = "_HANDLER";
    private final static HttpClient client = HttpClient.newHttpClient();
    private final static ObjectMapper json = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        var runtimeApiEndpoint = System.getenv(runtimeApiEndpointVariableName);
        var handler = System.getenv(handlerVariableName);
        var uri = new URI(String.format("%s/2018-06-01/runtime/invocation/next", runtimeApiEndpoint));

        var handlerInstance = (RequestHandler<ApiGatewayRequest, ApiGatewayResponse>) Class.forName(handler).getDeclaredConstructor().newInstance();

        while (true) {
            var invocationResponse = client.send(HttpRequest.newBuilder().GET().uri(uri).build(), HttpResponse.BodyHandlers.ofString());

            var requestId = invocationResponse.headers().firstValue(requestIdHeaderName).orElseThrow(NullPointerException::new);

            var request = json.readValue(invocationResponse.body(), ApiGatewayRequest.class);

            var response = handlerInstance.handleRequest(request, new CustomContext(requestId));
            var responseUri = new URI(String.format("%s/2018-06-01/runtime/invocation/%s/response", runtimeApiEndpoint, requestId));
            client.send(HttpRequest.newBuilder().uri(responseUri).POST(HttpRequest.BodyPublishers.ofString(json.writeValueAsString(response))).build(), HttpResponse.BodyHandlers.discarding());
        }
    }
}


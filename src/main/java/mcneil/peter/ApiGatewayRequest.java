package mcneil.peter;

import java.util.Map;

import static java.util.Collections.emptyMap;

public class ApiGatewayRequest {
    public String path = null;
    public String httpMethod = null;
    public Map<String, String> headers = emptyMap();
    public Map<String, String> queryStringParameters = emptyMap();
    public Map<String, String> pathParameters = emptyMap();
    public String body = "";
}

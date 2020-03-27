package mcneil.peter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

class ApiGatewayResponse {
    private final Integer statusCode;
    private final String body;
    private final Map<String, String> headers;
    private final Boolean isBase64Encoded;


    private ApiGatewayResponse(Builder from) {
        this.statusCode = from.statusCode;
        this.body = from.body;
        this.headers = from.headers;
        this.isBase64Encoded = from.isBase64Encoded;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Boolean getBase64Encoded() {
        return isBase64Encoded;
    }


    public static class Builder {
        private static ObjectMapper objectMapper = new ObjectMapper();

        Integer statusCode = 200;
        Map<String, String> headers = Collections.emptyMap();
        Boolean isBase64Encoded = false;
        String body = null;

        public Builder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder isBase64Encoded(Boolean isBase64Encoded) {
            this.isBase64Encoded = isBase64Encoded;
            return this;
        }

        public Builder rawBody(String rawBody) {
            if (this.body == null) {
                this.body = rawBody;
                return this;
            } else {
                throw new RuntimeException("Can't set two bodies");
            }
        }

        public Builder objectBody(Response objectBody) {
            if (this.body == null) {
                try {
                    this.body = objectMapper.writeValueAsString(objectBody);
                    return this;

                } catch (JsonProcessingException jpe) {
                    throw new RuntimeException(jpe);
                }
            } else {
                throw new RuntimeException("Can't set two bodies");
            }
        }

        public Builder binaryBody(byte[] binaryBody) {
            if (body == null) {
                this.body = new String(Base64.getEncoder().encode(binaryBody), StandardCharsets.UTF_8);
                return this;

            } else {
                throw new RuntimeException("Can't set two bodies");
            }
        }

        public ApiGatewayResponse build() {
            return new ApiGatewayResponse(this);
        }
    }
}

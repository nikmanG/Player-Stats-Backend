package integration.setup;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResponseResults {

    private int code;
    private String body;
    private HttpHeaders httpHeaders;

    public ResponseResults(int code, String body, HttpHeaders httpHeaders) {
        this.code = code;
        this.body = body;
        this.httpHeaders = httpHeaders;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public boolean wasError() {
        return code >= 400;
    }
}

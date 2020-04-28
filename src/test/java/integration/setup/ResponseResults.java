package integration.setup;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResponseResults {
    private final ClientHttpResponse theResponse;
    private final String body;

    public ResponseResults(final ClientHttpResponse response) throws IOException {
        this.theResponse = response;
        final InputStream bodyInputStream = response.getBody();
        this.body = IOUtils.toString(bodyInputStream, StandardCharsets.UTF_8);
    }

    public ClientHttpResponse getTheResponse() {
        return theResponse;
    }

    public String getBody() {
        return body;
    }
}

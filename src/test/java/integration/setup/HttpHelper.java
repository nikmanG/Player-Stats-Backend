package integration.setup;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class HttpHelper {

    private static final String BASE_URL = "http://localhost:8080/";

    private RestTemplate restTemplate = new RestTemplate();

    public ResponseResults call(HttpMethod httpMethod, String path) throws IOException {
        return call(httpMethod, path, Collections.emptyMap(), Collections.emptyMap());
    }

    public ResponseResults call(
            HttpMethod method,
            String path,
            Map<String, String> headers,
            Map<String, String> body) throws IOException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        headers.forEach(map::add);

        if (method == HttpMethod.POST) {
            return executePost(BASE_URL + path, map, body);
        } else {
            return executeGet(BASE_URL + path, map, body);
        }
    }

    private ResponseResults executeGet(String url, MultiValueMap<String, String> headers, Map<String, String> body) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.addAll(headers);

        HttpEntity entity = new HttpEntity(httpHeaders);

        List<NameValuePair> pairs = new ArrayList<>();

        for (Map.Entry<String, String> b : body.entrySet()) {
            pairs.add(new BasicNameValuePair(b.getKey(), b.getValue()));
        }

        try {
            ResponseEntity<String> result = restTemplate.exchange(
                    url + "?" + URLEncodedUtils.format(pairs, Charset.defaultCharset()),
                    HttpMethod.GET,
                    entity,
                    String.class);

            return new ResponseResults(result.getStatusCodeValue(), result.getBody(), result.getHeaders());
        } catch (HttpClientErrorException e) {
            return new ResponseResults(e.getStatusCode().value(), e.getStatusText(), e.getResponseHeaders());
        }
    }

    private ResponseResults executePost(
            String post,
            MultiValueMap<String, String> headers,
            Map<String, String> body) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.addAll(headers);

        HttpEntity<Map<String, String>> postRequest = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> result = restTemplate.postForEntity(post, postRequest, String.class);

        return new ResponseResults(result.getStatusCodeValue(), result.getBody(), result.getHeaders());
    }
}

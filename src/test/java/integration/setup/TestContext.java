package integration.setup;

import java.util.HashMap;
import java.util.Map;

public enum TestContext {

    CONTEXT;

    private String AUTH_HEADER;
    private ThreadLocal<Map<String, ResponseResults>> contextualResults;

    {
        AUTH_HEADER = "";
        contextualResults = ThreadLocal.withInitial(HashMap::new);
    }

    public void addResult(String name, ResponseResults responseResults) {
        contextualResults.get().put(name, responseResults);
    }

    public void addAuthorization(String auth) {
        this.AUTH_HEADER = auth;
    }

    public String getAuth() {
        return AUTH_HEADER;
    }

    public ResponseResults getResult(String response) {
        return contextualResults.get().get(response);
    }
}

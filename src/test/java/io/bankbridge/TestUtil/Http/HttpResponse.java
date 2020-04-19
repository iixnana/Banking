package io.bankbridge.TestUtil.Http;

public abstract class HttpResponse {
    public abstract int getStatus();
    public abstract String getBody();
    public abstract Object getJson();
}

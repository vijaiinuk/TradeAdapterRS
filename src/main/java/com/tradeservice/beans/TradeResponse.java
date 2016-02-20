package com.tradeservice.beans;

import org.codehaus.jackson.annotate.JsonProperty;

public class TradeResponse {

    @JsonProperty(value = "token")
    private String token;

    @JsonProperty(value = "response")
    private String response;

    @JsonProperty(value = "time")
    private long time;

    public TradeResponse() {
    }

    public TradeResponse(String token, String response, long time) {
        this.token = token;
        this.response = response;
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public String getResponse() {
        return response;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "TradeResponse{" +
                "token='" + token + '\'' +
                ", response='" + response + '\'' +
                ", time=" + time +
                '}';
    }
}

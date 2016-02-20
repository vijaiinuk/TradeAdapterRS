package com.tradeservice.beans;

public class TradeResponseWithStatus {

    public enum RESPONSE_STATUS {SUCCESS, FAILURE, NOT_FOUND}

    private RESPONSE_STATUS status;

    private TradeResponse tradeResponse;

    public TradeResponseWithStatus(RESPONSE_STATUS status, TradeResponse tradeResponse) {
        this.status = status;
        this.tradeResponse = tradeResponse;
    }

    public TradeResponse getTradeResponse() {
        return tradeResponse;
    }

    public RESPONSE_STATUS getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "TradeResponseWithStatus{" +
                "status=" + status +
                ", tradeResponse=" + tradeResponse +
                '}';
    }
}

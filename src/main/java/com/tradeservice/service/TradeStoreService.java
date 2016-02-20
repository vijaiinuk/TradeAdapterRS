package com.tradeservice.service;

import com.tradeservice.beans.TradeResponse;
import com.tradeservice.beans.TradeResponseWithStatus;
import com.tradeservice.exception.TradeAdapterException;

import java.util.concurrent.ExecutionException;

public interface TradeStoreService {

    public void addToTradeStore(long tradeId, TradeResponseWithStatus tradeResponse);
    public TradeResponseWithStatus getTradeResponseWithStatusFromTradeStore(long tradeId) throws ExecutionException, TradeAdapterException;

}

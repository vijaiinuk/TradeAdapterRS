package com.tradeservice.beans;

public interface TradeAdapterStatistics {
    void incrementSaveCounter();

    void incrementRetrieveCounter();

    void resetCounters();
}

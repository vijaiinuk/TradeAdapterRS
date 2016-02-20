package com.tradeservice.beans;

import java.util.concurrent.atomic.AtomicLong;

public class TradeAdapterStatisticsImpl implements TradeAdapterStatistics {

    private AtomicLong saveCounter = new AtomicLong();

    private AtomicLong retrieveCounter = new AtomicLong();

    @Override
    public void incrementSaveCounter()  {
        saveCounter.getAndIncrement();
    }

    @Override
    public void incrementRetrieveCounter()  {
        retrieveCounter.getAndIncrement();
    }

    @Override
    public void resetCounters()  {
        saveCounter.set(0);
        retrieveCounter.set(0);
    }
    @Override
    public String toString() {
        return "TradeAdapterStatistics{" +
                "No of Save Requests =" + saveCounter +
                ", No of Get Requests =" + retrieveCounter +
                '}';
    }
}

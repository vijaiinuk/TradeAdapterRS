package com.tradeservice.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tradeservice.beans.TradeResponse;
import com.tradeservice.beans.TradeResponseWithStatus;
import com.tradeservice.exception.TradeAdapterException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.tradeservice.beans.TradeResponseWithStatus.RESPONSE_STATUS.NOT_FOUND;

/**
 * This service class uses Guava API to cache the trade responses.
 *
 * This can be replaced with DB service to store and retrieve trade responses.
 *
 */
@Component
public class TradeStoreServiceImpl implements TradeStoreService, InitializingBean {

    private static Logger logger = Logger.getLogger(TradeStoreServiceImpl.class);

    private long cacheRefreshInterval;

    private long cacheMaximumSize;

    public void setCacheRefreshInterval(long cacheRefreshInterval) {
        this.cacheRefreshInterval = cacheRefreshInterval;
    }

    public void setCacheMaximumSize(long cacheMaximumSize) {
        this.cacheMaximumSize = cacheMaximumSize;
    }

    private LoadingCache<Long, TradeResponseWithStatus> tradeResponses = null;

    @Override
    public void addToTradeStore(long tradeId, TradeResponseWithStatus tradeResponseWithStatus) {
        logger.info("Adding tradeId: "+tradeId+" to cache");
        tradeResponses.put(tradeId, tradeResponseWithStatus);
    }

    @Override
    public TradeResponseWithStatus getTradeResponseWithStatusFromTradeStore(long tradeId) throws TradeAdapterException {
        try {
            return tradeResponses.get(tradeId);
        } catch (ExecutionException e) {
            logger.error("Error getting the tradeResponse from Cache");
            logger.error(e.getStackTrace());
            throw new TradeAdapterException();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("setting cacheRefreshInterval: "+cacheRefreshInterval+" cacheMaximumSize: "+cacheMaximumSize);
        tradeResponses = CacheBuilder.newBuilder()
                .maximumSize(cacheMaximumSize)
                .expireAfterWrite(cacheRefreshInterval, TimeUnit.HOURS)
                .build(new CacheLoader<Long, TradeResponseWithStatus>() {
                    @Override
                    public TradeResponseWithStatus load(Long aLong) throws Exception {
                        logger.warn("Cache Not hit. Returning dummy response");
                        return new TradeResponseWithStatus(NOT_FOUND, new TradeResponse("Dummy Token", "Dummy Response", 0));
                    }
                });
    }
}

package com.tradeservice.service;

import org.apache.cxf.jaxrs.client.WebClient;

import java.net.URISyntaxException;

public interface TradePostService {
    public WebClient getWebClient(String endPointUrl) throws URISyntaxException;
}

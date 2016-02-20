package com.tradeservice.service;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TradePostServiceProviderImpl implements TradePostService {

    private final Logger logger = Logger.getLogger(TradePostServiceProviderImpl.class);

    @Override
    public WebClient getWebClient(String endPointUrl) throws URISyntaxException {
        logger.info("EndpointUrl: "+endPointUrl);
        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJaxbJsonProvider());
        return WebClient.create(endPointUrl, providers);
    }
}

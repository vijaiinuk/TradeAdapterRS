package com.tradeservice.restservice;

import com.tradeservice.beans.*;
import com.tradeservice.exception.TradeAdapterException;
import com.tradeservice.service.TradePostService;
import com.tradeservice.service.TradeStoreService;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import static com.tradeservice.beans.TradeResponseWithStatus.RESPONSE_STATUS.SUCCESS;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration("/test-config.xml")
public class TestTradeAdapterRS {

    @Mock
    private TradeStoreService tradeStoreService;
    @Mock
    private TradePostService tradePostService;

    @Mock
    private TradeAdapterStatistics tradeAdapterStatistics;

    @InjectMocks
    private TradeAdapterRS tradeAdapterRS = new TradeAdapterRS();

    @Before
    public void setup()  {
        MockitoAnnotations.initMocks(this);
        try {
            WebClient webClient = mock(WebClient.class);
            when(tradeStoreService.getTradeResponseWithStatusFromTradeStore(anyLong()))
                    .thenReturn(new TradeResponseWithStatus(SUCCESS, new TradeResponse()));
            when(tradePostService.getWebClient(anyString())).thenReturn(webClient);

            Response response = mock(Response.class);
            when(webClient.accept(MediaType.APPLICATION_JSON)).thenReturn(webClient);
            when(webClient.type(MediaType.APPLICATION_JSON)).thenReturn(webClient);
            when(webClient.post(any(Trade.class))).thenReturn(response);

            when(response.getStatus()).thenReturn(Response.Status.ACCEPTED.getStatusCode());

        } catch (ExecutionException|TradeAdapterException|URISyntaxException e) {
            fail();
            e.printStackTrace();
        }
    }
    @Test
    public void testGetTradeStatus()  {
        assertNotNull(tradeAdapterRS);

        assertTrue("Status is not SUCCESS", tradeAdapterRS.getTradeStatus(10000).contains("SUCCESS"));

        try {
            verify(tradeStoreService, times(1)).getTradeResponseWithStatusFromTradeStore(10000);
        } catch (ExecutionException|TradeAdapterException e) {
            fail();
            e.printStackTrace();
        }

        verify(tradeAdapterStatistics, times(1)).incrementRetrieveCounter();
        verify(tradeAdapterStatistics, never()).incrementSaveCounter();
    }

    @Test
    public void testSaveTrade()  {
        Trade trade = new TradeBuilder().setId(123456).setCustomer("BUYER").setTrader("SELLER").setPrice(BigDecimal.valueOf(100.0)).createTrade();

        try {
            Response response = tradeAdapterRS.saveTrade(trade);
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());
        } catch (IOException|URISyntaxException e) {
            fail();
            e.printStackTrace();
        }

        verify(tradeAdapterStatistics, times(1)).incrementSaveCounter();
        verify(tradeAdapterStatistics, never()).incrementRetrieveCounter();

        verify(tradeStoreService, times(1)).addToTradeStore(anyLong(), any(TradeResponseWithStatus.class));

    }
}

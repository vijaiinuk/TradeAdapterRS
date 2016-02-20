package com.tradeservice.restservice;

import com.tradeservice.beans.Trade;
import com.tradeservice.beans.TradeAdapterStatistics;
import com.tradeservice.beans.TradeResponse;
import com.tradeservice.beans.TradeResponseWithStatus;
import com.tradeservice.exception.TradeAdapterException;
import com.tradeservice.service.TradePostService;
import com.tradeservice.service.TradeStoreService;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import static com.tradeservice.beans.TradeResponseWithStatus.RESPONSE_STATUS.FAILURE;
import static com.tradeservice.beans.TradeResponseWithStatus.RESPONSE_STATUS.SUCCESS;
import static javax.ws.rs.core.Response.Status.ACCEPTED;

@Path("/trade/service")
@Component
public class TradeAdapterRS {

    private final Logger logger = Logger.getLogger(TradeAdapterRS.class);

    private TradePostService tradePostService;

    private TradeStoreService tradeStoreService;

    private TradeAdapterStatistics tradeAdapterStatistics;

    @Value("${endPointUrl}")
    private String endPointUrl;

    @Value("${display.statistics.timeinterval.inmillis}")
    private long statisticsDisplayInterval;

    public void setEndPointUrl(String endPointUrl) {
        this.endPointUrl = endPointUrl;
    }

    public void setStatisticsDisplayInterval(long statisticsDisplayInterval) {
        this.statisticsDisplayInterval = statisticsDisplayInterval;
    }

    @Autowired
    public void setTradePostService(TradePostService tradePostService) {
        this.tradePostService = tradePostService;
    }

    @Autowired
    public void setTradeAdapterStatistics(TradeAdapterStatistics tradeAdapterStatistics) {
        this.tradeAdapterStatistics = tradeAdapterStatistics;
    }

    @Autowired
    public void setTradeStoreService(TradeStoreService tradeStoreService) {
        this.tradeStoreService = tradeStoreService;
    }

    //Daemon Thread to display statistics every 1 min
     {
        Thread t = new Thread(() -> {
            while (true)  {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                logger.info("No of requests Processed "+tradeAdapterStatistics);

                tradeAdapterStatistics.resetCounters();
            }
        });

        t.setDaemon(true);
         t.start();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_XML)
    @Path("/save")
    public Response saveTrade(Trade trade) throws IOException, URISyntaxException {
        logger.info("Trade received: "+trade);
        tradeAdapterStatistics.incrementSaveCounter();
        WebClient requestClient = tradePostService.getWebClient(endPointUrl);
        Response response = requestClient.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(trade);
        storeJsonResponse(trade.getId(), response);
        return response;
    }

    @GET
    @Path("/getStatus/{tradeId}")
    public String getTradeStatus(@PathParam("tradeId") long tradeId)  {
        tradeAdapterStatistics.incrementRetrieveCounter();
        TradeResponseWithStatus tradeResponseWithStatus = null;
        String responseString = "";
        try {
            tradeResponseWithStatus =
                    tradeStoreService.getTradeResponseWithStatusFromTradeStore(tradeId);
            responseString = tradeResponseWithStatus.toString();
        } catch (ExecutionException|TradeAdapterException e) {
            logger.error("Error Retrieving tradeId: "+tradeId);
            logger.error(e.getStackTrace());
            responseString = "Error Retrieving tradeId: "+tradeId;
        }
        logger.debug("getTradeStatus("+tradeId+") :"+responseString);
        return responseString;
    }

    private void storeJsonResponse(long id, Response response) {
        TradeResponse tradeResponse = response.readEntity(TradeResponse.class);
        TradeResponseWithStatus tradeResponseWithStatus;
        if(ACCEPTED.getStatusCode() == response.getStatus())  {
            tradeResponseWithStatus = new TradeResponseWithStatus(SUCCESS, tradeResponse);
        } else  {
            tradeResponseWithStatus = new TradeResponseWithStatus(FAILURE, tradeResponse);
        }
        logger.debug("Adding to cache id:"+id+" value: "+tradeResponseWithStatus);
        tradeStoreService.addToTradeStore(id, tradeResponseWithStatus);
    }

}

package com.tradeservice.restservice;

import com.tradeservice.beans.Trade;
import com.tradeservice.beans.TradeResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.UUID;

@Path("/v1/process")
@Component
public class TradePostServiceRS {

    private final static Logger logger = Logger.getLogger(TradePostServiceRS.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/trade")
    public Response saveTrade(Trade trade)  {

        logger.info("saveTrade received : "+trade);
        Response response = null;
        logger.debug("Trade created: "+trade);
        if(validTrade(trade))  {
            TradeResponse tradeResponse = getJsonResponse("SUCCESS");
            logger.debug("tradeResponse: "+tradeResponse);
            logger.info("request processed successfully and returning response: "+tradeResponse);
            response = Response.ok(tradeResponse).type(MediaType.APPLICATION_JSON).status(Response.Status.ACCEPTED).build();
        } else {
            TradeResponse tradeResponse = getJsonResponse("FAILURE");
            logger.info("Invalid Trade.  Request not processed and returning response: "+tradeResponse);
            response = Response.notModified().entity(tradeResponse)
                    .type(MediaType.APPLICATION_JSON).status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return response;
    }

    /**
     * Only Trades with id > 0 are valid
     * @param trade
     * @return
     */
    private boolean validTrade(Trade trade) {
        return trade.getId() > 0;
    }

    private TradeResponse getJsonResponse(String status) {
        return new TradeResponse(UUID.randomUUID().toString(),
                "Trade Save "+status, System.currentTimeMillis());
    }

    private Trade getJsonTrade(String jsonString) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.readValue(jsonString, Trade.class);
    }
}

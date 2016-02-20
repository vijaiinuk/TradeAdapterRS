package com.tradeservice.restservice;

import com.tradeservice.beans.Trade;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vijay on 2/17/2016.
 */
public class TradeAdapterRSIT {

    private static String endpointUrl;

    private static List<Object> providers = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() {
        endpointUrl = System.getProperty("service.url", "http://localhost:9002/jaxrs-service");
        providers.add(new JacksonJaxbJsonProvider());
    }

    @Test
    public void testSaveValidTrade() throws Exception {
        Trade trade = new Trade(1234567, "EXAMPLE BUYER", "EXAMPLE SELLER", new BigDecimal(200.5));

        WebClient requestClient = WebClient.create(endpointUrl + "/trade/service/save", providers);

        Response r = requestClient.accept("application/json")
                .type("application/xml")
                .post(trade);
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), r.getStatus());

        WebClient responseClient = WebClient.create(endpointUrl + "/trade/service/getStatus/1234567", providers);
        Response r1 = responseClient.accept("plain/text")
                .get();
        System.out.println(r1.readEntity(String.class));
        assertTrue(r1.readEntity(String.class).contains("SUCCESS"));
    }

    @Test
    public void testSaveInvalidTrade() throws Exception {
        Trade trade = new Trade(-1234567, "EXAMPLE BUYER", "EXAMPLE SELLER", new BigDecimal(200.5));

        WebClient requestClient = WebClient.create(endpointUrl + "/trade/service/save", providers);

        Response r = requestClient.accept("application/json")
                .type("application/xml")
                .post(trade);
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), r.getStatus());

        WebClient responseClient = WebClient.create(endpointUrl + "/trade/service/getStatus/-1234567", providers);
        Response r1 = responseClient.accept("plain/text")
                .get();
        System.out.println(r1.readEntity(String.class));
        assertTrue(r1.readEntity(String.class).contains("FAILURE"));
    }


    @Test
    public void testSaveSameTradeRepeatedly() throws Exception {
        Trade trade = new Trade(222222222, "EXAMPLE BUYER", "EXAMPLE SELLER", new BigDecimal(200.5));

        WebClient requestClient = WebClient.create(endpointUrl + "/trade/service/save", providers);

        Response r = requestClient.accept("application/json")
                .type("application/xml")
                .post(trade);
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), r.getStatus());

        //save again
        trade = new Trade(222222222, "EXAMPLE Duplicate BUYER", "EXAMPLE Duplicate SELLER", new BigDecimal(300.5));
        Response rDup = requestClient.accept("application/json")
                .type("application/xml")
                .post(trade);
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), r.getStatus());

        WebClient responseClient = WebClient.create(endpointUrl + "/trade/service/getStatus/222222222", providers);
        Response r1 = responseClient.accept("plain/text")
                .get();
        System.out.println(r1.readEntity(String.class));
        assertTrue(r1.readEntity(String.class).contains("SUCCESS"));
    }

    @Test
    public void testNotFoundTrade() throws Exception {

        WebClient responseClient = WebClient.create(endpointUrl + "/trade/service/getStatus/000000000", providers);
        Response r1 = responseClient.accept("plain/text")
                .get();
        System.out.printf(r1.readEntity(String.class));
        assertTrue(r1.readEntity(String.class).contains("NOT_FOUND"));
    }

}

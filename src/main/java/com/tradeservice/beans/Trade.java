package com.tradeservice.beans;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "Trade")
@XmlAccessorType(XmlAccessType.FIELD)
public class Trade {

    @XmlElement(name = "TradeId")
    @JsonProperty(value = "id")
    private long id;

    @XmlElement(name = "Buyer")
    @JsonProperty(value = "customer")
    private String customer;

    @XmlElement(name = "Seller")
    @JsonProperty(value = "trader")
    private String trader;

    @XmlElement(name = "Price")
    @JsonProperty(value = "price")
    private BigDecimal price;

    public Trade() {
    }

    public Trade(long id, String customer, String trader, BigDecimal price) {
        this.id = id;
        this.customer = customer;
        this.trader = trader;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }



    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", customer='" + customer + '\'' +
                ", trader='" + trader + '\'' +
                ", price=" + price +
                '}';
    }
}

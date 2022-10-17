package org.skhengui.models;

import java.io.Serializable;

public enum Currency implements Serializable {

    EURO("EUR");

    public final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

}


package org.skhengui.models;

import java.io.Serializable;
import java.math.BigDecimal;

public record Balance(BigDecimal amount, Currency currency) implements Serializable {
}

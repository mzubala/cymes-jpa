package pl.com.bottega.cymes.domain.model.pricing;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Money {

    public static final Money ZERO = Money.of(0);

    BigDecimal amount;

    public static Money of(BigDecimal value) {
        return new Money(value.setScale(2));
    }

    public static Money of(double amount) {
        return of(BigDecimal.valueOf(amount));
    }

    public BigDecimal toBigDecimal() {
        return amount;
    }

    public Money times(Integer value) {
        return of(amount.multiply(BigDecimal.valueOf(value)));
    }

    public Money add(Money other) {
        return of(amount.add(other.amount));
    }
}

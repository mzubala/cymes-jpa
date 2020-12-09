package pl.com.bottega.cymes.domain.model.pricing;

import pl.com.bottega.cymes.domain.model.Show;

public interface ShowPricesCalculator {
    PriceList calculate(Show show);
}

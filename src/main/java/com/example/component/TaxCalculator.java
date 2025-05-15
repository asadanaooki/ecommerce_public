package com.example.component;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//TODO:
//パッケージのリファクタリング
@Component
public class TaxCalculator {

    @Value("${settings.tax.rate-percent}")
    private int ratePercent;
    
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    
    public int calculatePriceIncludingTax(int exclTaxPrice) {
        BigDecimal excl = BigDecimal.valueOf(exclTaxPrice);
        BigDecimal incl = excl.multiply(
                BigDecimal.valueOf(ratePercent)
                .divide(HUNDRED)
                .add(BigDecimal.ONE));
        return incl.setScale(0,RoundingMode.FLOOR).intValue();
    }
}

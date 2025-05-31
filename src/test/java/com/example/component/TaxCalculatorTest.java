package com.example.component;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = TaxCalculator.class)
@SpringBootTest
class TaxCalculatorTest {
    // TODO:
    // @SpringBootTestを使わずにやる場合、どう書く？

    @Autowired
    TaxCalculator taxCalculator;

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1000,1100",
            "333,366"
    })
    void calculatePriceIncludingTax(int excl, int expected) {
        assertThat(taxCalculator.calculatePriceIncludingTax(excl)).isEqualTo(expected);
    }

}

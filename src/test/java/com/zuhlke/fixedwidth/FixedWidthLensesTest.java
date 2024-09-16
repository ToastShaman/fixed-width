package com.zuhlke.fixedwidth;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.BiFunction;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class FixedWidthLensesTest {

    @Test
    void zip_combines_two_lenses_correctly() {
        var lens1 = FixedWidthLenses.stringify(5);
        var lens2 = FixedWidthLenses.dateify(DateTimeFormatter.ofPattern("yyyyMMdd"));
        var ctx = ReadContext.of("hello20230101");

        var result = FixedWidthLenses.zip(lens1, lens2, "%s %s"::formatted).apply(ctx);

        assertThat(result).isEqualTo("hello 2023-01-01");
    }

    @Test
    void zip_handles_null_values_correctly() {
        var lens1 = FixedWidthLenses.stringify(5);
        var lens2 = FixedWidthLenses.dateify(DateTimeFormatter.ofPattern("yyyyMMdd"));
        var ctx = ReadContext.of("     20230101");

        BiFunction<String, LocalDate, String> zipper =
                (str, date) -> "%s %s".formatted(str == null ? "NULL" : str, date);

        var result = FixedWidthLenses.zip(lens1, lens2, zipper).apply(ctx);

        assertThat(result).isEqualTo("NULL 2023-01-01");
    }
}

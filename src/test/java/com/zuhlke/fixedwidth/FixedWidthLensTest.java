package com.zuhlke.fixedwidth;

import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class FixedWidthLensTest {

    @Test
    void applies_decode_function_correctly() {
        var lens = FixedWidthLenses.stringify(5);
        var ctx = ReadContext.of("hello");

        assertThat(lens.apply(ctx)).isEqualTo("hello");
    }

    @Test
    void applies_encode_function_correctly() {
        var lens = FixedWidthLenses.stringify(5);
        var ctx = WriteContext.create();

        lens.apply(ctx, "hello");

        assertThat(ctx).hasToString("hello");
    }

    @Test
    void applies_padding_function_correctly() {
        var lens = FixedWidthLenses.stringify(10);
        var ctx = WriteContext.create();

        lens.apply(ctx, "hello");

        assertThat(ctx).hasToString("hello     ");
    }

    @Test
    void applies_padding_function_correctly_for_numbers() {
        var lens = FixedWidthLenses.numberify(5);
        var ctx = WriteContext.create();

        lens.apply(ctx, 42);

        assertThat(ctx).hasToString("00042");
    }

    @Test
    void applies_decode_function_correctly_for_dates() {
        var lens = FixedWidthLenses.dateify(DateTimeFormatter.ofPattern("yyyyMMdd"));
        var ctx = ReadContext.of("20230101");

        assertThat(lens.apply(ctx)).isEqualTo(LocalDate.of(2023, 1, 1));
    }

    @Test
    void applies_encode_function_correctly_for_dates() {
        var lens = FixedWidthLenses.dateify(DateTimeFormatter.ofPattern("yyyyMMdd"));
        var ctx = WriteContext.create();

        lens.apply(ctx, LocalDate.of(2023, 1, 1));

        assertThat(ctx).hasToString("20230101");
    }

    @Test
    void handles_null_value_correctly() {
        var lens = FixedWidthLenses.stringify(5);
        var ctx = WriteContext.create();

        lens.apply(ctx, null);

        assertThat(ctx).hasToString("     ");
    }

    @Test
    void applies_decode_function_correctly_for_booleans() {
        var lens = FixedWidthLenses.booleanify(1);
        var ctx = ReadContext.of("Y");

        assertThat(lens.apply(ctx)).isTrue();
    }

    @Test
    void applies_encode_function_correctly_for_booleans() {
        var lens = FixedWidthLenses.booleanify(1);
        var ctx = WriteContext.create();

        lens.apply(ctx, true);

        assertThat(ctx).hasToString("Y");
    }

    @Test
    void applies_padding_function_correctly_for_booleans() {
        var lens = FixedWidthLenses.booleanify(5);
        var ctx = WriteContext.create();

        lens.apply(ctx, true);

        assertThat(ctx).hasToString("Y    ");
    }

    @Test
    void handles_null_value_correctly_for_booleans() {
        var lens = FixedWidthLenses.booleanify(5);
        var ctx = WriteContext.create();

        lens.apply(ctx, null);

        assertThat(ctx).hasToString("     ");
    }

    @Test
    void andThen_applies_functions_in_sequence_correctly() {
        var lens = FixedWidthLenses.stringify(5);
        var ctx = ReadContext.of("hello");

        var result = lens.andThen(String::toUpperCase).apply(ctx);

        assertThat(result).isEqualTo("HELLO");
    }

    @Test
    void andThen_handles_null_value_correctly() {
        var lens = FixedWidthLenses.stringify(5);
        var ctx = ReadContext.of("     ");

        var result = lens.andThen(String::toUpperCase).apply(ctx);

        assertThat(result).isNull();
    }

    @Test
    void throws_exception_for_invalid_column_width() {
        assertThatThrownBy(() -> new FixedWidthLens<>(0, identity(), identity(), StringUtils::rightPad))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("column width must be greater than 0");
    }
}

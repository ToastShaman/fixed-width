package com.zuhlke.fixedwidth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ReadContextTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -2})
    void throws_exception_when_length_is_zero(int length) {
        var ctx = ReadContext.of("input");

        assertThatThrownBy(() -> ctx.read(length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("length must be greater than 0");
    }

    @Test
    void throws_exception_when_not_enough_data_to_read() {
        var ctx = ReadContext.of("input");

        assertThatThrownBy(() -> ctx.read(10))
                .isInstanceOf(UnderflowException.class)
                .hasMessage("not enough data to read 10 characters at pos 0");
    }

    @Test
    void reads_correctly_when_enough_data_is_available() {
        var ctx = ReadContext.of("input");

        assertThat(ctx.read(3)).isEqualTo("inp");
    }

    @Test
    void reads_correctly_when_exact_data_is_available() {
        var ctx = ReadContext.of("input");

        assertThat(ctx.read(5)).isEqualTo("input");
    }

    @Test
    void reads_correctly_in_multiple_calls() {
        var ctx = ReadContext.of("input");

        assertThat(ctx.read(2)).isEqualTo("in");
        assertThat(ctx.read(2)).isEqualTo("pu");
        assertThat(ctx.read(1)).isEqualTo("t");
    }
}

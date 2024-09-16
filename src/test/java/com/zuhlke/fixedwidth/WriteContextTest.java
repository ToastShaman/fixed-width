package com.zuhlke.fixedwidth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class WriteContextTest {

    @Test
    void writes_single_value_correctly() {
        var ctx = WriteContext.create();
        ctx.write("value");

        assertThat(ctx).hasToString("value");
    }

    @Test
    void writes_multiple_values_correctly() {
        var ctx = WriteContext.create();
        ctx.write("value1");
        ctx.write("value2");

        assertThat(ctx).hasToString("value1value2");
    }

    @Test
    void writes_empty_string_correctly() {
        var ctx = WriteContext.create();
        ctx.write("");

        assertThat(ctx).hasToString("");
    }

    @Test
    void writes_null_value_as_empty_string() {
        var ctx = WriteContext.create();
        ctx.write(null);

        assertThat(ctx).hasToString("");
    }
}

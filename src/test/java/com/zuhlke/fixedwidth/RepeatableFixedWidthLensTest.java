package com.zuhlke.fixedwidth;

import static com.zuhlke.fixedwidth.FixedWidthLenses.stringify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class RepeatableFixedWidthLensTest {

    @Test
    void applies_decode_function_correctly() {
        var lens = new RepeatableFixedWidthLens<>(2, stringify(3));
        var ctx = ReadContext.of("02abcdef");

        assertThat(lens.apply(ctx)).containsExactly("abc", "def");
    }

    @Test
    void applies_encode_function_correctly() {
        var lens = new RepeatableFixedWidthLens<>(2, stringify(3));
        var ctx = WriteContext.create();

        lens.apply(ctx, List.of("abc", "de"));

        assertThat(ctx).hasToString("02abcde ");
    }

    @Test
    void handles_empty_list_correctly() {
        var lens = new RepeatableFixedWidthLens<>(2, stringify(3));
        var ctx = WriteContext.create();

        lens.apply(ctx, List.of());

        assertThat(ctx).hasToString("00");
    }

    @Test
    void throws_exception_for_invalid_column_width() {
        assertThatThrownBy(() -> new RepeatableFixedWidthLens<>(0, stringify(3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("column width must be greater than 0");
    }

    @Test
    void throws_exception_when_reader_is_null() {
        assertThatThrownBy(() -> new RepeatableFixedWidthLens<>(2, null, (ctx, val) -> {}))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("reader must not be null");
    }

    @Test
    void throws_exception_when_writer_is_null() {
        assertThatThrownBy(() -> new RepeatableFixedWidthLens<>(2, ctx -> "", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("writer must not be null");
    }

    @Test
    void throws_exception_when_value_exceeds_column_width() {
        var lens = FixedWidthLenses.stringify(1);

        var ctx = WriteContext.create();

        assertThatThrownBy(() -> lens.apply(ctx, "exceeds"))
                .isInstanceOf(ColumLengthExceededException.class)
                .hasMessage("length of 7 exceeds column size of 1 for [exceeds]");
    }
}

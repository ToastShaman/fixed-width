package com.zuhlke.fixedwidth;

import static java.util.Objects.requireNonNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FixedWidthLens<T> {

    private final int columnWidth;

    private final Function<String, T> decode;

    private final Function<T, String> encode;

    private final BiFunction<String, Integer, String> padding;

    public FixedWidthLens(
            int columnWidth,
            Function<String, T> decode,
            Function<T, String> encode,
            BiFunction<String, Integer, String> padding) {
        this.encode = requireNonNull(encode, "encode must not be null");
        this.decode = requireNonNull(decode, "decode must not be null");
        this.padding = requireNonNull(padding, "padding must not be null");
        this.columnWidth = columnWidth;
        if (columnWidth <= 0) {
            throw new IllegalArgumentException("column width must be greater than 0");
        }
    }

    public T apply(ReadContext readContext) {
        var read = readContext.read(columnWidth);
        var trimmed = read.trim();
        return trimmed.isEmpty() ? null : decode.apply(trimmed);
    }

    public void apply(WriteContext writeContext, T value) {
        var encoded = value == null ? "" : encode.apply(value);

        if (encoded.length() > columnWidth) {
            throw new ColumLengthExceededException("length of %d exceeds column size of %d for [%s]"
                    .formatted(encoded.length(), columnWidth, encoded));
        }

        var text = padding.apply(encoded, columnWidth);
        writeContext.write(text);
    }

    public <R> Function<ReadContext, R> andThen(Function<T, R> encode) {
        return ctx -> {
            var value = apply(ctx);
            return value == null ? null : encode.apply(value);
        };
    }
}

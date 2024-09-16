package com.zuhlke.fixedwidth;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class RepeatableFixedWidthLens<T> {

    private final int columnWidth;
    private final Function<ReadContext, T> reader;
    private final BiConsumer<WriteContext, T> writer;

    public RepeatableFixedWidthLens(int columnWidth, FixedWidthLens<T> lens) {
        this(columnWidth, lens::apply, lens::apply);
    }

    public RepeatableFixedWidthLens(
            int columnWidth, Function<ReadContext, T> reader, BiConsumer<WriteContext, T> writer) {
        this.columnWidth = columnWidth;
        this.reader = requireNonNull(reader, "reader must not be null");
        this.writer = requireNonNull(writer, "writer must not be null");
        if (columnWidth <= 0) {
            throw new IllegalArgumentException("column width must be greater than 0");
        }
    }

    public List<T> apply(ReadContext context) {
        var numberOfItems = context.read(columnWidth);
        var maxSize = numberOfItems.isBlank() ? 0 : Integer.parseInt(numberOfItems);
        return Stream.generate(() -> reader.apply(context)).limit(maxSize).toList();
    }

    public void apply(WriteContext context, List<T> values) {
        var numberOfItems = String.valueOf(values.size());
        var padded = StringUtils.leftPad(numberOfItems, columnWidth, '0');
        context.write(padded);
        values.forEach(value -> writer.accept(context, value));
    }
}

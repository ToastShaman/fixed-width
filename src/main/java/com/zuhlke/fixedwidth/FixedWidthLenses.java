package com.zuhlke.fixedwidth;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class FixedWidthLenses {

    private FixedWidthLenses() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static FixedWidthLens<String> stringify(int columnWidth) {
        return new FixedWidthLens<>(columnWidth, it -> it, it -> it, StringUtils::rightPad);
    }

    public static FixedWidthLens<Integer> numberify(int columnWidth) {
        return new FixedWidthLens<>(
                columnWidth,
                Integer::parseInt,
                String::valueOf,
                (text, length) -> StringUtils.leftPad(text, length, '0'));
    }

    public static FixedWidthLens<Boolean> booleanify(int columnWidth) {
        return new FixedWidthLens<>(
                columnWidth,
                text -> BooleanUtils.toBoolean(text, "Y", "N"),
                text -> BooleanUtils.toString(text, "Y", "N"),
                StringUtils::rightPad);
    }

    public static FixedWidthLens<LocalDate> dateify(DateTimeFormatter formatter) {
        return new FixedWidthLens<>(
                8, text -> LocalDate.parse(text, formatter), date -> date.format(formatter), StringUtils::rightPad);
    }

    public static <T1, T2, R> Function<ReadContext, R> zip(
            FixedWidthLens<T1> lens1, FixedWidthLens<T2> lens2, BiFunction<T1, T2, R> zipper) {
        return ctx -> {
            var t1 = lens1.apply(ctx);
            var t2 = lens2.apply(ctx);
            return zipper.apply(t1, t2);
        };
    }
}

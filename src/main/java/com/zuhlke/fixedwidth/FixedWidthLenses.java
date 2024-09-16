package com.zuhlke.fixedwidth;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        return booleanify(columnWidth, "Y", "N");
    }

    public static FixedWidthLens<Boolean> booleanify(int columnWidth, String yesString, String falseString) {
        return new FixedWidthLens<>(
                columnWidth,
                text -> BooleanUtils.toBoolean(text, yesString, falseString),
                text -> BooleanUtils.toString(text, yesString, falseString),
                StringUtils::rightPad);
    }

    public static FixedWidthLens<LocalDate> dateify(DateTimeFormatter formatter) {
        return new FixedWidthLens<>(
                8, text -> LocalDate.parse(text, formatter), date -> date.format(formatter), StringUtils::rightPad);
    }

    public static <T1, T2, R> Function1<ReadContext, R> zip(
            FixedWidthLens<T1> lens1, FixedWidthLens<T2> lens2, Function2<T1, T2, R> zipper) {
        return ctx -> zipper.apply(lens1.apply(ctx), lens2.apply(ctx));
    }

    public static <T1, T2, T3, R> Function1<ReadContext, R> zip(
            FixedWidthLens<T1> lens1,
            FixedWidthLens<T2> lens2,
            FixedWidthLens<T3> lens3,
            Function3<T1, T2, T3, R> zipper) {
        return ctx -> zipper.apply(lens1.apply(ctx), lens2.apply(ctx), lens3.apply(ctx));
    }

    public static <T1, T2, T3, T4, R> Function1<ReadContext, R> zip(
            FixedWidthLens<T1> lens1,
            FixedWidthLens<T2> lens2,
            FixedWidthLens<T3> lens3,
            FixedWidthLens<T4> lens4,
            Function4<T1, T2, T3, T4, R> zipper) {
        return ctx -> zipper.apply(lens1.apply(ctx), lens2.apply(ctx), lens3.apply(ctx), lens4.apply(ctx));
    }
}

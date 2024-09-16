package com.zuhlke.fixedwidth;

public interface ReadContext {

    String read(int length);

    static ReadContext of(String input) {
        return new ReadContext() {

            private int pos = 0;

            @Override
            public String read(int length) {
                if (length <= 0) {
                    throw new IllegalArgumentException("length must be greater than 0");
                }

                if (pos + length > input.length()) {
                    throw new UnderflowException(
                            "not enough data to read %d characters at pos %d".formatted(length, pos));
                }

                var result = input.substring(pos, pos + length);

                pos += length;

                return result;
            }
        };
    }
}

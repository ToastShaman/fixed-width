package com.zuhlke.fixedwidth;

public interface WriteContext {

    void write(String field);

    static WriteContext create() {
        return new WriteContext() {
            private final StringBuilder output = new StringBuilder();

            @Override
            public void write(String field) {
                if (field != null) {
                    output.append(field);
                }
            }

            @Override
            public String toString() {
                return output.toString();
            }
        };
    }
}

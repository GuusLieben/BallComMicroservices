package com.ball.gateway.config.filters;

public enum Role {
    CUSTOMER(1<<0), SUPPLIER(1<<1), EMPLOYEE(1<<2), SYSTEM(1<<4);

    private final int i;

    Role(int i) {
        this.i = i;
    }

    public int getI() {
        return this.i;
    }

    public static long getStatusValue(Role... flags) {
        long value=0;
        for (Role flag : flags) {
            value |= flag.i;
        }
        return value;
    }
}

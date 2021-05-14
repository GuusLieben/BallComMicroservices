package com.ball.auth.model;

import java.util.EnumSet;

public enum UserRole {
    CUSTOMER(1<<0), SUPPLIER(1<<1), EMPLOYEE(1<<2), SYSTEM(1<<4);

    private final int i;

    UserRole(int i) {
        this.i = i;
    }

    public int getI() {
        return this.i;
    }

    public static long getStatusValue(UserRole... flags) {
        long value=0;
        for (UserRole flag : flags) {
            value |= flag.i;
        }
        return value;
    }

    public static EnumSet<UserRole> getStatusFlags(long statusValue) {
        EnumSet<UserRole> statusFlags = EnumSet.noneOf(UserRole.class);
        for (UserRole flag : UserRole.values()) {
            long flagValue = flag.i;
            if ( (flagValue&statusValue ) == flagValue ) {
                statusFlags.add(flag);
            }
        }
        return statusFlags;
    }
}

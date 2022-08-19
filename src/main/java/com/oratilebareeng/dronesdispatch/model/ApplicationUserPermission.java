package com.oratilebareeng.dronesdispatch.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApplicationUserPermission {
    DRONE_READ("drone:read"),
    DRONE_WRITE("drone:write"),
    MEDICATION_READ("medication:read"),
    MEDICATION_WRITE("medication:write");

    private final String permission;
}

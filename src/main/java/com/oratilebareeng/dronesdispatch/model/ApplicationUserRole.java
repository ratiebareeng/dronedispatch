package com.oratilebareeng.dronesdispatch.model;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public enum ApplicationUserRole {
    USER(Sets.newHashSet(
            ApplicationUserPermission.DRONE_READ,
            ApplicationUserPermission.MEDICATION_READ
    )),
    ADMIN(Sets.newHashSet(ApplicationUserPermission.DRONE_READ,
            ApplicationUserPermission.DRONE_WRITE,
            ApplicationUserPermission.MEDICATION_WRITE,
            ApplicationUserPermission.MEDICATION_READ));

    private final Set<ApplicationUserPermission> permissions;



}

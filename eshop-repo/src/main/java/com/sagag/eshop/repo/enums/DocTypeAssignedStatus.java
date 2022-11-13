package com.sagag.eshop.repo.enums;

import lombok.Getter;

@Getter
public enum DocTypeAssignedStatus {
    IN_ACTIVE(0), ACTIVE(1);

    private final int value;

    private DocTypeAssignedStatus(int value) {
        this.value = value;
    }

}

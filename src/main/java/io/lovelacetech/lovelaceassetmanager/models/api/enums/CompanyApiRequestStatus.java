package io.lovelacetech.lovelaceassetmanager.models.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum CompanyApiRequestStatus {
    OK(0),
    INVALID_INPUT(1),
    UNKNOWN_ERROR(2),
    NOT_FOUND(3),
    COMPANY_ALREADY_EXISTS(4);

    private static Map<Integer, CompanyApiRequestStatus> valueMap = null;

    private int value;

    CompanyApiRequestStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CompanyApiRequestStatus map(int key) {
        if (valueMap == null) {
            valueMap = new HashMap<>();

            for (CompanyApiRequestStatus status : CompanyApiRequestStatus.values()) {
                valueMap.put(status.getValue(), status);
            }
        }

        return valueMap.getOrDefault(key, CompanyApiRequestStatus.UNKNOWN_ERROR);
    }
}

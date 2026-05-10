package com.commerce.project.domain;

import lombok.Getter;

@Getter
public enum RewardType {
    CASH("현금"),
    PRODUCT("제품"),
    SERVICE("서비스"),
    ETC("기타");

    private final String label;

    RewardType(String label) {
        this.label = label;
    }
}

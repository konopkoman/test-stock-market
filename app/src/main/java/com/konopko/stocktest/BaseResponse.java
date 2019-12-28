package com.konopko.stocktest;

import androidx.annotation.Nullable;

public class BaseResponse {

    private String error;

    @Nullable
    public String getError() {
        return error;
    }
}

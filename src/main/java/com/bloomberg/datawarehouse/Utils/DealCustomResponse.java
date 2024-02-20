package com.bloomberg.datawarehouse.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealCustomResponse<T> {
    private int statusCode;
    private String detailedStatusCode;
    private T data;
}
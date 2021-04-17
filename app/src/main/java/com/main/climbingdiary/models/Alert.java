package com.main.climbingdiary.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Alert {
    private String title;
    private String message;
    private int dialogType;
}

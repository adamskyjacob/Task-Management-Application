package com.adamsky.WebSocket.Activity;

import lombok.Data;

@Data
public class ActivityRequest {
    private Long userId;
    private  String token;
    private ActivityType type;
    public ActivityRequest(String token, String type,Long userId) {
        this.userId = userId;
        this.token = token;
        this.type = ActivityType.valueOf(type);
    }
}

package com.project.karenbot.dto;

import lombok.Data;

@Data
public class Client {
    private Long id;
    private String name;
    private String ip;
    private String mac;
    private String ssid;
}

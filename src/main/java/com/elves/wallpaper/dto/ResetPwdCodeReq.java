package com.elves.wallpaper.dto;

import lombok.Data;

@Data
public class ResetPwdCodeReq {
    private String code;
    private String email;
}

package com.navino.jenkinshelper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangZhuoWen
 * @ClassName NaviUsers
 * @date 2021/1/21 10:14
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaviUsers {

    private int userId;

    private String name;

    private String email;
}

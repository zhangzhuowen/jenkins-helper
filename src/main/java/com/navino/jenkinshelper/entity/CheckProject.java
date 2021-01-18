package com.navino.jenkinshelper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangZhuoWen
 * @ClassName CheckProject
 * @date 2021/1/17 0:41
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckProject {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String projectType;
    private String name;

}

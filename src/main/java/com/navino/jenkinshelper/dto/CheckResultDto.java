package com.navino.jenkinshelper.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author zhangZhuoWen
 * @ClassName CheckResultDto
 * @date 2021/1/18 8:57
 * @Description TODO
 */
@Data
public class CheckResultDto {

    @ExcelProperty("项目")
    private String projectName;

    @ExcelProperty("阻断")
    private Integer blocker;

    @ExcelProperty("严重")
    private Integer critical;

    @ExcelProperty("主要")
    private Integer major;

    @ExcelProperty("负责人")
    private String chargePerson;

}

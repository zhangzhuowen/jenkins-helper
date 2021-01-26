package com.navino.jenkinshelper.controller;

import com.navino.jenkinshelper.service.ExcelExportService;
import com.navino.jenkinshelper.service.SonarqubeCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangZhuoWen
 * @ClassName CheckResultController
 * @date 2021/1/18 8:37
 * @Description Sonarqube代码检测执行结果
 */
@RestController
public class CheckResultController {
//    @Autowired
//    ExcelExportService excelExportService;

    @RequestMapping("/build")
    public String getResults() throws Exception {
//        excelExportService.writeExcel();
        return null;
    }

    @RequestMapping("/hello")
    public String hello() throws Exception {
        return System.getProperty("user.dir");
    }

}

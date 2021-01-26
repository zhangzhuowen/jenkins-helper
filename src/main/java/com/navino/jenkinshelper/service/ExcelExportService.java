package com.navino.jenkinshelper.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.navino.jenkinshelper.constants.ProjectTypeConstant;
import com.navino.jenkinshelper.dto.CheckResultDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangZhuoWen
 * @ClassName EasyExcelExportService
 * @date 2021/1/18 8:53
 * @Description TODO
 */

@Slf4j
@Service
public class ExcelExportService {

    public static final String PATH = System.getProperty("user.dir");

    @Autowired
    SonarqubeCheck sonarqubeCheck;

    //表示每个星期五上午7点
    @Scheduled(cron = "0 0 07 ? * FRI")
    public void writeExcel() throws Exception{
        log.info("开始生成Excel!");

        Map<String, List<CheckResultDto>> data = sonarqubeCheck.getData();
        if(MapUtils.isEmpty(data)){
            return;
        }

        String fileName = PATH + "\\excel\\Sonarqube代码检测结果一览表.xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();

        if(data.containsKey(ProjectTypeConstant.ONE_MAP_PROJECT)){

            List<CheckResultDto> onemapData = data.get(ProjectTypeConstant.ONE_MAP_PROJECT);
            WriteSheet writeSheet = EasyExcel.writerSheet(0, ProjectTypeConstant.ONE_MAP_PROJECT).head(CheckResultDto.class).build();
            excelWriter.write(onemapData, writeSheet);
        }

        if(data.containsKey(ProjectTypeConstant.PARK_PROJECT)){

            List<CheckResultDto> parkData = data.get(ProjectTypeConstant.PARK_PROJECT);
            WriteSheet writeSheet = EasyExcel.writerSheet(1, ProjectTypeConstant.PARK_PROJECT).head(CheckResultDto.class).build();
            excelWriter.write(parkData, writeSheet);
        }
        excelWriter.finish();
    }

}

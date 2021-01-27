package com.navino.jenkinshelper.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.navino.jenkinshelper.constants.ProjectTypeConstant;
import com.navino.jenkinshelper.dao.NaviUsersDao;
import com.navino.jenkinshelper.dto.CheckResultDto;
import com.navino.jenkinshelper.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

    public static final String PATH = System.getProperty("user.dir") + "/excel/";

    @Autowired
    SonarqubeCheck sonarqubeCheck;

    @Autowired
    NaviUsersDao naviUsersDao;

    @Value("${jenkins.oneMap-url}")
    public String onemapUrl;

    @Value("${navi-email.account}")
    public String account;

    @Value("${navi-email.username}")
    public String username;

    @Value("${navi-email.password}")
    public String password;

    //表示每个星期五上午7点
    @Scheduled(cron = "0 0 07 ? * FRI")
    public void writeExcel() throws Exception {
        log.info("开始生成Excel!");

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        String time = dateTime.format(formatter);

        Map<String, List<CheckResultDto>> data = sonarqubeCheck.getData();
        if (MapUtils.isEmpty(data)) {
            return;
        }

        File f = new File(PATH);
        if(!f.exists()){
            f.mkdirs(); //创建目录
        }

        String fileName = PATH + "Sonarqube代码检测结果一览表" + time + ".xlsx";
        log.info("fileName : " + fileName);

        ExcelWriter excelWriter = EasyExcel.write(fileName).build();

        if (data.containsKey(ProjectTypeConstant.ONE_MAP_PROJECT)) {

            List<CheckResultDto> onemapData = data.get(ProjectTypeConstant.ONE_MAP_PROJECT);
            WriteSheet writeSheet = EasyExcel.writerSheet(0, ProjectTypeConstant.ONE_MAP_PROJECT).head(CheckResultDto.class).build();
            excelWriter.write(onemapData, writeSheet);
        }

        if (data.containsKey(ProjectTypeConstant.PARK_PROJECT)) {

            List<CheckResultDto> parkData = data.get(ProjectTypeConstant.PARK_PROJECT);
            WriteSheet writeSheet = EasyExcel.writerSheet(1, ProjectTypeConstant.PARK_PROJECT).head(CheckResultDto.class).build();
            excelWriter.write(parkData, writeSheet);
        }
        excelWriter.finish();

        //邮件发送
        sendExcel(fileName, dateTime);
    }

    /**
     * 邮件发送
     *
     * @param fileName
     * @throws Exception
     */
    private void sendExcel(String fileName, LocalDateTime dateTime) throws Exception {
        List<String> allEmail = naviUsersDao.getAllEmail();
        if (CollectionUtils.isEmpty(allEmail)) {
            return;
        }
        //List<String> allEmail = Arrays.asList("zhangzhuowen@navinfo.com");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日");
        String time = dateTime.format(formatter);

        String title = "【" + time + "】" + "Sonarqube代码检测结果";

        File file = new File(fileName);
        MailUtil.sendEMailFile(allEmail, "请查看附件！", account, username, password, title, file, "Sonarqube代码检测结果一览表.xlsx");
    }

}

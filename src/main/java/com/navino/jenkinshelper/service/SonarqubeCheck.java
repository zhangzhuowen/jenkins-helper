package com.navino.jenkinshelper.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.navino.jenkinshelper.constants.ProjectTypeConstant;
import com.navino.jenkinshelper.dao.CheckProjectDao;
import com.navino.jenkinshelper.dto.CheckResultDto;
import com.navino.jenkinshelper.entity.CheckProject;
import com.navino.jenkinshelper.service.sync.AsyncSonarTask;
import com.navino.jenkinshelper.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangZhuoWen
 * @ClassName SonarqubeCheck
 * @date 2021/1/18 9:18
 * @Description TODO
 */
@Service
@Slf4j
public class SonarqubeCheck {

    @Autowired
    CheckProjectDao checkProjectDao;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private AsyncSonarTask asyncService;

    @Value("${jenkins.oneMap-url}")
    public String onemapUrl;

    @Value("${jenkins.park-url}")
    public String parkUrl;

    @Value("${jenkins.build-suffix}")
    public String buildSuffix;

    @Value("${jenkins.monitor-suffix}")
    public String monitorSuffix;

    @Value("${jenkins.sonarqube-url}")
    public String sonarqubeUrl;

    @Value("${jenkins.sonarqube-suffix}")
    public String sonarqubeSuffix;

    @Value("${jenkins.cookie}")
    public String cookie;

    public Map<String, List<CheckResultDto>> getData() throws Exception {

        //1.查询所有Jenkins中所配置项目名称
        List<CheckProject> checkProjects = checkProjectDao.queryAllCheckProjects();

        if (CollectionUtils.isEmpty(checkProjects)) {
            return null;
        }

        //2.异步发送请求进行打包代码检测
        for (CheckProject project : checkProjects) {
            String jenkinsName = project.getJenkinsName();

            //Jenkins配置有问题，会重复构建，待修复
            if(jenkinsName.equals("service_baa_master")){
                continue;
            }

            //打包构建url
            String buildUrl = onemapUrl + jenkinsName + buildSuffix;
            //判断构建完成url
            String lastBuilUrl = onemapUrl + jenkinsName + monitorSuffix;
            asyncService.executeAsync(buildUrl, lastBuilUrl, jenkinsName);
        }

        //判断线程是否全部执行结束
        while (threadPoolTaskExecutor.getActiveCount() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //3.jenkins执行结束后请求sonarqube查询bug数量
        Map<String, List<CheckResultDto>> statics = getSonarqubeBugStatics(checkProjects);
        return statics;
    }

    /**
     * 请求SonarQube接口查询bug数量
     *
     * @param checkProjects
     * @return
     */
    private Map<String, List<CheckResultDto>> getSonarqubeBugStatics(List<CheckProject> checkProjects) {
        Map<String, List<CheckResultDto>> map = new HashMap<>();

        List<CheckResultDto> oneMapDatas = new ArrayList<>();
        List<CheckResultDto> parkDatas = new ArrayList<>();

        for (CheckProject project : checkProjects) {
            String projectName = project.getJavaName();
            String sonarName = project.getSonarName();
            String projectType = project.getProjectType();
            String chargePerson = project.getNaviUsers().getName();

            //sonarqube url
            String sonarUrl = sonarqubeUrl + sonarName + sonarqubeSuffix;
            log.info("项目: " + projectName + ",sonarqubeUrl : " + sonarqubeUrl);

            String str = HttpUtils.sendGetRequest(sonarUrl, "utf-8", cookie);

            if (StringUtils.isEmpty(str)) {
                //先排查cookie问题
                log.info("项目: " + projectName + ",sonarqubeUrl 无返回值！请检查");
            }

            JSONObject data = JSONObject.parseObject(str);
            JSONArray facets = data.getJSONArray("facets");
            JSONArray values = facets.getJSONObject(0).getJSONArray("values");

            CheckResultDto checkResultDto = new CheckResultDto();
            checkResultDto.setProjectName(projectName);
            checkResultDto.setChargePerson(chargePerson);

            for (int i = 0; i < values.size(); i++) {
                JSONObject obj = values.getJSONObject(i);

                String val = obj.getString("val");
                Integer count = obj.getInteger("count");

                //主要
                if ("MAJOR".equals(val)) {
                    checkResultDto.setMajor(count);
                }

                //严重
                if ("CRITICAL".equals(val)) {
                    checkResultDto.setCritical(count);
                }

                //阻断
                if ("BLOCKER".equals(val)) {
                    checkResultDto.setBlocker(count);
                }
            }

            if (ProjectTypeConstant.ONE_MAP_PROJECT.equals(projectType)) {
                oneMapDatas.add(checkResultDto);
            } else if (ProjectTypeConstant.PARK_PROJECT.equals(projectType)) {
                parkDatas.add(checkResultDto);
            }
        }
        map.put(ProjectTypeConstant.ONE_MAP_PROJECT, oneMapDatas);
        map.put(ProjectTypeConstant.PARK_PROJECT, parkDatas);
        return map;
    }
}

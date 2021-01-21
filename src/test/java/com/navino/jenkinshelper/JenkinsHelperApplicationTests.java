package com.navino.jenkinshelper;

import com.navino.jenkinshelper.dao.CheckProjectDao;
import com.navino.jenkinshelper.entity.CheckProject;
import com.navino.jenkinshelper.mapper.CheckProjectMapper;
import com.navino.jenkinshelper.service.ExcelExportService;
import com.navino.jenkinshelper.service.SonarqubeCheck;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class JenkinsHelperApplicationTests {
    @Autowired
    CheckProjectDao checkProjectDao;

    @Autowired
    ExcelExportService excelExportService;

    @Value("${jenkins.sonarqube-suffix}")
    public String sonarqubeSuffix;

    @Test
    void testCheck() throws Exception {
        excelExportService.writeExcel();
    }

    @Autowired
    CheckProjectMapper checkProjectMapper;

    @Test
    void ada() throws Exception {
        List<CheckProject> checkProjects =
                checkProjectMapper.queryAllCheckProjects();

        int a =1;
    }
//    /**
//     * 维护项目列表
//     *
//     */
//    @Test
//    void insert() {
//        List<String> list = Arrays.asList(
////                "navi_commons_master",
////                "service_3D_master",
////                "service_authcenter_master",
////                "service_baa_master",
////                "service_biz_common_master",
////                "service_collector_master",
////                "service_databrowser_master",
////                "service_dealership_master",
////                "service_dms_master",
////                "service_editor_master",
////                "service_limit_master",
////                "service_man_master",
////                "service_meta_master",
////                "service_openapi-dataview_master",
////                "service_openapi-edit_master",
////                "service_openapi-sap_master",
////                "service_openeditor_master",
////                "service_plan_master",
////                "service_pre_master",
////                "service_program_master",
////                "service_robot_master",
////                "service_statics_master"
//                "navi_commons_park",
//                "openapi_dataview_park",
//                "openapi_edit_park",
//                "openapi_tools_park",
//                "park-man",
//                "park_collector",
//                "park_meta_master",
//                "park_openapi_sap",
//                "park_sap_offline",
//                "park_sap_realtime",
//                "park_service_statics",
//                "service_biz_common_park",
//                "service_park_editor",
//                "service_sys_park"
//
//        );
//
//        for (String name : list) {
//            CheckProject checkProject = new CheckProject();
//            checkProject.setName(name);
//            checkProject.setProjectType(ProjectTypeConstant.PARK_PROJECT);
//
//            checkProjectDao.insert(checkProject);
//        }
//    }


}

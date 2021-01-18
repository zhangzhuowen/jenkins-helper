package com.navino.jenkinshelper;

import com.navino.jenkinshelper.constants.ProjectTypeConstant;
import com.navino.jenkinshelper.dao.CheckProjectDao;
import com.navino.jenkinshelper.entity.CheckProject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class JenkinsHelperApplicationTests {
    @Autowired
    DataSource dataSource;

    @Autowired
    CheckProjectDao checkProjectDao;

    @Test
    void contextLoads() {
        System.out.println(dataSource.getClass());
    }

    /**
     * 维护项目列表
     *
     */
    @Test
    void insert() {
        List<String> list = Arrays.asList(
//                "navi_commons_master",
//                "service_3D_master",
//                "service_authcenter_master",
//                "service_baa_master",
//                "service_biz_common_master",
//                "service_collector_master",
//                "service_databrowser_master",
//                "service_dealership_master",
//                "service_dms_master",
//                "service_editor_master",
//                "service_limit_master",
//                "service_man_master",
//                "service_meta_master",
//                "service_openapi-dataview_master",
//                "service_openapi-edit_master",
//                "service_openapi-sap_master",
//                "service_openeditor_master",
//                "service_plan_master",
//                "service_pre_master",
//                "service_program_master",
//                "service_robot_master",
//                "service_statics_master"
                "navi_commons_park",
                "openapi_dataview_park",
                "openapi_edit_park",
                "openapi_tools_park",
                "park-man",
                "park_collector",
                "park_meta_master",
                "park_openapi_sap",
                "park_sap_offline",
                "park_sap_realtime",
                "park_service_statics",
                "service_biz_common_park",
                "service_park_editor",
                "service_sys_park"

        );

        for (String name : list) {
            CheckProject checkProject = new CheckProject();
            checkProject.setName(name);
            checkProject.setProjectType(ProjectTypeConstant.PARK_PROJECT);

            checkProjectDao.insert(checkProject);
        }
    }


}

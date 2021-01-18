package com.navino.jenkinshelper.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.navino.jenkinshelper.entity.CheckProject;
import com.navino.jenkinshelper.mapper.CheckProjectMapper;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangZhuoWen
 * @ClassName CheckProjectDao
 * @date 2021/1/17 0:58
 * @Description TODO
 */
@Slf4j
@Repository
public class CheckProjectDao {

    @Autowired
    private CheckProjectMapper mapper;

    /**
     * insert
     *
     * @param checkProject
     */
    public void insert(CheckProject checkProject){
        mapper.insert(checkProject);
    }

    /**
     * queryAllProjectNames
     *
     * @return
     */
    public List<CheckProject> queryAllProjectNames(){
        return mapper.selectList(null);
    }

    /**
     * queryProNamesByProType
     *
     * @param projectType
     * @return
     */
    public List<CheckProject> queryProNamesByProType(String projectType){
        if(StringUtils.isEmpty(projectType)){
            return null;
        }

        QueryWrapper<CheckProject> wrapper = new QueryWrapper<>();
        wrapper.eq("project_type",projectType);

        return mapper.selectList(wrapper);
    }
}

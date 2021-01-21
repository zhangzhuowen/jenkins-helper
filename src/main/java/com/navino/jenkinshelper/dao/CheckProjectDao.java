package com.navino.jenkinshelper.dao;

import com.navino.jenkinshelper.entity.CheckProject;
import com.navino.jenkinshelper.mapper.CheckProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

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
     * queryAllCheckProjects
     *
     * @return
     */
    public List<CheckProject> queryAllCheckProjects(){
        return mapper.queryAllCheckProjects();
    }

}

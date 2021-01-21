package com.navino.jenkinshelper.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.navino.jenkinshelper.entity.CheckProject;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CheckProjectMapper
 *
 */
@Repository
public interface CheckProjectMapper extends BaseMapper<CheckProject> {
    List<CheckProject> queryAllCheckProjects();
}

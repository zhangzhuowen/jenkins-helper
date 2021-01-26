package com.navino.jenkinshelper.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.navino.jenkinshelper.entity.CheckProject;
import com.navino.jenkinshelper.entity.NaviUsers;
import com.navino.jenkinshelper.mapper.NaviUsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangZhuoWen
 * @ClassName NaviUsersDao
 * @date 2021/1/26 16:58
 * @Description TODO
 */
@Slf4j
@Repository
public class NaviUsersDao {

    @Autowired
    NaviUsersMapper naviUsersMapper;

    public List<String> getAllEmail(){
        List<String> emails = new ArrayList<>();

        List<NaviUsers> naviUsers = naviUsersMapper.selectList(null);
        if(CollectionUtils.isEmpty(naviUsers)){
            return emails;
        }

        for (NaviUsers user : naviUsers) {
            String email = user.getEmail();
            if(StringUtils.isNotEmpty(email)){
                emails.add(user.getEmail());
            }
        }
        return emails;
    }

}

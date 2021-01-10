package com.ufak.bar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.bar.entity.PostBar;
import com.ufak.bar.entity.UserBar;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import java.util.List;
import java.util.Map;

/**
 * @Description: 贴吧
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
public interface PostBarMapper extends BaseMapper<PostBar> {

    @Insert("INSERT INTO `t_user_bar` (`id`, `user_id`, `post_bar_id`) VALUES (#{id}, #{userId}, #{postBarId})")
    void insertUserBar(UserBar userBar);

    @Delete("delete from t_user_bar where user_id=#{userId} and post_bar_id=#{postBarId}")
    void deleteUserBar(UserBar userBar);

    List<PostBar> selectPage(Map paramMap);

    long totalCount(Map paramMap);

}

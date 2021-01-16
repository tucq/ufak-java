package com.ufak.bar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.bar.entity.PostBar;
import com.ufak.bar.entity.UserBar;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Insert("INSERT INTO `t_bar_likes` (`id`, `user_id`, `post_bar_id`) VALUES (#{id}, #{userId}, #{postBarId})")
    void insertBarLikes(UserBar userBar);

    @Delete("delete from t_bar_likes where user_id=#{userId} and post_bar_id=#{postBarId}")
    void deleteBarLikes(UserBar userBar);

    @Select("select count(1) from t_user_bar where post_bar_id=#{value}")
    int countFans(String postBarId);

    @Select("select count(1) from t_bar_likes where post_bar_id=#{value}")
    int countLikes(String postBarId);

    @Select("select count(1) from t_user_bar where post_bar_id=#{postBarId} and user_id = #{userId}")
    int isExistFans(@Param("postBarId") String postBarId, @Param("userId") String userId);

    @Select("select count(1) from t_bar_likes where post_bar_id=#{postBarId} and user_id = #{userId}")
    int isExistLikes(@Param("postBarId") String postBarId, @Param("userId") String userId);

    List<PostBar> selectPage(Map paramMap);

    long totalCount(Map paramMap);

}

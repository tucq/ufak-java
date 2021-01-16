package com.ufak.bar.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.bar.entity.PostBar;
import com.ufak.bar.entity.UserBar;
import com.ufak.bar.mapper.PostBarMapper;
import com.ufak.bar.service.IPostBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description: 贴吧
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Service
public class PostBarServiceImpl extends ServiceImpl<PostBarMapper, PostBar> implements IPostBarService {

    @Autowired
    private PostBarMapper postBarMapper;

    @Override
    public void fans(UserBar userBar) {
        postBarMapper.insertUserBar(userBar);
    }

    @Override
    public void cancelFans(UserBar userBar) {
        postBarMapper.deleteUserBar(userBar);
    }

    @Override
    public void likes(UserBar userBar) {
        postBarMapper.insertBarLikes(userBar);
    }

    @Override
    public void cancelLikes(UserBar userBar) {
        postBarMapper.deleteBarLikes(userBar);
    }

    @Override
    public IPage<PostBar> selectPage(Integer pageNo, Integer pageSize, Map paramMap) {
        int start = (pageNo - 1) * pageSize;
        paramMap.put("start",start);
        paramMap.put("size",pageSize);
        List<PostBar> list = postBarMapper.selectPage(paramMap);
        for(PostBar postBar : list){
            String images = postBar.getImages();
            if(images != null){
                String[] str = images.split(",");
                List<String> imgList = new ArrayList<>();
                Collections.addAll(imgList,str);
                postBar.setImageList(imgList);
            }
        }
        long totalCount = postBarMapper.totalCount(paramMap);
        Page page = new Page(pageNo, pageSize);
        page.setRecords(list);
        page.setTotal(totalCount);
        return page;
    }
}

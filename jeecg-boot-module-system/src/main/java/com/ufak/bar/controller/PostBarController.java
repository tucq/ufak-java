package com.ufak.bar.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.bar.entity.PostBar;
import com.ufak.bar.entity.UserBar;
import com.ufak.bar.mapper.PostBarMapper;
import com.ufak.bar.service.IPostBarService;
import com.ufak.bar.service.IReplyBarService;
import com.ufak.common.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 贴吧
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="贴吧")
@RestController
@RequestMapping("/postBar")
public class PostBarController extends JeecgController<PostBar, IPostBarService> {
	@Autowired
	private IPostBarService postBarService;
	@Autowired
	private IReplyBarService replyBarService;
	@Autowired
	private PostBarMapper postBarMapper;

	/**
	 * 分页列表查询
	 *
	 * @param postBar
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PostBar postBar,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String title = req.getParameter("title");
		String categoryId = req.getParameter("categoryId");
		String userId = req.getParameter("userId");
		String state = req.getParameter("state");
		Map paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(title)){
			paramMap.put("title",title);
		}
		if(StringUtils.isNotBlank(categoryId)){
			paramMap.put("categoryId",categoryId);
		}
		if(StringUtils.isNotBlank(userId)){
			paramMap.put("userId",userId);
		}
		if(StringUtils.isNotBlank(state)){
			paramMap.put("state",state);
		}
		IPage<PostBar> pageList = postBarService.selectPage(pageNo,pageSize,paramMap);
		return Result.ok(pageList);
	}


	/**
	 * 添加
	 *
	 * @param postBar
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PostBar postBar) {
		postBarService.save(postBar);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param postBar
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PostBar postBar) {
		postBarService.updateById(postBar);
		return Result.ok("编辑成功!");
	}

	/**
	 * 观注
	 * @return
	 */
	@PostMapping(value = "/fans")
	public Result<?> fans(@RequestParam(name="userId",required=true) String userId,
							@RequestParam(name="postBarId",required=true) String postBarId) {
		UserBar userBar = new UserBar();
		userBar.setId(UUIDGenerator.generate());
		userBar.setUserId(userId);
		userBar.setPostBarId(postBarId);
		postBarService.fans(userBar);
		return Result.ok();
	}

	/**
	 * 观注
	 * @return
	 */
	@PostMapping(value = "/cancel/fans")
	public Result<?> cancelFans(@RequestParam(name="userId",required=true) String userId,
						  @RequestParam(name="postBarId",required=true) String postBarId) {
		UserBar userBar = new UserBar();
		userBar.setUserId(userId);
		userBar.setPostBarId(postBarId);
		postBarService.cancelFans(userBar);
		return Result.ok();
	}


	 /**
	  * 分享成功后回调
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/share/callback")
	 public Result<?> shareCallBack(@RequestParam(name="id",required=true) String id) {
		 PostBar postBar = postBarService.getById(id);
		 if(postBar == null){
			return Result.error("该id找不到对应的贴吧");
		 }
		 Integer count = postBar.getShareNum() == null ? 0 : postBar.getShareNum();
		 count ++;
		 postBar.setShareNum(count);
		 postBarService.updateById(postBar);
		 return Result.ok(postBar);
	 }

	 /**
	  * 点赞
	  * @param postBarId
	  * @return
	  */
	 @PostMapping(value = "/like")
	 public Result<?> like(@RequestParam(name="postBarId",required=true) String postBarId,
	 						@RequestParam(name="userId",required=true) String userId) {
//		 PostBar postBar = postBarService.getById(postBarId);
//		 if(postBar == null){
//			 return Result.error("该id找不到对应的贴吧");
//		 }
//		 Integer count = postBar.getLikesNum() == null ? 0 : postBar.getLikesNum();
//		 count ++;
//		 postBar.setLikesNum(count);
//		 postBarService.updateById(postBar);
		 UserBar userBar = new UserBar();
		 userBar.setId(UUIDGenerator.generate());
		 userBar.setPostBarId(postBarId);
		 userBar.setUserId(userId);
		 postBarService.likes(userBar);
		 return Result.ok();
	 }

	 /**
	  * 取消点赞
	  * @return
	  */
	 @PostMapping(value = "/cancel/like")
	 public Result<?> cancelLike(@RequestParam(name="postBarId",required=true) String postBarId,
								 @RequestParam(name="userId",required=true) String userId) {
//		 PostBar postBar = postBarService.getById(postBarId);
//		 if(postBar == null){
//			 return Result.error("该id找不到对应的贴吧");
//		 }
//		 if(postBar.getLikesNum() != null){
//			 postBar.setLikesNum(postBar.getLikesNum() - 1);
//			 postBarService.updateById(postBar);
//		 }
		 UserBar userBar = new UserBar();
		 userBar.setUserId(userId);
		 userBar.setPostBarId(postBarId);
		 postBarService.cancelLikes(userBar);
		 return Result.ok();
	 }

	/**
	 * 禁言
	 * @return
	 */
	@PostMapping(value = "/forbidden")
	public Result<?> forbidden(@RequestBody PostBar postBar) {
		LambdaUpdateWrapper<PostBar> uw = new LambdaUpdateWrapper<>();
		uw.set(PostBar::getState,(1 - Integer.valueOf(postBar.getState())) + "");
		uw.eq(PostBar::getId,postBar.getId());
		postBarService.update(uw);
		return Result.ok("操作成功!");
	}

	@PostMapping(value = "/top")
	public Result<?> top(@RequestBody PostBar postBar) {
		postBarService.updateById(postBar);
		return Result.ok("操作成功!");
	}

	/**
	 * app 获取详情页
	 * @return
	 */
	@GetMapping(value = "/app/queryById")
	public Result<?> appQueryById(@RequestParam(name="postBarId",required=true) String postBarId,
								  @RequestParam(name="userId",required=true) String userId) {
		PostBar postBar = postBarService.getById(postBarId);
		Map paramMap = new HashMap<>();
		paramMap.put("postBarId",postBarId);
		paramMap.put("state","0");
		paramMap.put("orderBy","a.create_time");
		postBar.setFansNum(postBarMapper.countFans(postBarId));
		postBar.setLikesNum(postBarMapper.countLikes(postBarId));

		QueryWrapper qw = new QueryWrapper();
		qw.eq("post_bar_id",postBarId);
		int replyNum = replyBarService.count(qw);
		postBar.setReplyNum(replyNum);

		int i = postBarMapper.isExistFans(postBarId,userId);
		if(i > 0){
			postBar.setHasFans(true);
		}else {
			postBar.setHasFans(false);
		}

		int j = postBarMapper.isExistLikes(postBarId,userId);
		if(j > 0){
			postBar.setHasLikes(true);
		}else {
			postBar.setHasLikes(false);
		}

		String images = postBar.getImages();
		if(StringUtils.isNotBlank(images)){
			String[] str = images.split(",");
			List<String> imgList = new ArrayList<>();
			Collections.addAll(imgList,str);
			postBar.setImageList(imgList);
		}

//		IPage<ReplyBar> pageList = replyBarService.selectPage(1,10,paramMap);
//		postBar.setPageList(pageList);
		return Result.ok(postBar);
	}


	 /**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		PostBar pi = postBarService.getById(id);
		if(pi != null){
			String url = pi.getImages();
			String[] urls = url.split(",");
			for(int i = 0;i < urls.length; i++){
				FileUtil.delete(urls[i]);// 删除图片
			}
			postBarService.removeById(id);
		}

		return Result.ok("删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "贴吧-通过id查询")
	@ApiOperation(value="贴吧-通过id查询", notes="贴吧-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PostBar postBar = postBarService.getById(id);
		return Result.ok(postBar);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param postBar
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, PostBar postBar) {
      return super.exportXls(request, postBar, PostBar.class, "贴吧");
  }

  /**
   * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, PostBar.class);
  }

}

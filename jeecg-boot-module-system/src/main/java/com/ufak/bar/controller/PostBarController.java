package com.ufak.bar.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.bar.entity.PostBar;
import com.ufak.bar.entity.UserBar;
import com.ufak.bar.service.IPostBarService;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
		String categoryId = req.getParameter("categoryId");
		String userId = req.getParameter("userId");
		String state = req.getParameter("state");
		Map paramMap = new HashMap<>();
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
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/like")
	 public Result<?> like(@RequestParam(name="id",required=true) String id) {
		 PostBar postBar = postBarService.getById(id);
		 if(postBar == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 Integer count = postBar.getLikesNum() == null ? 0 : postBar.getLikesNum();
		 count ++;
		 postBar.setLikesNum(count);
		 postBarService.updateById(postBar);
		 return Result.ok(postBar);
	 }

	 /**
	  * 取消点赞
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/cancel/like")
	 public Result<?> cancelLike(@RequestParam(name="id",required=true) String id) {
		 PostBar postBar = postBarService.getById(id);
		 if(postBar == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 if(postBar.getLikesNum() != null){
			 postBar.setLikesNum(postBar.getLikesNum() - 1);
			 postBarService.updateById(postBar);
		 }
		 return Result.ok(postBar);
	 }




	 /**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "贴吧-通过id删除")
	@ApiOperation(value="贴吧-通过id删除", notes="贴吧-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		postBarService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "贴吧-批量删除")
	@ApiOperation(value="贴吧-批量删除", notes="贴吧-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.postBarService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
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

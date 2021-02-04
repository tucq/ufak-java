package com.ufak.bar.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.bar.entity.ReplyBar;
import com.ufak.bar.service.IReplyBarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 贴吧回复
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="贴吧回复")
@RestController
@RequestMapping("/replyBar")
public class ReplyBarController extends JeecgController<ReplyBar, IReplyBarService> {
	@Autowired
	private IReplyBarService replyBarService;
	
	/**
	 * 分页列表查询
	 *
	 * @param replyBar
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ReplyBar replyBar,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String postBarId = req.getParameter("postBarId");
		String state = req.getParameter("state");
		String orderBy = req.getParameter("orderBy");
		Map paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(postBarId)){
			paramMap.put("postBarId",postBarId);
		}
		if(StringUtils.isNotBlank(state)){
			paramMap.put("state",state);
		}
		if(StringUtils.isNotBlank(orderBy)){
			if("asc".equals(orderBy)){
				paramMap.put("orderBy","a.create_time");
			}else if("desc".equals(orderBy)){
				paramMap.put("orderBy","a.create_time desc");
			}else if("hot".equals(orderBy)){
				paramMap.put("orderBy","a.likes_num desc");
			}
		}else{
			paramMap.put("orderBy","a.create_time");
		}
		IPage<ReplyBar> pageList = replyBarService.selectPage(pageNo,pageSize,paramMap);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param replyBar
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ReplyBar replyBar) {
		replyBarService.save(replyBar);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param replyBar
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ReplyBar replyBar) {
		replyBarService.updateById(replyBar);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		replyBarService.removeById(id);
		return Result.ok("删除成功!");
	}

	 /**
	  * 点赞
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/like")
	 public Result<?> like(@RequestParam(name="id",required=true) String id) {
		 ReplyBar replyBar = replyBarService.getById(id);
		 if(replyBar == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 Integer count = replyBar.getLikesNum() == null ? 0 : replyBar.getLikesNum();
		 count ++;
		 replyBar.setLikesNum(count);
		 replyBarService.updateById(replyBar);
		 return Result.ok(replyBar);
	 }

	 /**
	  * 取消点赞
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/cancel/like")
	 public Result<?> cancelLike(@RequestParam(name="id",required=true) String id) {
		 ReplyBar replyBar = replyBarService.getById(id);
		 if(replyBar == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 if(replyBar.getLikesNum() != null){
			 replyBar.setLikesNum(replyBar.getLikesNum() - 1);
			 replyBarService.updateById(replyBar);
		 }
		 return Result.ok(replyBar);
	 }

	 /**
	  * 踩
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/down")
	 public Result<?> down(@RequestParam(name="id",required=true) String id) {
		 ReplyBar replyBar = replyBarService.getById(id);
		 if(replyBar == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 Integer count = replyBar.getDownNum() == null ? 0 : replyBar.getDownNum();
		 count ++;
		 replyBar.setDownNum(count);
		 replyBarService.updateById(replyBar);
		 return Result.ok(replyBar);
	 }

	 /**
	  * 取消踩
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/cancel/down")
	 public Result<?> cancelDown(@RequestParam(name="id",required=true) String id) {
		 ReplyBar replyBar = replyBarService.getById(id);
		 if(replyBar == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 if(replyBar.getDownNum() != null){
			 replyBar.setDownNum(replyBar.getDownNum() - 1);
			 replyBarService.updateById(replyBar);
		 }
		 return Result.ok(replyBar);
	 }

	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.replyBarService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}


	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ReplyBar replyBar = replyBarService.getById(id);
		return Result.ok(replyBar);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param replyBar
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ReplyBar replyBar) {
      return super.exportXls(request, replyBar, ReplyBar.class, "贴吧回复");
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
      return super.importExcel(request, response, ReplyBar.class);
  }

}

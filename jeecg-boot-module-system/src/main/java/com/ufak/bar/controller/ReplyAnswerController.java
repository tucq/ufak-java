package com.ufak.bar.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.bar.entity.ReplyAnswer;
import com.ufak.bar.service.IReplyAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 回复交流
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="回复交流")
@RestController
@RequestMapping("/replyAnswer")
public class ReplyAnswerController extends JeecgController<ReplyAnswer, IReplyAnswerService> {
	@Autowired
	private IReplyAnswerService replyAnswerService;
	
	/**
	 * 分页列表查询
	 *
	 * @param replyAnswer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ReplyAnswer replyAnswer,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReplyAnswer> queryWrapper = QueryGenerator.initQueryWrapper(replyAnswer, req.getParameterMap());
		Page<ReplyAnswer> page = new Page<ReplyAnswer>(pageNo, pageSize);
		IPage<ReplyAnswer> pageList = replyAnswerService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param replyAnswer
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ReplyAnswer replyAnswer) {
		replyAnswerService.save(replyAnswer);
		return Result.ok("添加成功！");
	}

	 /**
	  * 点赞
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/like")
	 public Result<?> like(@RequestParam(name="id",required=true) String id) {
		 ReplyAnswer replyAnswer = replyAnswerService.getById(id);
		 if(replyAnswer == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 Integer count = replyAnswer.getLikesNum() == null ? 0 : replyAnswer.getLikesNum();
		 count ++;
		 replyAnswer.setLikesNum(count);
		 replyAnswerService.updateById(replyAnswer);
		 return Result.ok(replyAnswer);
	 }

	 /**
	  * 取消点赞
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/cancel/like")
	 public Result<?> cancelLike(@RequestParam(name="id",required=true) String id) {
		 ReplyAnswer replyAnswer = replyAnswerService.getById(id);
		 if(replyAnswer == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 if(replyAnswer.getLikesNum() != null){
			 replyAnswer.setLikesNum(replyAnswer.getLikesNum() - 1);
			 replyAnswerService.updateById(replyAnswer);
		 }
		 return Result.ok(replyAnswer);
	 }

	 /**
	  * 踩
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/down")
	 public Result<?> down(@RequestParam(name="id",required=true) String id) {
		 ReplyAnswer replyAnswer = replyAnswerService.getById(id);
		 if(replyAnswer == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 Integer count = replyAnswer.getDownNum() == null ? 0 : replyAnswer.getDownNum();
		 count ++;
		 replyAnswer.setDownNum(count);
		 replyAnswerService.updateById(replyAnswer);
		 return Result.ok(replyAnswer);
	 }

	 /**
	  * 取消踩
	  * @param id
	  * @return
	  */
	 @PostMapping(value = "/cancel/down")
	 public Result<?> cancelDown(@RequestParam(name="id",required=true) String id) {
		 ReplyAnswer replyAnswer = replyAnswerService.getById(id);
		 if(replyAnswer == null){
			 return Result.error("该id找不到对应的贴吧");
		 }
		 if(replyAnswer.getDownNum() != null){
			 replyAnswer.setDownNum(replyAnswer.getDownNum() - 1);
			 replyAnswerService.updateById(replyAnswer);
		 }
		 return Result.ok(replyAnswer);
	 }

	
	/**
	 * 编辑
	 *
	 * @param replyAnswer
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ReplyAnswer replyAnswer) {
		replyAnswerService.updateById(replyAnswer);
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
		replyAnswerService.removeById(id);
		return Result.ok("删除成功!");
	}


	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.replyAnswerService.removeByIds(Arrays.asList(ids.split(",")));
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
		ReplyAnswer replyAnswer = replyAnswerService.getById(id);
		return Result.ok(replyAnswer);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param replyAnswer
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ReplyAnswer replyAnswer) {
      return super.exportXls(request, replyAnswer, ReplyAnswer.class, "回复交流");
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
      return super.importExcel(request, response, ReplyAnswer.class);
  }

}

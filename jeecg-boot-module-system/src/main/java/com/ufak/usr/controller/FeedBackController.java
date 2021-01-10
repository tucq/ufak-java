package com.ufak.usr.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.usr.entity.FeedBack;
import com.ufak.usr.entity.UserAddress;
import com.ufak.usr.service.IFeedBackService;
import com.ufak.usr.service.IUserAddressService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 意见反馈
 * @Author: jeecg-boot
 * @Date:   2021-01-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags="意见反馈")
@RestController
@RequestMapping("/feedBack")
public class FeedBackController extends JeecgController<FeedBack, IFeedBackService> {
	@Autowired
	private IFeedBackService feedBackService;
	 @Autowired
	 private IUserAddressService userAddressService;
	
	/**
	 * 分页列表查询
	 *
	 * @param feedBack
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FeedBack feedBack,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FeedBack> queryWrapper = QueryGenerator.initQueryWrapper(feedBack, req.getParameterMap());
		Page<FeedBack> page = new Page<FeedBack>(pageNo, pageSize);
		IPage<FeedBack> pageList = feedBackService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 * @param feedBack
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FeedBack feedBack) {
		LambdaQueryWrapper<UserAddress> qw = new LambdaQueryWrapper();
		qw.eq(UserAddress::getUserId,feedBack.getUserId());
		List<UserAddress> list = userAddressService.list(qw);
		if(list.size() > 0){
			feedBack.setUsername(list.get(0).getUsername());
			feedBack.setTelephone(list.get(0).getTelephone());
		}
		feedBackService.save(feedBack);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param feedBack
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FeedBack feedBack) {
		feedBackService.updateById(feedBack);
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
		feedBackService.removeById(id);
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
		this.feedBackService.removeByIds(Arrays.asList(ids.split(",")));
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
		FeedBack feedBack = feedBackService.getById(id);
		return Result.ok(feedBack);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param feedBack
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, FeedBack feedBack) {
      return super.exportXls(request, feedBack, FeedBack.class, "意见反馈");
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
      return super.importExcel(request, response, FeedBack.class);
  }

}

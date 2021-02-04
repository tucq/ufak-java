package com.ufak.usr.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.common.Constants;
import com.ufak.usr.entity.UserAddress;
import com.ufak.usr.service.IUserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 客户地址信息表
 * @Author: jeecg-boot
 * @Date:   2020-03-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="客户地址信息表")
@RestController
@RequestMapping("/usr/address")
public class UserAddressController extends JeecgController<UserAddress, IUserAddressService> {
	@Autowired
	private IUserAddressService userAddressService;
	 @Autowired
	 private ISysUserService sysUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param userAddress
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(UserAddress userAddress,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<UserAddress> queryWrapper = new QueryWrapper();
		String userId = req.getParameter("userId");
		if(StringUtils.isNotBlank(userId)){
			queryWrapper.eq("user_id",userId);
		}else{
			queryWrapper.eq("user_id","ItIsNull");
		}
		Page<UserAddress> page = new Page<UserAddress>(pageNo, pageSize);
		IPage<UserAddress> pageList = userAddressService.page(page, queryWrapper);
		return Result.ok(pageList);

	}

	 /**
	  * 帐号列表
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/userList")
	 public Result<?> queryUserList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 String username = req.getParameter("username");
		 String telephone = req.getParameter("telephone");
		 Map paramMap = new HashMap<>();
		 if(StringUtils.isNotBlank(username)){
			 paramMap.put("username",username);
		 }
		 if(StringUtils.isNotBlank(telephone)){
			 paramMap.put("telephone",telephone);
		 }
		 paramMap.put("orgCode",Constants.CLIENT_ORG_CODE);
		 IPage<SysUser> pageList = userAddressService.selectPage2(pageNo,pageSize,paramMap);
		 return Result.ok(pageList);
	 }


	
	/**
	 * 添加
	 *
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		String userId = jsonObject.getString("userId");
		if(StringUtils.isNotEmpty(userId)){
			UserAddress userAddress = new UserAddress();
			userAddress.setUserId(userId);
			userAddress.setUsername(jsonObject.getString("username"));
			userAddress.setTelephone(jsonObject.getString("telephone"));
			userAddress.setAddress(jsonObject.getString("address"));
			userAddress.setDetailAddress(jsonObject.getString("detailAddress"));
			userAddress.setDefaule(jsonObject.getString("defaule"));
			userAddressService.save(userAddress);
			return Result.ok("添加成功！");
		}else{
			return Result.error("未获取到用户信息,添加失败！");
		}
	}
	
	/**
	 * 编辑
	 *
	 * @param userAddress
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody UserAddress userAddress) {
		userAddressService.updateById(userAddress);
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
		userAddressService.removeById(id);
		return Result.ok("删除成功!");
	}

	@GetMapping(value = "/getAddress")
	public Result<?> getAddress(@RequestParam(name="userId",required=true) String userId) {
		QueryWrapper qw = new QueryWrapper();
		qw.eq("user_id",userId);
		qw.eq("defaule","0");
		List<UserAddress> list = userAddressService.list(qw);
		if(list.size() > 0){
			return Result.ok(list.get(0));
		}else{
			QueryWrapper qw2 = new QueryWrapper();
			qw2.eq("user_id",userId);
			List<UserAddress> list2 = userAddressService.list(qw2);
			if(list2.size() > 0){
				return Result.ok(list2.get(0));
			}else{
				return Result.ok(null);
			}
		}
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.userAddressService.removeByIds(Arrays.asList(ids.split(",")));
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
		UserAddress userAddress = userAddressService.getById(id);
		return Result.ok(userAddress);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param userAddress
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, UserAddress userAddress) {
      return super.exportXls(request, userAddress, UserAddress.class, "客户地址信息表");
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
      return super.importExcel(request, response, UserAddress.class);
  }

}

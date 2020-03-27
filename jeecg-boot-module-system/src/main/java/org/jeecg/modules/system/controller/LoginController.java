package org.jeecg.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.ufak.common.Constants;
import com.ufak.common.HttpRequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.common.util.encryption.AesEncryptUtil;
import org.jeecg.common.util.encryption.EncryptedString;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysLogService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys")
@Api(tags="用户登录")
@Slf4j
public class LoginController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISysLogService logService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
    private ISysDepartService sysDepartService;
	
	private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

	@ApiOperation("登录接口")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel){
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();
		//update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
		//前端密码加密，后端进行密码解密
		//password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
		//update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

		//update-begin-author:taoyan date:20190828 for:校验验证码
		Object checkCode = redisUtil.get(sysLoginModel.getCheckKey());
		if(checkCode==null) {
			result.error500("验证码失效");
			return result;
		}
		if(!checkCode.equals(sysLoginModel.getCaptcha())) {
			result.error500("验证码错误");
			return result;
		}
		//update-end-author:taoyan date:20190828 for:校验验证码
		
		//1. 校验用户是否有效
		SysUser sysUser = sysUserService.getUserByName(username);
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}
		
		//2. 校验用户名或密码是否正确
		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
		String syspassword = sysUser.getPassword();
		if (!syspassword.equals(userpassword)) {
			result.error500("用户名或密码错误");
			return result;
		}
				
		//用户登录信息
		userInfo(sysUser, result);
		sysBaseAPI.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

		return result;
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public Result<Object> logout(HttpServletRequest request,HttpServletResponse response) {
		//用户退出逻辑
	    String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
	    if(oConvertUtils.isEmpty(token)) {
	    	return Result.error("退出登录失败！");
	    }
	    String username = JwtUtil.getUsername(token);
		LoginUser sysUser = sysBaseAPI.getUserByName(username);
	    if(sysUser!=null) {
	    	sysBaseAPI.addLog("用户名: "+sysUser.getRealname()+",退出成功！", CommonConstant.LOG_TYPE_1, null);
	    	log.info(" 用户名:  "+sysUser.getRealname()+",退出成功！ ");
	    	//清空用户登录Token缓存
	    	redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
	    	//清空用户登录Shiro权限缓存
			redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
			//清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
			redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
			//调用shiro的logout
			SecurityUtils.getSubject().logout();
	    	return Result.ok("退出登录成功！");
	    }else {
	    	return Result.error("Token无效!");
	    }
	}
	
	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("loginfo")
	public Result<JSONObject> loginfo() {
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		//update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
		// 获取一天的开始和结束时间
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date dayStart = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		Date dayEnd = calendar.getTime();
		// 获取系统访问记录
		Long totalVisitCount = logService.findTotalVisitCount();
		obj.put("totalVisitCount", totalVisitCount);
		Long todayVisitCount = logService.findTodayVisitCount(dayStart,dayEnd);
		obj.put("todayVisitCount", todayVisitCount);
		Long todayIp = logService.findTodayIp(dayStart,dayEnd);
		//update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
		obj.put("todayIp", todayIp);
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}
	
	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("visitInfo")
	public Result<List<Map<String,Object>>> visitInfo() {
		Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        List<Map<String,Object>> list = logService.findVisitCount(dayStart, dayEnd);
		result.setResult(oConvertUtils.toLowerCasePageList(list));
		return result;
	}
	
	
	/**
	 * 登陆成功选择用户当前部门
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
	public Result<JSONObject> selectDepart(@RequestBody SysUser user) {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = user.getUsername();
		if(oConvertUtils.isEmpty(username)) {
			LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
			username = sysUser.getUsername();
		}
		String orgCode= user.getOrgCode();
		this.sysUserService.updateUserDepart(username, orgCode);
		SysUser sysUser = sysUserService.getUserByName(username);
		JSONObject obj = new JSONObject();
		obj.put("userInfo", sysUser);
		result.setResult(obj);
		return result;
	}

	/**
	 * 短信登录接口
	 * 
	 * @param jsonObject
	 * @return
	 */
	@PostMapping(value = "/sms")
	public Result<String> sms(@RequestBody JSONObject jsonObject) {
		Result<String> result = new Result<String>();
		String mobile = jsonObject.get("mobile").toString();
		String smsmode=jsonObject.get("smsmode").toString();
		log.info(mobile);	
		Object object = redisUtil.get(mobile);
		if (object != null) {
			result.setMessage("验证码10分钟内，仍然有效！");
			result.setSuccess(false);
			return result;
		}

		//随机数
		String captcha = RandomUtil.randomNumbers(6);
		JSONObject obj = new JSONObject();
    	obj.put("code", captcha);
		try {
			boolean b = false;
			//注册模板
			if (CommonConstant.SMS_TPL_TYPE_1.equals(smsmode)) {
				SysUser sysUser = sysUserService.getUserByPhone(mobile);
				if(sysUser!=null) {
					result.error500(" 手机号已经注册，请直接登录！");
					sysBaseAPI.addLog("手机号已经注册，请直接登录！", CommonConstant.LOG_TYPE_1, null);
					return result;
				}
				b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.REGISTER_TEMPLATE_CODE);
			}else {
				//登录模式，校验用户有效性
				SysUser sysUser = sysUserService.getUserByPhone(mobile);
				result = sysUserService.checkUserIsEffective(sysUser);
				if(!result.isSuccess()) {
					return result;
				}
				
				/**
				 * smsmode 短信模板方式  0 .登录模板、1.注册模板、2.忘记密码模板
				 */
				if (CommonConstant.SMS_TPL_TYPE_0.equals(smsmode)) {
					//登录模板
					b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.LOGIN_TEMPLATE_CODE);
				} else if(CommonConstant.SMS_TPL_TYPE_2.equals(smsmode)) {
					//忘记密码模板
					b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
				}
			}

			if (b == false) {
				result.setMessage("短信验证码发送失败,请稍后重试");
				result.setSuccess(false);
				return result;
			}
			//验证码10分钟内有效
			redisUtil.set(mobile, captcha, 600);
			//update-begin--Author:scott  Date:20190812 for：issues#391
			//result.setResult(captcha);
			//update-end--Author:scott  Date:20190812 for：issues#391
			result.setSuccess(true);

		} catch (ClientException e) {
			e.printStackTrace();
			result.error500(" 短信接口未配置，请联系管理员！");
			return result;
		}
		return result;
	}
	

	/**
	 * 手机号登录接口
	 * 
	 * @param jsonObject
	 * @return
	 */
	@ApiOperation("手机号登录接口")
	@PostMapping("/phoneLogin")
	public Result<JSONObject> phoneLogin(@RequestBody JSONObject jsonObject) {
		Result<JSONObject> result = new Result<JSONObject>();
		String phone = jsonObject.getString("mobile");
		
		//校验用户有效性
		SysUser sysUser = sysUserService.getUserByPhone(phone);
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}
		
		String smscode = jsonObject.getString("captcha");
		Object code = redisUtil.get(phone);
		if (!smscode.equals(code)) {
			result.setMessage("手机验证码错误");
			return result;
		}
		//用户信息
		userInfo(sysUser, result);
		//添加日志
		sysBaseAPI.addLog("用户名: " + sysUser.getUsername() + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

		return result;
	}


	/**
	 * 用户信息
	 *
	 * @param sysUser
	 * @param result
	 * @return
	 */
	private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
		String syspassword = sysUser.getPassword();
		String username = sysUser.getUsername();
		// 生成token
		String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME*2 / 1000);

		// 获取用户部门信息
		JSONObject obj = new JSONObject();
		List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
		obj.put("departs", departs);
		if (departs == null || departs.size() == 0) {
			obj.put("multi_depart", 0);
		} else if (departs.size() == 1) {
			sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
			obj.put("multi_depart", 1);
		} else {
			obj.put("multi_depart", 2);
		}
		obj.put("token", token);
		obj.put("userInfo", sysUser);
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}

	/**
	 * 获取加密字符串
	 * @return
	 */
	@GetMapping(value = "/getEncryptedString")
	public Result<Map<String,String>> getEncryptedString(){
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		map.put("key", EncryptedString.key);
		map.put("iv",EncryptedString.iv);
		result.setResult(map);
		return result;
	}
	
	/**
	 * 获取校验码
	 */
	@ApiOperation("获取验证码")
	@GetMapping(value = "/getCheckCode")
	public Result<Map<String,String>> getCheckCode(){
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		try {
			String code = RandomUtil.randomString(BASE_CHECK_CODES,4);
			String key = MD5Util.MD5Encode(code+System.currentTimeMillis(), "utf-8");
			redisUtil.set(key, code, 60);
			map.put("key", key);
			map.put("code",code);
			result.setResult(map);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * app登录
	 * @param sysLoginModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mLogin", method = RequestMethod.POST)
	public Result<JSONObject> mLogin(@RequestBody SysLoginModel sysLoginModel) throws Exception {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();
		
		//1. 校验用户是否有效
		SysUser sysUser = sysUserService.getUserByName(username);
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}
		
		//2. 校验用户名或密码是否正确
		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
		String syspassword = sysUser.getPassword();
		if (!syspassword.equals(userpassword)) {
			result.error500("用户名或密码错误");
			return result;
		}
		
		String orgCode = sysUser.getOrgCode();
		if(oConvertUtils.isEmpty(orgCode)) {
			//如果当前用户无选择部门 查看部门关联信息
			List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
			if (departs == null || departs.size() == 0) {
				result.error500("用户暂未归属部门,不可登录!");
				return result;
			}
			orgCode = departs.get(0).getOrgCode();
			sysUser.setOrgCode(orgCode);
			this.sysUserService.updateUserDepart(username, orgCode);
		}
		JSONObject obj = new JSONObject();
		//用户登录信息
		obj.put("userInfo", sysUser);
		
		// 生成token
		String token = JwtUtil.sign(username, syspassword);
		// 设置超时时间
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME*2 / 1000);
		//token 信息
		obj.put("token", token);
		result.setResult(obj);
		result.setSuccess(true);
		result.setCode(200);
		sysBaseAPI.addLog("用户名: " + username + ",登录成功[移动端]！", CommonConstant.LOG_TYPE_1, null);
		return result;
	}

//	/**
//	 * 微信授权登陆
//	 * @param jsonObject
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/wxLogin", method = RequestMethod.POST)
//	public Result<JSONObject> wxLogin(@RequestBody JSONObject jsonObject) throws Exception {
//		String js_code = jsonObject.getString("code");
//		String appId = Constants.WX_APPID;
//		String secret = Constants.WX_SECRET;
//		String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+secret+"&js_code="+js_code+"&grant_type=authorization_code";
//		return  null;
//	}

	@ApiOperation("获取微信小程序的openid")
	@ResponseBody
	@RequestMapping(value = "/getOpenid", method = RequestMethod.GET)
	public Map getOpenid(String code, String encryptedData, String iv ) throws Exception{

		Map<String,Object> map = new HashMap<String,Object>();
		//code = "081ZExyD0qnP4j2LV5yD0hFLyD0ZExyK";
		//登录凭证不能为空
		if (code == null || code.length() == 0) {
			map.put("status", 0);
			map.put("msg", "code 不能为空");
			System.out.println("map1:" + map);
			return map;
		}
		//小程序唯一标识   (在微信小程序管理后台获取)
		String wxspAppid = Constants.WX_APPID;
		//小程序的 app secret (在微信小程序管理后台获取)
		String wxspSecret = Constants.WX_SECRET;
		//授权（必填）
		String grant_type = "authorization_code";
		//////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
		//请求参数
		String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
		//发送请求
		String sr = HttpRequestUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
		//解析相应内容（转换成json对象）
		JSONObject json = JSONObject.parseObject(sr);
		//获取会话密钥（session_key）
		String session_key = json.get("session_key").toString();
		//用户的唯一标识（openid）
		String openid = (String) json.get("openid");
		System.out.println("openid:" + openid);
		//////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
		try {
//			String result = AesUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
			String result = AesEncryptUtil.desEncrypt(encryptedData, session_key, iv);
			if (null != result && result.length() > 0) {
				map.put("status", 1);
				map.put("msg", "解密成功");

				JSONObject userInfoJSON = JSONObject.parseObject(result);
				Map<String,Object> userInfo = new HashMap<String,Object>();
				userInfo.put("openId", userInfoJSON.get("openId"));
				userInfo.put("nickName", userInfoJSON.get("nickName"));
				userInfo.put("gender", userInfoJSON.get("gender"));
				userInfo.put("city", userInfoJSON.get("city"));
				userInfo.put("province", userInfoJSON.get("province"));
				userInfo.put("country", userInfoJSON.get("country"));
				userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
				userInfo.put("unionId", userInfoJSON.get("unionId"));
				map.put("userInfo", userInfo);
				System.out.println("map2:" + map);
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("status", 0);
		map.put("msg", "解密失败");
		System.out.println("map3:" + map);
		return map;
	}

}
package com.ufak.api;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags="数据字典")
@RestController
@RequestMapping("/api/dict")
public class DictItemController {
    @Autowired
    private ISysDictService sysDictService;

    @RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
    public Result<List<DictModel>> getDictItems(@PathVariable String dictCode) {
        log.info(" dictCode : "+ dictCode);
        Result<List<DictModel>> result = new Result<List<DictModel>>();
        List<DictModel> ls = null;
        try {
            if(dictCode.indexOf(",")!=-1) {
                //关联表字典（举例：sys_user,realname,id）
                String[] params = dictCode.split(",");

                if(params.length<3) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
                //SQL注入校验（只限制非法串改数据库）
                final String[] sqlInjCheck = {params[0],params[1],params[2]};
                SqlInjectionUtil.filterContent(sqlInjCheck);

                if(params.length==4) {
                    //SQL注入校验（查询条件SQL 特殊check，此方法仅供此处使用）
                    SqlInjectionUtil.specialFilterContent(params[3]);
                    ls = sysDictService.queryTableDictItemsByCodeAndFilter(params[0],params[1],params[2],params[3]);
                }else if (params.length==3) {
                    ls = sysDictService.queryTableDictItemsByCode(params[0],params[1],params[2]);
                }else{
                    result.error500("字典Code格式不正确！");
                    return result;
                }
            }else {
                //字典表
                ls = sysDictService.queryDictItemsByCode(dictCode);
            }

            result.setSuccess(true);
            result.setResult(ls);
            log.info(result.toString());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }

    /**
     * 获取字典数据
     * @param dictCode
     * @return
     */
    @RequestMapping(value = "/getDictText/{dictCode}/{key}", method = RequestMethod.GET)
    public Result<String> getDictItems(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
        log.info(" dictCode : "+ dictCode);
        Result<String> result = new Result<String>();
        String text = null;
        try {
            text = sysDictService.queryDictTextByKey(dictCode, key);
            result.setSuccess(true);
            result.setResult(text);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
            return result;
        }
        return result;
    }
}

package com.ufak.product.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.product.entity.ProductCategory;
import com.ufak.product.service.IProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.model.TreeSelectModel;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Description: 分类字典
* @Author: jeecg-boot
* @Date:   2019-05-29
* @Version: V1.0
*/
@RestController
@RequestMapping("/product/category")
@Slf4j
public class ProductCategoryController {
   @Autowired
   private IProductCategoryService productCategoryService;

   /**
     * 分页列表查询
    * @param productCategory
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/rootList")
   public Result<IPage<ProductCategory>> queryPageList(ProductCategory productCategory,
                                                       @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                       HttpServletRequest req) {
       if(oConvertUtils.isEmpty(productCategory.getPid())){
           productCategory.setPid("0");
       }
       Result<IPage<ProductCategory>> result = new Result<IPage<ProductCategory>>();

       //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------start
       //QueryWrapper<ProductCategory> queryWrapper = QueryGenerator.initQueryWrapper(productCategory, req.getParameterMap());
       QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<ProductCategory>();
       queryWrapper.eq("pid", productCategory.getPid());
       //--author:os_chengtgen---date:20190804 -----for: 分类字典页面显示错误,issues:377--------end

       Page<ProductCategory> page = new Page<ProductCategory>(pageNo, pageSize);
       IPage<ProductCategory> pageList = productCategoryService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   @GetMapping(value = "/childList")
   public Result<List<ProductCategory>> queryPageList(ProductCategory productCategory,HttpServletRequest req) {
       Result<List<ProductCategory>> result = new Result<List<ProductCategory>>();
       QueryWrapper<ProductCategory> queryWrapper = QueryGenerator.initQueryWrapper(productCategory, req.getParameterMap());
       List<ProductCategory> list = productCategoryService.list(queryWrapper);
       result.setSuccess(true);
       result.setResult(list);
       return result;
   }


   /**
     *   添加
    * @param productCategory
    * @return
    */
   @PostMapping(value = "/add")
   public Result<ProductCategory> add(@RequestBody ProductCategory productCategory) {
       Result<ProductCategory> result = new Result<ProductCategory>();
       try {
           productCategoryService.addProductCategory(productCategory);
           result.success("添加成功！");
       } catch (Exception e) {
           log.error(e.getMessage(),e);
           result.error500("操作失败");
       }
       return result;
   }

   /**
     *  编辑
    * @param productCategory
    * @return
    */
   @PutMapping(value = "/edit")
   public Result<ProductCategory> edit(@RequestBody ProductCategory productCategory) {
       Result<ProductCategory> result = new Result<ProductCategory>();
       ProductCategory sysCategoryEntity = productCategoryService.getById(productCategory.getId());
       if(sysCategoryEntity==null) {
           result.error500("未找到对应实体");
       }else {
           productCategoryService.updateProductCategory(productCategory);
           result.success("修改成功!");
       }
       return result;
   }

   /**
     *   通过id删除
    * @param id
    * @return
    */
   @DeleteMapping(value = "/delete")
   public Result<ProductCategory> delete(@RequestParam(name="id",required=true) String id) {
       Result<ProductCategory> result = new Result<ProductCategory>();
       ProductCategory productCategory = productCategoryService.getById(id);
       if(productCategory==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = productCategoryService.removeById(id);
           if(ok) {
               result.success("删除成功!");
           }
       }

       return result;
   }

   /**
     *  批量删除
    * @param ids
    * @return
    */
   @DeleteMapping(value = "/deleteBatch")
   public Result<ProductCategory> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       Result<ProductCategory> result = new Result<ProductCategory>();
       if(ids==null || "".equals(ids.trim())) {
           result.error500("参数不识别！");
       }else {
           this.productCategoryService.removeByIds(Arrays.asList(ids.split(",")));
           result.success("删除成功!");
       }
       return result;
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/queryById")
   public Result<ProductCategory> queryById(@RequestParam(name="id",required=true) String id) {
       Result<ProductCategory> result = new Result<ProductCategory>();
       ProductCategory productCategory = productCategoryService.getById(id);
       if(productCategory==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(productCategory);
           result.setSuccess(true);
       }
       return result;
   }

 /**
     * 导出excel
  *
  * @param request
  */
 @RequestMapping(value = "/exportXls")
 public ModelAndView exportXls(HttpServletRequest request, ProductCategory productCategory) {
     // Step.1 组装查询条件查询数据
     QueryWrapper<ProductCategory> queryWrapper = QueryGenerator.initQueryWrapper(productCategory, request.getParameterMap());
     List<ProductCategory> pageList = productCategoryService.list(queryWrapper);
     // Step.2 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     // 过滤选中数据
     String selections = request.getParameter("selections");
     if(oConvertUtils.isEmpty(selections)) {
         mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
     }else {
         List<String> selectionList = Arrays.asList(selections.split(","));
         List<ProductCategory> exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
         mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
     }
     //导出文件名称
     mv.addObject(NormalExcelConstants.FILE_NAME, "分类字典列表");
     mv.addObject(NormalExcelConstants.CLASS, ProductCategory.class);
     LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("分类字典列表数据", "导出人:"+user.getRealname(), "导出信息"));
     return mv;
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
     MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
     Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
     for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
         MultipartFile file = entity.getValue();// 获取上传文件对象
         ImportParams params = new ImportParams();
         params.setTitleRows(2);
         params.setHeadRows(1);
         params.setNeedSave(true);
         try {
             List<ProductCategory> listSysCategorys = ExcelImportUtil.importExcel(file.getInputStream(), ProductCategory.class, params);
             for (ProductCategory sysCategoryExcel : listSysCategorys) {
                 productCategoryService.save(sysCategoryExcel);
             }
             return Result.ok("文件导入成功！数据行数:" + listSysCategorys.size());
         } catch (Exception e) {
             log.error(e.getMessage(),e);
             return Result.error("文件导入失败:"+e.getMessage());
         } finally {
             try {
                 file.getInputStream().close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }
     return Result.ok("文件导入失败！");
 }



 /**
    * 加载单个数据 用于回显
  */
   @RequestMapping(value = "/loadOne", method = RequestMethod.GET)
    public Result<ProductCategory> loadOne(@RequestParam(name="field") String field,@RequestParam(name="val") String val) {
        Result<ProductCategory> result = new Result<ProductCategory>();
        try {

            QueryWrapper<ProductCategory> query = new QueryWrapper<ProductCategory>();
            query.eq(field, val);
            List<ProductCategory> ls = this.productCategoryService.list(query);
            if(ls==null || ls.size()==0) {
                result.setMessage("查询无果");
                 result.setSuccess(false);
            }else if(ls.size()>1) {
                result.setMessage("查询数据异常,["+field+"]存在多个值:"+val);
                 result.setSuccess(false);
            }else {
                result.setSuccess(true);
                result.setResult(ls.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

   /**
         * 加载节点的子数据
    */
   @RequestMapping(value = "/loadTreeChildren", method = RequestMethod.GET)
   public Result<List<TreeSelectModel>> loadTreeChildren(@RequestParam(name="pid") String pid) {
       Result<List<TreeSelectModel>> result = new Result<List<TreeSelectModel>>();
       try {
           List<TreeSelectModel> ls = this.productCategoryService.queryListByPid(pid);
           result.setResult(ls);
           result.setSuccess(true);
       } catch (Exception e) {
           e.printStackTrace();
           result.setMessage(e.getMessage());
           result.setSuccess(false);
       }
       return result;
   }

   /**
        * 加载一级节点/如果是同步 则所有数据
    */
   @RequestMapping(value = "/loadTreeRoot", method = RequestMethod.GET)
      public Result<List<TreeSelectModel>> loadTreeRoot(@RequestParam(name="async") Boolean async,@RequestParam(name="pcode") String pcode) {
          Result<List<TreeSelectModel>> result = new Result<List<TreeSelectModel>>();
          try {
              List<TreeSelectModel> ls = this.productCategoryService.queryListByCode(pcode);
              if(!async) {
                  loadAllCategoryChildren(ls);
              }
              result.setResult(ls);
              result.setSuccess(true);
          } catch (Exception e) {
              e.printStackTrace();
              result.setMessage(e.getMessage());
              result.setSuccess(false);
          }
          return result;
      }

   /**
        * 递归求子节点 同步加载用到
    */
     private void loadAllCategoryChildren(List<TreeSelectModel> ls) {
         for (TreeSelectModel tsm : ls) {
           List<TreeSelectModel> temp = this.productCategoryService.queryListByPid(tsm.getKey());
           if(temp!=null && temp.size()>0) {
               tsm.setChildren(temp);
               loadAllCategoryChildren(temp);
           }
       }
     }

    /**
     * 校验编码
     * @param pid
     * @param code
     * @return
     */
    @GetMapping(value = "/checkCode")
    public Result<?> checkCode(@RequestParam(name="pid",required = false) String pid,@RequestParam(name="code",required = false) String code) {
       if(oConvertUtils.isEmpty(code)){
           return Result.error("错误,类型编码为空!");
       }
       if(oConvertUtils.isEmpty(pid)){
           return Result.ok();
       }
       ProductCategory parent = this.productCategoryService.getById(pid);
       if(code.startsWith(parent.getCode())){
           return Result.ok();
       }else{
           return Result.error("编码不符合规范,须以\""+parent.getCode()+"\"开头!");
       }

    }


    /**
     * 分类字典树控件 加载节点
     * @param pid
     * @param pcode
     * @param condition
     * @return
     */
    @RequestMapping(value = "/loadTreeData", method = RequestMethod.GET)
    public Result<List<TreeSelectModel>> loadDict(@RequestParam(name="pid",required = false) String pid,@RequestParam(name="pcode",required = false) String pcode, @RequestParam(name="condition",required = false) String condition) {
        Result<List<TreeSelectModel>> result = new Result<List<TreeSelectModel>>();
        //pid如果传值了 就忽略pcode的作用
        if(oConvertUtils.isEmpty(pid)){
            if(oConvertUtils.isEmpty(pcode)){
               result.setSuccess(false);
               result.setMessage("加载分类字典树参数有误.[null]!");
               return result;
           }else{
                if(productCategoryService.ROOT_PID_VALUE.equals(pcode)){
                   pid = productCategoryService.ROOT_PID_VALUE;
               }else{
                   pid = this.productCategoryService.queryIdByCode(pcode);
               }
               if(oConvertUtils.isEmpty(pid)){
                   result.setSuccess(false);
                   result.setMessage("加载分类字典树参数有误.[code]!");
                   return result;
               }
           }
        }
        Map<String, String> query = null;
        if(oConvertUtils.isNotEmpty(condition)) {
            query = JSON.parseObject(condition, Map.class);
        }
        List<TreeSelectModel> ls = productCategoryService.queryListByPid(pid,query);
        result.setSuccess(true);
        result.setResult(ls);
        return result;
    }

    /**
     * 分类字典控件数据回显[表单页面]
     * @return
     */
    @RequestMapping(value = "/loadDictItem", method = RequestMethod.GET)
    public Result<List<String>> loadDictItem(@RequestParam(name="ids") String ids) {
        Result<List<String>> result = new Result<>();
        LambdaQueryWrapper<ProductCategory> query = new LambdaQueryWrapper<ProductCategory>().in(ProductCategory::getId,ids);
        List<ProductCategory> list = this.productCategoryService.list(query);
        List<String> textList = new ArrayList<String>();
        for (String id : ids.split(",")) {
            for (ProductCategory c : list) {
               if(id.equals(c.getId())){
                   textList.add(c.getName());
                   break;
               }
            }
        }
        result.setSuccess(true);
        result.setResult(textList);
        return result;
    }


    /**
     * [列表页面]加载分类字典数据 用于值的替换
     * @param code
     * @return
     */
    @RequestMapping(value = "/loadAllData", method = RequestMethod.GET)
    public Result<List<DictModel>> loadAllData(@RequestParam(name="code",required = true) String code) {
        Result<List<DictModel>> result = new Result<List<DictModel>>();
        LambdaQueryWrapper<ProductCategory> query = new LambdaQueryWrapper<ProductCategory>();
        if(oConvertUtils.isNotEmpty(code) && !"0".equals(code)){
            query.likeRight(ProductCategory::getCode,code);
        }
        List<ProductCategory> list = this.productCategoryService.list(query);
        if(list==null || list.size()==0) {
            result.setMessage("无数据,参数有误.[code]");
            result.setSuccess(false);
            return result;
        }
        List<DictModel> rdList = new ArrayList<DictModel>();
        for (ProductCategory c : list) {
            rdList.add(new DictModel(c.getId(),c.getName()));
        }
        result.setSuccess(true);
        result.setResult(rdList);
        return result;
    }




}

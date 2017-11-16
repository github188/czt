/*
 * Copyright (c) 2017, All rights reserved.
 */
package com.ztel.app.web.ctrl.perform;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztel.app.service.perform.KeyworkService;
import com.ztel.app.service.sys.OperationlogService;
import com.ztel.app.vo.perform.DeptmonthLineVo;
import com.ztel.app.vo.perform.KeyworkVo;
import com.ztel.app.vo.sys.UserVo;
import com.ztel.framework.util.DateUtil;
import com.ztel.framework.web.ctrl.BaseCtrl;

/**
 * @author lcf
 * @since 2017年2月26日
 * 
 */
@Controller
@RequestMapping("/perform/keywork")
public class KeyworkCtrl extends BaseCtrl {
	
	private static Logger logger = LogManager.getLogger(KeyworkCtrl.class);
	
	@Autowired
	private KeyworkService keyworkService = null;
	@Autowired
	private OperationlogService operationlogService = null;
	/**
	 * 重点工作计划
	 * @param request
	 * @return
	 */
	@RequestMapping("tokeywork")
	public String tokeywork(HttpServletRequest request) {
		return "/perform/v_keywork";
	}
	
	/**
	 * 月计划调整
	 * @param request
	 * @return
	 */
	@RequestMapping("tokwmonthplanadjust")
	public String tokwmonthplanadjust(HttpServletRequest request) {
		return "/perform/v_kwmonthplanadjust";
	}
	
	@RequestMapping(value="getKeyworkList")
	 @ResponseBody
	 public List<KeyworkVo> keywork(KeyworkVo keyworkVo, HttpServletRequest request) throws Exception{
		 Map<String, Object> result=new HashMap<String, Object>();  
		 
		 Long userid = 0L;
		 int deptid = 0;
		 String username = "";
		 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
		 if(userVo!=null&&userVo.getUsername().trim().length()>0){
			 userid = userVo.getId();
			 deptid = userVo.getDeptid();
		 }
		 
		 String keyword = keyworkVo.getKeyword();
		 String searchdept = request.getParameter("searchdept");
		 if(searchdept==null||searchdept.equals(""))searchdept=deptid+"";
		 String ctype = request.getParameter("ctype");
		 String searchtime = request.getParameter("searchtime");
		 if(searchtime==null||searchtime.equals("")){
			 searchtime=DateUtil.getyyyy_mm();
		 }
			 searchtime = searchtime + "-01";
		 //if(searchtime==null)searchtime="2017-02-01";
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		 if(searchtime!=null&&!searchtime.equals("")){
				Date searchtimeD = sdf.parse(searchtime);
				keyworkVo.setWorkdate(searchtimeD);
		 }

		 if(searchdept!=null&&!searchdept.equals("")){
			 keyworkVo.setDeptid(Integer.parseInt(searchdept));
		 }
		 if(ctype!=null&&!ctype.equals("")){
			 keyworkVo.setCtype(ctype);
		 }
		 List<KeyworkVo> keyworkList = new ArrayList<KeyworkVo>();
		keyworkList = keyworkService.selectKeyworkList(keyworkVo);
		
		return keyworkList;
	}
	
	/**
	 * 获取副部长考核列表，包括重点工作及日常工作,在绩效考核（经理考核即副部长考核）的新增中显示列表
	 * @param keyworkVo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getAllKeyworkList")
	 @ResponseBody
	 public List<DeptmonthLineVo> getAllKeyworkList(KeyworkVo keyworkVo, HttpServletRequest request) throws Exception{
		 Map<String, Object> result=new HashMap<String, Object>();  
		 
		 String keyword = keyworkVo.getKeyword();
		 String searchdept = request.getParameter("searchdept");
		 String ctype = request.getParameter("ctype");
		 String searchtime = request.getParameter("searchtime");
		 if(searchtime==null){
			 searchtime=DateUtil.getyyyy_mm_dd();
		 }
		 if(searchtime.length()==10)searchtime = searchtime.substring(0, 8)+"01";
		 //if(searchtime==null)searchtime="2017-02-01";
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); 
		 if(searchtime!=null&&!searchtime.equals("")){
				Date searchtimeD = sdf.parse(searchtime);
				keyworkVo.setWorkdate(searchtimeD);
		 }

		 if(searchdept!=null&&!searchdept.equals("")){
			 keyworkVo.setDeptid(Integer.parseInt(searchdept));
		 }
		 if(ctype!=null&&!ctype.equals("")){
			 keyworkVo.setCtype(ctype);
		 }
		 List<DeptmonthLineVo> keyworkList = new ArrayList<DeptmonthLineVo>();
		keyworkList = keyworkService.selectAllKeyworkList(keyworkVo);
		
		return keyworkList;
	}
	
	/**
	 * 获取部长考核列表，包括重点工作及日常工作,在部长考核的新增中显示列表
	 * 根据部长id，需要获取部长管辖的用户所在部门id，从而根据部门id获取重点工作或日常工作
	 * @param keyworkVo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getDeptmngAllKeyworkList")
	 @ResponseBody
	 public List<DeptmonthLineVo> getDeptmngAllKeyworkList(KeyworkVo keyworkVo, HttpServletRequest request) throws Exception{
		 Map<String, Object> result=new HashMap<String, Object>();  
		 
		 String keyword = keyworkVo.getKeyword();
		 String searchdept = request.getParameter("searchdept");
		 String ctype = request.getParameter("ctype");
		 String searchtime = request.getParameter("searchtime");
		 if(searchtime==null){
			 searchtime=DateUtil.getyyyy_mm_dd();
		 }
		 
		 Long userid = 0L;
		 int deptid = 0;
		 String username = "";
		 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
		 if(userVo!=null&&userVo.getUsername().trim().length()>0){
			 userid = userVo.getId();
			 deptid = userVo.getDeptid();
			 username = userVo.getUsername();
		 }
		 keyworkVo.setCreateid(userid);//传入部长id，然后根据部长id获取所管辖人员id及部门,部长自己添加的列表需要根据自己id和部门id获取，因为部长都属于高层领导部门
		 if(searchtime.length()==10)searchtime = searchtime.substring(0, 8)+"01";
		 //if(searchtime==null)searchtime="2017-02-01";
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM"); 
		 if(searchtime!=null&&!searchtime.equals("")){
				Date searchtimeD = sdf.parse(searchtime);
				keyworkVo.setWorkdate(searchtimeD);
		 }

		 if(searchdept!=null&&!searchdept.equals("")){
			 keyworkVo.setDeptid(Integer.parseInt(searchdept));
		 }
		 if(ctype!=null&&!ctype.equals("")){
			 keyworkVo.setCtype(ctype);
		 }
		 List<DeptmonthLineVo> keyworkList = new ArrayList<DeptmonthLineVo>();
		keyworkList = keyworkService.selectDeptmngAllKeyworkList(keyworkVo);
		
		return keyworkList;
	}
	
	 /**
	  * 新增
	  * @return
	  */
	 @RequestMapping(value="doSave")
	 @ResponseBody
	 public  Map<String, Object> doSave(HttpServletRequest request) throws Exception
	 {
		 Map<String, Object> map=new HashMap<String, Object>();
		 String ctype = request.getParameter("ctype");//类型(10:重点工作  20:月度计划调整)
		 String workdate_new = request.getParameter("workdate_new");//工作时间
		 if(workdate_new==null){
			 workdate_new=DateUtil.getyyyy_mm();
		 }
		 workdate_new = workdate_new + "-01";
		 String deptid_new = request.getParameter("deptid_new");//计划调整部门
		 String weight_new = request.getParameter("weight_new");//权重
		 if(weight_new==null||weight_new.equals(""))weight_new="0";
		 String content_new = request.getParameter("content_new");//工作内容
		 String finishdate_new = request.getParameter("finishdate_new");//完成期限
		 String process_new = request.getParameter("process_new");//过程进度
		 String target_new = request.getParameter("target_new");//要求及目标
		 String partake_new = request.getParameter("partake_new");//实施人
		 
		 Long userid = 0L;
		 int checkdeptid = 0;
		 String username = "";
		 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
		 operationlogService.insertLog(userVo, "/perform/keywork/doSave", "月计划调整/重点工作计划", "新增", "");
		 if(userVo!=null&&userVo.getUsername().trim().length()>0){
			 userid = userVo.getId();
			 checkdeptid = userVo.getDeptid();
			 username = userVo.getUsername();
		 }
		 
		 KeyworkVo keyworkVo = new KeyworkVo();
		 keyworkVo.setDeptid(Integer.parseInt(deptid_new));
		 keyworkVo.setWeight(new BigDecimal(weight_new));
		 keyworkVo.setContent(content_new);
		 keyworkVo.setFinishdate(finishdate_new);
		 keyworkVo.setProcess(process_new);
		 keyworkVo.setTarget(target_new);
		 keyworkVo.setPartake(partake_new);
		 keyworkVo.setCreateid(userid);
		 keyworkVo.setStatus("0");
		 keyworkVo.setCtype(ctype);//类型(10:重点工作  20:月度计划调整)
		 keyworkVo.setCreatedate(new Date());

		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
				Date workdate_newD = sdf.parse(workdate_new);
				keyworkVo.setWorkdate(workdate_newD);
		 
		 try{
			 keyworkService.insertKeywork(keyworkVo);
		 
		 map.put("msg", "新增成功");
		 }catch(Exception e){
			 map.put("msg", "新增失败");
			 e.printStackTrace();
		 }
		 map.put("total", "1");
		 return map;
	 }
	 
	 /**
	  * 删除
	  * @return
	  */
	 @RequestMapping(value="doDel")
	 @ResponseBody
	 public  Map<String, Object> doDel(HttpServletRequest request)
	 {
		 Map<String, Object> resultMap=new HashMap<String, Object>();  
			
		 String id = request.getParameter("id");
		 if(id==null||id.equals(""))id="0";

		 try{
			 keyworkService.doDel(new Integer(id));
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 operationlogService.insertLog(userVo, "/perform/keywork/doDel", "月计划调整/重点工作计划", "删除", "");
		 resultMap.put("msg", "删除成功！");
		 }catch(Exception e){
			 resultMap.put("msg", "删除失败！");
		 }
		 resultMap.put("total", 1);
		 return resultMap;
	 }
	 
}
	
    

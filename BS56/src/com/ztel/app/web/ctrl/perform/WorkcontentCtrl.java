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

import com.ztel.app.service.perform.WorkcontentService;
import com.ztel.app.service.sys.OperationlogService;
import com.ztel.app.vo.perform.KeyworkVo;
import com.ztel.app.vo.perform.TransverseAssessVo;
import com.ztel.app.vo.perform.WorkcontentVo;
import com.ztel.app.vo.sys.UserVo;
import com.ztel.framework.util.DateUtil;
import com.ztel.framework.web.ctrl.BaseCtrl;

/**
 * @author lcf
 * @since 2017年2月26日
 * 重点工作
 */
@Controller
@RequestMapping("/perform/workcontent")
public class WorkcontentCtrl extends BaseCtrl {
	
	private static Logger logger = LogManager.getLogger(WorkcontentCtrl.class);
	
	@Autowired
	private WorkcontentService workcontentService = null;
	@Autowired
	private OperationlogService operationlogService = null;
	
	/**
	 * 日常工作
	 * @param request
	 * @return
	 */
	@RequestMapping("toworkcontent")
	public String tokeywork(HttpServletRequest request) {
		return "/perform/v_workcontent";
	}
	
	
	
	@RequestMapping(value="getWorkcontentList")
	 @ResponseBody
	 public List<WorkcontentVo> getWorkcontentList(WorkcontentVo workcontentVo, HttpServletRequest request) throws Exception{
		 Map<String, Object> result=new HashMap<String, Object>();  
		 Long userid = 0L;
		 int deptid = 0;
		 String username = "";
		 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
		 if(userVo!=null&&userVo.getUsername().trim().length()>0){
			 userid = userVo.getId();
			 deptid = userVo.getDeptid();
		 }
		 //String keyword = workcontentVo.getKeyword();
		 String searchdept = request.getParameter("searchdept");
		 if(searchdept==null||searchdept.equals(""))searchdept=deptid+"";
		 String searchproperty = request.getParameter("searchproperty");
		 String searchtime = request.getParameter("searchtime");
		 String showhj = request.getParameter("showhj");//是否显示权重合计 0：不显示  1：显示
		 if(searchtime==null||searchtime.equals("")){
			 searchtime=DateUtil.getyyyy_mm();
		 }
		 //if(searchtime==null)searchtime="2017-02-01";
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		 if(searchtime!=null&&!searchtime.equals("")){
				//Date searchtimeD = sdf.parse(searchtime);
				workcontentVo.setWorkdatestr(searchtime);
		 }
		 if(searchdept!=null&&!searchdept.equals("")){
			 workcontentVo.setDeptid(Integer.parseInt(searchdept));
		 }
		 if(searchproperty!=null&&!searchproperty.equals("")){
			 workcontentVo.setProperty(searchproperty);
		 }
		 List<WorkcontentVo> workcontentList = new ArrayList<WorkcontentVo>();
		 workcontentList = workcontentService.selectWorkcontentList(workcontentVo);
		 if(showhj!=null && showhj.equals("1"))//显示权重合计
		 {
			 if(workcontentList!=null&&workcontentList.size()>0)
			 {
				 WorkcontentVo workcontentVo_hj= new WorkcontentVo();
				 BigDecimal weight_hj = new BigDecimal("0");
				 for(int i=0;i<workcontentList.size();i++){
					 WorkcontentVo workcontentVo1=workcontentList.get(i);
					 weight_hj = weight_hj.add(workcontentVo1.getWeight());
				 }
				 workcontentVo_hj.setWeight(weight_hj);
				 workcontentVo_hj.setStandard("合计");
				 workcontentList.add(workcontentVo_hj);
			 }
		 }
		return workcontentList;
	}
	
	 /**
	  * 重点工作-新增
	  * @return
	  */
	 @RequestMapping(value="doSave")
	 @ResponseBody
	 public  Map<String, Object> doSave(HttpServletRequest request) throws Exception
	 {
		 Map<String, Object> map=new HashMap<String, Object>();
		 String property_new = request.getParameter("property_new");//类型(1：关键隐患控制 2：日常工作  3：临时性工作   4：日工作 5：周工作  6：月工作)
		 String workdate_new = request.getParameter("workdate_new");//工作时间
		 if(workdate_new==null){
			 workdate_new=DateUtil.getyyyy_mm();
		 }
		 workdate_new = workdate_new + "-01";
		 String weight_new = request.getParameter("weight_new");//权重
		 if(weight_new==null||weight_new.equals(""))weight_new="0";
		 String matters_new = request.getParameter("matters_new");//工作事项
		 String standard_new = request.getParameter("standard_new");//考评标准
		 String deptid_new = request.getParameter("deptid_new");//部门
		 String userid_new = request.getParameter("userid_new");//人员
		 
		 Long userid = 0L;
		 int checkdeptid = 0;
		 String username = "";
		 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
		 operationlogService.insertLog(userVo, "/perform/workcontent/doSave", "重点工作", "新增", "");
		 if(userVo!=null&&userVo.getUsername().trim().length()>0){
			 userid = userVo.getId();
			 checkdeptid = userVo.getDeptid();
			 username = userVo.getUsername();
		 }
		 
		 WorkcontentVo workcontentVo = new WorkcontentVo();
		 workcontentVo.setDeptid(Integer.parseInt(deptid_new));
		 workcontentVo.setWeight(new BigDecimal(weight_new));
		 workcontentVo.setMatters(matters_new);
		 workcontentVo.setStandard(standard_new);
		 workcontentVo.setDeptid(Integer.parseInt(deptid_new));
		 workcontentVo.setCreateid(userid);
		 workcontentVo.setProperty(property_new);
		 workcontentVo.setCreatedate(new Date());

		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
				Date workdate_newD = sdf.parse(workdate_new);
				workcontentVo.setWorkdate(workdate_newD);
		 
		 try{
			 workcontentService.insertWorkcontent(workcontentVo);
		 
		 map.put("msg", "新增成功");
		 }catch(Exception e){
			 map.put("msg", "新增失败");
			 e.printStackTrace();
		 }
		 map.put("total", "1");
		 return map;
	 }
	 
	 /**
	  * 重点工作-修改
	  * @return
	  */
	 @RequestMapping(value="doUpdate")
	 @ResponseBody
	 public  Map<String, Object> doUpdate(HttpServletRequest request)
	 {
		 Map<String, Object> map=new HashMap<String, Object>();
		 String id = request.getParameter("wkid");
		 String weight = request.getParameter("weight");
		 String matters = request.getParameter("matters");
		 String standard = request.getParameter("standard");
		 
		 WorkcontentVo workcontentVo = new WorkcontentVo();
		 workcontentVo.setId(Integer.parseInt(id));
		 workcontentVo.setMatters(matters);
		 workcontentVo.setStandard(standard);
		 workcontentVo.setWeight(new BigDecimal(weight));
		 try{
			 workcontentService.doUpdate(workcontentVo);
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 operationlogService.insertLog(userVo, "/perform/workcontent/doUpdate", "重点工作", "修改", "");
			// cigarettedamagedService.doAudit(cigarettedamagedVo);
		 
		 map.put("msg", "成功");
		 }catch(Exception e){
			 map.put("msg", "失败");
			 e.printStackTrace();
		 }
		 map.put("total", "1");
		 return map;
	 }
	 
	 /**
	  * 重点工作-删除
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
			 workcontentService.doDel(new Integer(id));
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 operationlogService.insertLog(userVo, "/perform/workcontent/doDel", "重点工作", "删除", "");
		 resultMap.put("msg", "删除成功！");
		 }catch(Exception e){
			 resultMap.put("msg", "删除失败！");
		 }
		 resultMap.put("total", 1);
		 return resultMap;
	 }
	 
}
	
    

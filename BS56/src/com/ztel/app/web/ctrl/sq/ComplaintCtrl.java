package com.ztel.app.web.ctrl.sq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ztel.app.service.sq.ComplaintService;
import com.ztel.app.service.sys.RouteInfoService;
import com.ztel.app.service.wms.CustomerService;
import com.ztel.app.vo.CTitle;
import com.ztel.app.vo.sq.ComplaintVo;
import com.ztel.app.vo.sys.RouteInfoVo;
import com.ztel.app.vo.sys.UserVo;
import com.ztel.app.vo.wms.CustomerVo;
import com.ztel.framework.util.DateUtil;
import com.ztel.framework.util.FileUtil;
import com.ztel.framework.vo.Pagination;
import com.ztel.framework.web.ctrl.BaseCtrl;


/**
 * @author lcf
 * @since 2017年3月16日
 * 菜单控制
 */
@Controller
@RequestMapping("/sq/complaint")
public class ComplaintCtrl extends BaseCtrl{
	private static Logger logger = LogManager.getLogger(ComplaintCtrl.class);
	
	@Autowired
	private ComplaintService complaintService = null;
	
	@Autowired 
	private RouteInfoService routeInfoService = null;
	
	@Autowired
	private CustomerService customerService = null;
	
	@RequestMapping("toComplaint")
	public String toComplaint(HttpServletRequest request) {
		return "/sq/v_complaint";
	}
	
	@RequestMapping("toYearReport")
	public String toYearReport(HttpServletRequest request,HttpServletResponse resp) throws Exception{
		 
		
		 String starttime  = request.getParameter("starttime");
		 String endtime  = request.getParameter("endtime");
		 if(starttime==null||starttime.equals(""))starttime = DateUtil.getFirstDayOfTheYear();
		 
		 if(endtime==null||endtime.equals("")) endtime  = DateUtil.getyyyy_mm_dd();
		 Map<String, Object> map = complaintService.getComplaintReport(starttime,endtime);
		 
		 String[] basetypeInfoVoList  = (String[]) map.get("baseTypeList");
		 List<ComplaintVo> complaintVoList = (List<ComplaintVo>) map.get("complaintList");
		 request.setAttribute("complaintList", complaintVoList);
		 request.setAttribute("basetypeInfoVoList", basetypeInfoVoList);
		return "/sq/v_complaintYearReport";
	}
	
	@RequestMapping("toComplaintNew")
	public String toComplaintNew(HttpServletRequest request,HttpServletResponse resp) throws Exception{
		//resp.sendRedirect("/BS56/sq/complaint/openComplaintNew");
		return "/sq/v_complaintNew";
	}
	
	@RequestMapping("openComplaintNew")
	public String openComplaintNew(HttpServletRequest request) {
		return "redirect:/sq/v_complaintNew";
	}
	
	@RequestMapping(value="getComplaintList")
	 @ResponseBody
	 public Map<String, Object> getComplaintList(ComplaintVo complaintVo, HttpServletRequest request) throws Exception{
		 Map<String, Object> result=new HashMap<String, Object>();  
		 
		Pagination<?> page = this.getPagination(request);
		if (complaintVo!=null) {
			 page.setParam(complaintVo);
		}
		List<ComplaintVo> complaintVoList = new ArrayList<ComplaintVo>();
		complaintVoList = complaintService.getComplaintList(page);
		
		int totals=0;

		 result.put("rows",complaintVoList);  
		 result.put("total",page.getTotalCount());  
		 
		return result;
	}
	
	/**
	 * 根据车组code获取
	 * @param routecode
	 * @return
	 */
	 @RequestMapping("getDriverAndCashierByRotecode")
	 @ResponseBody
	 public RouteInfoVo getDriverAndCashierByRotecode(String routecode){
		 return  routeInfoService.getDriverAndCashierByRotecode(routecode);
	 }
	 
	 @RequestMapping("getCustListByRouteCode")
	 @ResponseBody
	 public List<CustomerVo> getCustListByRouteCode(HttpServletRequest request)
	 {
		 List<CustomerVo>  custList = null;
		 
		 //String routecode = request.getParameter("routecode");
		// if(routecode==null)routecode="";
		 String condname = request.getParameter("condname");
		 if(condname==null)condname="";
		 
		 CustomerVo customerVo = new CustomerVo();
		 //customerVo.setRoutecode(routecode);
		 customerVo.setParam(condname);
		 custList = customerService.getListByCond(customerVo);
		 return custList;
	 }
	 
	 /**
	  * 投诉受理入库
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value="doSave",method=RequestMethod.POST)
	// @ResponseBody
	 public   void doSave(ComplaintVo complaintVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		 Map<String, Object> map=new HashMap<String, Object>();  
		 int total=0;
        
		 try {
			 String cuser = complaintVo.getCusername();
			 String preuser = complaintVo.getPreusername();
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 if(userVo!=null&&userVo.getUsername().trim().length()>0){
				 complaintVo.setCreateid(userVo.getId());
				 complaintVo.setCreatename(userVo.getUsername());
			 }
			 if(cuser!=null && cuser.trim().length()>0&&cuser.split(";").length>0){
				 String cuserid = cuser.split(";")[0];
				 String cusername= cuser.split(";")[1];
				 complaintVo.setCuserid(Long.parseLong(cuserid));
				 complaintVo.setCusername(cusername);
			 }
			 if(preuser!=null && preuser.trim().length()>0&&preuser.split(";").length>0){
				 String preuserid = preuser.split(";")[0];
				 String preusername= preuser.split(";")[1];
				 complaintVo.setPreuserid(Long.parseLong(preuserid));
				 complaintVo.setPreusername(preusername);
			 }
			 System.out.println(complaintVo.toString());
			 complaintService.doNewComplaint(complaintVo);
			 map.put("msg", "成功");
			 total=1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();  
			map.put("msg", "失败");
		}
		 map.put("total", total);
		 
		 //直接使用注解@ResponseBody，框架自动返回json串，但是form形式提交的返回json在IE在会出现下载json的提示，所以修改成设置response的形式
		 String result = JSON.toJSONString(map);
		 response.setContentType("text/html;charset=UTF-8");
		 response.getWriter().write(result);  

		 //return result;
	 }
	 
	 /**
	  * 核实、审核、处理、回访
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value="doUpdate",method=RequestMethod.POST)
	// @ResponseBody
	 public   void doUpdate(ComplaintVo complaintVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		 Map<String, Object> map=new HashMap<String, Object>();  
		 int total=0;
        String status = "";
        if(complaintVo!=null&&complaintVo.getStatus()!=null&&!complaintVo.getStatus().equals("")){
        	status = complaintVo.getStatus();
        }
		 try {
			 Integer id = complaintVo.getId();
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 Long userid = Long.parseLong("0");
			 String username="";
			 if(userVo!=null&&userVo.getUsername().trim().length()>0){
				 userid = userVo.getId();
				 username = userVo.getUsername();
			 }
			 
			 ComplaintVo complaintVoNew = new ComplaintVo();
			 if(status.equals("20")){//核实
				 complaintVoNew.setId(id);
				 complaintVoNew.setPreuserid(userid);
				 complaintVoNew.setPreusername(username);
				 complaintVoNew.setPresituation(complaintVo.getPresituation());
				 complaintVoNew.setStatus("20");//标志
				 complaintVoNew.setPretime(DateUtil.getDateyyyy_mm_dd_hh_mi_s());//核实时间
			 }
			 else if(status.equals("30")){//审核
				 complaintVoNew.setId(id);
				 complaintVoNew.setAuditid(userid);
				 complaintVoNew.setAuditname(username);
				 complaintVoNew.setChecknote(complaintVo.getChecknote());
				 complaintVoNew.setStatus("30");//标志
				 complaintVoNew.setAudittime(DateUtil.getDateyyyy_mm_dd_hh_mi_s());//审核时间
			 }
			 else if(status.equals("40")){//处理
				 complaintVoNew.setId(id);
				 complaintVoNew.setHandleid(userid);
				 complaintVoNew.setHandlename(username);
				 complaintVoNew.setReason(complaintVo.getReason());//原因分析
				 complaintVoNew.setMeasure(complaintVo.getMeasure());//措施
				 complaintVoNew.setLimitation(complaintVo.getLimitation());//时效
				 complaintVoNew.setHandletime(DateUtil.getDateyyyy_mm_dd_hh_mi_s());//处理时间
				 complaintVoNew.setStatus("40");//标志
			 }
			 else if(status.equals("50")){//回访
				 complaintVoNew.setId(id);
				 complaintVoNew.setVisituserid(userid);
				 complaintVoNew.setVisitname(username);
				 complaintVoNew.setVisittype(complaintVo.getVisittype());//回访形式
				 complaintVoNew.setSatisfy(complaintVo.getSatisfy());
				 complaintVoNew.setResults(complaintVo.getResults());//回访处理情况
				 complaintVoNew.setVisittime(DateUtil.getDateyyyy_mm_dd_hh_mi_s());//回访时间
				 complaintVoNew.setStatus("50");//标志
			 }
			 complaintService.doUpdate(complaintVoNew);
			 map.put("msg", "成功");
			 total=1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();  
			map.put("msg", "失败");
		}
		 map.put("total", total);
		 
		 //直接使用注解@ResponseBody，框架自动返回json串，但是form形式提交的返回json在IE在会出现下载json的提示，所以修改成设置response的形式
		 String result = JSON.toJSONString(map);
		 response.setContentType("text/html;charset=UTF-8");
		 response.getWriter().write(result);  

		 //return result;
	 }
	 
	 /**
	  * 3.审核
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value="doAdit",method=RequestMethod.POST)
	// @ResponseBody
	 public   void doAdit(ComplaintVo complaintVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		 Map<String, Object> map=new HashMap<String, Object>();  
		 int total=0;
        
		 try {
			 Integer id = complaintVo.getId();
			 
			 ComplaintVo complaintVoNew = new ComplaintVo();
			
			 complaintService.doUpdate(complaintVoNew);
			 map.put("msg", "成功");
			 total=1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();  
			map.put("msg", "失败");
		}
		 map.put("total", total);
		 
		 //直接使用注解@ResponseBody，框架自动返回json串，但是form形式提交的返回json在IE在会出现下载json的提示，所以修改成设置response的形式
		 String result = JSON.toJSONString(map);
		 response.setContentType("text/html;charset=UTF-8");
		 response.getWriter().write(result);  

		 //return result;
	 }
	 
	 @RequestMapping(value="doDel",method=RequestMethod.POST)
	 @ResponseBody
	 public   Map<String, Object> doDel(@RequestParam("id") List<Integer> id) throws Exception {
		 Map<String, Object> map=new HashMap<String, Object>();  
		 int total=0;
		 if (id!=null&&id.size()>0) {
			 total = id.size();
		}
		 try {
			 complaintService.deleteByPrimaryKey(id);
			 map.put("msg", "成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();  
			map.put("msg", "失败");
		}
		 map.put("total", total);
		 
		 return map;
	 }
	 
	 @RequestMapping(value="getComplaintReport")
	 public   String getComplaintReport(HttpServletRequest request) throws Exception {
		 
		 String starttime  = request.getParameter("starttime");
		 String endtime  = request.getParameter("endtime");
		 System.out.println("starttime = "+starttime +",endtime="+endtime);
		 Map<String, Object> map = complaintService.getComplaintReport(starttime,endtime);
		 
		 String[] basetypeInfoVoList  = ( String[]) map.get("baseTypeList");
		 List<ComplaintVo> complaintVoList = (List<ComplaintVo>) map.get("complaintList");
		 request.setAttribute("complaintList", complaintVoList);
		 request.setAttribute("basetypeInfoVoList", basetypeInfoVoList);
		 return "/sq/v_complaintYearReportList";
	 }
	 
	 @RequestMapping(value="toExcel")
	 @ResponseBody
	 public   void toExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
		 
		 String starttime  = request.getParameter("starttime");
		 String endtime  = request.getParameter("endtime");
		 System.out.println("starttime = "+starttime +",endtime="+endtime);
		 Map<String, Object> map = complaintService.getComplaintReport(starttime,endtime);
		 
		 String[] basetypeInfoVoList  = ( String[]) map.get("baseTypeList");
		 ArrayList<ComplaintVo> complaintVoList = (ArrayList<ComplaintVo>) map.get("complaintList");
		 request.setAttribute("complaintList", complaintVoList);
		 request.setAttribute("basetypeInfoVoList", basetypeInfoVoList);
		 
		 List<List<CTitle>> cTitleListList = new ArrayList<List<CTitle>>();
		 List<CTitle> cTitleList=new ArrayList<CTitle>();
		 for(int i=0;i<basetypeInfoVoList.length;i++)
		 {
			 CTitle cTitle=new CTitle();
			 cTitle.setTitle(basetypeInfoVoList[i]);
			 cTitleList.add(cTitle);
		 }
		 String sheetName = "投诉年报表";
		 List<String> needPrintFields = new ArrayList<String>();
		 needPrintFields.add("deptname");
		 needPrintFields.add("ctstr");
		 String needAnalyze = "ctstr";
		 FileUtil.ExportToExcelComplaint(complaintVoList, sheetName, cTitleList, needPrintFields, needAnalyze, response);
	 }
}

/*
 * Copyright (c) 2017, All rights reserved.
 */
package com.ztel.app.web.ctrl.sys;

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
//import com.fsj.spring.web.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ztel.app.service.sys.MenuinfoService;
import com.ztel.app.service.sys.OperationlogService;
import com.ztel.app.service.sys.RoleinfoService;
import com.ztel.app.service.sys.UserVoService;
import com.ztel.app.vo.sys.MenuInfoVo;
import com.ztel.app.vo.sys.RoleInfoVo;
import com.ztel.app.vo.sys.UserVo;
import com.ztel.framework.vo.Pagination;
import com.ztel.framework.web.ctrl.BaseCtrl;

/**
 * @author Zeal
 * @since 2017年3月16日
 * 系统角色信息表
 */
@Controller
@RequestMapping("/sys/role")
public class RoleinfoCtrl extends BaseCtrl {
	
	private static Logger logger = LogManager.getLogger(RoleinfoCtrl.class);
	
	@Autowired
	private RoleinfoService roleinfoService = null;
	
	@Autowired
	private UserVoService userVoService = null;
	
	@Autowired
	private MenuinfoService menuinfoService = null;
	
	@Autowired
	private com.ztel.app.service.sys.UserrolerelativeService UserrolerelativeService=null;
	
	@Autowired
	private OperationlogService operationlogService = null;
	
	@RequestMapping("toRoleList")
	//@AuthorityPage("role")
	public String index(HttpServletRequest request) {
		
		return "/sys/v_role";
	}
	
	/**
	 * 角色功能点菜单授权页面
	 * @param request
	 * @return
	 */
	@RequestMapping("toRoleoperation")
	//@AuthorityPage("role")
	public String toRoleoperation(HttpServletRequest request) {
		String roleid = request.getParameter("roleid");
		request.setAttribute("roleid", roleid);
		
		String sysid = request.getParameter("sysid");
		 if(sysid==null||sysid.equals(""))sysid="1";
		 List<MenuInfoVo> list = menuinfoService.searchMenuinfoListForOperation(sysid,roleid);
//		 if(list!=null&&list.size()>0){
//			 for(int i=0;i<list.size();i++){
//				 MenuInfoVo menuInfoVo = list.get(i);
//				 System.out.println(menuInfoVo.getFmenuname()+","+menuInfoVo.getMenuname()+","+menuInfoVo.getOperationinfoVoList().size()+","+menuInfoVo.getMenucode());
//			 }
//		 }
		 request.setAttribute("menuInfoList", list);
		 request.setAttribute("roleid",roleid);
		 request.setAttribute("sysid",sysid);
		return "/sys/v_roleoperation";
	}
	
	/**
	 * 角色功能点菜单授权页面,
	 * @param request
	 * @return
	 */
	@RequestMapping("getRoleoperationList")
	@ResponseBody
	public List<MenuInfoVo> toRoleoperationDetail(HttpServletRequest request) {
		String roleid = request.getParameter("roleid");
		request.setAttribute("roleid", roleid);
		
		String sysid = request.getParameter("sysid");
		 if(sysid==null||sysid.equals(""))sysid="1";
		 List<MenuInfoVo> list = menuinfoService.searchMenuinfoListForOperation(sysid,roleid);
//		 if(list!=null&&list.size()>0){
//			 for(int i=0;i<list.size();i++){
//				 MenuInfoVo menuInfoVo = list.get(i);
//				 System.out.println(menuInfoVo.getFmenuname()+","+menuInfoVo.getMenuname()+","+menuInfoVo.getOperationinfoVoList().size()+","+menuInfoVo.getMenucode());
//			 }
//		 }
		 //request.setAttribute("menuInfoList", list);
		 request.setAttribute("roleid",roleid);
		 request.setAttribute("sysid",sysid);
		return list;
	}
	
	/**
	  * 获取角色列表
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping("getRoleinfos")
	 @ResponseBody
	 public  Map<String, Object> getRoleinfoList(/*DataGridModel dgm,*/RoleInfoVo roleinfo,HttpServletRequest request) throws Exception {
		 Pagination<?> page = this.getPagination(request);
//		// System.out.println("重置之前numPerPage="+page.getNumPerPage()+","+page.getSortColumn()+",isSortAsc="+page.isSortAsc()); 
//		 //按照DataGrid的分页重新设置分页参数
//		 if (dgm!=null&&dgm.getSort()!=null&&dgm.getSort()!="") {
//			 page.setSortKey(dgm.getSort());//设置排序字段
//			
//			 boolean isSortAsc=false;
//			 if(dgm.getOrder()!=null&&dgm.getOrder()!=""&&dgm.getOrder().equals("asc"))isSortAsc=true;
//			 page.setSortAsc(isSortAsc);//设置是否正序排练
//			// System.out.println("dgm sort= "+dgm.getSort()+",order ="+dgm.getOrder()+",isSortAsc="+isSortAsc+",pageNum="+dgm.getPage()+",getRows="+dgm.getRows()); 
//			 page.setNumPerPage(dgm.getRows());//设置每页显示记录数
//		}

		 Map<String, Object> result=new HashMap<String, Object>();  
		
		 if (roleinfo!=null) {
			// System.out.println("roleinfo="+roleinfo.getRolename()+","+roleinfo.getId()); 
			 page.setParam(roleinfo);
		}
		 List<RoleInfoVo> ones = roleinfoService.searchRoleinfoList(page);
		 
		 int totals=0;

		 result.put("rows",ones);  
		 result.put("total",page.getTotalCount());  

		 return result;
	 }
	 	
	 /**
	  * 删除角色
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value="roledelete",method=RequestMethod.POST)
	 @ResponseBody
	 public   Map<String, Object> deleteRole(@RequestParam("id") List<Integer> id,HttpServletRequest request) throws Exception {
		 Map<String, Object> map=new HashMap<String, Object>();  
		 int total=0;
		 if (id!=null&&id.size()>0) {
			 total = id.size();
		}
		 try {
			 roleinfoService.deleteRoleById(id);
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 operationlogService.insertLog(userVo, "/sys/role/roledelete", "角色管理", "删除", "");
			 map.put("msg", "成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();  
			map.put("msg", "失败");
		}
		 map.put("total", total);
		 
		 return map;
	 }
    
	 /**
	  * 新增角色
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value="roleNew",method=RequestMethod.POST)
	// @ResponseBody
	 public   void roleNew(RoleInfoVo roleinfo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		 Map<String, Object> map=new HashMap<String, Object>();  
		 int total=0;
        
		 try {
			 roleinfoService.newRole(roleinfo);
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 operationlogService.insertLog(userVo, "/sys/role/roleNew", "角色管理", "新增", "");
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
	  * 修改角色
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value="roleUpdate",method=RequestMethod.POST)
	 //@ResponseBody
	 public   void roleUpdate(RoleInfoVo roleinfo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		 Map<String, Object> map=new HashMap<String, Object>();  
		 int total=0;
        
		 try {
			 roleinfoService.updateRole(roleinfo);
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 operationlogService.insertLog(userVo, "/sys/role/roleUpdate", "角色管理", "修改", "");
			 map.put("msg", "成功");
			 total=1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();  
			map.put("msg", "失败");
		}
		 map.put("total", total);
		 String result = JSON.toJSONString(map);
		 response.setContentType("text/html;charset=UTF-8");
		 response.getWriter().write(result);
		 //return map;
	 }
	 
	 /*
	  * 权限设置
	  */
	 @RequestMapping(value="/toRoleMenu",method=RequestMethod.GET)
		public String toRoleMenu(HttpServletRequest request) throws Exception{
		 String roleid= request.getParameter("roleid");
			return "/sys/v_roleMenu";
		}
	 
	/*
	 * 权限设置主体部分
	 */
	 @RequestMapping(value="/toRoleSetting",method=RequestMethod.GET)
		public String toRoleSetting(HttpServletRequest request) throws Exception{
		 String sysid= request.getParameter("sysid");
		 String roleid= request.getParameter("roleid");
		 //传递参数
			request.setAttribute("sysid", sysid);
			request.setAttribute("roleid", roleid);
			return "/sys/v_roleSetting";
		}
	 
	 /*
		 * 提交角色菜单权限设置
		 */
		 @RequestMapping(value="/doRoleSetting",method=RequestMethod.POST)
		 @ResponseBody
			public  Map<String, Object> doRoleSetting(HttpServletRequest request) {
			 Map<String, Object> map=new HashMap<String, Object>();  
			 String idList= request.getParameter("idList");
			 String roleid= request.getParameter("roleid");
			 //System.out.println("idList="+idList+",roleid="+roleid);
			 roleinfoService.doRoleSetting(idList, roleid);
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 operationlogService.insertLog(userVo, "/sys/role/doRoleSetting", "角色管理", "权限", "");
			 map.put("msg", "操作成功！");
				return map;
				//return "/sys/v_roleSetting";
			}
		 
		 /**
		  * 提交角色对用户授权
		  * @param request
		  * @return
		  */
		 @RequestMapping(value="/doRoleGrant",method=RequestMethod.GET)
		 @ResponseBody
			public  Map<String, Object> doRoleGrant(HttpServletRequest request) {
			 Map<String, Object> map=new HashMap<String, Object>();  
			 String userstr= request.getParameter("userstr");
			 String roleid= request.getParameter("roleid");
			 //System.out.println("roleinfoCtr------------userstr="+userstr+",roleid="+roleid);
			 roleinfoService.doRoleGrant(userstr,roleid);
			 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
			 operationlogService.insertLog(userVo, "/sys/role/doRoleGrant", "角色管理", "授权", "");
				return map;
				//return "/sys/v_roleSetting";
			}
		 
		 /**
		  * 根据角色获取已经授权的用户列表
		  * @param request
		  * @return
		  */
		 @RequestMapping(value="/getRoleGrantUserList",method=RequestMethod.GET)
		 @ResponseBody
			public  List<UserVo> getRoleGrantUserList(HttpServletRequest request) {
			 Map<String, Object> map=new HashMap<String, Object>();  
			 String roleid= request.getParameter("roleid");
			 //System.out.println("roleinfoCtr------------userstr="+userstr+",roleid="+roleid);
			 List<UserVo> userList = userVoService.selectUserListByroleid(roleid);
				return userList;
				//return "/sys/v_roleSetting";
			}
		 
		 
		 /*
			 * 提交角色功能点设置，用于角色功能点授权
			 */
			 @RequestMapping(value="/doRoleOperation",method=RequestMethod.POST)
			 @ResponseBody
				public  Map<String, Object> doRoleOperation(HttpServletRequest request,@RequestParam("opid") List<String> opid) {
				 Map<String, Object> map=new HashMap<String, Object>();  
				 String roleid= request.getParameter("roleid");
				 if(roleid==null||roleid.equals(""))roleid="1";
				 String sysid= request.getParameter("sysid");
				 if(sysid==null||sysid.equals(""))sysid="1";
				 int total=0;
				 if (opid!=null&&opid.size()>0) {
					 total = opid.size();
				}
				 roleinfoService.doRoleOperation(opid, roleid,sysid);
				 UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
				 operationlogService.insertLog(userVo, "/sys/role/doRoleOperation", "角色管理", "功能点授权", "");
				 map.put("msg", "操作成功！");
				 map.put("total", total);
					return map;
					//return "/sys/v_roleSetting";
				}
		 
}

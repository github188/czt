<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<!-- 调拨出库界面 -->
<%@include file="/WEB-INF/jsp/pub/commonJsCss.jsp" %>
<script type="text/javascript" src="<spring:url value="/js/jsp/perform/transverseAssess.js"/>"></script>
  <head>
  <script type="text/javascript">
  var outboundtype = "${outboundtype}";////出库类型(10订单出库 20 调拨出库)
  var username = "${userVo.username}";
  var deptname = "${userVo.deptname}";
  var obid=0;//出库单主表id
</script>
  </head>
  
  <body>
	
	<!-- datagrid页面列表数据 -->
	<div style="padding:10" id="tabdiv">
		<table id="dataTabel"></table>
	</div>
	
	<!-- 查询-->
	<div id="toolbar" style="padding:5px;height:auto;border-top:1px solid #95B8E7">
	<form id="queryForm" style="margin:10;">
	<input type="hidden" name="datetype" id="datetype" value="10"></input>
		<div style="margin-bottom:5px">
		<a href="#" id="viewBtn" class="easyui-linkbutton" iconCls="icon-view" plain="true" onclick="viewD()">查看</a>
		<a href="#" id="newBtn" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openCreate()">新增</a>
		<a href="#" id="delBtn" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleterow()">删除</a>
		
		考核日期从：<input name="searchtime" id="searchtime" class="easyui-datebox" style="width:120px" >&nbsp;
		到：<input name="searchtime2" id="searchtime2" class="easyui-datebox" style="width:120px" >&nbsp;&nbsp;&nbsp;
		类型：<input name="ctype" id="ctype" class="easyui-textbox" style="width:120px" >
		<input class="easyui-textbox"  name="keyword" id="keyword" data-options="buttonText:'查询',buttonIcon:'icon-search',onClickButton:function(){searchData();},prompt:'请输入检查部门或被考核部门'" style="width:300px;height:24px;">
			<a href="#" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-search" style="height:24px;">清空</a>
		</div>
		</form>
	</div>
	
    <!-- 1、新增--------------------------------------------------------------------------------------->
	<div id="add-dlg" class="easyui-dialog" style="width:800px;height:320px;padding:5px 10px;align:center;"
			 closed="true" buttons="#dlg-buttons"  data-options="modal:true,draggable:false">
		<form  id="add-fm" method="post" action="" novalidate  >
			<div >
			<tr>
			<td>
			<table>
			<tr>
		  		 	<td width="5%" height="20" align="left" nowrap>记录人：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input name="username_new" id="username_new" class="easyui-textbox"  style="width:120px"  >
		           	</td>
		           	<td width="5%"  height="20" align="left" nowrap>记录日期：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		               <input name="createtime_new" id="createtime_new" class="easyui-datebox" style="width:160px" />
		           </td>  
		            <td width="5%" height="20" align="left" nowrap>检查部门：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input name="checkdeptid_new" id="checkdeptid_new" class="easyui-textbox" style="width:120px" >
		           	</td>
	         </tr>
	          <tr>
		  		 	<td width="5%" height="20" align="left" nowrap>被考核部门：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input name="checkeddeptid_new" id="checkeddeptid_new" class="easyui-textbox"  style="width:120px"  >
		           	</td>
		           	<td width="5%"  height="20" align="left" nowrap>检查日期：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		               <input name="assessdate_new" id="assessdate_new" class="easyui-datebox" style="width:160px" />
		           </td>  
		            <td width="5%" height="20" align="left" nowrap>检查项目：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input name="checktype_new" id="checktype_new"  style="width:120px" >
		           	</td>
	         </tr>
	         <tr>
		  		 	<td width="5%" height="20" align="left" nowrap>考核类别：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input type="radio" name="ctype_new" value="20"/>奖励
		                <input type="radio" name="ctype_new" value="10"/>考核
		           	</td>
		           	<td width="5%"  height="20" align="left" nowrap>考核金额：</td>
		           	<td width="14%" height="20" align="left" nowrap colspan="3">
		               <input name="amount_new" id="amount_new" class="easyui-numberbox" style="width:140px" />(元)
		           </td>  
	         </tr>
	         <tr>
		  		 	<td width="5%" height="20" align="left" nowrap>考核内容：</td>
		           	<td width="14%" height="20" align="left" nowrap colspan="5">
		                <input name="content_new" id="content_new" class="easyui-textbox" data-options="multiline:true"  style="width:630px"  >
		           	</td>
	         </tr>
	         <input type="hidden" id="ctype_new" class="easyui-textbox"/>
			</table>
			</td>
			</tr>
			</div>
		</form>
		
	 <div id="dlg-buttons">
	    <a href="#" class="easyui-linkbutton" id="btn-done" iconCls="icon-ok" onclick="toSave()">保存</a>
		<a href="#" class="easyui-linkbutton" id="btn-done" iconCls="icon-cancel" onclick="javascript:$('#add-dlg').dialog('close')">关闭</a>
	</div>
	</div>
	
	    <!-- 1、查看--------------------------------------------------------------------------------------->
	<div id="view-dlg" class="easyui-dialog" style="width:800px;height:320px;padding:5px 10px;align:center;"
			 closed="true" buttons="#view-buttons"  data-options="modal:true,draggable:false">
		<form  id="view-fm" method="post" action="" novalidate  >
			<div >
			<tr>
			<td>
			<table>
			<tr>
		  		 	<td width="5%" height="20" align="left" nowrap>记录人：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input name="createname" id="createname" class="easyui-textbox"  style="width:120px"  readonly>
		           	</td>
		            <td width="5%" height="20" align="left" nowrap>考核日期：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input name="assessdate" id="assessdate" class="easyui-textbox" style="width:120px" readonly>
		           	</td>
		           	<td width="5%"  height="20" align="left" nowrap>检查部门：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		               <input name="checkdeptname" id="checkdeptname" class="easyui-textbox" style="width:120px" readonly/>
		           </td>  
		           	 
	         </tr>
	         <tr>
		  		 	
		            <td width="5%" height="20" align="left" nowrap>被考核部门：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input name="checkeddeptname" id="checkeddeptname" class="easyui-textbox" style="width:120px" readonly>
		           	</td>
		           	<td width="5%"  height="20" align="left" nowrap>考核类别：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		               <input name="ctypename" id="ctypename" class="easyui-textbox" style="width:120px" readonly/>
		           </td> 
		           	<td width="5%" height="20" align="left" nowrap>考核金额：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input name="amount" id="amount" class="easyui-textbox"  style="width:100px"  readonly>(元)
		           	</td>
		           	
	         </tr>
	          <tr>
	          	<td width="5%"  height="20" align="left" nowrap>考核进度：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		               <input name="flagname" id="flagname" class="easyui-textbox" style="width:120px" readonly/>
		           </td>  
		  		 		<td width="5%"  height="20" align="left" nowrap>考核项目：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		               <input name="checktypename" id="checktypename" class="easyui-textbox" style="width:120px" readonly/>
		           </td>  
		            <td width="5%" height="20" align="left" nowrap>审核人员姓名：</td>
		           	<td width="14%" height="20" align="left" nowrap>
		                <input name="auditname" id="auditname" class="easyui-textbox" style="width:120px" readonly>
		           	</td>
		           
	         </tr>
	         <tr>
		  		 	<td width="5%" height="20" align="left" nowrap>考核内容：</td>
		           	<td width="14%" height="20" align="left" nowrap colspan="5">
		                <input name="content" id="content" class="easyui-textbox" data-options="multiline:true"  style="width:630px"  readonly>
		           	</td>
	         </tr>
			</table>
			</td>
			</tr>
			</div>
			
			<div >
			<tr>
			<td>
			<table width="100%" id="outboundlinelist1">
			</table>
			</td>
			</tr>
			</div>
		</form>
	 <div id="view-buttons">
		<a href="#" class="easyui-linkbutton" id="btn-done" iconCls="icon-cancel" onclick="javascript:$('#view-dlg').dialog('close')">关闭</a>
	</div>
	</div>
	
  </body>
</html>

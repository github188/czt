<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/include/taglib.jsp" %><!doctype html>

<html>

<%@include file="/WEB-INF/jsp/pub/commonJsCss.jsp" %>
<script type="text/javascript" src="<spring:url value="/js/jsp/safe/facilities.js"/>"></script>
  </head>
  
  <body>
	
	
	<!-- datagrid页面列表数据 -->
	<div style="padding:10" id="tabdiv">
		<table id="dataTable"></table>
	</div>

	<!-- 查询-->
	<div id="toolbar" style="padding:5px;height:auto;border-top:1px solid #95B8E7">
	<form id="queryForm" style="margin:10;">
		<div style="margin-bottom:5px">使用状态
			<select name="ctype" id="ctype" class="easyui-combobox" style="width:70px;" data-option='selected:true;'>
			<option value="40" selected>全部</option>
			<option value="10" >完好</option>
			<option value="20">一般</option> 
			<option value="30">差</option>
			</select>
			<input class="easyui-textbox"  name="device" id="devices" data-options="buttonText:'查询',buttonIcon:'icon-search',onClickButton:function(){searchFacilities();},prompt:'请输入设施名称/型号...'" style="width:350px;height:24px;">
			<a href="#" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-search" style="height:24px;">清空</a>
		</div>
		</form>
	</div>
	
	
	<c:import url="/WEB-INF/jsp/pub/sessionPage.jsp?paramPage=Facilities"></c:import>


  </body>
</html>

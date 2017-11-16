/**
 * 页面列表datagrid初始化
 */
jQuery(function($){
	var nowTime01 = getDateYM01();
	var nowTime = getDateYMD();
	$('#searchtime').datebox("setValue",nowTime01);
	$('#searchtime2').datebox("setValue",nowTime);
	//initdatetype();
	//var obj = $("#consignsorsearch");
	//initconsignsor(obj);
	initctype();
	$('#keyword').textbox('textbox').keydown(function(e){
		if(e.keyCode==13){
			searchData();
		}
	})

	$('#dataTabel').datagrid({
		title:'横向考核', //标题
		method:'post',
		iconCls:'icon-edit', //图标
		singleSelect:true, //单选
		height:490, //高度
		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
		striped: true, //奇偶行颜色不同
		collapsible:true,//可折叠
		nowrap:false,
		//货主默认为长沙11430101
		url:baseURL+"/perform/transverseAssess/getTransverseAssessPageList.json?outboundtype="+outboundtype, //数据来源
		sortName: 'id', //排序的列
		sortOrder: 'desc', //倒序
		remoteSort: true, //服务器端排序
		idField:'id', //主键字段
		pageNumber: 1, 
		pageSize : 15,
		pageList: [15,30,50],
		queryParams:{
			}, //查询条件
		pagination:true, //显示分页
		//pageSize : 1,
		rownumbers:true, //显示行号
		columns:[[
			{field:'id',checkbox:true,width:2}, //显示复选框
			{field:'assessdate',title:'考核日期',width:$(this).width()*0.05,sortable:true,
				formatter: formatDatebox //需要formatter一下才能显示正确的数据
			},
			{field:'checkdeptname',title:'检查部门',width:$(this).width()*0.07,
				formatter:function(value,row,index){return row.checkdeptname;}
			},
			{field:'checktypename',title:'检查项目',width:$(this).width()*0.07,
				formatter:function(value,row,index){return row.checktypename;}
			},
			{field:'checkeddeptname',title:'被考核部门',width:$(this).width()*0.07,
				formatter:function(value,row,index){return row.checkeddeptname;}
			},
			{field:'content',title:'考核内容',width:$(this).width()*0.24,
				formatter:function(value,row,index){return row.content;}
			},
			{field:'amount',title:'考核金额',width:$(this).width()*0.05,sortable:true,
				formatter:function(value,row,index){return row.amount;}
			},
			{field:'flagname',title:'考核状态',width:$(this).width()*0.05,
				formatter:function(value,row,index){return row.flagname;}
			}
			
		]],
		toolbar:'#toolbar',
		onLoadSuccess:function(){
			$('#dataTabel').datagrid('clearSelections'); //一定要加上这一句，要不然datagrid会记住之前的选择状态，删除时会出问题
			$('#tabdiv .panel-header').css('display','none'); 
			
		},

		onDblClickCell:function(index,field,value){
					viewD();
				}
	});
	

});

//初始化考核类型
function initctype(){
	$("#ctype").combobox({
        valueField:'id',
        textField:'value',
        data:[
        	{
        		id:10,
        		value:"考核"
        	},{
        		id:20,
        		value:"奖励"
        	}],
        	onLoadSuccess: function () {
        		$("#ctype").combobox('select', ""); //默认不选中
        	}
    });
}

//初始化日期类型
function initdatetype(){
	$("#datetype").combobox({
        valueField:'id',
        textField:'value',
        data:[
        	{
        		id:10,
        		value:"出库日期"
        	},{
        		id:20,
        		value:"订单日期"
        	}],
	onLoadSuccess: function () {
		 var val = $(this).combobox("getData");
		 for (var item in val[0]) {
             if (item == "id") {
                 $(this).combobox("select", val[0][item]);
             }
         }
	}
    });
}

//初始被考核部门
function initdep(obj){
	obj.combotree({
      url:baseURL+"/sys/user/getDeptTree.json", //数据来源
	onLoadSuccess: function (node, data) {
		if (data != null) { 
			obj.combotree('setValue', { id: data[0].id, text: data[0].text }); 
		}
	}
    });
}

function initchecktype(){
	$('#checktype_new').combobox({
        valueField:'id',
        textField:'contentlist',
        url:baseURL+"/comm/combobox/getComboboxByTypeCode.json?typeCode=ASSESSTYPE", //数据来源
	onLoadSuccess: function () {
		 var val = $(this).combobox("getData");
		 for (var item in val[0]) {
             if (item == "id") {
                 $(this).combobox("select", val[0][item]);
             }
         }
	}
    });
}

//查询
function searchData(){
	var params = $('#dataTabel').datagrid('options').queryParams; //先取得 datagrid 的查询参数
	var fields =$('#queryForm').serializeArray(); //自动序列化表单元素为JSON对象
	$.each( fields, function(i, field){
		params[field.name] = field.value; //设置查询参数
	}); 
	$('#dataTabel').datagrid('reload'); //设置好查询参数 reload 一下就可以了
}
//清空查询条件
function clearForm(){
	//$('#queryForm').form('clear');
	$('#keyword').textbox("setValue","");
	$('#keyword').textbox("setText","");
	initctype();
	searchData();
}


/**
 * 打开新增窗口
 */
function openCreate(){
	$('#add-fm').form('clear');
	$('#add-dlg').dialog('open').dialog('setTitle','横向考核新增');
	
	//初始化记录人、记录日期、检查日期、检查部门
	var nowTimeYMD = getDateYMD();
	$('#username_new').textbox("setValue",username);
	$('#username_new').textbox("setText",username);
	$('#createtime_new').datebox("setValue",nowTimeYMD);//创建日期
	$('#assessdate_new').datebox("setValue",nowTimeYMD);//检查日期
	$('#checkdeptid_new').textbox("setValue",deptname);//检查部门
	$('#checkdeptid_new').textbox("setText",deptname);
	
		//初始化被考核部门
		var obj = $('#checkeddeptid_new');
		initdep(obj);
 
		//初始化检查项目
		initchecktype();
		
		//默认选中奖励
		setRadioValue("20");  
	
}

function setRadioValue(value) {  
    $("input[name='ctype_new']").each(function() {  
        if ($(this).val() == value) {  
            $(this).prop("checked", "checked");  
        }  
    });  
} 


/**
 * 保存
 * @returns
 */
function toSave()
{
	var ctypevalue = $("input[name='ctype_new']:checked").val();  
	$('#ctype_new').textbox("setValue",ctypevalue);//通过隐藏变量设置考核类别时奖励还是考核
	var amount_new = $("#amount_new").val();
	 var content_new = $("#content_new").val();
	 
	 var cktype = $('#checktype_new').combobox('getValue')
	 
	  if(amount_new=="" || parseFloat(amount_new)==0 || isNaN(parseFloat(amount_new))){
		  $.messager.alert('提示',"请输入考核金额或您输入的考核金额为0，请重新输入！",'info');
			return;
	  }
	  if(content_new==""){
		  $.messager.alert('提示',"请输入考核内容！",'info');
			return;
	  }
	  if(cktype==""){
		  $.messager.alert('提示',"请输检查项目！",'info');
			return;
	  }
	 
	  $('#add-fm').form('submit',{
		  url:baseURL+"/perform/transverseAssess/doSave.json", 
			success : function(data) {
				data = eval('('+data+')');
				$('#add-dlg').dialog('close');
				$('#dataTabel').datagrid('reload'); 
				$.messager.show({
					title : '提示',
					msg :  '横向考核'+data.msg+'！',
				});
	         },
	         error: function(e) { 
	        	 $.messager.show({
	 				title : '提示',
	 				msg :  '新增出错，请联系管理员!',
	 			});
	          	} 
	  });
	  

	}

/**
 * 查看窗口
 */
  function viewD(){
	  $('#view-fm').form('clear');
	  var rows = $('#dataTabel').datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert('提示',"请选择你要查看的信息",'info');
			return;
		}
		if(rows.length > 1){
			$.messager.alert('提示',"只能选择一条信息进行查看",'info');
			return;
		}
		
		var row = $('#dataTabel').datagrid('getSelected');
		//var outboundid = row.outboundid;
		row.assessdate=formatDatebox(row.assessdate);
		//row.outtime=formatDatebox(row.outtime);
		if (row){
			$('#view-dlg').dialog('open').dialog('setTitle',"查看");
			$('#view-dlg').form('load',row);
		}

	}
  
	//出库单明细列表
	  function getoutboundlinelist(obj,outboundid,dgheight)
	  {
	  		//获取领料新增分配的物资明细列表
	  	obj.datagrid({
	  		title:'出库单明细列表', //标题
	  		method:'post',
	  		iconCls:'icon-view', //图标
	  		singleSelect:false, //多选
	  		height:dgheight, //高度
	  		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
	  		striped: true, //奇偶行颜色不同
	  		collapsible:false,//可折叠
	  		url:baseURL+"/wms/outbound/getOutboundLineList.json?outboundid="+outboundid, 
	  		sortName: 'id', //排序的列
	  		sortOrder: 'desc', //倒序
	  		remoteSort: true, //服务器端排序
	  		idField:'id', //主键字段
	  		singleSelect: true,
	  		pagination:false, //显示分页
	  		//pageSize : 1,
	  		rownumbers:true, //显示行号
	  		columns:[[
	  			{field:'cigarettename',title:'卷烟名称',width:$(this).width()*0.1,
	  				formatter:function(value,row,index){return row.cigarettename;}
	  			},
	  			{field:'cigarettecode',title:'卷烟编码',width:$(this).width()*0.1,sortable:true,
	  				formatter:function(value,row,index){return row.cigarettecode;} //需要formatter一下才能显示正确的数据
	  			},
	  			{field:'itemqty',title:'条烟数量',width:$(this).width()*0.1,sortable:true,
	  				formatter:function(value,row,index){return row.itemqty;} //需要formatter一下才能显示正确的数据
	  			},
	  			{field:'boxqty',title:'件烟数量',width:$(this).width()*0.1,sortable:true,
	  				formatter:function(value,row,index){return row.boxqty;}
	  			}
	  		]],
	  		onLoadSuccess:function(){
	  			//$('#inboundlinediv .panel-header').css('display','none'); 
	  			
	  		}
	  	});
	  	}
	  
	  
	//删除
		function deleterow(){
			var rows = $('#dataTabel').datagrid('getSelections');
			if(rows.length==0){
			$.messager.alert('提示',"请选择你要删除的信息",'info');
			return;
		}
			var row = $('#dataTabel').datagrid('getSelected');
			//var outboundid = row.outboundid;
			var flag=row.flag;
			var id = row.id;
			if(flag!="10"){
				$.messager.alert('提示',"您选择的信息已经考核完成或分配完成，不能再删除！",'info');
				return;
			}
			
			$.messager.confirm('提示','确定要删除吗?',function(result){
	        if (result){
	        	
	        	$.post(baseURL+'/perform/transverseAssess/doDel.json?id='+id,function(data){
		        	$('#dataTabel').datagrid('reload'); 
	        		$.messager.show({
						title : '提示',
						msg :  data.total+'个信息被删除'+data.msg+'！',
					});
	        	});
	        }
	    });
		}

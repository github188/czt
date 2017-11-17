/**
 * 页面列表datagrid初始化
 */
jQuery(function($){
	$('#equipname').textbox('textbox').keydown(function(e){
		if(e.keyCode==13){
			searchEquipment();
		}
	})
	$('#dataTable').datagrid({
		title:'设备管理', //标题
		method:'post',
		iconCls:'icon-edit', //图标
		singleSelect:false, //多选
		height:490, //高度
		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
		striped: true, //奇偶行颜色不同
		collapsible:true,//可折叠
		url:baseURL+"/cost/equipment/getEquipmentPageList.json", //数据来源
		sortName: 'purchasetime', //排序的列
		sortOrder: 'desc', //倒序
		remoteSort: false, //服务器端排序
		idField:'id', //主键字段
		pageNumber: 1, 
		pageSize : 10,
		pageList: [10,30,50],
		queryParams:{
			}, //查询条件
		pagination:true, //显示分页
		//pageSize : 1,
		rownumbers:true, //显示行号
		columns:[[
			{field:'id',checkbox:true,width:2}, //显示复选框
			{field:'equipname',title:'设备名称',width:50,
				formatter:function(value,row,index){return row.equipname;} //需要formatter一下才能显示正确的数据
			},
			{field:'typename',title:'类型名称',width:30,
				formatter:function(value,row,index){return row.typename;}
			},
			{field:'brand',title:'品牌',width:30,
				formatter:function(value,row,index){return row.brand;}
			},
			{field:'model',title:'型号',width:30,
				formatter:function(value,row,index){return row.model;}
			},
			{field:'qty',title:'数量',width:20,sortable:true,
				formatter:function(value,row,index){return row.qty;}
			},
			{field:'manufacturer',title:'生产厂家',width:30,
				formatter:function(value,row,index){return row.manufacturer;}
			},
			{field:'purchasetime',title:'采购时间',width:30,
				formatter:function(value,row,index){
					var date = new Date(value);
					            var year = date.getFullYear().toString();
					            var month = (date.getMonth() + 1);
					            var day = date.getDate().toString();
					  if (month < 10) {
						                month = "0" + month;
						            }
					 if (day < 10) {
						                day = "0" + day;
						            }
						 return year + "-" + month + "-" + day ;
				}
			},
			{field:'purchaseamount',title:'采购金额',width:30,
				formatter:function(value,row,index){return row.purchaseamount;}
			},
			{field:'purchasestyle',title:'采购方式',width:30,
				formatter:function(value,row,index){return row.purchasestyle;}
			},
			{field:'servicelife',title:'规定使用年限',width:30,
				formatter:function(value,row,index){return row.servicelife;}
			},
			{field:'designability',title:'设计能力',width:30,
				formatter:function(value,row,index){return row.designability;}
			},
			{field:'actualability',title:'实际能力',width:30,
				formatter:function(value,row,index){return row.actualability;}
			},
			{field:'power',title:'额定功率',width:20,
				formatter:function(value,row,index){return row.power;}
			},
			{field:'guaranteed',title:'质保期',width:20,
				formatter:function(value,row,index){return row.guaranteed;}
			},
			{field:'status',title:'状态',width:10,
				formatter:function(value,row,index){
					if( row.status == '10'){
						return '在用';
					}
					else if( row.status == '20'){
						return '置闲';
					}
					else if( row.status == '30'){
						return '报废';
					}
				}
			},
		]],
		toolbar:'#toolbar',
		onLoadSuccess:function(){
			$('#dataTable').datagrid('clearSelections'); //一定要加上这一句，要不然datagrid会记住之前的选择状态，删除时会出问题
			$('#tabdiv .panel-header').css('display','none'); 
			
		}
	});
	
});
/**
 * 打开修改窗口
 */
  function openEdit(){
	  $('#edit-fm').form('clear');
	    $('#edit-fm').form('reset');
	    $(".formtd").each(function(){
		 	   $(this).html("");
		 	 });
		var rows = $('#dataTable').datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert('提示',"请选择你要更新的设备管理信息",'info');
			return;
		}
		if(rows.length > 1){
			$.messager.alert('提示',"只能选择一条设备管理信息进行更新",'info');
			return;
		}
		var row = $('#dataTable').datagrid('getSelected');
		if (row){
			$('#edit-dlg').dialog('open').dialog('setTitle','修改设备管理信息');
			$('#edit-fm').form('load',row);
			//alert(row.id);
			//url = '/BS56/sys/roleNew.json';
		}

	}
  
  /**
   * 保存修改的信息

   */
  function saveEdit(){
	 // alert('---');
  	$('#edit-fm').form('submit',{
  		onSubmit: function(){
			var isValidate = $(this).form('validate');
			 //alert('11');
			if(isValidate){ 
				//alert('33');
				//$('#loading').window('open');
			}
			return isValidate;	
		},
  		url:baseURL+"/cost/equipment/doupdateEquipment.json",
		data:$("#edit-fm").serializeArray(),
		beforeSend : function () {
			$.messager.progress({	
				text : '正在修改中...',
			});		 
		},
  		success: function(data){
  			//alert('44');	
			//$('#loading').window('close');
  			//alert(data);
  		data = eval('('+data+')');
			$('#edit-dlg').dialog('close');
			$('#dataTable').datagrid('reload'); 
  		$.messager.show({
				title : '提示',
				msg :  data.total+'个设备管理信息修改'+data.msg+'！',
			});
		}
  	});
  }
  /**
   * 打开设备管理信息新增窗口
   */
  function newadd(){
  	$('#add-dlg').dialog('open').dialog('setTitle','新增设备管理信息');
  	$('#add-fm').form('reset');
  	var nowTime = getDateYMD();
  	 $('#purchasetime').datebox('setValue',nowTime );
  	//物资类别一级
     $("#lvl1").combobox({
     	url : baseURL+"/cost/suppliesimp/getSPLTypesCombobox.json?clevel=2&fid=2",//返回json数据的url
     	valueField : "id",//这个id和你返回json里面的id对应
     	textField : "typename", //这个text和你返回json里面的text对应
     	required:true,
     	onSelect: function (row1) {  
             if (row1 != null) {  
             	//物资类别二级
                 $('#lvl2').combobox({  
                 	url : baseURL+"/cost/suppliesimp/getSPLTypesCombobox.json?clevel=3&fid="+row1.id,//返回json数据的url
                     valueField : "id",//这个id和你返回json里面的id对应
                 	textField : "typename", //这个text和你返回json里面的text对应
                 	required:true,
                 	onSelect: function (row2) {  
                         if (row2 != null) {  
                         	//物资类别三级
                             $('#typeid').combobox({  
                             	url : baseURL+"/cost/suppliesimp/getSPLTypesCombobox.json?clevel=4&fid="+row2.id,//返回json数据的url
                             	valueField : "id",//这个id和你返回json里面的id对应
                             	textField : "typename", //这个text和你返回json里面的text对应
                             	required:true,
                             	onSelect: function (row3) {
                             		$('#unitid').val(row3.unit);
                             	}
                             });  
                         }  
                     }  
                 });  
             }  
         }  
     })
  }


  /**
   * 保存新建设备管理信息
   */
  function saveNew(){


  	//var bdate=$('#buydate_string').val();
  	//alert("---");
  	$('#add-fm').form('submit',{
  		onSubmit: function(){
  			var isValidate = $(this).form('validate');
  			if(isValidate){
  				//$('#loading').window('open');
  			}
  			return isValidate;
  		},
  		//url:baseURL+"/sys/vehicle/doinsertVehicleVo.json?bdate="+bdate,
  		url:baseURL+"/cost/equipment/doEquipmentNew.json",
  		data:$("#add-fm").serializeArray(),
  		beforeSend : function () {
  			$.messager.progress({
  				text : '正在新增中...',
  			});
  		},
  		success: function(data){
  			//$('#loading').window('close');
  			data = eval('('+data+')');
  			$('#add-dlg').dialog('close');
  			$('#dataTable').datagrid('reload'); 
      		$.messager.show({
  				title : '提示',
  				msg :  data.total+'个设备管理信息新增'+data.msg+'！',
  			});
  			//$('#loading').window('close');
  		}
  	});
  }
//删除
	function deleterow(){
		var rows = $('#dataTable').datagrid('getSelections');
		if(rows.length==0){
		$.messager.alert('提示',"请选择你要删除的设备管理信息",'info');
		return;
	}
		$.messager.confirm('提示','确定要删除吗?',function(result){
      if (result){
      	var ps = "";
      	$.each(rows,function(i,n){
      		if(i==0) 
      			ps += "?id="+n.id;
      		else
      			ps += "&id="+n.id;
      	});
      	$.post(baseURL+'/cost/equipment/doEquipmentDel.json'+ps,function(data){
	        	$('#dataTable').datagrid('reload'); 
	        	//console.log("del--"+data);
      		$.messager.show({
					title : '提示',
					msg :  data.total+'个设备管理信息'+data.msg+'！',
				});
      	});
      }
  });
	}

//查询
function searchEquipment(){
	var params = $('#dataTable').datagrid('options').queryParams; //先取得 datagrid 的查询参数
	var fields =$('#queryForm').serializeArray(); //自动序列化表单元素为JSON对象
	$.each( fields, function(i, field){
		params[field.name] = field.value; //设置查询参数
	}); 
	$('#dataTable').datagrid('reload'); //设置好查询参数 reload 一下就可以了
}
//清空查询条件
function clearForm(){
	$('#queryForm').form('clear');
	searchEquipment();
}


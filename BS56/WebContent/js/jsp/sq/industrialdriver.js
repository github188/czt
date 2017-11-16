var url;

//信息上传进度条初始化
$(function(){
	  $('#loading').window({
			title:'系统提示',
			closable:false,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false,
			width:355,
			height:120,
			closed:true,
		    modal:true   
		});
});

/**
 * 页面列表datagrid初始化
 */

jQuery(function($){
	$.extend($.fn.validatebox.defaults.rules, {    
		    phonenum: { //验证手机号   
		        validator: function(value, param){ 
		         return /^1[3-8]+\d{9}$/.test(value);
		        },    
		        message: '请输入正确的手机号码。'   
		    },
	});

	$('#drivernamex').textbox('textbox').keydown(function(e){
			if(e.keyCode==13){
				searchIndustrialdriver();
			}
		})
	$('#dataTabel').datagrid({
		title:'工业司机信息', //标题
		method:'post',
		iconCls:'icon-edit', //图标
		singleSelect:false, //多选
		height:420, //高度
		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
		striped: true, //奇偶行颜色不同
		collapsible:true,//可折叠
		url:baseURL+"/sq/industrialdriver/getIndustrialdrivers.json", //数据来源
		sortName: 'id', //排序的列
		sortOrder: 'desc', //倒序
		remoteSort: true, //服务器端排序
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
			{field:'drivername',title:'司机姓名',width:20,
				formatter:function(value,row,index){return row.drivername;}
			},
			{field:'phonenum',title:'电话',width:20,
				formatter:function(value,row,index){return row.phonenum;}
			},
			{field:'factoryname',title:'卷烟厂家名称',width:20,
				formatter:function(value,row,index){return row.factoryname;} //需要formatter一下才能显示正确的数据
		
			},
		
		]],
		toolbar:'#toolbar',
		onLoadSuccess:function(){
			$('#dataTabel').datagrid('clearSelections'); //一定要加上这一句，要不然datagrid会记住之前的选择状态，删除时会出问题
			$('#tabdiv .panel-header').css('display','none');
		}
	});
});

/**
 * 打开新增工业司机信息窗口
 */
function newIndustrialdriver(){
	$('#add-dlg').dialog('open').dialog('setTitle','工业司机信息');
	$('#add-fm').form('clear');
	//url = '/BS56/sq/starNew.json';
	//$('#add-dlg').dialog('colse');
	//厂家下拉列表
	$("#factoryid").combobox({
		url : baseURL+"/sq/industrialdriver/getCigfactoryCombobox.json",//返回json数据的url
		valueField : "id",//这个id和你返回json里面的id对应
		textField : "name", //这个text和你返回json里面的text对应
		//panelHeight: 'auto'
		   onLoadSuccess : function(){       
			 $("#factoryid").combobox('setValue', '7');
		  $("#factoryid").combobox('setText', '湖南中烟公司');
			
		}
	})
}


/**
 * 保存新建工业司机信息
 */
function saveAdd(){
	//获取XX名称传入后台
var textval=$('#factoryid').combobox('getText');
$('#factoryname').val(textval);
	
	$('#add-fm').form('submit',{
		onSubmit: function(){
			var isValidate = $(this).form('validate');
			if(isValidate){
				//$('#loading').window('open');
			}
			return isValidate;
		},
		url:baseURL+"/sq/industrialdriver/doindustrialdriverNew.json",
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
			$('#dataTabel').datagrid('reload'); 
    		$.messager.show({
				title : '提示',
				msg :  data.total+'个工业司机信息新增'+data.msg+'！',
			});
			//$('#loading').window('close');
		}
	});
	
}


/**
 * 打开修改窗口
 */
  function openEdit(){
		var rows = $('#dataTabel').datagrid('getSelections');
		$('#edit-dlg').dialog('open').dialog('setTitle','工业司机信息');
		$('#edit-fm').form('clear');
		if(rows.length==0){
			$.messager.alert('提示',"请选择你要更新的工业司机信息",'info');
			return;
		}
		if(rows.length > 1){
			$.messager.alert('提示',"只能选择一条工业司机信息进行更新",'info');
			return;
		}
		var row = $('#dataTabel').datagrid('getSelected');
		if (row){
			$('#edit-dlg').dialog('open').dialog('setTitle','工业司机信息');
			$('#edit-fm').form('load',row);
			
			
		
			$("#factoryid1").combobox({
				url : baseURL+"/sq/industrialdriver/getCigfactoryCombobox.json",//返回json数据的url
				valueField : "id",//这个id和你返回json里面的id对应
				textField : "name", //这个text和你返回json里面的text对应
				//panelHeight: 'auto'
				   onLoadSuccess : function(){         
					$("#factoryid1").combobox('setText', row.factoryname);
				}
			})
		
		}

	}
  
  /**
   * 保存修改的参数信息

   */

  function saveEdit(){
	  var textval=$('#factoryid1').combobox('getText');
		$('#factoryname1').val(textval);
		$('#edit-fm').form('submit',{
			onSubmit: function(){
				var isValidate = $(this).form('validate');
				if(isValidate){
					//$('#loading').window('open');
				}
				return isValidate;
			},
			url:baseURL+"/sq/industrialdriver/doindustrialdriverUpdate.json",
			data:$("#edit-fm").serializeArray(),
			beforeSend : function () {
				$.messager.progress({
					text : '正在新增中...',
				});
			},
			success: function(data){
				//$('#loading').window('close');
				data = eval('('+data+')');
				$('#edit-dlg').dialog('close');
				$('#dataTabel').datagrid('reload'); 
	    		$.messager.show({
					title : '提示',
					msg :  data.total+'个工业司机信息修改'+data.msg+'！',
				});
				//$('#loading').window('close');
			}
		});
	}

  
//删除
	function industrialdriverDel(){
		var rows = $('#dataTabel').datagrid('getSelections');
		if(rows.length==0){
		$.messager.alert('提示',"请选择你要删除的工业司机信息",'info');
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
        	$.post(baseURL+'/sq/industrialdriver/doindustrialdriverdelete.json'+ps,function(data){
	        	$('#dataTabel').datagrid('reload'); 
	        	//console.log("del--"+data);
        		$.messager.show({
					title : '提示',
					msg :  data.total+'个工业司机信息被删除'+data.msg+'！',
				});
        		//$('#dataTabel').datagrid('reload');
        	});
        }
    });
	}
	
	  //查询
	function searchIndustrialdriver(){
		//alert("------");
		var params = $('#dataTabel').datagrid('options').queryParams; //先取得 datagrid 的查询参数
		var fields =$('#queryForm').serializeArray(); //自动序列化表单元素为JSON对象
		//alert(params.length);
		//alert(fields.length);
		$.each( fields, function(i, field){
			//alert(field.name);
			//alert(field.value);
			params[field.name] = field.value; //设置查询参数
		}); 
		$('#dataTabel').datagrid('reload'); //设置好查询参数 reload 一下就可以了

	}
	//清空查询条件
	function clearForm(){
		$('#queryForm').form('clear');
		searchIndustrialdriver();
	}
	
	function openTopWindow(url, title, width, height) {
        title = title == undefined ? '&nbsp;' : title;
        width = width == undefined ? 800 : width;
        height = height == undefined ? 300 : height;
        if (url != undefined) {
            var content = '<iframe name="eui-open-page" scrolling="auto" frameborder="0" src="' + url + '" style="width:100%;height:100%;"></iframe>';
            parent.$('#openWindow').window({
                title: title,
                width: width,
                height: height,
                content: content,
                modal: true,
                animate: true,
                minimizable: false
            });
        }
    }
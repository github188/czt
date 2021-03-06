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

	$('#dataTabel').datagrid({
		title:'散烟区至立库', //标题
		method:'post',
		iconCls:'icon-edit', //图标
		singleSelect:true, //单选
		height:490, //高度
		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
		striped: true, //奇偶行颜色不同
		collapsible:true,//可折叠
		//货主默认为长沙11430101
		url:baseURL+"/wms/moveareastock/getMoveareastockPageList.json?movetype="+movetype, //数据来源
		sortName: 'id', //排序的列
		sortOrder: 'desc', //倒序
		remoteSort: true, //服务器端排序
		idField:'id', //主键字段
		pageNumber: 1, 
		pageSize : 20,
		pageList: [20,30,50],
		queryParams:{
			}, //查询条件
		pagination:true, //显示分页
		//pageSize : 1,
		rownumbers:true, //显示行号
		columns:[[
			{field:'id',checkbox:true,width:2}, //显示复选框
			{field:'sourceareaname',title:'原区域',width:$(this).width()*0.1,
				formatter:function(value,row,index){return row.sourceareaname;} 
			},
			{field:'targetareaname',title:'目标区域',width:$(this).width()*0.1,
				formatter:function(value,row,index){return row.targetareaname;} 
			},
			{field:'boxqty',title:'移库数量(件)',width:$(this).width()*0.1,
				formatter:function(value,row,index){return row.boxqty;} 
			},
			{field:'barqty',title:'移库数量(条)',width:$(this).width()*0.1,
				formatter:function(value,row,index){return row.barqty;} 
			},
			{field:'outchecktime',title:'出库日期',width:$(this).width()*0.1,formatter: formatDatebox,sortable:true},
			{field:'createusername',title:'记录人',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.createusername;}
			},
			{field:'outcheckusername',title:'出库人员',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.outcheckusername;}
			},
			{field:'receivecheckusername',title:'收货人员',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.receivecheckusername;}
			},
			{field:'auditflagname',title:'状态',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.auditflagname;}
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

function initOrderDatebox(){
    $('#orderdate_new').datebox({
    	onSelect: function(date){
    		var orderdate = $("#orderdate_new").val();
    		var consignsor = $("#consignsor_new").val();
    		initRow(orderdate,consignsor);
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
	$('#queryForm').form('clear');
	searchData();
}


/**
 * 打开新增窗口
 */
function openCreate(){
	$('#add-fm').form('clear');
	$('#add-dlg').dialog('open').dialog('setTitle','散烟区至立库移库');
	
	//初始化订单日期
	$('#username_new').textbox("setValue",username);
	$('#username_new').textbox("setText",username);
	$('#sourceareaname_new').textbox("setValue","散烟区");
	$('#sourceareaname_new').textbox("setText","散烟区");
	$('#targetareaname_new').textbox("setValue","立库");
	$('#targetareaname_new').textbox("setText","立库");
		//初始化货主
		//var obj = $('#consignsor_new');
		//initconsignsor(obj);
 
	var nowTime = getDateYMD();
	//初始化订单日期从系统订单任务表中取订单日期
	//var consignsor = $("#consignsor_new").val();
	
	$('#createtime_new').datebox("setValue",nowTime);
	
	//卷烟列表
	var keyword = $("#keyword_item").val();
	initItemList(keyword);
	

	//移库明细列表
	obid = 0;
	var obj = $('#Moveareastocklinelist');
	getmovearealinelist(obj,obid,150);
}



function toSave()
{
	var cigarettecode = $("#cigarettecode").val();
	var cigarettename = $("#cigarettename").val();
	var jtsize = $("#jtsize").val();
	var qtynew = $("#qtynew").val();
	if(cigarettecode=="" ) {
		$.messager.alert('提示',"请选择您要移库的商品！",'info');
		return;
	}
	if(qtynew=="" || qtynew==0) {
		$.messager.alert('提示',"请输入您要移库的数量！",'info');
		return;
	}
		$.ajax({ 
		    url: baseURL+'/wms/moveareastock/doCheckqty.json?cigarettecode='+cigarettecode+"&qtynew="+qtynew+"&areaid="+areaid, 
		    type: 'POST', 
		    success: function(data){
		    	var actualqty = data.actualqty;
		    	if(parseFloat(qtynew) > parseFloat(actualqty) || parseFloat(actualqty)==0 || isNaN(parseFloat(qtynew))) {
		    		$.messager.alert('提示',"您的商品"+cigarettename+"移库量"+qtynew+"(条),但实际库存显示为"+actualqty+"(条),您不能完成移库，请联系管理员！",'info');
		    		return;
		    	}
		    	else{
		    		//$('#add-dlg').form('load',row);
		    		
		    		 $('#add-fm').form('submit',{
		    				onSubmit: function(){
		    					var isValidate = $(this).form('validate');
		    					if(isValidate){
		    						//$('#loading').window('open');
		    					}
		    					return isValidate;
		    				},
		    				url:baseURL+"/wms/moveareastock/doSave.json?movetype=20&targetareaid="+targetareaid+"&areaid="+areaid+"&obid="+obid, 
		    				success : function(data) {
		    					data = eval('('+data+')');
		    					$('#dataTabel').datagrid('reload'); 
		    					obid=data.obid;
		    					//获取出库单明细列表
		    					var obj = $('#Moveareastocklinelist');
		    					getmovearealinelist(obj,obid,150);
		    					$.messager.show({
		    						title : '提示',
		    						msg :  '移库'+data.msg+'！',
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
			}
		   }); 
	}

function searchType(){
	var keyword = $("#keyword_item").val();
	initItemList(keyword);
}

//清空查询条件
function clearForm(){
	$('#add-fm').form('clear');
	searchType();
}
//待续............
function initItemList(keyword){
	$('#itemlist').datagrid({
		title:'卷烟明细', //标题
		method:'post',
		iconCls:'icon-view', //图标
		singleSelect:false, //多选
		height:140, //高度
		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
		striped: true, //奇偶行颜色不同
		collapsible:false,//可折叠
		url:baseURL+"/wms/outbound/getItemList.json?keyword="+keyword, 
		sortName: 'id', //排序的列
		sortOrder: 'desc', //倒序
		remoteSort: true, //服务器端排序
		idField:'id', //主键字段
		singleSelect: true,
		pagination:false, //显示分页
		//pageSize : 1,
		rownumbers:true, //显示行号
		columns:[[
			{field:'itemno',title:'卷烟编码',width:$(this).width()*0.05,sortable:true,
				formatter:function(value,row,index){return row.itemno;} //需要formatter一下才能显示正确的数据
			},
			{field:'itemname',title:'卷烟名称',width:$(this).width()*0.1,
				formatter:function(value,row,index){return row.itemname;}
			},
			{field:'shiptypename',title:'卷烟类别',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.shiptypename;} //需要formatter一下才能显示正确的数据
			},
			{field:'bigboxBar',title:'件码',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.bigboxBar;} //需要formatter一下才能显示正确的数据
			},
			{field:'jtSize',title:'件条比例',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.jtSize;}
			},
			{field:'_operate',title:'操作',width:$(this).width()*0.05,
				formatter:function(value,row,index){return '<input name="isShow" type="radio" onclick="radClick('+index+')"/>';}
			}
		]],
		onLoadSuccess:function(){
			//$('#inboundlinediv .panel-header').css('display','none'); 
			
		}
	});
}

function radClick(index){ 
	 $('#itemlist').datagrid('selectRow',index);// 关键在这里  
	    var row = $('#itemlist').datagrid('getSelected');  
	    if (row){  
	    	$('#cigarettecode').textbox("setValue",row.itemno);
	    	$('#cigarettecode').textbox("setText",row.itemno);
	    	$('#cigarettename').textbox("setValue",row.itemname);
	    	$('#cigarettename').textbox("setText",row.itemname);
	    	$('#jtsize').textbox("setValue",row.jtSize);
	    	$('#jtsize').textbox("setText",row.jtSize);
	    	$('#barcode').textbox("setValue",row.bigboxBar);
	    	$('#barcode').textbox("setText",row.bigboxBar);
	    }
}

//第二步中分拣通道数据
function initroughist(cigarettecode){
	$('#troughlist').datagrid({
		title:'分拣通道明细', //标题
		method:'post',
		iconCls:'icon-view', //图标
		singleSelect:false, //多选
		height:140, //高度
		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
		striped: true, //奇偶行颜色不同
		collapsible:false,//可折叠
		url:baseURL+"/produce/sorttrough/selectListByCond.json?cigarettecode="+cigarettecode+"&troughtype=10", //10表示分拣通道
		sortName: 'id', //排序的列
		sortOrder: 'desc', //倒序
		remoteSort: true, //服务器端排序
		idField:'id', //主键字段
		singleSelect: true,
		pagination:false, //显示分页
		//pageSize : 1,
		rownumbers:true, //显示行号
		columns:[[
			{field:'troughnum',title:'通道编号',width:$(this).width()*0.05,sortable:true,
				formatter:function(value,row,index){return row.troughnum;} //需要formatter一下才能显示正确的数据
			},
			{field:'machineseq',title:'物理编号',width:$(this).width()*0.1,
				formatter:function(value,row,index){return row.machineseq;}
			},
			{field:'cigarettecode',title:'卷烟编码',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.cigarettecode;} //需要formatter一下才能显示正确的数据
			},
			{field:'cigarettename',title:'卷烟名称',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.cigarettename;} //需要formatter一下才能显示正确的数据
			},
			{field:'cigarettetypename',title:'通道类型',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.cigarettetypename;}
			},
			{field:'groupno',title:'组次',width:$(this).width()*0.1,sortable:true,
				formatter:function(value,row,index){return row.groupno;}
			},
			{field:'_operate',title:'操作',width:$(this).width()*0.05,
				formatter:function(value,row,index){return '<input name="isShow" type="radio" onclick="radClick2('+index+')"/>';}
			}
		]],
		onLoadSuccess:function(){
			//$('#inboundlinediv .panel-header').css('display','none'); 
			
		}
	});
}

function radClick2(index){ 
	 $('#troughlist').datagrid('selectRow',index);// 关键在这里  
	    var row = $('#troughlist').datagrid('getSelected');  
	    if (row){  
	    	$('#cigarettecode_secondok').textbox("setValue",row.cigarettecode);
	    	$('#cigarettecode_secondok').textbox("setText",row.cigarettecode);
	    	$('#cigarettename_secondok').textbox("setValue",row.cigarettename);
	    	$('#cigarettename_secondok').textbox("setText",row.cigarettename);
	    	$('#cigarettetypename_secondok').textbox("setValue",row.cigarettetypename);
	    	$('#cigarettetypename_secondok').textbox("setText",row.cigarettetypename);
	    	$('#troughnum_secondok').textbox("setValue",row.troughnum);
	    	$('#troughnum_secondok').textbox("setText",row.troughnum);
	    	$('#machineseq_secondok').textbox("setValue",row.machineseq);
	    	$('#machineseq_secondok').textbox("setText",row.machineseq);
	    	$('#groupno_secondok').textbox("setValue",row.groupno);
	    	$('#groupno_secondok').textbox("setText",row.groupno);
	    }
}



/**
 * 查看窗口
 */
  function viewD(){
	  $('#view-fm').form('clear');
	  $("#audit-div" ).css("display", "none");
	  $('#btn-audit').linkbutton('disable');
	  $('#btn-receive').linkbutton('disable');
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
		var outboundid = row.id;
		//row.orderdate=formatDatebox(row.orderdate);
		//row.outtime=formatDatebox(row.outtime);
		if (row){
			$('#view-dlg').dialog('open').dialog('setTitle',"查看");
			$('#view-dlg').form('load',row);
		}
		var obj = $('#Moveareastocklinelist1');
		getmovearealinelist(obj,outboundid,240);
	}
  
	//移库明细列表
	  function getmovearealinelist(obj,outboundid,dgheight)
	  {
	  		//获取领料新增分配的物资明细列表
	  	obj.datagrid({
	  		title:'移库明细列表', //标题
	  		method:'post',
	  		iconCls:'icon-view', //图标
	  		singleSelect:false, //多选
	  		height:dgheight, //高度
	  		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
	  		striped: true, //奇偶行颜色不同
	  		collapsible:false,//可折叠
	  		url:baseURL+"/wms/moveareastock/getMoveareastockLineList.json?parentid="+outboundid, 
	  		sortName: 'id', //排序的列
	  		sortOrder: 'desc', //倒序
	  		remoteSort: true, //服务器端排序
	  		idField:'id', //主键字段
	  		singleSelect: true,
	  		pagination:false, //显示分页
	  		//pageSize : 1,
	  		rownumbers:true, //显示行号
	  		columns:[[
	  			{field:'troughnum',title:'通道编号',width:$(this).width()*0.1,
	  				formatter:function(value,row,index){return row.troughnum;}
	  			},
	  			{field:'cigarettecode',title:'卷烟编码',width:$(this).width()*0.1,sortable:true,
	  				formatter:function(value,row,index){return row.cigarettecode;} //需要formatter一下才能显示正确的数据
	  			},
	  			{field:'cigarettename',title:'卷烟名称',width:$(this).width()*0.1,sortable:true,
	  				formatter:function(value,row,index){return row.cigarettename;} //需要formatter一下才能显示正确的数据
	  			},
	  			{field:'barqty',title:'移库数量(条)',width:$(this).width()*0.1,sortable:true,
	  				formatter:function(value,row,index){return row.barqty;} //需要formatter一下才能显示正确的数据
	  			},
	  			{field:'boxqty',title:'移库数量(件)',width:$(this).width()*0.1,sortable:true,
	  				formatter:function(value,row,index){return row.boxqty;}
	  			}
	  		]],
	  		onLoadSuccess:function(){
	  			//$('#inboundlinediv .panel-header').css('display','none'); 
	  			
	  		}
	  	});
	  	}
	  
	  /**
	   * 审核窗口
	   */
	    function openAudit(checkflag){
	  	  $('#view-fm').form('clear');
	  	$("#audit-div" ).css("display", "block");
	  	if(checkflag=="20"){
	  		$('#btn-audit').linkbutton('enable');
	  		$('#btn-receive').linkbutton('disable');
	  	}else
	  		{
	  		$('#btn-audit').linkbutton('disable');
	  		$('#btn-receive').linkbutton('enable');
	  		}
	  	
	  	  var rows = $('#dataTabel').datagrid('getSelections');
	  		if(rows.length==0){
	  			$.messager.alert('提示',"请选择你要审核的信息",'info');
	  			return;
	  		}
	  		if(rows.length > 1){
	  			$.messager.alert('提示',"只能选择一条信息进行审核",'info');
	  			return;
	  		}
	  		$('#checkusername1').textbox("setValue",username);
	  		$('#checkusername1').textbox("setText",username);
	  		var nowTime = getDateYMD();
	  		$('#checktime1').textbox("setValue",nowTime);
	  		$('#checktime1').textbox("setText",nowTime);
	  		var row = $('#dataTabel').datagrid('getSelected');
	  		var id = row.id;
	  		obid = id;
	  		var checkflag1 = row.auditflag;
	  		if(checkflag1==checkflag || checkflag1=="30"){
	  			$.messager.alert('提示',"该信息已完成出库或收货，请选择其它信息进行操作！",'info');
	  			return;
	  		}

	  		if (row){
	  			$('#view-dlg').dialog('open').dialog('setTitle',"查看");
	  			$('#view-dlg').form('load',row);
	  		}
	  		var obj = $('#Moveareastocklinelist1');
			getmovearealinelist(obj,id,150);
	  		//getsplapplylistline(inboundid);
	  	}

	    function doAudit(checkflag){
	    	var checkdescribe = $("#checkdescribe1").val();
	    	var alertMsg = "确定出库吗？";
	    	if(checkflag=="30")alertMsg = "确定收货吗？";
	    	$.messager.confirm('提示',alertMsg,function(result){
	    		if(result){
	    			$('#add-fm').form('submit',{
	        			url:baseURL+"/wms/moveareastock/doAudit.json?id="+obid+"&checkdescribe="+checkdescribe+"&checkflag="+checkflag+"&movetype="+movetype+"&operationType=20", 
	            		data:$("#add-fm").serializeArray(),
	            		beforeSend : function () {
	            			$.messager.progress({
	            				text : '正在审核中...',
	            			});
	            		},
	            		success: function(data){
	            			data = eval('('+data+')');
	            			$('#view-dlg').dialog('close');
	            			$('#dataTabel').datagrid('reload'); 
	                		$.messager.show({
	            				title : '提示',
	            				msg :  data.msg,
	            			});
	                		
	                		//$("#listtabdiv" ).css("display", "block");
	                		//getsplapplylistline();
	            		}
	            	});
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
			if(rows.length > 1){
	  			$.messager.alert('提示',"只能选择一条信息进行删除",'info');
	  			return;
	  		}
			var row = $('#dataTabel').datagrid('getSelected');
			var checkflag = row.checkflag;
			var id = row.id;
			if(checkflag=="20"){
	  			$.messager.alert('提示',"对不起，您无权删除已经审核通过的信息！",'info');
	  			return;
	  		}
			
			$.messager.confirm('提示','确定要删除吗?',function(result){
	        if (result){
	        	
	        	$.post(baseURL+'/wms/moveareastock/doDel.json?id='+id,function(data){
		        	$('#dataTabel').datagrid('reload'); 
		        	//console.log("del--"+data);
	        		$.messager.show({
						title : '提示',
						msg :  data.total+'个信息被删除'+data.msg+'！',
					});
	        	});
	        }
	    });
		}

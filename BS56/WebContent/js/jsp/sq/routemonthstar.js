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
	$('#assesstime').datebox({    
        onShowPanel : function() {// 显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层    
            span.trigger('click'); // 触发click事件弹出月份层    
            if (!tds)    
                setTimeout(function() {// 延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔    
                    tds = p.find('div.calendar-menu-month-inner td');    
                    tds.click(function(e) {    
                        e.stopPropagation(); // 禁止冒泡执行easyui给月份绑定的事件    
                        var year = /\d{4}/.exec(span.html())[0]// 得到年份    
                        , month = parseInt($(this).attr('abbr'), 10) ; // 月份    
                        $('#assesstime').datebox('hidePanel')// 隐藏日期对象    
                        .datebox('setValue', year + month); // 设置日期的值    
                    });    
                }, 0);    
        },    
        parser : function(s) {// 配置parser，返回选择的日期    
            if (!s)    
                return new Date();    
           /* var arr = s.split('-');    
            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1); */
            
            var yearValue = s.substr(0,4); 
            var monthValue = s.substr(4,2);
            return new Date(parseInt(yearValue, 10), parseInt(monthValue, 10) , 1);    
        },    
        formatter : function(d) {    
            var month = d.getMonth();  
            if(month<=10){  
                month = "0"+month;  
            }  
            if (d.getMonth() == 0) {    
                return d.getFullYear()-1 +  "12";    
            } else {    
                return d.getFullYear() + month+"";    
            }    
        }// 配置formatter，只返回年月    
    }); 
	
    var p = $('#assesstime').datebox('panel'), // 日期选择对象    
    tds = false, // 日期选择对象中月份    
    span = p.find('span.calendar-text'); // 显示月份层的触发控件    
    
    
    //显示当前日期前一月
 formatterDate = function (date) {
       if (date.getMonth() == 0) {    
                 return date.getFullYear()-1 +  "12";    
             } else if(date.getMonth()<10) {    
                 return date.getFullYear() +"0"+date.getMonth();    
             }else{
 			    return date.getFullYear() +date.getMonth(); 
 			} 
 };

 $('#assesstime').datebox('setValue', formatterDate(new Date()));
 
 var time=$('#assesstime').datebox('getValue');
 //var time='201702';
 
 $('#drivername').textbox('textbox').keydown(function(e){
		if(e.keyCode==13){
			searchRouteMonthstar();
		}
	})
	
 $('#dataTable').datagrid({
		title:'车组星级统计', //标题
		method:'post',
		iconCls:'icon-edit', //图标
		singleSelect:true, //多选
		height:420, //高度
		fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
		striped: true, //奇偶行颜色不同
		collapsible:true,//可折叠
		url:baseURL+"/sq/routemonthstar/getRoutestarbymonth.json", //数据来源
		//sortName: 'id', //排序的列
		sortOrder: 'asc', //升序
		remoteSort: false, //服务器端排序
		//idField:'id', //主键字段
		pageNumber: 1, 
		pageSize : 55,
		pageList: [10,30,55],
		queryParams:{
			ctypeval:"30"
			}, //查询条件
		pagination:true, //显示分页
		//pageSize : 1,
		rownumbers:true, //显示行号
		//showFooter:true,//进行数据统计
		columns:[[
			//{field:'id',checkbox:true,width:2}, //显示复选框
			{field:'routecode',title:'所属车组',width:10,sortable:true,
				formatter:function(value,row,index){return row.routecode;
				 }
			},
			{field:'drivername',title:'驾驶员姓名',width:10,
				formatter:function(value,row,index){
					var dname=row.drivername;
					if(dname==null||dname=="")dname=0;
					return dname;}
			},
			{field:'dstarid',title:'驾驶员星级',width:10,sortable:true,
				formatter:function(value,row,index){
					var dstar="<font style='display:none;'>"+row.dstarid+"</font>"+row.dstarname;
					//var dstar=row.dstarid+row.dstarname;
					if(dstar==null||dstar=="")dstar=0;
					return dstar;
			       }
			},
			{field:'dtotalscore',title:'驾驶员实际得分',width:10,sortable:true,
				formatter:function(value,row,index){
					var dtotal=row.dtotalscore;
					if(dtotal==null||dtotal=="")dtotal=0;
					return dtotal;
			       }
			},
			{field:'cashiername',title:'收款员姓名',width:10,
				formatter:function(value,row,index){
					var cname=row.cashiername;
					if(cname==null||cname=="")cname=0;
					return cname;}
			},
			{field:'cstarid',title:'收款员星级',width:10,sortable:true,
				formatter:function(value,row,index){
					var cstar="<font style='display:none;'>"+row.cstarid+"</font>"+row.cstarname;
					if(cstar==null||cstar=="")cstar=0;
					return cstar;
			       }
			},
			{field:'ctotalscore',title:'收款员实际得分',width:10,sortable:true,
				formatter:function(value,row,index){
					var ctotal=row.ctotalscore;
					if(ctotal==null||ctotal=="")ctotal=0;
					return ctotal;
			       }
			},
			{field:'assesstime',title:'评定时间',width:10,
				formatter:function(value,row,index){
					var assess=row.assesstime;
					if(assess==null||assess=="")assess=0;
					return assess;}
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
 * 注册onchange方法
 */
$(document).ready(function () {

	$("#ctype").combobox({

	onChange: function (n,o) {
		var colsval="dstarname";
		var colname='驾驶员星级';
		var colval="drivername";
		var ctypeval=$("#ctype").val();
		if(ctypeval==null)ctypeval="30";
		if(ctypeval=='20'){
			colname='收款员姓名';
			colval="cashiername";
			
		}
		var assesstimeval=$("#assesstime").val();
		var drivernameval=$("#drivername").val();
		if(ctypeval=='30')
			{
			$('#dataTable').datagrid({
				title:'车组星级统计', //标题
				method:'post',
				iconCls:'icon-edit', //图标
				singleSelect:true, //多选
				height:420, //高度
				fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
				striped: true, //奇偶行颜色不同
				collapsible:true,//可折叠
				url:baseURL+"/sq/routemonthstar/getRoutestarbymonth.json", //数据来源
				//sortName: 'id', //排序的列
				sortOrder: 'asc', //升序
				remoteSort: false, //服务器端排序
				//idField:'id', //主键字段
				pageNumber: 1, 
				pageSize : 55,
				pageList: [10,30,55],
				queryParams:{
					assesstime:assesstimeval,
					ctype:ctypeval,
					drivername:drivernameval
					}, //查询条件
				pagination:true, //显示分页
				//pageSize : 1,
				rownumbers:true, //显示行号
				//showFooter:true,//进行数据统计
				columns:[[
					//{field:'id',checkbox:true,width:2}, //显示复选框
					{field:'routecode',title:'所属车组',width:10,sortable:true,
						formatter:function(value,row,index){return row.routecode;
						 }
					},
					{field:'drivername',title:'驾驶员姓名',width:10,
						formatter:function(value,row,index){
							var dname=row.drivername;
							if(dname==null||dname=="")dname=0;
							return dname;}
					},
					{field:'dstarid',title:'驾驶员星级',width:10,sortable:true,
						formatter:function(value,row,index){
							var dstar="<font style='display:none;'>"+row.dstarid+"</font>"+row.dstarname;
							//var dstar=row.dstarid+row.dstarname;
							if(dstar==null||dstar=="")dstar=0;
							return dstar;
					       }
					},
					{field:'dtotalscore',title:'驾驶员实际得分',width:10,sortable:true,
						formatter:function(value,row,index){
							var dtotal=row.dtotalscore;
							if(dtotal==null||dtotal=="")dtotal=0;
							return dtotal;
					       }
					},
					{field:'cashiername',title:'收款员姓名',width:10,
						formatter:function(value,row,index){
							var cname=row.cashiername;
							if(cname==null||cname=="")cname=0;
							return cname;}
					},
					{field:'cstarid',title:'收款员星级',width:10,sortable:true,
						formatter:function(value,row,index){
							var cstar="<font style='display:none;'>"+row.cstarid+"</font>"+row.cstarname;
							if(cstar==null||cstar=="")cstar=0;
							return cstar;
					       }
					},
					{field:'ctotalscore',title:'收款员实际得分',width:10,sortable:true,
						formatter:function(value,row,index){
							var ctotal=row.ctotalscore;
							if(ctotal==null||ctotal=="")ctotal=0;
							return ctotal;
					       }
					},
					{field:'assesstime',title:'评定时间',width:10,
						formatter:function(value,row,index){
							var assess=row.assesstime;
							if(assess==null||assess=="")assess=0;
							return assess;}
					},
					
				]],
				
				toolbar:'#toolbar',
				onLoadSuccess:function(){
					$('#dataTable').datagrid('clearSelections'); //一定要加上这一句，要不然datagrid会记住之前的选择状态，删除时会出问题
					$('#tabdiv .panel-header').css('display','none');
					//导出按钮取消置灰
					$("#toexcelBtn").linkbutton("enable");
				}
				
			});
			}
		else
		{
		$('#dataTable').datagrid({
			title:'车组每月星级表', //标题
			method:'post',
			iconCls:'icon-edit', //图标
			singleSelect:false, //多选
			height:420, //高度
			fitColumns: true, //自动调整各列，用了这个属性，下面各列的宽度值就只是一个比例。
			striped: true, //奇偶行颜色不同
			collapsible:true,//可折叠
			url:baseURL+"/sq/routemonthstar/getRouteMonthstars.json", //数据来源
			//sortName: 'id', //排序的列
			sortOrder: 'asc', //升序
			remoteSort: false, //服务器端排序
			//idField:'id', //主键字段
			pageNumber: 1, 
			pageSize : 55,
			pageList: [10,30,55],
			queryParams:{
				assesstime:assesstimeval,
				ctype:ctypeval,
				drivername:drivernameval
			
			}, //查询条件
			pagination:true, //显示分页
			//pageSize : 1,
			rownumbers:true, //显示行号
			columns:[[
				//{field:'id',checkbox:true,width:2}, //显示复选框
				{field:colval,title:colname,width:20,
					formatter:function(value,row,index){
						var val=row.drivername;
						if(ctypeval=='20')val=row.cashiername;
						return val;
					}
				},
				{field:'colsval',title:'星级',width:20,
					formatter:function(value,row,index){
						var val=row.dstarname;
						if(ctypeval=='20')val=row.cstarname;
						return val;}
					
				},
				{field:'routecode',title:'所属车组',width:20,sortable:true,
					formatter:function(value,row,index){return row.routecode;} //需要formatter一下才能显示正确的数据
			
				},
				{field:'assesstime',title:'评定时间',width:20,
					formatter:function(value,row,index){return row.assesstime;}
				},
			
			]],
			toolbar:'#toolbar',
			onLoadSuccess:function(){
				$('#dataTable').datagrid('clearSelections'); //一定要加上这一句，要不然datagrid会记住之前的选择状态，删除时会出问题
				$('#tabdiv .panel-header').css('display','none');
				//将导出按钮置灰
				$("#toexcelBtn").linkbutton("disable");
			}
		});
		}
	}

	});

	});

function exportxls()
{
	
	 $.messager.confirm('提示','确定要导出到excel吗?',function(result){
	        if (result){
	        	 $('#queryForm').form('submit', {
	        		 url: baseURL+'/sq/routemonthstar/getRoutestarbymonthExcel.json' 
	        	 });
	        }
	 });
	
}


	  //查询
	function searchRouteMonthstar(){
		var params = $('#dataTable').datagrid('options').queryParams; //先取得 datagrid 的查询参数
		var fields =$('#queryForm').serializeArray(); //自动序列化表单元素为JSON对象
		$.each( fields, function(i, field){
			params[field.name] = field.value; //设置查询参数
		}); 
		$('#dataTable').datagrid('reload'); 
	}
	//清空查询条件
	function clearForm(){
		$('#queryForm').form('clear');
		$('#queryForm').form('reset');
		$('#assesstime').datebox('setValue', formatterDate(new Date()));		
		searchRouteMonthstar();
		//导出按钮取消置灰
		$("#toexcelBtn").linkbutton("enable");
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
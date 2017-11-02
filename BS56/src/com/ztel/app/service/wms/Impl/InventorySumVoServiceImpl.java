package com.ztel.app.service.wms.Impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ztel.app.service.produce.SortTroughService;
import com.ztel.app.service.wms.ATSCellInfoDetailVoService;
import com.ztel.app.service.wms.InventoryLineVoService;
import com.ztel.app.service.wms.InventorySumVoService;
import com.ztel.app.service.wms.ItemVoService;
import com.ztel.app.service.wms.ItemstockService;
import com.ztel.app.service.wms.StorageAreaInOutService;
import com.ztel.app.util.Constant;
import com.ztel.app.vo.produce.SortTroughVo;
import com.ztel.app.vo.wms.ATSCellInfoDetailVo;
import com.ztel.app.vo.wms.InventoryLineVo;
import com.ztel.app.vo.wms.InventorySumVo;
import com.ztel.app.vo.wms.ItemVo;
import com.ztel.app.vo.wms.ItemstockLineVo;
import com.ztel.app.vo.wms.StorageAreaInOutVo;
@Service
public class InventorySumVoServiceImpl implements InventorySumVoService {
    
    @Autowired
    private ATSCellInfoDetailVoService atsCellInfoDetailVoService = null;
    
    @Autowired
    private StorageAreaInOutService storageAreaInOutService = null;
    
    @Autowired
    private SortTroughService sortTroughService = null;
    
    @Autowired
    private InventoryLineVoService inventoryLineVoService = null;
    
    @Autowired
    private ItemstockService itemstockService = null;
    
    @Autowired
    private ItemVoService itemVoService = null;
    
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public List<InventorySumVo> selectInventoryList(String searchDate) {
		// TODO Auto-generated method stub
		//取品牌信息
		ItemVo itemVo=new ItemVo();
		List<ItemVo>itemList=itemVoService.selectItemListByCond(itemVo);
		//取账面量信息
		//inventortime:本次日清日结时间  consignsor：传“”
		List<ItemstockLineVo> itemStockList=new ArrayList<>();
		try {
			itemStockList = itemstockService.getStock(sdf.parse(searchDate),"");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//取立库信息
		ATSCellInfoDetailVo atsCellInfoDetailVo=new ATSCellInfoDetailVo();
		atsCellInfoDetailVo.setSearchdate(searchDate);
		List<ATSCellInfoDetailVo> ATSCellInfoDetailVoVoList = new ArrayList<ATSCellInfoDetailVo>();
		ATSCellInfoDetailVoVoList = atsCellInfoDetailVoService.getATSCellInfoSummary(atsCellInfoDetailVo);
		ATSCellInfoDetailVoVoList.remove(ATSCellInfoDetailVoVoList.size()-1);
		
		//散烟区信息
		//最近一次盘点信息查询条件
		 InventoryLineVo inventoryLineVo=new InventoryLineVo();
		 inventoryLineVo.setInventorytype(new BigDecimal(10));
		 inventoryLineVo.setAreaid(new BigDecimal(Constant.storagearea_sy));
		 //散烟汇总查询条件
		 StorageAreaInOutVo storageAreaInOutVo=new StorageAreaInOutVo();
		 storageAreaInOutVo.setSearchdate(searchDate);
		 storageAreaInOutVo.setAreaid(new BigDecimal(Constant.storagearea_sy));
		 List<InventoryLineVo>  inOutList=new ArrayList<InventoryLineVo>();
		 inOutList=storageAreaInOutService.getInOutInfoSummaryByCond(storageAreaInOutVo, inventoryLineVo);
		 
		 //分拣区信息
		//通道尾数查询条件
		 SortTroughVo sortTroughVo=new SortTroughVo();
		 sortTroughVo.setCigarettetype(new BigDecimal(20));
		 sortTroughVo.setTroughtype(new BigDecimal(10));
		 //调拨移库信息
		 StorageAreaInOutVo storageAreaInOutVo_fj=new StorageAreaInOutVo();
		 storageAreaInOutVo_fj.setSearchdate(searchDate);
		 storageAreaInOutVo_fj.setAreaid(new BigDecimal(Constant.storagearea_fj));
		 List<SortTroughVo>  troughList=new ArrayList<SortTroughVo>();
		 troughList=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_fj, sortTroughVo);
		 //由于分拣区卷烟品牌会有重复,将重复品牌组成List放入Map
		 Map<String, List<SortTroughVo>>sortMap=new TreeMap<>();
		 int len=troughList.size();String cigarettecode="";
		 SortTroughVo vo=new SortTroughVo();
		 for(int i=0;i<len;i++){
			 vo=troughList.get(i);
			 cigarettecode=vo.getCigarettecode();
			 List<SortTroughVo>list=null;
			 if(sortMap.containsKey(cigarettecode)){
				 list=sortMap.get(cigarettecode);
			 }else{
				 list=new ArrayList<SortTroughVo>();
			 }
			 list.add(vo);
			 sortMap.put(cigarettecode, list);
		 }
		 
		 //重力式货架信息
		//通道尾数查询条件
		 SortTroughVo sortTroughVo1=new SortTroughVo();
		 sortTroughVo1.setCigarettetype(new BigDecimal(20));
		 sortTroughVo1.setTroughtype(new BigDecimal(20));
		 //调拨移库信息
		 StorageAreaInOutVo storageAreaInOutVo_zlshj=new StorageAreaInOutVo();
		 storageAreaInOutVo_zlshj.setSearchdate(searchDate);
		 storageAreaInOutVo_zlshj.setAreaid(new BigDecimal(Constant.storagearea_zlshj));
		 List<SortTroughVo>  zlshjList=new ArrayList<SortTroughVo>();
		 zlshjList=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_zlshj, sortTroughVo1);
		//由于重力式货架区卷烟品牌会有重复,将重复品牌组成List放入Map
		 Map<String, List<SortTroughVo>>shelfMap=new TreeMap<>();
		 len=zlshjList.size();
		 //SortTroughVo vo=new SortTroughVo();
		 for(int i=0;i<len;i++){
			 vo=zlshjList.get(i);
			 cigarettecode=vo.getCigarettecode();
			 List<SortTroughVo>list=null;
			 if(shelfMap.containsKey(cigarettecode)){
				 list=shelfMap.get(cigarettecode);
			 }else{
				 list=new ArrayList<SortTroughVo>();
			 }
			 list.add(vo);
			 shelfMap.put(cigarettecode, list);
		 }
		 
		 //异型烟一区
		//通道尾数查询条件
		 SortTroughVo sortTroughVo_yx1=new SortTroughVo();
		 sortTroughVo_yx1.setUncigarettetype(new BigDecimal(20));
		 sortTroughVo_yx1.setTroughtype(new BigDecimal(10));
		 sortTroughVo_yx1.setGroupno(new BigDecimal(1));
		//调拨移库信息
		 //StorageAreaInOutVo storageAreaInOutVo_fj=new StorageAreaInOutVo();
		 //storageAreaInOutVo_fj.setSearchdate(searchDate);
		 //storageAreaInOutVo_fj.setAreaid(new BigDecimal(Constant.storagearea_fj));
		 List<SortTroughVo>  untroughList1=new ArrayList<SortTroughVo>();
		 untroughList1=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_fj, sortTroughVo_yx1);
			 
		 //异型烟二区
		//通道尾数查询条件
		 SortTroughVo sortTroughVo_yx2=new SortTroughVo();
		 sortTroughVo_yx2.setUncigarettetype(new BigDecimal(20));
		 sortTroughVo_yx2.setTroughtype(new BigDecimal(10));
		 sortTroughVo_yx2.setGroupno(new BigDecimal(2));
		//调拨移库信息
		 //StorageAreaInOutVo storageAreaInOutVo_fj=new StorageAreaInOutVo();
		 //storageAreaInOutVo_fj.setSearchdate(searchDate);
		 //storageAreaInOutVo_fj.setAreaid(new BigDecimal(Constant.storagearea_fj));
		 List<SortTroughVo>  untroughList2=new ArrayList<SortTroughVo>();
		 untroughList2=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_fj, sortTroughVo_yx2);
		 
		 //公共区
		//通道尾数查询条件
		 SortTroughVo sortTroughVo_yx3=new SortTroughVo();
		 sortTroughVo_yx3.setUncigarettetype(new BigDecimal(20));
		 sortTroughVo_yx3.setTroughtype(new BigDecimal(10));
		 sortTroughVo_yx3.setGroupno(new BigDecimal(3));
		//调拨移库信息
		 //StorageAreaInOutVo storageAreaInOutVo_fj=new StorageAreaInOutVo();
		 //storageAreaInOutVo_fj.setSearchdate(searchDate);
		 //storageAreaInOutVo_fj.setAreaid(new BigDecimal(Constant.storagearea_fj));
		 List<SortTroughVo>  untroughList3=new ArrayList<SortTroughVo>();
		 untroughList3=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_fj, sortTroughVo_yx3);
		 
		 //组装数据
		 List<InventorySumVo>  resultList=new ArrayList<InventorySumVo>();
		 int itemLen=itemList.size();
		 int stockLen=itemStockList.size();
		 int lklen=ATSCellInfoDetailVoVoList.size();
		 int sylen=inOutList.size();
		 int un1len=untroughList1.size();
		 int un2len=untroughList2.size();
		 int un3len=untroughList3.size();
		 //String cigarettecode="";
		 BigDecimal diffqty=new BigDecimal(0);
		 BigDecimal paperqty=null,ATSCellqty=null,scatteredqty=null,shelfqty=null,sortingqty=null,
				 			 unnormalqty1=null,unnormalqty2=null,commonqty=null;
		 String cigarettename="",shelfno="",sortingno="",unnormalno1="",unnormalno2="",commonno="",flag="";
				 
		 InventorySumVo sumVo=null;
		 //品牌为基数
		 for(int z=0;z<itemLen;z++){
			 itemVo=itemList.get(z);
			 cigarettecode=itemVo.getItemno();
			 cigarettename=itemVo.getItemname();
			 diffqty=new BigDecimal(0);
			 flag="";//判断该品牌是否有数据
			 paperqty=null;
			 //账面量信息
			 for(int a=0;a<stockLen;a++){
				 ItemstockLineVo v=itemStockList.get(a);
				 if(cigarettecode.equals(v.getCigarettecode())){
					 paperqty=v.getItemqty();//立库数量件转条
					 //sumVo.setATSCellqty(ATSCellqty);
					 diffqty=diffqty.add(paperqty);
					 flag="1";
					 break;
				 }
			 }
			 //立库数量
			 ATSCellqty=null;
			 for(int b=0;b<lklen;b++){
				 ATSCellInfoDetailVo cellInfoDetailVo=ATSCellInfoDetailVoVoList.get(b);
				 if(cigarettecode.equals(cellInfoDetailVo.getCigarettecode())){
					 ATSCellqty=cellInfoDetailVo.getQty().multiply(cellInfoDetailVo.getJt_size());//立库数量件转条
					 //sumVo.setATSCellqty(ATSCellqty);
					 diffqty=diffqty.subtract(ATSCellqty);
					 flag="1";
					 break;
				 }
			 }
			 //散烟区数量
			 scatteredqty=null;
			 for(int b=0;b<sylen;b++){
				 InventoryLineVo InventoryLineVo=inOutList.get(b);
				 if(cigarettecode.equals(InventoryLineVo.getCigarettecode())){
					 scatteredqty=InventoryLineVo.getTotalqty();
					 //sumVo.setScatteredqty(scatteredqty);
					 diffqty=diffqty.subtract(scatteredqty);
					 flag="1";
					 break;
				 }
			 }
			 //异型烟一区数量
			 unnormalqty1=null;
			 unnormalno1="";
			 for(int b=0;b<un1len;b++){
				 SortTroughVo troughVo1=untroughList1.get(b);
				 if(cigarettecode.equals(troughVo1.getCigarettecode())){
					 unnormalno1=troughVo1.getTroughnum();
					 unnormalqty1=troughVo1.getFillqty();
					 //sumVo.setUnnormalno1(unnormalno1);
					 //sumVo.setUnnormalqty1(unnormalqty1);
					 diffqty=diffqty.subtract(unnormalqty1);
					 flag="1";
					 break;
				 }
			 }
			 //异型烟二区数量
			 unnormalqty2=null;
			 unnormalno2="";
			 for(int b=0;b<un2len;b++){
				 SortTroughVo troughVo2=untroughList2.get(b);
				 if(cigarettecode.equals(troughVo2.getCigarettecode())){
					 unnormalno2=troughVo2.getTroughnum();
					 unnormalqty2=troughVo2.getFillqty();
					 if(unnormalqty2==null)unnormalqty2=new BigDecimal(0);
					 //sumVo.setUnnormalno2(unnormalno2);
					 //sumVo.setUnnormalqty2(unnormalqty2);
					 diffqty=diffqty.subtract(unnormalqty2);
					 flag="1";
					 break;
				 }
			 }
			 //异型烟三区(公共区)数量
			 commonqty=null;
			 commonno="";
			 for(int b=0;b<un3len;b++){
				 SortTroughVo troughVo3=untroughList3.get(b);
				 if(cigarettecode.equals(troughVo3.getCigarettecode())){
					 commonno=troughVo3.getTroughnum();
					 commonqty=troughVo3.getFillqty();
					 //sumVo.setCommonno(commonno);
					 //sumVo.setCommonqty(commonqty);
					 diffqty=diffqty.subtract(commonqty);
					 flag="1";
					 break;
				 }
			 }
			 
			 //分拣区和重力式货架   有重复的品牌
			 shelfqty=null;
			 shelfno="";
			 sortingqty=null;
			 sortingno="";
			 if(sortMap.containsKey(cigarettecode)||shelfMap.containsKey(cigarettecode)){
				 List<SortTroughVo>sList=sortMap.get(cigarettecode);
				 List<SortTroughVo>zList=shelfMap.get(cigarettecode);
				 int slen=0;if(sList!=null)slen=sList.size();
				 int zlen=0;if(zList!=null)zlen=zList.size();
				 SortTroughVo troughVo=new SortTroughVo();
				 flag="1";
				 //计算差额
				 //分拣区
				 for(int d=0;d<slen;d++){
					 troughVo=sList.get(d);
					 diffqty=diffqty.subtract(troughVo.getFillqty());
				 }
				 //重力式货架
				 for(int d=0;d<zlen;d++){
					 troughVo=zList.get(d);
					 //System.out.println("diffqty=="+diffqty+"--------------fillqty=="+troughVo.getFillqty()+"--------------Jt_size=="+troughVo.getJt_size());
					 diffqty=diffqty.subtract(troughVo.getFillqty().multiply(troughVo.getJt_size()));//重力式货架数量件转条
				 }
				 //重力式货架通道多于分拣区通道
				 if(zlen>slen){
					 for(int c=0;c<zlen;c++){
						 sumVo=new InventorySumVo();
						 sumVo.setCigarettecode(cigarettecode);
						 sumVo.setCigarettename(cigarettename);
						 sumVo.setPaperqty(paperqty);
						 sumVo.setAtscellqty(ATSCellqty);
						 sumVo.setScatteredqty(scatteredqty);
						 sumVo.setUnnormalno1(unnormalno1);
						 sumVo.setUnnormalqty1(unnormalqty1);
						 sumVo.setUnnormalno2(unnormalno2);
						 sumVo.setUnnormalqty2(unnormalqty2);
						 //diffqty=diffqty.subtract(unnormalqty2);
						 sumVo.setCommonno(commonno);
						 sumVo.setCommonqty(commonqty);
						 flag="1";
						 sumVo.setFlag(flag);
						 
						 troughVo=zList.get(c);
						 shelfno=troughVo.getTroughnum();
						 shelfqty=troughVo.getFillqty().multiply(troughVo.getJt_size());//重力式货架数量件转条
						 
						 sumVo.setShelfno(shelfno);
						 sumVo.setShelfqty(shelfqty);
						 
						 if(c<slen){
							 troughVo=sList.get(c);
							 sortingno=troughVo.getTroughnum();
							 sortingqty=troughVo.getFillqty();
							 
							 sumVo.setSortingno(sortingno);
							 sumVo.setSortingqty(sortingqty);
						 }
						 sumVo.setDiffqty(diffqty);
						 resultList.add(sumVo);
					 }
				 }else{
					 //分拣区通道多于重力式货架通道
					 for(int c=0;c<slen;c++){
						 sumVo=new InventorySumVo();
						 sumVo.setCigarettecode(cigarettecode);
						 sumVo.setCigarettename(cigarettename);
						 sumVo.setPaperqty(paperqty);
						 sumVo.setAtscellqty(ATSCellqty);
						 sumVo.setScatteredqty(scatteredqty);
						 sumVo.setUnnormalno1(unnormalno1);
						 sumVo.setUnnormalqty1(unnormalqty1);
						 sumVo.setUnnormalno2(unnormalno2);
						 sumVo.setUnnormalqty2(unnormalqty2);
						 //diffqty=diffqty.subtract(unnormalqty2);
						 sumVo.setCommonno(commonno);
						 sumVo.setCommonqty(commonqty);
						 flag="1";
						 sumVo.setFlag(flag);
						 
						 troughVo=sList.get(c);
						 sortingno=troughVo.getTroughnum();
						 sortingqty=troughVo.getFillqty();
						 
						 sumVo.setSortingno(sortingno);
						 sumVo.setSortingqty(sortingqty);
						 
						 if(c<zlen){
							 troughVo=zList.get(c);
							 shelfno=troughVo.getTroughnum();
							 shelfqty=troughVo.getFillqty().multiply(troughVo.getJt_size());//重力式货架数量件转条
							 
							 sumVo.setShelfno(shelfno);
							 sumVo.setShelfqty(shelfqty);
						 }
						 sumVo.setDiffqty(diffqty);
						 resultList.add(sumVo);
					 }
				 }
			 }else{
				 sumVo=new InventorySumVo();
				 sumVo.setCigarettecode(cigarettecode);
				 sumVo.setCigarettename(cigarettename);
				 sumVo.setPaperqty(paperqty);
				 //没有分拣以及重力式货架的品牌
				 sumVo.setAtscellqty(ATSCellqty);
				 sumVo.setScatteredqty(scatteredqty);
				 sumVo.setUnnormalno1(unnormalno1);
				 sumVo.setUnnormalqty1(unnormalqty1);
				 sumVo.setUnnormalno2(unnormalno2);
				 sumVo.setUnnormalqty2(unnormalqty2);
				 //System.out.println("diffqty=="+diffqty+"--------------unnormalqty2=="+unnormalqty2);
				 //diffqty=diffqty.subtract(unnormalqty2);
				 sumVo.setCommonno(commonno);
				 sumVo.setCommonqty(commonqty);
				 sumVo.setSortingno(sortingno);
				 sumVo.setSortingqty(sortingqty);
				 sumVo.setShelfno(shelfno);
				 sumVo.setShelfqty(shelfqty);
				 sumVo.setDiffqty(diffqty);
				 sumVo.setFlag(flag);
				 resultList.add(sumVo);
			 }
		 }
		 Iterator<InventorySumVo> it = resultList.iterator();
		 while(it.hasNext()){
			 InventorySumVo x = it.next();
		     if("".equals(x.getFlag())){
		         it.remove();
		     }
		 }
		return resultList;
	}

	/**
	 * 盘点信息查看
	 */
	@Override
	public List<InventorySumVo> selectInventoryListView(String inventoryid, String searchDate) {
				// TODO Auto-generated method stub
			   //取品牌信息
				ItemVo itemVo=new ItemVo();
				List<ItemVo>itemList=itemVoService.selectItemListByCond(itemVo);
				//取账面量信息
				//inventortime:本次日清日结时间  consignsor：传“”
				List<ItemstockLineVo> itemStockList=new ArrayList<>();
				try {
					itemStockList = itemstockService.getStock(sdf.parse(searchDate),"");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				InventoryLineVo inventoryLineVo=new InventoryLineVo();
				inventoryLineVo.setInventoryid(new BigDecimal(inventoryid));
				
				//取立库信息
				inventoryLineVo.setAreaid(new BigDecimal(Constant.storagearea_lk));
				List<InventoryLineVo>ATSCellInfoDetailVoVoList=inventoryLineVoService.getInventoryInfoByCond(inventoryLineVo);
				//取立库信息
//				ATSCellInfoDetailVo atsCellInfoDetailVo=new ATSCellInfoDetailVo();
//				atsCellInfoDetailVo.setSearchdate(searchDate);
//				List<ATSCellInfoDetailVo> ATSCellInfoDetailVoVoList = new ArrayList<ATSCellInfoDetailVo>();
//				ATSCellInfoDetailVoVoList = atsCellInfoDetailVoService.getATSCellInfoSummary(atsCellInfoDetailVo);
//				ATSCellInfoDetailVoVoList.remove(ATSCellInfoDetailVoVoList.size()-1);
				
				//散烟区信息
				inventoryLineVo.setAreaid(new BigDecimal(Constant.storagearea_sy));
				List<InventoryLineVo>inOutList=inventoryLineVoService.getInventoryInfoByCond(inventoryLineVo);
				//最近一次盘点信息查询条件
//				 InventoryLineVo inventoryLineVo=new InventoryLineVo();
//				 inventoryLineVo.setInventorytype(new BigDecimal(10));
//				 inventoryLineVo.setAreaid(new BigDecimal(Constant.storagearea_sy));
//				 //散烟汇总查询条件
//				 StorageAreaInOutVo storageAreaInOutVo=new StorageAreaInOutVo();
//				 storageAreaInOutVo.setSearchdate(searchDate);
//				 storageAreaInOutVo.setAreaid(new BigDecimal(Constant.storagearea_sy));
//				 List<InventoryLineVo>  inOutList=new ArrayList<InventoryLineVo>();
//				 inOutList=storageAreaInOutService.getInOutInfoSummaryByCond(storageAreaInOutVo, inventoryLineVo);
				 
				 //分拣区信息
				inventoryLineVo.setAreaid(new BigDecimal(Constant.storagearea_fj));
				inventoryLineVo.setCigarettetype(new BigDecimal(20));
				inventoryLineVo.setTroughtype(new BigDecimal(10));
				List<InventoryLineVo>troughList=inventoryLineVoService.getInventoryTroughInfoByCond(inventoryLineVo);
				//通道尾数查询条件
//				 SortTroughVo sortTroughVo=new SortTroughVo();
//				 sortTroughVo.setCigarettetype(new BigDecimal(20));
//				 sortTroughVo.setTroughtype(new BigDecimal(10));
//				 //调拨移库信息
//				 StorageAreaInOutVo storageAreaInOutVo_fj=new StorageAreaInOutVo();
//				 storageAreaInOutVo_fj.setSearchdate(searchDate);
//				 storageAreaInOutVo_fj.setAreaid(new BigDecimal(Constant.storagearea_fj));
//				 List<SortTroughVo>  troughList=new ArrayList<SortTroughVo>();
//				 troughList=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_fj, sortTroughVo);
				 //由于分拣区卷烟品牌会有重复,将重复品牌组成List放入Map
				 Map<String, List<InventoryLineVo>>sortMap=new TreeMap<>();
				 int len=troughList.size();String cigarettecode="";
				 InventoryLineVo vo=new InventoryLineVo();
				 for(int i=0;i<len;i++){
					 vo=troughList.get(i);
					 cigarettecode=vo.getCigarettecode();
					 List<InventoryLineVo>list=null;
					 if(sortMap.containsKey(cigarettecode)){
						 list=sortMap.get(cigarettecode);
					 }else{
						 list=new ArrayList<InventoryLineVo>();
					 }
					 list.add(vo);
					 sortMap.put(cigarettecode, list);
				 }
				 
				 //重力式货架信息
				 InventoryLineVo shelfvo=new InventoryLineVo();
				 shelfvo.setInventoryid(new BigDecimal(inventoryid));
				 shelfvo.setAreaid(new BigDecimal(Constant.storagearea_zlshj));
				 shelfvo.setCigarettetype(new BigDecimal(20));
				 shelfvo.setTroughtype(new BigDecimal(20));
				 List<InventoryLineVo>zlshjList=inventoryLineVoService.getInventoryTroughInfoByCond(shelfvo);
				//通道尾数查询条件
//				 SortTroughVo sortTroughVo1=new SortTroughVo();
//				 sortTroughVo1.setCigarettetype(new BigDecimal(20));
//				 sortTroughVo1.setTroughtype(new BigDecimal(20));
//				 //调拨移库信息
//				 StorageAreaInOutVo storageAreaInOutVo_zlshj=new StorageAreaInOutVo();
//				 storageAreaInOutVo_zlshj.setSearchdate(searchDate);
//				 storageAreaInOutVo_zlshj.setAreaid(new BigDecimal(Constant.storagearea_zlshj));
//				 List<SortTroughVo>  zlshjList=new ArrayList<SortTroughVo>();
//				 zlshjList=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_zlshj, sortTroughVo1);
				//由于重力式货架区卷烟品牌会有重复,将重复品牌组成List放入Map
				 Map<String, List<InventoryLineVo>>shelfMap=new TreeMap<>();
				 len=zlshjList.size();
				 //SortTroughVo vo=new SortTroughVo();
				 for(int i=0;i<len;i++){
					 vo=zlshjList.get(i);
					 cigarettecode=vo.getCigarettecode();
					 List<InventoryLineVo>list=null;
					 if(shelfMap.containsKey(cigarettecode)){
						 list=shelfMap.get(cigarettecode);
					 }else{
						 list=new ArrayList<InventoryLineVo>();
					 }
					 list.add(vo);
					 shelfMap.put(cigarettecode, list);
				 }
				 
				 //异型烟一区
				 InventoryLineVo un1vo=new InventoryLineVo();
				 un1vo.setInventoryid(new BigDecimal(inventoryid));
				 un1vo.setAreaid(new BigDecimal(Constant.storagearea_fj));
				 un1vo.setUncigarettetype(new BigDecimal(20));
				 un1vo.setTroughtype(new BigDecimal(10));
				 un1vo.setGroupno(new BigDecimal(1));
				 List<InventoryLineVo>untroughList1=inventoryLineVoService.getInventoryTroughInfoByCond(un1vo);
				//通道尾数查询条件
//				 SortTroughVo sortTroughVo_yx1=new SortTroughVo();
//				 sortTroughVo_yx1.setUncigarettetype(new BigDecimal(20));
//				 sortTroughVo_yx1.setTroughtype(new BigDecimal(10));
//				 sortTroughVo_yx1.setGroupno(new BigDecimal(1));
//				//调拨移库信息
//				 //StorageAreaInOutVo storageAreaInOutVo_fj=new StorageAreaInOutVo();
//				 //storageAreaInOutVo_fj.setSearchdate(searchDate);
//				 //storageAreaInOutVo_fj.setAreaid(new BigDecimal(Constant.storagearea_fj));
//				 List<SortTroughVo>  untroughList1=new ArrayList<SortTroughVo>();
//				 untroughList1=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_fj, sortTroughVo_yx1);
					 
				 //异型烟二区
				 InventoryLineVo un2vo=new InventoryLineVo();
				 un2vo.setInventoryid(new BigDecimal(inventoryid));
				 un2vo.setAreaid(new BigDecimal(Constant.storagearea_fj));
				 un2vo.setUncigarettetype(new BigDecimal(20));
				 un2vo.setTroughtype(new BigDecimal(10));
				 un2vo.setGroupno(new BigDecimal(2));
				 List<InventoryLineVo>untroughList2=inventoryLineVoService.getInventoryTroughInfoByCond(un2vo);
				//通道尾数查询条件
//				 SortTroughVo sortTroughVo_yx2=new SortTroughVo();
//				 sortTroughVo_yx2.setUncigarettetype(new BigDecimal(20));
//				 sortTroughVo_yx2.setTroughtype(new BigDecimal(10));
//				 sortTroughVo_yx2.setGroupno(new BigDecimal(2));
//				//调拨移库信息
//				 //StorageAreaInOutVo storageAreaInOutVo_fj=new StorageAreaInOutVo();
//				 //storageAreaInOutVo_fj.setSearchdate(searchDate);
//				 //storageAreaInOutVo_fj.setAreaid(new BigDecimal(Constant.storagearea_fj));
//				 List<SortTroughVo>  untroughList2=new ArrayList<SortTroughVo>();
//				 untroughList2=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_fj, sortTroughVo_yx2);
				 
				 //公共区
				 InventoryLineVo un3vo=new InventoryLineVo();
				 un3vo.setInventoryid(new BigDecimal(inventoryid));
				 un3vo.setAreaid(new BigDecimal(Constant.storagearea_fj));
				 un3vo.setUncigarettetype(new BigDecimal(20));
				 un3vo.setTroughtype(new BigDecimal(10));
				 un3vo.setGroupno(new BigDecimal(3));
				 List<InventoryLineVo>untroughList3=inventoryLineVoService.getInventoryTroughInfoByCond(un3vo);
				//通道尾数查询条件
//				 SortTroughVo sortTroughVo_yx3=new SortTroughVo();
//				 sortTroughVo_yx3.setUncigarettetype(new BigDecimal(20));
//				 sortTroughVo_yx3.setTroughtype(new BigDecimal(10));
//				 sortTroughVo_yx3.setGroupno(new BigDecimal(3));
//				//调拨移库信息
//				 //StorageAreaInOutVo storageAreaInOutVo_fj=new StorageAreaInOutVo();
//				 //storageAreaInOutVo_fj.setSearchdate(searchDate);
//				 //storageAreaInOutVo_fj.setAreaid(new BigDecimal(Constant.storagearea_fj));
//				 List<SortTroughVo>  untroughList3=new ArrayList<SortTroughVo>();
//				 untroughList3=sortTroughService.getSortTroughSummaryByCond(storageAreaInOutVo_fj, sortTroughVo_yx3);
				 
				 //组装数据
				 List<InventorySumVo>  resultList=new ArrayList<InventorySumVo>();
				 int itemLen=itemList.size();
				 int stockLen=itemStockList.size();
				 int lklen=ATSCellInfoDetailVoVoList.size();
				 int sylen=inOutList.size();
				 int un1len=untroughList1.size();
				 int un2len=untroughList2.size();
				 int un3len=untroughList3.size();
				 //String cigarettecode="";
				 BigDecimal diffqty=new BigDecimal(0);
				 BigDecimal paperqty=null,ATSCellqty=null,scatteredqty=null,shelfqty=null,sortingqty=null,
						 			 unnormalqty1=null,unnormalqty2=null,commonqty=null;
				 String cigarettename="",shelfno="",sortingno="",unnormalno1="",unnormalno2="",commonno="",flag="";
						 
				 InventorySumVo sumVo=null;
				 for(int z=0;z<itemLen;z++){
					 itemVo=itemList.get(z);
					 cigarettecode=itemVo.getItemno();
					 cigarettename=itemVo.getItemname();
					 diffqty=new BigDecimal(0);
					 flag="";//判断该品牌是否有数据
					 paperqty=null;
					 //账面量信息
					 for(int a=0;a<stockLen;a++){
						 ItemstockLineVo v=itemStockList.get(a);
						 if(cigarettecode.equals(v.getCigarettecode())){
							 paperqty=v.getItemqty();//立库数量件转条
							 //sumVo.setATSCellqty(ATSCellqty);
							 diffqty=diffqty.add(paperqty);
							 flag="1";
							 break;
						 }
					 }
					 //立库数量
					 ATSCellqty=null;
					 for(int b=0;b<lklen;b++){
						 InventoryLineVo cellInfoDetailVo=ATSCellInfoDetailVoVoList.get(b);
						 if(cigarettecode.equals(cellInfoDetailVo.getCigarettecode())){
							 ATSCellqty=cellInfoDetailVo.getItemqty();
							 //sumVo.setATSCellqty(ATSCellqty);
							 diffqty=diffqty.subtract(ATSCellqty);
							 flag="1";
							 break;
						 }
					 }
					 //散烟区数量
					 scatteredqty=null;
					 for(int b=0;b<sylen;b++){
						 InventoryLineVo InventoryLineVo=inOutList.get(b);
						 if(cigarettecode.equals(InventoryLineVo.getCigarettecode())){
							 scatteredqty=InventoryLineVo.getItemqty();
							 //sumVo.setScatteredqty(scatteredqty);
							 diffqty=diffqty.subtract(scatteredqty);
							 flag="1";
							 break;
						 }
					 }
					 //异型烟一区数量
					 unnormalqty1=null;
					 unnormalno1="";
					 for(int b=0;b<un1len;b++){
						 InventoryLineVo troughVo1=untroughList1.get(b);
						 if(cigarettecode.equals(troughVo1.getCigarettecode())){
							 unnormalno1=troughVo1.getTroughno()+"";
							 unnormalqty1=troughVo1.getItemqty();
							 //sumVo.setUnnormalno1(unnormalno1);
							 //sumVo.setUnnormalqty1(unnormalqty1);
							 diffqty=diffqty.subtract(unnormalqty1);
							 flag="1";
							 break;
						 }
					 }
					 //异型烟二区数量
					 unnormalqty2=null;
					 unnormalno2="";
					 for(int b=0;b<un2len;b++){
						 InventoryLineVo troughVo2=untroughList2.get(b);
						 if(cigarettecode.equals(troughVo2.getCigarettecode())){
							 unnormalno2=troughVo2.getTroughno()+"";
							 unnormalqty2=troughVo2.getItemqty();
							 if(unnormalqty2==null)unnormalqty2=new BigDecimal(0);
							 //sumVo.setUnnormalno2(unnormalno2);
							 //sumVo.setUnnormalqty2(unnormalqty2);
							 diffqty=diffqty.subtract(unnormalqty2);
							 flag="1";
							 break;
						 }
					 }
					 //异型烟三区(公共区)数量
					 commonqty=null;
					 commonno="";
					 for(int b=0;b<un3len;b++){
						 InventoryLineVo troughVo3=untroughList3.get(b);
						 if(cigarettecode.equals(troughVo3.getCigarettecode())){
							 commonno=troughVo3.getTroughno()+"";
							 commonqty=troughVo3.getItemqty();
							 //sumVo.setCommonno(commonno);
							 //sumVo.setCommonqty(commonqty);
							 diffqty=diffqty.subtract(commonqty);
							 flag="1";
							 break;
						 }
					 }
					 
					 //分拣区和重力式货架   有重复的品牌
					 shelfqty=null;
					 shelfno="";
					 sortingqty=null;
					 sortingno="";
					 if(sortMap.containsKey(cigarettecode)||shelfMap.containsKey(cigarettecode)){
						 List<InventoryLineVo>sList=sortMap.get(cigarettecode);
						 List<InventoryLineVo>zList=shelfMap.get(cigarettecode);
						 int slen=0;if(sList!=null)slen=sList.size();
						 int zlen=0;if(zList!=null)zlen=zList.size();
						 InventoryLineVo troughVo=new InventoryLineVo();
						 //计算差额
						 //分拣区
						 for(int d=0;d<slen;d++){
							 troughVo=sList.get(d);
							 diffqty=diffqty.subtract(troughVo.getItemqty());
						 }
						 //重力式货架
						 for(int d=0;d<zlen;d++){
							 troughVo=zList.get(d);
							 //System.out.println("diffqty=="+diffqty+"--------------fillqty=="+troughVo.getFillqty()+"--------------Jt_size=="+troughVo.getJt_size());
							 diffqty=diffqty.subtract(troughVo.getItemqty());//重力式货架数量件转条
						 }
						 //重力式货架通道多于分拣区通道
						 if(zlen>slen){
							 for(int c=0;c<zlen;c++){
								 sumVo=new InventorySumVo();
								 sumVo.setCigarettecode(cigarettecode);
								 sumVo.setCigarettename(cigarettename);
								 sumVo.setPaperqty(paperqty);
								 sumVo.setAtscellqty(ATSCellqty);
								 sumVo.setScatteredqty(scatteredqty);
								 sumVo.setUnnormalno1(unnormalno1);
								 sumVo.setUnnormalqty1(unnormalqty1);
								 sumVo.setUnnormalno2(unnormalno2);
								 sumVo.setUnnormalqty2(unnormalqty2);
								 //diffqty=diffqty.subtract(unnormalqty2);
								 sumVo.setCommonno(commonno);
								 sumVo.setCommonqty(commonqty);
								 flag="1";
								 sumVo.setFlag(flag);
								 
								 troughVo=zList.get(c);
								 shelfno=troughVo.getTroughno()+"";
								 shelfqty=troughVo.getItemqty();//重力式货架数量件转条
								 
								 sumVo.setShelfno(shelfno);
								 sumVo.setShelfqty(shelfqty);
								 
								 if(c<slen){
									 troughVo=sList.get(c);
									 sortingno=troughVo.getTroughno()+"";
									 sortingqty=troughVo.getItemqty();
									 
									 sumVo.setSortingno(sortingno);
									 sumVo.setSortingqty(sortingqty);
								 }
								 sumVo.setDiffqty(diffqty);
								 resultList.add(sumVo);
							 }
						 }else{
							 //分拣区通道多于重力式货架通道
							 for(int c=0;c<slen;c++){
								 sumVo=new InventorySumVo();
								 sumVo.setCigarettecode(cigarettecode);
								 sumVo.setCigarettename(cigarettename);
								 sumVo.setPaperqty(paperqty);
								 sumVo.setAtscellqty(ATSCellqty);
								 sumVo.setScatteredqty(scatteredqty);
								 sumVo.setUnnormalno1(unnormalno1);
								 sumVo.setUnnormalqty1(unnormalqty1);
								 sumVo.setUnnormalno2(unnormalno2);
								 sumVo.setUnnormalqty2(unnormalqty2);
								 //diffqty=diffqty.subtract(unnormalqty2);
								 sumVo.setCommonno(commonno);
								 sumVo.setCommonqty(commonqty);
								 flag="1";
								 sumVo.setFlag(flag);
								 
								 troughVo=sList.get(c);
								 sortingno=troughVo.getTroughno()+"";
								 sortingqty=troughVo.getItemqty();
								 
								 sumVo.setSortingno(sortingno);
								 sumVo.setSortingqty(sortingqty);
								 
								 if(c<zlen){
									 troughVo=zList.get(c);
									 shelfno=troughVo.getTroughno()+"";
									 shelfqty=troughVo.getItemqty();//重力式货架数量件转条
									 
									 sumVo.setShelfno(shelfno);
									 sumVo.setShelfqty(shelfqty);
								 }
								 sumVo.setDiffqty(diffqty);
								 resultList.add(sumVo);
							 }
						 }
					 }else{
						 sumVo=new InventorySumVo();
						 sumVo.setCigarettecode(cigarettecode);
						 sumVo.setCigarettename(cigarettename);
						 sumVo.setPaperqty(paperqty);
						 //没有分拣以及重力式货架的品牌
						 sumVo.setAtscellqty(ATSCellqty);
						 sumVo.setScatteredqty(scatteredqty);
						 sumVo.setUnnormalno1(unnormalno1);
						 sumVo.setUnnormalqty1(unnormalqty1);
						 sumVo.setUnnormalno2(unnormalno2);
						 sumVo.setUnnormalqty2(unnormalqty2);
						 //System.out.println("diffqty=="+diffqty+"--------------unnormalqty2=="+unnormalqty2);
						 //diffqty=diffqty.subtract(unnormalqty2);
						 sumVo.setCommonno(commonno);
						 sumVo.setCommonqty(commonqty);
						 sumVo.setSortingno(sortingno);
						 sumVo.setSortingqty(sortingqty);
						 sumVo.setShelfno(shelfno);
						 sumVo.setShelfqty(shelfqty);
						 sumVo.setDiffqty(diffqty);
						 sumVo.setFlag(flag);
						 resultList.add(sumVo);
					 }
				 }
				 Iterator<InventorySumVo> it = resultList.iterator();
				 while(it.hasNext()){
					 InventorySumVo x = it.next();
				     if("".equals(x.getFlag())){
				         it.remove();
				     }
				 }
				return resultList;
	}
}

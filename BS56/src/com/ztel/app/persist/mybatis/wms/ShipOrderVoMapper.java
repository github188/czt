package com.ztel.app.persist.mybatis.wms;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ztel.app.vo.wms.ShipOrderVo;
import com.ztel.framework.vo.Pagination;

public interface ShipOrderVoMapper {
	
	List<ShipOrderVo> selectShiporderPageList(Pagination<?> page);
	
	List<ShipOrderVo> selectShiporderAnalyzeList(ShipOrderVo shipOrderVo);
	
	/**
	 * 工商协同：品牌销售汇总
	 * @param shipOrderVo
	 * @return
	 */
	List<ShipOrderVo> selectShiporderBrandSaleReport(ShipOrderVo shipOrderVo);
	
	/**
	 * 根据订单日期获取配送车组code列表
	 * @param shipOrderVo
	 * @return
	 */
	List<ShipOrderVo> selectRoutecodeList(ShipOrderVo shipOrderVo);
	
	/**
	 * 取预付款客户订单 ,用于货款核算预付款客户车组查看
	 * @param shipOrderVo
	 * @return
	 */
	List<ShipOrderVo> selectShiporderPerpayList(ShipOrderVo shipOrderVo);
	
	/**
	 * 取预付款客户订单 ,用于货款核算预付款客户订单查看
	 * @param shipOrderVo
	 * @return
	 */
	List<ShipOrderVo> selectShiporderPerpayOrderList(ShipOrderVo shipOrderVo);
	
	/**
	 * 取预付款客户订单统计 ,用于货款核算预付款客户车组统计
	 * @param shipOrderVo
	 * @return
	 */
	List<ShipOrderVo> selectShiporderPerpayCountList(ShipOrderVo shipOrderVo);
	
	/**
	 * 根据传入参数查询订单列表
	 * @param shipOrderVo
	 * @return
	 */
	List<ShipOrderVo> selectShiporderByCondition(ShipOrderVo shipOrderVo);
	
	/**
	 * 取指定日期订单数量
	 * @param orderdate
	 * @return
	 */
	int selectShiporderCount(@Param("orderdate")String orderdate);
	/**
    *
    * @mbggenerated 2017-08-04
    */
   int deleteByPrimaryKey(String id);

   /**
    *
    * @mbggenerated 2017-08-04
    */
   int insert(ShipOrderVo record);

   /**
    *
    * @mbggenerated 2017-08-04
    */
   int insertSelective(ShipOrderVo record);

   /**
    *
    * @mbggenerated 2017-08-04
    */
   ShipOrderVo selectByPrimaryKey(String id);

   /**
    *
    * @mbggenerated 2017-08-04
    */
   int updateByPrimaryKeySelective(ShipOrderVo record);

   /**
    *
    * @mbggenerated 2017-08-04
    */
   int updateByPrimaryKey(ShipOrderVo record);
}
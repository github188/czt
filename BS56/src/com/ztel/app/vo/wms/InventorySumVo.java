package com.ztel.app.vo.wms;

import java.math.BigDecimal;

public class InventorySumVo {
	private String cigarettecode;
	private String cigarettename;
	private BigDecimal paperqty;
	private BigDecimal atscellqty;
	private BigDecimal scatteredqty;
	private String shelfno;
	private BigDecimal shelfqty;
	private String sortingno;
	private BigDecimal sortingqty;
	private String unnormalno1;
	private BigDecimal unnormalqty1;
	private String unnormalno2;
	private BigDecimal unnormalqty2;
	private String commonno;
	private BigDecimal commonqty;
	private BigDecimal diffqty;
	private String flag;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCigarettecode() {
		return cigarettecode;
	}
	public void setCigarettecode(String cigarettecode) {
		this.cigarettecode = cigarettecode;
	}
	public String getCigarettename() {
		return cigarettename;
	}
	public void setCigarettename(String cigarettename) {
		this.cigarettename = cigarettename;
	}
	public BigDecimal getPaperqty() {
		return paperqty;
	}
	public void setPaperqty(BigDecimal paperqty) {
		this.paperqty = paperqty;
	}
//	public BigDecimal getATSCellqty() {
//		return atscellqty;
//	}
//	public void setATSCellqty(BigDecimal aTSCellqty) {
//		atscellqty = aTSCellqty;
//	}
	
	public BigDecimal getScatteredqty() {
		return scatteredqty;
	}
	public BigDecimal getAtscellqty() {
		return atscellqty;
	}
	public void setAtscellqty(BigDecimal atscellqty) {
		this.atscellqty = atscellqty;
	}
	public void setScatteredqty(BigDecimal scatteredqty) {
		this.scatteredqty = scatteredqty;
	}
	public String getShelfno() {
		return shelfno;
	}
	public void setShelfno(String shelfno) {
		this.shelfno = shelfno;
	}
	public BigDecimal getShelfqty() {
		return shelfqty;
	}
	public void setShelfqty(BigDecimal shelfqty) {
		this.shelfqty = shelfqty;
	}
	public String getSortingno() {
		return sortingno;
	}
	public void setSortingno(String sortingno) {
		this.sortingno = sortingno;
	}
	public BigDecimal getSortingqty() {
		return sortingqty;
	}
	public void setSortingqty(BigDecimal sortingqty) {
		this.sortingqty = sortingqty;
	}
	public String getUnnormalno1() {
		return unnormalno1;
	}
	public void setUnnormalno1(String unnormalno1) {
		this.unnormalno1 = unnormalno1;
	}
	public BigDecimal getUnnormalqty1() {
		return unnormalqty1;
	}
	public void setUnnormalqty1(BigDecimal unnormalqty1) {
		this.unnormalqty1 = unnormalqty1;
	}
	public String getUnnormalno2() {
		return unnormalno2;
	}
	public void setUnnormalno2(String unnormalno2) {
		this.unnormalno2 = unnormalno2;
	}
	public BigDecimal getUnnormalqty2() {
		return unnormalqty2;
	}
	public void setUnnormalqty2(BigDecimal unnormalqty2) {
		this.unnormalqty2 = unnormalqty2;
	}
	public String getCommonno() {
		return commonno;
	}
	public void setCommonno(String commonno) {
		this.commonno = commonno;
	}
	public BigDecimal getCommonqty() {
		return commonqty;
	}
	public void setCommonqty(BigDecimal commonqty) {
		this.commonqty = commonqty;
	}
	public BigDecimal getDiffqty() {
		return diffqty;
	}
	public void setDiffqty(BigDecimal diffqty) {
		this.diffqty = diffqty;
	}
	
}
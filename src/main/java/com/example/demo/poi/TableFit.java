package com.example.demo.poi;

import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public interface TableFit {

	/**
	 * 
	 * @param poiTable
	 */
	public static void fitTable(XWPFTable poiTable) {
		setTableWidth(poiTable,STTblWidth.PCT);
		setTableAlignment(poiTable,STJc.RIGHT);
	}

	/**
	 * 
	 * @param poiTable
	 * @param stTblWidth
	 */
	public static void setTableWidth(XWPFTable poiTable,STTblWidth.Enum stTblWidth) {
		CTTbl table = poiTable.getCTTbl();
		CTTblPr pr = table.getTblPr();
		CTTblWidth tblW = pr.getTblW();
		tblW.setW(BigInteger.valueOf(5000));
		tblW.setType(stTblWidth);
		pr.setTblW(tblW);
		table.setTblPr(pr);
	}
	
	/**
	 * 
	 * @param table
	 * @param justification
	 */
	public static void setTableAlignment(XWPFTable table, STJc.Enum justification) {
		CTTblPr tblPr = table.getCTTbl().getTblPr();
		CTJc jc = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
		jc.setVal(justification);
	}

}

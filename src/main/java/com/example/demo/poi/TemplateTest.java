package com.example.demo.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.core.io.ClassPathResource;

public class TemplateTest {

	/**
	 * 
	 * @param tableData
	 * @return
	 * @throws Exception
	 */
	public XWPFTable createTable(String[][] tableData, TableFit tableFit) throws Exception {
		XWPFDocument doc = new XWPFDocument();

		int nRows = tableData.length;
		int nCols = tableData[0].length;
		XWPFTable table = doc.createTable(nRows, nCols);
		TableFit.fitTable(table);

		// Set the table style. If the style is not defined, the table style
		// will become "Normal".
		CTTblPr tblPr = table.getCTTbl().getTblPr();

		CTString styleStr = tblPr.addNewTblStyle();
		styleStr.setVal("StyledTable");

		// Get a list of the rows in the table
		List<XWPFTableRow> rows = table.getRows();
		int rowCt = 0;
		int colCt = 0;
		for (XWPFTableRow row : rows) {
			CTTrPr trPr = row.getCtRow().addNewTrPr();
			CTHeight ht = trPr.addNewTrHeight();
			ht.setVal(BigInteger.valueOf(360));

			List<XWPFTableCell> cells = row.getTableCells();
			for (XWPFTableCell cell : cells) {
				CTTcPr tcpr = cell.getCTTc().addNewTcPr();

				CTVerticalJc va = tcpr.addNewVAlign();
				va.setVal(STVerticalJc.CENTER);

				// get 1st paragraph in cell's paragraph list
				XWPFParagraph para = cell.getParagraphs().get(0);
				CTP ctp = para.getCTP();
				CTPPr ctppr;
				if ((ctppr = ctp.getPPr()) == null) {
					ctppr = ctp.addNewPPr();
				}
				ctppr.addNewBidi().setVal(STOnOff.ON);

				// create a run to contain the content
				XWPFRun rh = para.createRun();
				
				if (rowCt == 0) {
					// header row
					rh.setText(tableData[rowCt][colCt]);
					rh.setBold(true);
					para.setAlignment(ParagraphAlignment.CENTER);
				} else {
					rh.setText(tableData[rowCt][colCt]);
					para.setAlignment(ParagraphAlignment.LEFT);
				}
				colCt++;
			} 
			colCt = 0;
			rowCt++;
		} 

		if (doc != null) {
			doc.close();
		}
		return table;
	}

	public void update(XWPFDocument doc, XWPFTable table) {

		doc.setTable(0, table);
	}

	public static void main(String[] args) throws Exception {

		String outputPath = new Date().getTime() + ".docx";
		OutputStream out = null;
		try {
			File file = new ClassPathResource("empty.docx").getFile();
			XWPFDocument doc = new XWPFDocument(new FileInputStream(file));

			TemplateTest simpleTable = new TemplateTest();
			String[][] tableData = new String[3][3];
			tableData[0][0] = "כותרת1";
			tableData[0][1] = "כותרת2";
			tableData[0][2] = "כותרת3";
			tableData[1][0] = "טקסט1";
			tableData[1][1] = "טקסט2";
			tableData[1][2] = "טקסט3";
			tableData[2][0] = "טקסט4";
			tableData[2][1] = "טקסט5";
			tableData[2][2] = "טקסט6";

			TableFit tableFit = new TableFit() {
			};
			XWPFTable createTable = simpleTable.createTable(tableData, tableFit);

			simpleTable.update(doc, createTable);

			out = new FileOutputStream(new File(outputPath));
			doc.write(out);
			out.flush();
			out.close();
			doc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
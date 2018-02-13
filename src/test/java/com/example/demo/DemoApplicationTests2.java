package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests2 {

	@Test
	public void contextLoads() {
		try {
			XWPFDocument doc = new XWPFDocument();

			XWPFParagraph title = doc.createParagraph();
			XWPFRun run = title.createRun();
			run.setText("Fig.1 A Natural Scene");
			run.setBold(true);
			title.setAlignment(ParagraphAlignment.CENTER);

			String imgFile = "";
			FileInputStream is = new FileInputStream(imgFile);
//			BufferedImage bimg = ImageIO.read(is);
//			int width = bimg.getWidth();
//			int height = bimg.getHeight();
			run.addBreak();
			run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(500), Units.toEMU(500)); // 200x200 pixels
			is.close();

			FileOutputStream fos = new FileOutputStream("test5.docx");
			doc.write(fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

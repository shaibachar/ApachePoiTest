package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
		try {
			String pdfFilename = "/home/shai/Downloads/תגובה לסוגיות המוסכמות בבית המשפט קמא - בראי המשפט הבינלאומי להגשה";
			PDDocument document = PDDocument.load(new File(pdfFilename+".pdf"));
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int page = 0; page < document.getNumberOfPages(); ++page) {
				BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

				// suffix in filename will be used as the file format
				ImageIOUtil.writeImage(bim, "/home/shai/Downloads/temp/a" + "-" + (page + 1) + ".jpg", 300);
			}
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
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

	public List<BufferedImage> convertPDFToImages(String pdfFilename) {
		List<BufferedImage> res = new ArrayList<>();
		try {

			PDDocument document = PDDocument.load(new File(pdfFilename));
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int page = 0; page < document.getNumberOfPages(); ++page) {
				BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.GRAY);
				res.add(bim);
				// suffix in filename will be used as the file format
				//ImageIOUtil.writeImage(bim, pdfFilename + "-" + (page + 1) + ".jpg", 300);
			}
			document.close();
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public List<InputStream> convertImagesToArray(List<BufferedImage> images) throws IOException {
		List<InputStream> res = new ArrayList<>();
		for (BufferedImage image : images) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			res.add(is);
		}

		return res;
	}

	@Test
	public void contextLoads() {
		try {

			String pdfFile = "/home/shai/Downloads/תגובה לסוגיות המוסכמות בבית המשפט קמא - בראי המשפט הבינלאומי להגשה.pdf";
			List<BufferedImage> images = convertPDFToImages(pdfFile);
			List<InputStream> convertImagesToArray = convertImagesToArray(images);
			XWPFDocument doc = new XWPFDocument();

			XWPFParagraph title = doc.createParagraph();
			XWPFRun run = title.createRun();
			run.setText("Fig.1 A Natural Scene");
			run.setBold(true);
			title.setAlignment(ParagraphAlignment.CENTER);

			// String imgFile = "";
			// FileInputStream is = new FileInputStream(imgFile);
			// BufferedImage bimg = ImageIO.read(is);
			// int width = bimg.getWidth();
			// int height = bimg.getHeight();
			for (InputStream is : convertImagesToArray) {
				run.addBreak();
				run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, "1", Units.toEMU(500), Units.toEMU(500)); // 200x200 pixels
				is.close();
			}

			FileOutputStream fos = new FileOutputStream("/home/shai/Downloads/temp/test5.docx");
			doc.write(fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

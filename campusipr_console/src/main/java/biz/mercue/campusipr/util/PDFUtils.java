package biz.mercue.campusipr.util;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;

public class PDFUtils {
	
	private static Logger log = Logger.getLogger(JacksonJSONUtils.class.getName());
	
	
	public static int convertTiff2PDF(String tiffPath,String pdfPath) {
		try {
			RandomAccessFileOrArray tiffFile = new RandomAccessFileOrArray(tiffPath);
			int numberOfPages = TiffImage.getNumberOfPages(tiffFile);
			Document tiffToPDF =  new com.itextpdf.text.Document(PageSize.LETTER_LANDSCAPE);  
			String temp = tiffPath.substring(0, tiffPath.lastIndexOf("."));
			PdfWriter pdfWriter = PdfWriter.getInstance(tiffToPDF, new FileOutputStream(temp+".pdf"));
	        pdfWriter.setStrictImageSequence(true);
	        tiffToPDF.open();
	        for(int tiffImageCounter = 1;tiffImageCounter <= numberOfPages;tiffImageCounter++) {
	                Image img = TiffImage.getTiffImage(tiffFile, tiffImageCounter);

	                img.setAbsolutePosition(0,0);

	                img.scaleToFit(612,792);

	                tiffToPDF.add(img);

	                tiffToPDF.newPage();
	           } 

	        
		} catch (IOException | DocumentException e) {
			log.error(e.getMessage());
		}
		
		
		return 0;
	}
	

}

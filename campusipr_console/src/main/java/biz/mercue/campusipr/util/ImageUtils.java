package biz.mercue.campusipr.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageUtils {

	private static Logger log = Logger.getLogger(ImageUtils.class.getName());


	private static IdentifyCmd identifyCmd;
	private static ConvertCmd convertCmd;

	public ImageUtils() {
		identifyCmd = new IdentifyCmd(true);
		convertCmd = new ConvertCmd();
	}
	
	public static boolean writeFile(MultipartFile file, File finalFile) {
		BufferedOutputStream outputStream = null;
		boolean result = false;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(finalFile));
			log.info("save file : " + finalFile.getAbsolutePath());
			outputStream.write(file.getBytes());
			outputStream.flush();
			result = true;
			log.info("file: " +file.getOriginalFilename() + " saved ");
		} catch (FileNotFoundException e) {
			log.error("file dir not found error: " + e.getMessage());
		} catch (IOException e) {
			log.error("upload file error: " + e.getMessage());
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error("fail to close output stream, error: " + e);
				}
			}
		}
		return result;
	}


	public static void resizeImageSquare(Integer width,String srcPath,String desPath) throws IOException, InterruptedException, IM4JavaException {
		log.info("start resizing image: " + srcPath);
		IMOperation op = new IMOperation();
		op.quality(0.80);
		op.addImage(srcPath); //place holder for input file
		op.resize(width, width);
		op.addImage(desPath); //place holder for output file
		if (!StringUtils.isNULL(Constants.IMAGEMAGICK_PATH)) {
			log.info("magick path: " + Constants.IMAGEMAGICK_PATH);
			convertCmd.setSearchPath(Constants.IMAGEMAGICK_PATH);	    	   
		}

		convertCmd.run(op);

	}

	public static void resizeImage(Integer width,String srcPath,String desPath) throws IOException, InterruptedException, IM4JavaException {
		log.info("start resizing image: " + srcPath);
		IMOperation op = new IMOperation();
		op.quality(0.80);
		op.addImage(srcPath); //place holder for input file
		op.resize(width);
		op.addImage(desPath); //place holder for output file
		if (!StringUtils.isNULL(Constants.IMAGEMAGICK_PATH)) {
			log.info("magick path: " + Constants.IMAGEMAGICK_PATH);
			convertCmd.setSearchPath(Constants.IMAGEMAGICK_PATH);	    	   
		}

		convertCmd.run(op);

	}

	public static void copyImage(String srcPath,String desPath) throws IOException, InterruptedException, IM4JavaException {
		log.info("start coping image: " + srcPath);
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.crop(0, 0);
		op.addImage(desPath);
		if (!StringUtils.isNULL(Constants.IMAGEMAGICK_PATH)) {
			log.info("magick path: " + Constants.IMAGEMAGICK_PATH);
			convertCmd.setSearchPath(Constants.IMAGEMAGICK_PATH);	    	   
		}
		convertCmd.run(op);
		log.info("copy image to: " + desPath);
	}

	/**
	 * 
	 * @param imagePath
	 * @param sizeMB
	 * @return a map with key: result, size (has it if result = false)
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static Map<String, Object> checkImageSizeMB(String imagePath, int sizeMB) throws IOException, InterruptedException, IM4JavaException {
		log.info("start checking image size: " + imagePath);
		Double sizeNum = 0.0;
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> map = getImageInfo(imagePath);

		String sizeStr = getImageFileSize(imagePath);
		log.info("size: " + map.get(Constants.KEY_SIZE));
		if (sizeStr.contains("M")) {
			String size = sizeStr.substring(0, sizeStr.lastIndexOf("M"));
			sizeNum = Double.valueOf(size);
			if (sizeNum > sizeMB) {
				result.put(Constants.KEY_SIZE, sizeNum);
				result.put(Constants.KEY_RESULT, false);
			} else {
				result.put(Constants.KEY_RESULT, true);
			}
		} else {
			result.put(Constants.KEY_RESULT, true);
		}

		return result;
	}

	public static String getImageFileSize(String imagePath) throws IOException, InterruptedException, IM4JavaException {
		log.info("get image file size, path: " + imagePath);
		IMOperation op = new IMOperation();
		op.ping();
		op.format("%b");
		op.addImage();

		ArrayListOutputConsumer output = new ArrayListOutputConsumer();
		if (!StringUtils.isNULL(Constants.GRAPHICSMAGICK_PATH)) {
			log.info("graphic magick path: " + Constants.GRAPHICSMAGICK_PATH);
			identifyCmd.setSearchPath(Constants.GRAPHICSMAGICK_PATH);			
		}
		identifyCmd.setOutputConsumer(output);

		identifyCmd.run(op, imagePath);

		ArrayList<String> cmdOutput = output.getOutput();

		String result = cmdOutput.get(0);
		return result;
	}

	/**
	 * 
	 * @param imagePath
	 * @return a map with key: width, height, size, suffix
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public static Map<String, String> getImageInfo(String imagePath) throws IOException, InterruptedException, IM4JavaException {
		log.info("get image info, path: " + imagePath);
		Map<String, String> imageInfo = new HashMap<>();
		IMOperation op = new IMOperation();
		op.ping();
		op.format("%w,%h,%e,%b");
		op.addImage();

		ArrayListOutputConsumer output = new ArrayListOutputConsumer();
		if (!StringUtils.isNULL(Constants.GRAPHICSMAGICK_PATH)) {
			log.info("graphic magick path: " + Constants.GRAPHICSMAGICK_PATH);
			identifyCmd.setSearchPath(Constants.GRAPHICSMAGICK_PATH);			
		}
		
		identifyCmd.setOutputConsumer(output);
		identifyCmd.run(op, imagePath);

		ArrayList<String> cmdOutput = output.getOutput();

		String[] result = cmdOutput.get(0).split(",");

		if(result.length == 4) {
			
			imageInfo.put(Constants.KEY_WIDTH, result[0]);
			imageInfo.put(Constants.KEY_HEIGHT, result[1]);
			imageInfo.put(Constants.KEY_SUFFIX, result[2]);
			imageInfo.put(Constants.KEY_SIZE, result[3]);
		}

		return imageInfo;
	}
}

package se.vgr.webbisar.svc.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;

import se.vgr.webbisar.svc.ImageSize;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {

	/**
	 * Resizes and overwrites the image. Will keep the original pictures dimensions. 
	 * @param sourceFile the file to rezise
	 * @param newLongSideSize the new size, in pixels.
	 * @param quality a number between 0 and 100 where 100 gives the best quality
	 * @throws IOException
	 */
	public synchronized static void scaleImage(File sourceFile, ImageSize imageSize, float quality) throws IOException {
		System.gc();
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		int srcWidth = sourceImage.getWidth();
		int srcHeight = sourceImage.getHeight();
				
		double longSideForSource = (double) Math.max(srcWidth, srcHeight); 
		double longSideForDest = srcWidth > srcHeight ? imageSize.getWidth() : imageSize.getHeight();
		double multiplier = longSideForDest / longSideForSource;
		
		int destWidth = (int) (srcWidth * multiplier);
		int destHeight = (int) (srcHeight * multiplier);

		BufferedImage destImage = new BufferedImage((int)imageSize.getWidth(), (int)imageSize.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = destImage.createGraphics();
		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
		
		AffineTransform affineTransform = AffineTransform.getScaleInstance(multiplier, multiplier);
		AffineTransform trans = new AffineTransform();
	    trans.setToTranslation((imageSize.getWidth() - destWidth)/2, (imageSize.getHeight() - destHeight)/2);
		graphics.transform(trans);

		graphics.drawRenderedImage(sourceImage, affineTransform);
		saveImageAsJPEG(sourceFile.getAbsolutePath(), destImage, quality);
		
	}
	
	protected static ImageSize calculateImageSize(ImageSize src, ImageSize dest) {
		
		double wScale = dest.getWidth() / src.getWidth();
		double hScale = dest.getHeight() / src.getHeight();
		
		double multiplier = (hScale < wScale) ? hScale : wScale;
				
		int destWidth = (int) (src.getWidth() * multiplier);
		int destHeight = (int) (src.getHeight() * multiplier);
		
		return new ImageSize(destWidth, destHeight);
		
	}
	
	/**
	 * Saves the image as a JPEG.
	 * @param file the path to the file 
	 * @param bufferedImage the image to save
	 * @param quality a number between 0 and 100 where 100 gives the highest quality
	 * @throws IOException
	 */
    public static void saveImageAsJPEG(String file, BufferedImage bufferedImage, float quality )
        throws IOException
    {
        BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( file ) );
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( out );
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam( bufferedImage );
        param.setQuality( quality / 100.0f, false );
        encoder.setJPEGEncodeParam( param );
        encoder.encode( bufferedImage );

        out.close();
    }
    
	/**
	 * This method will remove the temporary directory from disk and all files
	 * @param dir the directory
	 */
	public static void removeDir(File dir) {
		// remove the directory and all its files
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				file.delete();
			}
			dir.delete();
		}
	}
	
	
	/**
	 * Will check if the baseDir has a subdir with todays date and and return it. If it doesn't exist it will create it. 
	 * @param baseDir
	 * @return a File corresponding to the dir with todays date
	 */
	public static File getDirForTodaysDate(String baseDir) {
		Calendar cal = new GregorianCalendar();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todaysDate = format.format(cal.getTime());
		File todaysDateDir = new File(baseDir,todaysDate);
		if (!todaysDateDir.exists()) {
			todaysDateDir.mkdir();
			if (!todaysDateDir.exists()) {
				throw new RuntimeException("Failed to create dir "
						+ todaysDateDir.getPath());
			}
		}
		return todaysDateDir;
	}
	
	/**
	 * Will copy a file from the srcFile to the destFile. 
	 * 
	 * @param srcFile the source file to copy from
	 * @param destFile the destination file to copy to
	 */
	public static void copyFile(File srcFile, File destFile) {
		InputStream in = null;
		OutputStream out = null;
		
		try {
			
			if(!destFile.getParentFile().exists()) destFile.getParentFile().mkdir();
			//if(!destFile.exists()) destFile.createNewFile();
			
			in = new FileInputStream(srcFile);
			// Overwrite the file.
			out = new FileOutputStream(destFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace(); // we just log when the file doesn't exist. 
			//throw new RuntimeException(ex);
		} catch (IOException e) {
			e.printStackTrace();
			//throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					//throw new RuntimeException(e);
				}
			}

		}
	}
	
	

}

package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gillccwoodworks.Constants;

public class ImageUtilTest
{
	
	public static void main(String[] args)
	{
		double ratio = Constants.DEFAULT_RATIO;
		
		//double ratio = (double)3.0/2.0;
		String path = "/home/nils/problem_images/portrait.jpg";
		String outputPath = "/home/nils/problem_images/portrait_resized.jpg";
		
		try
		{
			BufferedImage image = ImageIO.read(new File(path));
			BufferedImage croppedImage = ImageUtil.cropImage(image, ratio);
			
			int type = croppedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : croppedImage.getType();
			
			BufferedImage resizedImage = ImageUtil.resizeImage(croppedImage, type, 900, 600);
			
			ImageUtil.writeImageToFile(resizedImage, outputPath);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	
}

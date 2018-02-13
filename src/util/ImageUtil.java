package util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil
{
	public static BufferedImage resizeImage(BufferedImage image, int type, int width, int height)
	{
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	}
	
	public static BufferedImage cropImage(BufferedImage image, double ratio)
	{
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
		double imageRatio =  (double)imgWidth/(double)imgHeight;
		
		System.out.println("You want this image's ratio to be: " + ratio);
		System.out.println("It is currenty: " + imageRatio);
		
		// this means we chop off the top and bottom
		if(imageRatio < ratio)
		{
			System.out.println("Image Ratio: " + imageRatio + " - This image is too tall, cropping off top and bottom");
			
			double targetHeight = imgWidth*1.0/ratio;
			double y = ((imgHeight-targetHeight)/2);
			return image.getSubimage(0, (int)y, imgWidth, (int)targetHeight);
		}
		// this means we chop off the sides
		else if (imageRatio > ratio)
		{
			System.out.println("Image Ratio: " + imageRatio +" - This image is too wide, cropping off sides");
			
			double targetWidth = imgHeight*1.0/ratio;
			double x = ((imgWidth-targetWidth)/2);
			return image.getSubimage((int) x, 0, (int)targetWidth, imgHeight);
		}
		else if (imageRatio == ratio)
		{
			System.out.println("This image already has the proper ratio");
		}
		
		return image;
	}
	
	public static void writeImageToFile(BufferedImage image, String path) throws IOException
	{
		ImageIO.write(image, "jpg", new File(path));
	}
}


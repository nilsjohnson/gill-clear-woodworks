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
			return image.getSubimage((int) x, 0, imgWidth, (int)targetWidth);
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

/*	public static void resizeImage(String filePath)
{
	double ratioWidth = 3;
	double ratioHeight = 2;
	double ratio = ratioWidth/ratioHeight;
	
	BufferedImage croppedImage = null;
	BufferedImage resizedImage = null; 
	
	try
	{
		BufferedImage originalImage = ImageIO.read(new File(filePath));
		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		
		// this image
		int imgWidth = originalImage.getHeight();
		int imgHeight = originalImage.getHeight();
		
		double curImageRatio =  (double)imgWidth/(double)imgHeight;
		
		// this means we chop off from the top and bottom
		System.out.println("Your chosen ratio is: " + ratio);
		System.out.println("this images ratio is: " + imgWidth/imgHeight);
		
		if(curImageRatio < ratio)
		{
			double targetHeight = imgWidth*ratioHeight/ratioWidth;
			double x = ((imgHeight-targetHeight)/2)+1;
			
			System.out.println("x: " + x);
			System.out.println("y: " + "0");
			System.out.println("imgWidth: " + imgWidth);
			System.out.println("target height: " + targetHeight);
			
			croppedImage = originalImage.getSubimage(0, (int)x, imgWidth, (int)targetHeight);
			resizedImage = resizeImage(croppedImage, type, 900, 600);

		}
		// this means we chop off the sides
		else if (imgWidth/imgHeight > ratio)
		{
			System.out.println("this doesnt need resizng");
			resizedImage = resizeImage(originalImage, type, 900, 600);
		}
		
		ImageIO.write(resizedImage, "jpg", new File("/home/nils/bearsm.jpg"));
	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
}*/

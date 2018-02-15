package data;

import java.sql.SQLException;
import java.util.ArrayList;

public class DAOtest
{

	public static void main(String[] args)
	{
		ImageDAO dao = null;

		try
		{
			dao = new ImageDAO();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		// gallery images
		String[] images = { "1 Collie", "2 Golden", "3 Spaniel", "4 Shep", "5 Pitbull", "6 Afgan" };
		String[] images2 = { "Guppy", "Goldfish", "Platy", "Crab" };

		String title = "gallery one";
		String title2 = "Fish";

	

		ImageGallery gallery = new ImageGallery(title, images);
		ImageGallery gallery2 = new ImageGallery(title2, images2);
		
		System.out.println("before bump right:");
		System.out.println(gallery.toString());
		
		gallery.bumpRight("6 Afgan");
		
		System.out.println("\nafter bump right");
		System.out.println(gallery.toString());
		
		System.out.println("before bump left:");
		System.out.println(gallery.toString());
		
		gallery.bumpLeft("1 Collie");
		
		System.out.println("\nafter bump left");
		System.out.println(gallery.toString());
		
		try
		{
			dao.insertOrRelaceGallery(gallery);
			dao.insertOrRelaceGallery(gallery2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	

		// get the galleries just made and print them
		ArrayList<ImageGallery> galleryList = dao.getAllGalleries();

		for (ImageGallery gal : galleryList)
		{
			System.out.println(gal.toString());
		}
		
		ImageGallery anotherGallery = dao.getGallery(title);
		
		System.out.println("\n" + anotherGallery.toString());
		
		dao.deleteGallery(title);
		dao.deleteGallery(title2);
	}
}

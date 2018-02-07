package data;

import java.sql.SQLException;
import java.util.ArrayList;

public class DAOtest
{

	public static void main(String[] args)
	{

		String[] images = { "BorderCollie", "Golden Retreiver", "Spaniel", "German Shepherd" };
		String[] images2 = { "Guppy", "Goldfish", "Platy", "Crab" };
		String[] images3 = { "Sparrow", "Dove", "Crow", "Warbler", "Woodpecker" };

		String title = "Dogs to Birds";
		String title2 = "Fish";

		ImageDAO dao = null;

		try
		{
			dao = new ImageDAO();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		ImageGallery gallery = new ImageGallery(title, images);
		ImageGallery gallery2 = new ImageGallery(title2, images2);
		dao.addGallery(gallery);
		dao.addGallery(gallery2);

		// get the galleries just made and print them
		ArrayList<ImageGallery> galleryList = dao.getAllGalleries();

		for (ImageGallery gal : galleryList)
		{
			System.out.println(gal.toString());
		}

		System.out.println("updating gallery");
		gallery.setImages(images3);
		dao.updateGalleryImages(gallery);

		galleryList = dao.getAllGalleries();

		for (ImageGallery gal : galleryList)
		{
			System.out.println(gal.toString());
		}

		// adding on on
		System.out.println("adding a path");
		dao.appendToGallery(title, "a new path in dogs to birds, at the end");

		galleryList = dao.getAllGalleries();

		for (ImageGallery gal : galleryList)
		{
			System.out.println(gal.toString());
		}

		System.out.println("testing delete");
		gallery.deleteImage("Crow");
		System.out.println(gallery.toString());
		
		dao.deleteGallery(title);
		dao.deleteGallery(title2);
	}
}

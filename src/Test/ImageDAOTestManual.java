/*package Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import data.Collection;
import data.ImageDAO;

public class ImageDAOTestManual
{
	public static void main(String[] args)
	{
		ImageDAO imageDAO = null;
		String path = "/home/nils/";
		
		// make the DAO
		try
		{
			imageDAO = new ImageDAO();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		// make collection 1
		ArrayList<String> imageList = new ArrayList<>();
		imageList.add("1_Image_1");
		imageList.add("1_Image_2");
		imageList.add("1_Image_3");
		imageList.add("1_Image_4");
		imageList.add("1_Image_5");
		imageList.add("1_Image_6");
		Collection collection1 = new Collection("Test Title 1", "Test Desc 1", imageList, null);
		
		// make collection 2
		ArrayList<String> imageList2 = new ArrayList<>();
		imageList2.add("2_Image_1");
		imageList2.add("2_Image_2");
		imageList2.add("2_Image_3");
		imageList2.add("2_Image_4");
		imageList2.add("2_Image_5");
		imageList2.add("2_Image_6");
		Collection collection2 = new Collection("Test Title 2", "Test Desc 2", imageList2, null);
		
		// make collection 3
		ArrayList<String> imageList3 = new ArrayList<>();
		imageList3.add("3_Image_1");
		imageList3.add("3_Image_2");
		imageList3.add("3_Image_3");
		imageList3.add("3_Image_4");
		imageList3.add("3_Image_5");
		imageList3.add("3_Image_6");
		
		Collection collection3 = new Collection("Test Title 3", "Test Desc 3", imageList3, null);
		
		
		try
		{
			// insert 3 collections
			imageDAO.InsertCollection(collection1);
			imageDAO.InsertCollection(collection2);
			imageDAO.InsertCollection(collection3);
			
			// retrieve these
			ArrayList<Collection> collectionList = imageDAO.getAllCollections();
			// print them
			printCollections(collectionList);
			
			// get the second one, delete it
			Collection collectionToDelete = collectionList.get(1);
			System.out.println("Deleting: " + collectionToDelete.getId().toString());
			imageDAO.DeleteCollection(collectionToDelete.getId());
			//get the all collections again and print
			ArrayList<Collection> collectionList2= imageDAO.getAllCollections();
			printCollections(collectionList2);
			
			System.out.println("now Deletting '3_Image_2");
			imageDAO.DeleteImage("3_Image_2");
			ArrayList<Collection> collectionList3= imageDAO.getAllCollections();
			printCollections(collectionList3);
			
			System.out.println("getting collection by UUID: ");
			Collection col = imageDAO.getCollection(UUID.fromString("18356a86-1100-4197-b793-daa2e03e4697"));
			printCollection(col);
			
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	public static void printCollections(ArrayList<Collection> collectionList)
	{
		for(Collection collection : collectionList)
		{
			printCollection(collection);
		}
	}
	
	public static void printCollection(Collection collection)
	{
		System.out.println("Title: " + collection. getTitle());
		System.out.println("Description: " + collection.getDescription());
		System.out.println("UUID: " + collection.getId().toString());
		System.out.println("Image Paths: ");
		for(int i = 0; i < collection.getNumberOfImages(); i++)
		{
			System.out.println("\t" + i + ".) " + collection.getImageAt(i));
		}
		System.out.println();
	}
}
*/
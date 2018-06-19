package data;

import java.util.ArrayList;

public class DbCollectionAdder
{
	public static void main(String[] args)
	{
		try
		{
			ImageDAO dao = new ImageDAO();
			
			ArrayList<Image> list1 = new ArrayList<>();
			ArrayList<Image> list2 = new ArrayList<>();
			ArrayList<Image> list3 = new ArrayList<>();
			
			list1.add(new Image("1_IMG_1"));
			list1.add(new Image("1_IMG_2"));
			list1.add(new Image("1_IMG_3"));
			
			list2.add(new Image("2_IMG_1"));
			list2.add(new Image("2_IMG_2"));
			list2.add(new Image("2_IMG_3"));
			
			list3.add(new Image("3_IMG_1"));
			list3.add(new Image("3_IMG_2"));
			list3.add(new Image("3_IMG_3"));
			
			
			Collection col1 = new Collection("Collection 1", "Desc. 1", list1);
			Collection col2 = new Collection("Collection 2", "Desc. 2", list2);
			Collection col3 = new Collection("Collection 3", "Desc. 3", list3);
			
			dao.InsertCollection(col1);
			dao.InsertCollection(col2);
			dao.InsertCollection(col3);
			
			ArrayList<Collection> allCollections = dao.getAllCollections();
			
			printCollections(allCollections);
			
		}
		catch(Exception e)
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
			System.out.println("\t" + i + ".) " + collection.getImageAt(i).path + ", UUID: " + collection.getImageAt(i).uuid.toString()) ;
		}
		System.out.println();
	}
}

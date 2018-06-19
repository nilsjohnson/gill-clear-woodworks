package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gillccwoodworks.Constants;

import data.Collection;
import data.Image;
import data.ImageDAO;

class ImageDAOTest
{
	private ImageDAO imageDAO;
	private String path = "/home/nils/";
	
	@BeforeEach
    void init() 
	{		
		try
		{
			imageDAO = new ImageDAO();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
    }
	
	@AfterEach
    void tearDown() 
	{
		File file = new File(path + Constants.DB_NAME);
		if (file.exists())
		{
		    // file.delete();
		    // System.out.println("DB deleted");
		}
    }
	
	
	@Test
	void insertCollectionTest()
	{
		ArrayList<Image> imageList = new ArrayList<>();
		imageList.add(new Image("Image_1"));
		imageList.add(new Image("Image_2"));
		imageList.add(new Image("Image_3"));
		imageList.add(new Image("Image_4"));
		imageList.add(new Image("Image_5"));
		imageList.add(new Image("Image_6"));
		
		Collection collection = new Collection("Test Title", "Test Desc", imageList);
		
		try
		{
			imageDAO.insertCollection(collection);
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	void testRetrievingAllCollections()
	{
		try
		{
			ArrayList<Collection> collectionList = imageDAO.getAllCollections();
			
			for(Collection collection : collectionList)
			{
				System.out.println("Title: " + collection. getTitle());
				System.out.println("Description: " + collection.getDescription());
				System.out.println("UUID: " + collection.getId().toString());
				System.out.println("Image Paths: ");
				for(int i = 0; i < collection.getNumberOfImages(); i++)
				{
					System.out.println("\t" + i + ".) " + collection.getImageAt(i));
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}
	}
	
}

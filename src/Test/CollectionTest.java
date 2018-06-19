package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data.Collection;
import data.Image;

class CollectionTest
{
	private Collection collection;
	ArrayList<Image> imageList;
	
	@BeforeEach
    void init() 
	{		
		imageList = new ArrayList<>();
		imageList.add(new Image("Image_1", UUID.fromString("bcb24f62-ba6c-40e5-8109-f9e287cbefc1")));
		imageList.add(new Image("Image_2", UUID.fromString("bcb24f62-ba6c-40e5-8109-f9e287cbefc2")));
		imageList.add(new Image("Image_3", UUID.fromString("bcb24f62-ba6c-40e5-8109-f9e287cbefc3")));
		imageList.add(new Image("Image_4", UUID.fromString("bcb24f62-ba6c-40e5-8109-f9e287cbefc4")));
		imageList.add(new Image("Image_5", UUID.fromString("bcb24f62-ba6c-40e5-8109-f9e287cbefc5")));
		imageList.add(new Image("Image_6", UUID.fromString("bcb24f62-ba6c-40e5-8109-f9e287cbefc6")));
		
		collection = new Collection("Test Title", "Test Desc", imageList);
    }

	@Test
	void bumpRightWrapsTest()
	{
		try
		{
			collection.bumpRight(UUID.fromString("bcb24f62-ba6c-40e5-8109-f9e287cbefc6"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(!collection.getImageAt(5).path.equals("Image_1")) 
		{
			fail("Bumping image in last index right did not wrap the first image into the last index.");
		}
		if(!collection.getImageAt(0).path.equals("Image_6"))
		{
			fail("Bumping image in last index right did not wrap it into the first index.");
		}
		
		
	}
	

	@Test
	void bumpLeftWrapsTest()
	{
		try
		{
			collection.bumpLeft(UUID.fromString("bcb24f62-ba6c-40e5-8109-f9e287cbefc1"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(!collection.getImageAt(0).path.equals("Image_6")) 
		{
			fail("Bumping first image left did not cause the last image to wrap to first position");
		}
		if(!collection.getImageAt(5).path.equals("Image_1"))
		{
			fail("Bumping first image left did not cause the first image to wrap to last position.");
		}	
	}
	
	@Test
	void returnsCorrectNumOfImagesTest()
	{
		if(collection.getNumberOfImages() != 6)
		{
			fail("There should be 6 images in the image list.");
		}
	}
	
	@Test 
	void bumpLeftNonEdgeCaseTest()
	{
		try
		{
			collection.bumpLeft(UUID.fromString("bcb24f62-ba6c-40e5-8109-f9e287cbefc2"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(!collection.getImageAt(0).path.equals("Image_2"))
		{
			fail("'Image_2' should have been in the first index after bumping left");
		}
		if(!collection.getImageAt(1).path.equals("Image_1"))
		{
			fail("'Image_1' should have been in the second index after bumping left");
		}
		
	}
	
	@Test 
	void bumpRightNonEdgeCaseTest()
	{
		
	}

}

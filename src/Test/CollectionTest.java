package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data.Collection;

class CollectionTest
{
	private Collection collection;
	ArrayList<String> imageList;
	
	@BeforeEach
    void init() 
	{		
		imageList = new ArrayList<>();
		imageList.add("Image_1");
		imageList.add("Image_2");
		imageList.add("Image_3");
		imageList.add("Image_4");
		imageList.add("Image_5");
		imageList.add("Image_6");
		
		collection = new Collection("Test Title", "Test Desc", imageList, null);
    }

	@Test
	void bumpRightWrapsTest()
	{
		try
		{
			collection.bumpRight("Image_6");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(!collection.getImageAt(5).equals("Image_1")) 
		{
			fail("Bumping image in last index right did not wrap the first image into the last index.");
		}
		if(!collection.getImageAt(0).equals("Image_6"))
		{
			fail("Bumping image in last index right did not wrap it into the first index.");
		}
		
		
	}
	

	@Test
	void bumpLeftWrapsTest()
	{
		try
		{
			collection.bumpLeft("Image_1");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(!collection.getImageAt(0).equals("Image_6")) 
		{
			fail("Bumping first image left did not cause the last image to wrap to first position");
		}
		if(!collection.getImageAt(5).equals("Image_1"))
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
			collection.bumpLeft("Image_2");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(!collection.getImageAt(0).equals("Image_2"))
		{
			fail("'Image_2' should have been in the first index after bumping left");
		}
		if(!collection.getImageAt(1).equals("Image_1"))
		{
			fail("'Image_1' should have been in the second index after bumping left");
		}
		
	}
	
	@Test 
	void bumpRightNonEdgeCaseTest()
	{
		
	}

}

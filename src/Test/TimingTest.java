package Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gillccwoodworks.GalleryServlet;

import data.ImageDAO;

public class TimingTest
{
	private static final String COLLECTION_LIST_PATH = "/home/nils/collectionList.dat";

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}
	
	private ImageDAO imageDAO;
	
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
	
	@Test
	void timingTest() throws Exception
	{
		List<data.Collection> allCollections = imageDAO.getAllCollections();
		for(data.Collection collection : allCollections) {
			GalleryServlet.addCollection(collection.getId());
		}

		long fileStartTime = System.nanoTime();
		for(int i = 0; i < 10000; i++) {
			loadFromFile();
		}
		long fileEndTime = System.nanoTime();

		long fileDuration = (fileEndTime - fileStartTime);
		
		long dbStartTime = System.nanoTime();
		for(int i = 0; i < 10000; i++) {
			imageDAO.getUUIDs();
		}
		long dbEndTime = System.nanoTime();

		long dbDuration = (dbEndTime - dbStartTime);
		
		long diff = dbDuration - fileDuration;
		
		System.out.println(diff);
		
	}

	@SuppressWarnings("unchecked")
	private static List<UUID> loadFromFile()
	{
		List<UUID> collectionList = null;
		try
        {   
            // Reading the object from a file
            FileInputStream file = new FileInputStream(COLLECTION_LIST_PATH);
            ObjectInputStream in = new ObjectInputStream(file);
             
            // Method for deserialization of object
            collectionList  = (ArrayList<UUID>)in.readObject();
             
            in.close();
            file.close();
            
        }
         
        catch(IOException ex)
        {
        	File file = new File(COLLECTION_LIST_PATH);
			if (!file.exists())
			{
				collectionList = new ArrayList<>();
			}
		}
         
        catch(ClassNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
		
		return collectionList;
	}

}

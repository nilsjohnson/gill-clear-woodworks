package com.gillccwoodworks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/gallery")
public class GalleryServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private NilsHtmlEngine engine;
	private static final String COLLECTION_LIST_PATH = Constants.HOME + "collectionList.dat";
	
	private static ArrayList<UUID> collectionList = null;

	public GalleryServlet()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		engine = new NilsHtmlEngine(this.getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//generate and display the head
		response.getWriter().append(engine.getGallery());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.sendRedirect("index");
	}
	
	
	public static void addCollection(UUID id)
	{
		if(collectionList == null)
		{
			loadCollectionList();
		}
		
		collectionList.add(id);
		writeCollectionList();
	}
	
	/**
	 * Removes a the id of a collection that is shown on this page.
	 * Does nothing if the collection is not shown on page.
	 * @param id
	 */
	public static void removeCollection(UUID id)
	{
		if(collectionList == null)
		{
			loadCollectionList();
		}
		
		for(int i = 0; i < collectionList.size(); i++)
		{
			if(collectionList.get(i).equals(id))
			{
				collectionList.remove(i);
			}
		}
		writeCollectionList();
	}
	
	
	public static ArrayList<UUID> getCollectionList()
	{
		if(collectionList == null)
		{
			loadCollectionList();
		}
		return collectionList;
	
	}

	private static void writeCollectionList()
	{
		try
		{
			FileOutputStream file = new FileOutputStream(COLLECTION_LIST_PATH);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(collectionList);
			out.close();
			file.close();
		}
		catch (Exception e)
		{
			// TODO
			System.out.println(e.getMessage());
		}
		
	}


	
	private static void loadCollectionList()
	{	
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
	}
	
}

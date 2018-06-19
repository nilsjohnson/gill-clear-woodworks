package com.gillccwoodworks;

import javax.servlet.ServletConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
{ "/index", "" })
public class IndexServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final String CAROUSEL_FILE_PATH = Constants.HOME + "Carousel.dat";
	public static UUID carouselId;


	public IndexServlet()
	{
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		NilsHtmlEngine engine = new NilsHtmlEngine(this.getServletContext());
		response.getWriter().append(engine.getHomePage());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	
	
	public static void setCarouselCollectionId(UUID id)
	{
		try
		{
			FileOutputStream file = new FileOutputStream(CAROUSEL_FILE_PATH);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(id);
			out.close();
			file.close();
			carouselId = id;	
		}
		catch(Exception e)
		{
			// TODO
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * @return The id of the current collection set as the carousel. Null if not set.
	 */
	public static UUID getCarouselCollectionID()
	{
		if(carouselId != null)
		{
			return carouselId;
		}
		else
		{
			
			 try
		        {   
		            // Reading the object from a file
		            FileInputStream file = new FileInputStream(CAROUSEL_FILE_PATH);
		            ObjectInputStream in = new ObjectInputStream(file);
		             
		            // Method for deserialization of object
		            UUID id = (UUID)in.readObject();
		             
		            in.close();
		            file.close();
		            
		            carouselId = id;
		     
		        }
		         
		        catch(IOException ex)
		        {
		        	File file = new File(CAROUSEL_FILE_PATH);
					if (!file.exists())
					{
						System.out.println("Unable to load carousuel id. File does not exist.");
					}
		        }
		         
		        catch(ClassNotFoundException ex)
		        {
		            System.out.println(ex.getMessage());
		        }
		}
		return carouselId;
	}
	
}

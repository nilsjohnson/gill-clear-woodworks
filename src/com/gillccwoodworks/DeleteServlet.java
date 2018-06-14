package com.gillccwoodworks;

import java.io.IOException;
import java.util.regex.PatternSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.ImageDAO;



@WebServlet("/delete")
public class DeleteServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public DeleteServlet()
	{
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		response.sendRedirect("");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().append("Delete Request: " + request.getParameter("rowName"));
		/*try
		{
			ImageDAO imageDAO = new ImageDAO(this.getServletContext().getContextPath());
			
			// if the request was to delete an entire row
			if(request.getParameter("rowName") != null)
			{
				imageDAO.deleteGallery(request.getParameter("rowName"));
				response.sendRedirect("admin");
			}
			// if the request was to delete a main slider image
			else if (request.getParameter("rowNameSlider") != null)
			{
				String imageToDelete = request.getParameter("rowNameSlider");
				ImageGallery carosuelGallery = imageDAO.getGallery(Constants.CAROUSEL_TITLE);
				
				carosuelGallery.deleteImage(imageToDelete);
				
				imageDAO.insertOrRelaceGallery(carosuelGallery);				
				
			}
			// if the request was 'bumpRight
			else if (request.getParameter("bumpRight") != null)
			{
				String galleryAndPath = request.getParameter("bumpRight");
				try
				{
					String[] params = galleryAndPath.split("%");
					
					ImageGallery gallery = imageDAO.getGallery(params[0]);
					gallery.bumpRight(params[1]);
					imageDAO.insertOrRelaceGallery(gallery);
				}
				catch (PatternSyntaxException ex)
				{
					ex.printStackTrace();
				}
			}
			//if the request was "bumpLeft"
			else if (request.getParameter("bumpLeft") != null)
			{
				String galleryAndPath = request.getParameter("bumpLeft");
				try
				{
					String[] params = galleryAndPath.split("%");
					
					ImageGallery gallery = imageDAO.getGallery(params[0]);
					gallery.bumpLeft(params[1]);
					imageDAO.insertOrRelaceGallery(gallery);
				}
				catch (PatternSyntaxException ex)
				{
					ex.printStackTrace();
				}
			}
			response.sendRedirect("admin");
		}
		catch(Exception e)
		{
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}*/
		

	}

}

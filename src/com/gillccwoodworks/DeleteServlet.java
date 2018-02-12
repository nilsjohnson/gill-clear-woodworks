package com.gillccwoodworks;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.ImageDAO;
import data.ImageGallery;


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
		try
		{
			ImageDAO imageDAO = new ImageDAO();
			
			
			if(request.getParameter("rowName") != null)
			{
				//System.out.println("Deleting a gallery. Row name: " + request.getParameter("rowName"));
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
				response.sendRedirect("admin");
			}
		}
		catch(Exception e)
		{
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}

	}

}

package com.gillccwoodworks;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Delete
 */
@WebServlet("/delete")
public class Delete extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Delete()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().append("Delete Post Request: " + request.getParameter("rowName"));
		try
		{
			ImageDAO imageDAO = new ImageDAO(this.getServletContext());
			
			
			if(request.getParameter("rowName") != null)
			{
				//System.out.println("Deleting a gallery. Row name: " + request.getParameter("rowName"));
				imageDAO.deleteGalleryRow(request.getParameter("rowName"));
				response.sendRedirect("admin");
			}
			else if (request.getParameter("rowNameSlider") != null)
			{
				//System.out.println("Request to delete: " + request.getParameter("rowNameSlider"));
				String imageToDelete = request.getParameter("rowNameSlider");
				imageDAO.deleteCarouselImage(imageToDelete);
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

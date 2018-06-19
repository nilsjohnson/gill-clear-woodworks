package admin;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Collection;
import data.ImageDAO;

/**
 * Servlet implementation class BumpLeftServlet
 */
@WebServlet("/BumpLeftServlet")
public class BumpLeftServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BumpLeftServlet()
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
		try
		{
			ImageDAO imageDAO = new ImageDAO();
			UUID imgId = UUID.fromString(request.getParameter("img-id"));
			UUID collectionId = UUID.fromString(request.getParameter("collection-id"));
			
			Collection col = imageDAO.getCollection(collectionId);
			col.bumpLeft(imgId);
			
			imageDAO.updateCollection(col);
			response.sendRedirect("admin");
			
		}
		catch(Exception e)
		{
			response.getWriter().append("Error: " + e.getMessage());
		}
	}

}

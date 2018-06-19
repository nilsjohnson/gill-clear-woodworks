package admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gillccwoodworks.GalleryServlet;
import com.gillccwoodworks.IndexServlet;

import data.ImageDAO;

/**
 * Servlet implementation class DeleteCollectionServlet
 */
@WebServlet("/DeleteCollectionServlet")
public class DeleteCollectionServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteCollectionServlet()
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
			UUID uuid = UUID.fromString(request.getParameter("collection-id"));
			// remove from DB
			imageDAO.deleteCollection(uuid);
			// remove from gallery page
			GalleryServlet.removeCollection(uuid);
			// remove from main carousel if necessary
			if(IndexServlet.getCarouselCollectionID() != null &&
					IndexServlet.getCarouselCollectionID().equals(uuid))
			{
				IndexServlet.setCarouselCollectionId(null);
			}
			response.sendRedirect("admin");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.getWriter().append("Error Deleting Collection: " + e.getMessage());
		}
		
	}

}

package admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.ImageDAO;

/**
 * Servlet implementation class UpdateTitleServlet
 */
@WebServlet("/UpdateTitleServlet")
public class UpdateTitleServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateTitleServlet()
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
			ImageDAO dao = new ImageDAO();
			String idStr = request.getParameter("collection-id");
			UUID uuid = UUID.fromString(idStr);
			String newTitle = request.getParameter("new-title");
			
			dao.upldateCollectionTitle(newTitle, uuid);
			response.sendRedirect("admin");
		}
		catch (Exception e)
		{
			response.getWriter().append("Error: " + e.getMessage());
		}
	}

}

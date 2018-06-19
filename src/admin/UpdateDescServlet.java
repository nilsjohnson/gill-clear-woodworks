package admin;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.ImageDAO;

/**
 * Servlet implementation class UpdateDescServlet
 */
@WebServlet("/UpdateDescServlet")
public class UpdateDescServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateDescServlet()
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
			String newDesc = request.getParameter("new-desc");

			
			dao.upldateCollectionDesc(newDesc, uuid);
			response.sendRedirect("admin");
		}
		catch (Exception e)
		{
			response.getWriter().append("Error: " + e.getMessage());
		}
	}

}

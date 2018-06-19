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
 * Servlet implementation class DeleteImageServlet
 */
@WebServlet("/DeleteImageServlet")
public class DeleteImageServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteImageServlet()
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
			UUID uuid = UUID.fromString(request.getParameter("image-id"));
			if(uuid != null)
			{
				imageDAO.deleteImage(uuid);
			}
			response.sendRedirect("admin");
			
			
		}
		catch (Exception e)
		{
			response.getWriter().append(e.getMessage());
		}
	}

}

package com.gillccwoodworks;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Admin
 */
@WebServlet("/Admin")
public class Admin extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Admin()
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
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if(!username.equals("sam") && !password.equals("pass"))
		{
			response.sendRedirect("gallery");
		}
		
		response.getWriter().append("<form method=\"post\" enctype=\"multipart/form-data\">\r\n" + 
				" <div>\r\n" + 
				"   <label for=\"file\">Choose file to upload</label>\r\n" + 
				"   <input type=\"file\" id=\"file\" name=\"file\" multiple>\r\n" + 
				" </div>\r\n" + 
				" <div>\r\n" + 
				"   <button>Submit</button>\r\n" + 
				" </div>\r\n" + 
				"</form>");
	}

}

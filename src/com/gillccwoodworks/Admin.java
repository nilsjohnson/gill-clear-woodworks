package com.gillccwoodworks;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Admin
 */
@WebServlet("/admin")
public class Admin extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private NilsHtmlEngine engine;
	private ServletContext servletContext;

	// cookies expire in 10 minutes
	private static final int EXP_DATE = 60 * 10;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Admin()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		// TODO Auto-generated method stub
		super.init(config);
		servletContext = config.getServletContext();
		ServletContext context = this.getServletContext();
		engine = new NilsHtmlEngine(context);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = null;
		String password = null;

		Cookie[] cookies = request.getCookies();

		if (cookies != null)
		{
			System.out.println("cookies were not null");
			for (Cookie cookie : cookies)
			{
				System.out.println("cookie name: " + cookie.getName());

				if (cookie.getName().equals("username"))
				{
					username = cookie.getValue();
				}
				if (cookie.getName().equals("password"))
				{
					password = cookie.getValue();
				}
			}

			if (username == null || password == null)
			{
				username = request.getParameter("username");
				password = request.getParameter("password");
			}

			if (username == null || password == null || !isValidUser(username, password))
			{
				response.sendRedirect("index");
				return;
			}
			// security is not a priority.
			Cookie usernameCookie = new Cookie("username", username);
			Cookie passwordCookie = new Cookie("password", password);

			// set cookies' max life. keepin' it short, since security is important.
			usernameCookie.setMaxAge(EXP_DATE);
			passwordCookie.setMaxAge(EXP_DATE);

			// add cookies
			response.addCookie(usernameCookie);
			response.addCookie(passwordCookie);
		} else
		{
			System.out.println("cookies were null");

			username = request.getParameter("username");
			password = request.getParameter("password");

			if (username == null || password == null || !isValidUser(username, password))
			{
				response.sendRedirect("index");
				return;
			}

			// security is not a priority.
			Cookie usernameCookie = new Cookie("username", username);
			Cookie passwordCookie = new Cookie("password", password);

			// set cookies' max life. keepin' it short, since security is important.
			usernameCookie.setMaxAge(EXP_DATE);
			passwordCookie.setMaxAge(EXP_DATE);

			// add cookies
			response.addCookie(usernameCookie);
			response.addCookie(passwordCookie);
		}

		String uploadHtml = engine.getUploadPage();

		response.getWriter().append(uploadHtml);
	}

	private boolean isValidUser(String username, String password)
	{
		// only work for this hardcoded user.
		if (!username.equals("sam") || !password.equals("pass"))
		{
			return false;
		} else
		{
			return true;
		}
	}
}

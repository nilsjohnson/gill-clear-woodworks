package com.gillccwoodworks;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Gallery
 */
@WebServlet("/gallery")
public class Gallery extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private ServletContext servletContext;
	private NilsHtmlEngine engine;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Gallery()
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
		//generate and display the head
		response.getWriter().append(engine.getHead("Gill Country Clear Wood Works - Gallery", "Handcrafted Woodwork"));
		
		// get the navbar, set this page to active and display
		String navBar = engine.getNavbar();
		response.getWriter().append(navBar);
		
		// generate and display the body
		response.getWriter().append(engine.getGalleryBody());
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

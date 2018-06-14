package com.gillccwoodworks;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/gallery")
public class GalleryServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private NilsHtmlEngine engine;

	public GalleryServlet()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		engine = new NilsHtmlEngine(this.getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//generate and display the head
		response.getWriter().append(engine.getHead("Gill Country Clear Wood Works - Gallery", "Handcrafted Woodwork"));
		
		// get the navbar, set this page to active and display
		String navBar = engine.getNavbar();
		response.getWriter().append(navBar);
		
		// generate and display the body
		//response.getWriter().append(engine.getGalleryBody());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.sendRedirect("index");
	}
}

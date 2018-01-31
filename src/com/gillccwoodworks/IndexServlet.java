package com.gillccwoodworks;

import java.io.BufferedReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Index
 */
@WebServlet({"/index", ""})
public class IndexServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private ServletContext context;
	private String rootPath;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IndexServlet()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		// TODO Auto-generated method stub
		super.init(config);
		context = config.getServletContext();
		rootPath = context.getRealPath("/");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{	
		NilsHtmlEngine engine = new NilsHtmlEngine(context);
		response.getWriter().append(engine.getHomePage());
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

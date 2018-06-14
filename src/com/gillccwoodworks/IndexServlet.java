package com.gillccwoodworks;

import javax.servlet.ServletConfig;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
{ "/index", "" })
public class IndexServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public IndexServlet()
	{
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		NilsHtmlEngine engine = new NilsHtmlEngine(this.getServletContext());
		response.getWriter().append(engine.getHomePage());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}

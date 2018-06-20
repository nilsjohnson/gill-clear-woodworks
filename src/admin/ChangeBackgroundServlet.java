package admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.gillccwoodworks.Constants;

import data.ImageDAO;
import util.FileUtil;
import util.ImageUtil;

/**
 * Servlet implementation class ChangeBackgroundServlet
 */
@MultipartConfig
@WebServlet("/ChangeBackgroundServlet")
public class ChangeBackgroundServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangeBackgroundServlet()
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
			Part filePart = request.getPart("background-file");

			String fileName = filePart.getSubmittedFileName();
			String ext = fileName.substring(fileName.lastIndexOf('.') + 1);

			if (ext.equals("jpg") || ext.equals("jpeg"))
			{
				//FileUtil.saveFile(filePart, this.getServletContext().getRealPath("") + "/img/background.jpg");
				
				// save to home directory
				FileUtil.saveFile(filePart);
				
				// load it from home directory
				BufferedImage image = ImageIO.read(new File(Constants.HOME + filePart.getSubmittedFileName()));
				
				// crop
				image = ImageUtil.cropImage(image, Constants.DEFAULT_RATIO);
				
				// resize
				int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
				image = ImageUtil.resizeImage(image, type, Constants.WIDTH, Constants.HEIGHT);
				// save over original name
				ImageUtil.writeImageToFile(image, this.getServletContext().getRealPath("") + "/img/background.jpg");
				
				FileUtil.deleteFile(Constants.HOME + filePart.getSubmittedFileName());
				
				response.sendRedirect("admin");
			}
			else
			{
				throw new Exception("Image must be a have extension of '.jpg' or '.jpeg'.");
			}
		}
		catch (Exception e)
		{
			response.getWriter().append("Problem updating background image: " + e.getMessage());
		}
	}

}



package com.gillccwoodworks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import util.FileUtil;
import util.ImageUtil;
import data.Collection;
import data.Image;
import data.ImageDAO;


@WebServlet("/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet
{
	private final String BUCKET_NAME = "forestfriends";
	private ImageDAO imageDAO;

	public FileUploadServlet()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.sendRedirect("gallery");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// to hold the urls and UUIDs of the new images
		ArrayList<Image> imageList = new ArrayList<>();

		try
		{	
			imageDAO = new ImageDAO();

			// Retrieves <input type="text" name="gallery_title">
			String galleryTitle = request.getParameter("gallery_title");
			
			// Retrieves <textarea type="input" name="gallery_description" id="gallery_description">
			String galleryDesc = request.getParameter("gallery_description");
			

			// Retrieves <input type="file" name="file" multiple="true">
			List<Part> filePartList = request.getParts()
					.stream()
					.filter(part -> "file".equals(part.getName()))
					.collect(Collectors.toList());
			
			if(filePartList.get(0).getSize() == 0)
			{
				throw new Exception("No images selected.");
			}
			
			
			for (Part filePart : filePartList)
			{	
				// temp save image
				FileUtil.saveFile(filePart);
				
				// load it as an image
				BufferedImage image = ImageIO.read(new File(Constants.HOME + filePart.getSubmittedFileName()));
				// crop
				image = ImageUtil.cropImage(image, Constants.DEFAULT_RATIO);
				int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
				// resize
				image = ImageUtil.resizeImage(image, type, Constants.WIDTH, Constants.HEIGHT);
				// save over original name
				ImageUtil.writeImageToFile(image, Constants.HOME + filePart.getSubmittedFileName());
				
				// upload to aws  bucketfrom where it was just saved
				upload(Constants.HOME + filePart.getSubmittedFileName(), filePart.getSubmittedFileName());
				
				// delete file now that its in the aws bucket
				FileUtil.deleteFile(Constants.HOME + filePart.getSubmittedFileName());
				
				// add that to the list to make the gallery item
				String imgUrl = "https://s3.amazonaws.com/" + BUCKET_NAME + "/" + filePart.getSubmittedFileName();
				imageList.add(new Image(imgUrl));
				
				response.getWriter().append(filePart.getSubmittedFileName() + " uploaded!\n\n");
			}
			
			Collection collection = new Collection(galleryTitle, galleryDesc, imageList);
			imageDAO.insertCollection(collection);
	

			response.sendRedirect("admin");
		}
		catch (SQLException e)
		{
			response.getWriter().append(e.getMessage());
			e.printStackTrace();
		}
		catch (AmazonServiceException ase)
		{
			ase.printStackTrace();
			// not sure why it has two messages?
			response.getWriter().append(ase.getErrorMessage());
			response.getWriter().append(ase.getMessage());
		}
		catch (AmazonClientException ace)
		{
			ace.printStackTrace();
			response.getWriter().append(ace.getMessage());
		}
		catch (Exception e)
		{
			response.getWriter().append(e.getMessage());
			e.printStackTrace();
		}
	}

	private void upload(String filePath, String fileName) throws AmazonServiceException
	{

		File file = new File(filePath);
		
		BasicAWSCredentials creds = Constants.getAwsCreds(this.getServletContext());
		AmazonS3 s3Client = AmazonS3ClientBuilder.
				standard().
				withCredentials(new AWSStaticCredentialsProvider(creds))
				.withRegion("us-east-1")
				.build();

		s3Client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file));

	}
	
	
	
}

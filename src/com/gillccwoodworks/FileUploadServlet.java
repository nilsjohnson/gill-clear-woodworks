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

import data.ImageGallery;
import util.ImageUtil;
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
		// to hold the urls of the new images
		List<String> urlList = new ArrayList<>();

		try
		{	
			imageDAO = new ImageDAO(this.getServletContext());

			// Retrieves <input type="text" name="description">
			String galleryTitle = request.getParameter("description");

			// Retrieves <input type="file" name="file" multiple="true">
			List<Part> filePartList = request.getParts()
					.stream()
					.filter(part -> "file".equals(part.getName()))
					.collect(Collectors.toList());
			
			// validate input
			if(filePartList.size() > Constants.MAX_GALLERY_SIZE)
			{
				throw new Exception("A gallery cannot contain more than 6 images.");
			}
			if(galleryTitle.equals(""))
			{
				throw new Exception("Please enter a gallery title.");
			}
			if(filePartList.get(0).getSize() == 0)
			{
				throw new Exception("No images selected.");
			}
			
			
			for (Part filePart : filePartList)
			{	
				// temp save image
				saveFile(filePart);
				
				// load it as an image
				BufferedImage image = ImageIO.read(new File(Constants.getHome(this.getServletContext()) + filePart.getSubmittedFileName()));
				// crop
				image = ImageUtil.cropImage(image, Constants.DEFAULT_RATIO);
				int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
				// resize
				image = ImageUtil.resizeImage(image, type, Constants.WIDTH, Constants.HEIGHT);
				// save over original name
				ImageUtil.writeImageToFile(image, Constants.getHome(this.getServletContext()) + filePart.getSubmittedFileName());
				
				// upload to aws  bucketfrom where it was just saved
				upload(Constants.getHome(this.getServletContext()) + filePart.getSubmittedFileName(), filePart.getSubmittedFileName());
				
				// delete file now that its in the aws bucket
				deleteFile(filePart.getSubmittedFileName());
				
				// add that to the list to make the gallery item
				String imgUrl = "https://s3.amazonaws.com/" + BUCKET_NAME + "/" + filePart.getSubmittedFileName();
				urlList.add(imgUrl);
				
				response.getWriter().append(filePart.getSubmittedFileName() + " uploaded!\n\n");

			}

			// if it wasn't flagged as a carousel image upload, make a new gallery
			if (!galleryTitle.equals(Constants.CAROUSEL_TITLE))
			{
				String[] imgArr = new String[urlList.size()];

				for (int i = 0; i < urlList.size(); i++)
				{
					imgArr[i] = urlList.get(i);
				}

				ImageGallery gallery = new ImageGallery(galleryTitle, "Dummy Description", imgArr);
				imageDAO.insertOrRelaceGallery(gallery);
			}
			// otherwise it must be the carousel
			else if (galleryTitle.equals(Constants.CAROUSEL_TITLE))
			{
				// add the new images to the gallery, then upload
				ImageGallery carouselGallery = imageDAO.getGallery(Constants.CAROUSEL_TITLE);

				if (carouselGallery == null)
				{
					carouselGallery = new ImageGallery(Constants.CAROUSEL_TITLE, null, new String[0]);
				}

				for (String path : urlList)
				{
					carouselGallery.addImage(path);
				}
	
				imageDAO.insertOrRelaceGallery(carouselGallery);
			}

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
	
	private void saveFile(Part filePart)
	{
		InputStream input = null;
		FileOutputStream output = null;
		
		// write file to destination
		try
		{
			input = filePart.getInputStream();
			output = new FileOutputStream(Constants.getHome(this.getServletContext()) + filePart.getSubmittedFileName());
			byte[] buf = new byte[1024];
			int bytesRead;

			while ((bytesRead = input.read(buf)) > 0)
			{
				output.write(buf, 0, bytesRead);
			}

			input.close();
			output.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			input = null;
			output = null;
		}
	}
	
	private void deleteFile(String path) throws IOException
	{
		File file = new File(Constants.getHome(this.getServletContext()) + path);
		Files.deleteIfExists(file.toPath());
	}
}

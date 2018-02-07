package com.gillccwoodworks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import data.ImageGallery;
import data.ImageDAO;

/**
 * Servlet implementation class FileUploadServlet
 */

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

		// instantiate the DAO
		try
		{
			imageDAO = new ImageDAO();
		}
		catch (SQLException e)
		{
			response.getWriter().append(e.getMessage());
			e.printStackTrace();
		}

		// Retrieves <input type="text" name="description">
		String galleryTitle = request.getParameter("description");

		// Retrieves <input type="file" name="file" multiple="true">
		List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName()))
				.collect(Collectors.toList());

		if (!galleryTitle.equals(Constants.CAROUSEL_TITLE) && fileParts.size() > Constants.MAX_GALLERY_SIZE)
		{
			response.getWriter().append("Please Only Upload " + Constants.MAX_GALLERY_SIZE + " Images Per Gallery.");
		}
		else
		{

			for (Part filePart : fileParts)
			{
				try
				{
					System.out.println("Uploading: " + filePart.getSubmittedFileName());
					response.getWriter().append("Uploading " + filePart.getSubmittedFileName() + " \n");

					// upload
					upload(filePart);
					// get the image url
					// https://s3.amazonaws.com/forestfriends/train2.jpeg
					String imgUrl = "https://s3.amazonaws.com/" + BUCKET_NAME + "/" + filePart.getSubmittedFileName();
					// add that to the list to make the gallery item
					urlList.add(imgUrl);

					response.getWriter().append(filePart.getSubmittedFileName() + " uploaded!\n\n");

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
					response.getWriter().append("Something really unexpected happened, talk to nils");
				}
			}

			// if it wasn't flagged as a carousel image upload, make a new gallery
			if (!galleryTitle.equals(Constants.CAROUSEL_TITLE))
			{

				// try to retrieve this gallery to update
				ImageGallery gallery = imageDAO.getGallery(galleryTitle);
				if (gallery != null)
				{
					for (String path : urlList)
					{
						gallery.addImage(path);
					}
					imageDAO.updateGalleryImages(gallery);

				}
				// otherwise makea new one
				else
				{
					ImageGallery newGallery = new ImageGallery(galleryTitle, urlList);
					imageDAO.addGallery(newGallery);
				}
			}
			// otherwise it must be the carousel
			else if (galleryTitle.equals(Constants.CAROUSEL_TITLE))
			{
				System.out.println("This is a Front page Slider Image!");
				for (String url : urlList)
				{
					imageDAO.appendToGallery(Constants.CAROUSEL_TITLE, url);
				}
			}

			response.getWriter().append("\n\n-------Testing DB-------\n\n");

			List<ImageGallery> galleryItemList = imageDAO.getAllGalleries();

			for (ImageGallery itemDB : galleryItemList)
			{
				response.getWriter().append(itemDB.toString() + "\n\n");
			}

			response.sendRedirect("admin");
		}

	}

	private void upload(Part filePart) throws IOException, AmazonServiceException
	{
		String fileName = filePart.getSubmittedFileName();
		InputStream input = filePart.getInputStream();
		long size = filePart.getSize();

		BasicAWSCredentials creds = Constants.getAwsCreds(this.getServletContext());

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
				.withRegion("us-east-1").build();

		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setContentLength(size);
		metaData.setContentType("image");

		s3Client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, input, metaData));
		// TODO ImageMagick
	}
}

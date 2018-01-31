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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

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
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("gallery");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// to hold the urls of the new images
		List<String> urlList = new ArrayList<>();

		// instantiate the DAO
		try {
			imageDAO = new ImageDAO(this.getServletContext());
		} catch (SQLException e) {
			response.getWriter().append(e.getMessage());
			e.printStackTrace();
		}

		response.getWriter().append("Upload processing...\n\n");

		// Retrieves <input type="text" name="description">
		String galleryTitle = request.getParameter("description");

		response.getWriter().append("This gallery is called " + galleryTitle + "\n\n");

		// Retrieves <input type="file" name="file" multiple="true">
		List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName()))
				.collect(Collectors.toList());

		for (Part filePart : fileParts) {
			try {
				response.getWriter().append("Uploading " + filePart.getSubmittedFileName() + " \n");

				// upload
				upload(filePart);
				// get the image url
				// https://s3.amazonaws.com/forestfriends/train2.jpeg
				String imgUrl = "https://s3.amazonaws.com/" + BUCKET_NAME + "/" + filePart.getSubmittedFileName();
				// add that to the list to make the gallery item
				urlList.add(imgUrl);

				response.getWriter().append(filePart.getSubmittedFileName() + " uploaded!\n\n");

			} catch (AmazonServiceException ase) {
				ase.printStackTrace();
				// not sure why it has two messages?
				response.getWriter().append(ase.getErrorMessage());
				response.getWriter().append(ase.getMessage());
			} catch (AmazonClientException ace) {
				ace.printStackTrace();
				response.getWriter().append(ace.getMessage());
			} catch (Exception e) {
				response.getWriter().append("Something really unexpected happened, talk to nils");
			}
		}

		if (!galleryTitle.equals("CAROUSEL_UPLOAD")) {
			GalleryItem item = new GalleryItem(galleryTitle, urlList);
			imageDAO.insertGallery(item);

			response.getWriter().append(item.toString());
		} else {
			System.out.println("This is a Front page Slider Image!");
			for (String url : urlList) {
				imageDAO.addCarouselImage(galleryTitle, url);
			}
		}

		response.getWriter().append("\n\n-------Testing DB-------\n\n");

		List<GalleryItem> galleryItemList = imageDAO.getAllGalleries();

		for (GalleryItem itemDB : galleryItemList) {
			response.getWriter().append(itemDB.toString() + "\n\n");
		}

		response.sendRedirect("admin");

	}

	private void upload(Part filePart) throws IOException, AmazonServiceException {
		String fileName = filePart.getSubmittedFileName();
		InputStream input = filePart.getInputStream();
		long size = filePart.getSize();

		// TODO not this
		BasicAWSCredentials creds = FileUploadServlet.getAwsCreds(this.getServletContext());
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
				.withRegion("us-east-1").build();

		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setContentLength(size);
		metaData.setContentType("image");

		s3Client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, input, metaData));
		// TODO ImageMagick
	}

	public static BasicAWSCredentials getAwsCreds(ServletContext context) {
		String accessKey = null;
		String secretKey = null;

		String path = context.getRealPath("/") + "WEB-INF/" + "aws_cred.txt";
		
		File file = new File(path);
		System.out.print(file.getAbsolutePath());
		try {
			Scanner scanner = new Scanner(file);

			accessKey = scanner.nextLine();
			secretKey = scanner.nextLine();
			
			scanner.close();

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new BasicAWSCredentials(accessKey, secretKey);
	}
}

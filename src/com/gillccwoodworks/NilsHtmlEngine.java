package com.gillccwoodworks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import data.CarouselItem;
import data.GalleryItem;
import data.ImageDAO;

public class NilsHtmlEngine
{
	private ServletContext context;
	ImageDAO imageDAO = null;

	public NilsHtmlEngine(ServletContext context)
	{
		this.context = context;
		try
		{
			imageDAO = new ImageDAO();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getGalleryBody()
	{
		String galleryBody = new String(getHtmlTemplate("gallery_body.html"));
		StringBuilder galleryItems = new StringBuilder("");

		ArrayList<GalleryItem> galleriesList = getGalleryList();

		// for each gallery item, generate the html
		for (GalleryItem item : galleriesList)
		{
			// template for each gallery item
			String galleryTemplate = getHtmlTemplate("gallery_item.html");

			int colSize = 12 / item.getImages().length;
			String title = item.getTitle();
			String id = title.replace(" ", "_");
			id = id.toLowerCase();

			// set the title and primary image
			galleryTemplate = galleryTemplate.replace("TITLE", item.getTitle());
			galleryTemplate = galleryTemplate.replace("PRIMARY_IMAGE", item.getImages()[0]);
			galleryTemplate = galleryTemplate.replace("ID", id);

			StringBuilder thumbnails = new StringBuilder("");
			// for each image in the item,
			for (int i = 0; i < item.getImages().length; i++)
			{
				// template for each image div
				String str = getHtmlTemplate("gallery_thumb.html");
				str = str.replaceAll("ID", id);
				str = str.replaceAll("COL_SIZE", Integer.toString(colSize));
				str = str.replaceAll("SOURCE", item.getImages()[i]);
				// add this string as a thumbnail image
				thumbnails.append(str + "\n");
			}
			// add to the bigger string
			galleryTemplate = galleryTemplate.replace("THUMBNAILS", thumbnails.toString());
			galleryItems.append(galleryTemplate);
		}

		galleryBody = galleryBody.replace("GALLERY", galleryItems.toString());

		return galleryBody.toString();
	}

	private ArrayList<GalleryItem> getGalleryList()
	{		
		ArrayList<GalleryItem> galleryList = imageDAO.getAllGalleries();
				
		return galleryList;
	}

	public String getHead(String title, String description)
	{
		String output = getHtmlTemplate("head.html");

		output = output.replace("TITLE", title);
		output = output.replace("DESCRIPTION", description);

		return output;
	}

	public String getNavbar()
	{
		return getHtmlTemplate("navbar.html");
	}

	public String getHtmlTemplate(String fileName)
	{
		String output = null;

		String path = context.getRealPath("/") + "WEB-INF/html/" + fileName;

		try (BufferedReader br = new BufferedReader(new FileReader(path)))
		{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null)
			{
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			output = sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return output;
	}

	public String getUploadPage()
	{
		String output = getHtmlTemplate("image_upload_body.html");
		StringBuilder galleryTableData = new StringBuilder("");
		StringBuilder carouselTableData = new StringBuilder("");
		ArrayList<GalleryItem> galleryItemList = null;

		String noUploadAddress = "img//no_uploaded.png";

		// get the galleries table, generate html table, and put in output
		galleryItemList = imageDAO.getAllGalleries();
		
		for(GalleryItem item: galleryItemList)
		{
			String galleryRowTemplate = getHtmlTemplate("gallery_table_row.html");
			int numImages = item.getImages().length;
			
			galleryRowTemplate = galleryRowTemplate.replaceAll("TITLE", item.getTitle());
			
			galleryRowTemplate = galleryRowTemplate.replace("IMG_1", (numImages >= 1 ? item.getImages()[0] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_2", (numImages >= 2 ? item.getImages()[1] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_3", (numImages >= 3 ? item.getImages()[2] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_4", (numImages >= 4 ? item.getImages()[3] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_5", (numImages >= 5 ? item.getImages()[4] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_6", (numImages == 6 ? item.getImages()[5] : noUploadAddress));
			
			galleryTableData.append(galleryRowTemplate + "\n");
		}
		
		output = output.replace("CURRENT_GALLERY_IMAGES_TABLE", galleryTableData);
		
		// get the carosuel table, generate html table and put in output
		
		CarouselItem[] carouselitems = imageDAO.getAllCarouselImages();
		
		for(CarouselItem item : carouselitems)
		{
			String carouselRowTemplate = getHtmlTemplate("carousel_image_row.html");
			
			carouselRowTemplate = carouselRowTemplate.replace("TITLE", item.getCarousel());
			carouselRowTemplate = carouselRowTemplate.replaceAll("IMG", item.getAddress());
			
			carouselTableData.append(carouselRowTemplate + "\n");
		}
		
		output = output.replace("CAROUSEL_IMAGES_TABLE", carouselTableData);
		
		return output;
	}

	public String getHomePage()
	{
		String output = getHtmlTemplate("index.html");
		StringBuilder indicatorHtml = new StringBuilder("");
		StringBuilder imageHtml = new StringBuilder("");
		
		CarouselItem[] carouselImages = imageDAO.getAllCarouselImages();
		
		for(int i = 0; i < carouselImages.length; i++)
		{
			// get the item
			CarouselItem item = carouselImages[i];
			// get the template for the <img src=...> tag
			String carImgTemplate = getHtmlTemplate("carousel_image.html");
			// get the template for the carousel indicatior tag 
			String carIndicatorTag = getHtmlTemplate("carousel_indicator.html");
			
			carImgTemplate = carImgTemplate.replace("IMG", item.getAddress());
			if(i > 0)
			{
				carImgTemplate = carImgTemplate.replace("active", "");
			}
			imageHtml = imageHtml.append(carImgTemplate + "\n");
			
			carIndicatorTag = carIndicatorTag.replaceAll("NUM", Integer.toString(i));
			if(i == 0)
			{
				carIndicatorTag = carIndicatorTag.replace("class=\"\"", "class=\"active\"");
			}
			indicatorHtml = indicatorHtml.append(carIndicatorTag + "\n");
		}
		
		output = output.replace("INDICATORS", indicatorHtml.toString());
		output = output.replace("IMAGES", imageHtml.toString());
		
		return output;
	}
}

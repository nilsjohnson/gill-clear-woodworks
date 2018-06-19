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

import data.Collection;
import data.Image;
import data.ImageDAO;

public class NilsHtmlEngine
{
	private String servletPath;
	private ImageDAO imageDAO = null;

	public NilsHtmlEngine(ServletContext context)
	{
		try
		{
			servletPath = context.getRealPath("/");
			imageDAO = new ImageDAO();
		}
		catch (SQLException e)
		{
			System.out.println("DAO was not instanciated.");
			e.printStackTrace();
		}
	}



	/**
	 * makes the general head that this site uses.
	 * 
	 * @param title
	 *            - what goes in the browser's title bar
	 * @param description
	 *            - page meta content. SEO buzzwords here.
	 * @return
	 */
	public String getHead(String title, String description)
	{
		String output = getHtmlTemplate("head.html");

		output = output.replace("TITLE", title);
		output = output.replace("DESCRIPTION", description);

		return output;
	}

	/**
	 * @return navigation bar for each page.
	 */
	public String getNavbar()
	{
		return getHtmlTemplate("navbar.html");
	}

	/**
	 * 
	 * @param fileName
	 *            - name of the html template file.
	 * @return string containing the html in that file.
	 */
	public String getHtmlTemplate(String fileName)
	{
		String output = null;

		String path = servletPath + "WEB-INF/html/" + fileName;

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

	/**
	 * @return the html for FileUploadServlet.java
	 */
	public String getUploadPage()
	{
		// outermost template
		String output = getHtmlTemplate("admin/admin_body.html");
		ArrayList<Collection> collectionList;
		
		// get the images
		try
		{
			collectionList = imageDAO.getAllCollections();
		}
		catch (Exception e)
		{
			return "Problem retrieving collection: " + e.getMessage();
		}
		
		StringBuilder allCollectionsHtml = new StringBuilder();
		
		System.out.println("number of collections: " + collectionList.size());
		
		for(Collection curCollection : collectionList)
		{
			// set title, desc, ids for buttons
			String collectionTemplate = getHtmlTemplate("admin/collection.html");
			collectionTemplate = collectionTemplate.replaceAll("COLLECTION_TITLE", curCollection.getTitle());
			collectionTemplate = collectionTemplate.replaceAll("COLLECTION_DESCRIPTION", curCollection.getDescription());
			collectionTemplate = collectionTemplate.replaceAll("COLLECTION_UUID", curCollection.getId().toString());
			
			StringBuilder curCollectionAllImagesHtml = new StringBuilder();
			for(int i = 0; i < curCollection.getNumberOfImages(); i++)
			{
				String imageDivHtml = getHtmlTemplate("admin/img_div.html");
				imageDivHtml = imageDivHtml.replace("IMAGE_PATH", curCollection.getImageAt(i).path);
				imageDivHtml = imageDivHtml.replaceAll("IMG_ID", curCollection.getImageAt(i).uuid.toString());
				curCollectionAllImagesHtml = curCollectionAllImagesHtml.append(imageDivHtml);
			}
			
			collectionTemplate = collectionTemplate.replace("IMAGES", curCollectionAllImagesHtml.toString());
			
			allCollectionsHtml = allCollectionsHtml.append(collectionTemplate);
		}
		
		output = output.replace("COLLECTIONS", allCollectionsHtml.toString());
		
		return output;
		
	}

	/**
	 * @return the html for the IndexServlet.java
	 */
	public String getHomePage()
	{
		String output = getHtmlTemplate("index.html");
		StringBuilder indicatorHtml = new StringBuilder("");
		StringBuilder imageHtml = new StringBuilder("");
		try
		{
			Collection carouselCollection = imageDAO.getCollection(Constants.CAROUSEL_UUID);

			if(carouselCollection != null)
			{
				for (int i = 0; i < carouselCollection.getNumberOfImages(); i++)
				{
					// get the item
					String path = carouselCollection.getImageAt(i).path;
					// get the template for the <img src=...> tag
					String carImgTemplate = getHtmlTemplate("carousel_image.html");
					// get the template for the carousel indicatior tag
					String carIndicatorTag = getHtmlTemplate("carousel_indicator.html");

					carImgTemplate = carImgTemplate.replace("IMG", path);
					if (i > 0)
					{
						carImgTemplate = carImgTemplate.replace("active", "");
					}
					imageHtml = imageHtml.append(carImgTemplate + "\n");

					carIndicatorTag = carIndicatorTag.replaceAll("NUM", Integer.toString(i));
					if (i == 0)
					{
						carIndicatorTag = carIndicatorTag.replace("class=\"\"", "class=\"active\"");
					}
					indicatorHtml = indicatorHtml.append(carIndicatorTag + "\n");
				}
			}
			output = output.replace("INDICATORS", indicatorHtml.toString());
			output = output.replace("IMAGES", imageHtml.toString());

			return output;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
	
	
	/*
	 StringBuilder allCollections = new StringBuilder();
		String collectionTemplate = "";
		for(Collection col : collectionList)
		{
			collectionTemplate = getHtmlTemplate("admin/collection.html");
			
			collectionTemplate = collectionTemplate.replace("COLLECTION_TITLE", col.getTitle());
			collectionTemplate = collectionTemplate.replace("COLLECTION_DESCRIPTION", col.getDescription());
			collectionTemplate = collectionTemplate.replace("GALLERY_ID", col.getId().toString());
			
			StringBuilder imgListHtml = new StringBuilder();
			int num = col.getNumberOfImages();
			for(int i = 0; i < num; i++)
			{
				String imageTemplate = getHtmlTemplate("admin/img_div.html");
				
				imageTemplate = imageTemplate.replace("IMAGE_PATH", col.getImageAt(i).path);
				imageTemplate = imageTemplate.replaceAll("IMG_ID", col.getImageAt(i).uuid.toString());
				
				imgListHtml = imgListHtml.append(imageTemplate);
				
			}
			
			collectionTemplate = collectionTemplate.replaceAll("IMAGES", imgListHtml.toString());
			allCollections = allCollections.append(collectionTemplate);
		}
		
		output = output.replace("COLLECTIONS", allCollections.toString());
		
		return output;
	 */
}

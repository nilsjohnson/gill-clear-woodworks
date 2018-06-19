package com.gillccwoodworks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
			
			StringBuilder stb = new StringBuilder();
			if(IndexServlet.carouselId != null && 
					IndexServlet.carouselId.equals(curCollection.getId()))
			{
				stb.append("<p>This collection the main page carousel.</p>");
			}
			if(GalleryServlet.getCollectionList() != null && 
					GalleryServlet.getCollectionList().contains(curCollection.getId()))
			{
				stb.append("<p>This collection is on the gallery page.</p>");
			}
			collectionTemplate = collectionTemplate.replace("NOTES", stb.toString());
			
			
			StringBuilder curCollectionAllImagesHtml = new StringBuilder();
			for(int i = 0; i < curCollection.getNumberOfImages(); i++)
			{
				String imageDivHtml = getHtmlTemplate("admin/img_div.html");
				imageDivHtml = imageDivHtml.replace("IMAGE_PATH", curCollection.getImageAt(i).path);
				imageDivHtml = imageDivHtml.replaceAll("IMG_ID", curCollection.getImageAt(i).uuid.toString());
				imageDivHtml = imageDivHtml.replaceAll("COLLECTION-ID", curCollection.getId().toString());
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
			

			if(IndexServlet.getCarouselCollectionID() != null)
			{
				Collection carouselCollection = imageDAO.getCollection(IndexServlet.getCarouselCollectionID());
				
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
				output = output.replace("INDICATORS", indicatorHtml.toString());
				output = output.replace("IMAGES", imageHtml.toString());
			}
			else
			{
				// so page shows without placeholders if no images present
				output = output.replace("INDICATORS", "");
				output = output.replace("IMAGES", "");	
			}
		

			return output;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}



	public String getGallery()
	{
		String output = getHtmlTemplate("gallery/gallery_page.html");
		StringBuilder collectionOutput = new StringBuilder();
		
		try
		{
			ArrayList<UUID> collectionIdList = GalleryServlet.getCollectionList();
		
			for(UUID id : collectionIdList)
			{
				String collectionTemplate = getHtmlTemplate("gallery/collection.html");

				Collection col = imageDAO.getCollection(id);
				collectionTemplate = collectionTemplate.replace("TITLE", col.getTitle());
				collectionTemplate = collectionTemplate.replace("DESCRIPTION", col.getDescription());
			
				// to hold the html for all images in this collection
				StringBuilder imagesHtml = new StringBuilder();
				for(int i = 0; i < col.getNumberOfImages(); i++)
				{
					String imgDivTemplate = getHtmlTemplate("gallery/img_div.html");
					imgDivTemplate = imgDivTemplate.replace("IMG_SOURCE", col.getImageAt(i).path);
					imagesHtml = imagesHtml.append(imgDivTemplate);
				}
				collectionTemplate = collectionTemplate.replaceAll("IMAGES", imagesHtml.toString());
				collectionOutput = collectionOutput.append(collectionTemplate);
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		output = output.replaceAll("COLLECTIONS", collectionOutput.toString());
		return output;
	}
	
}

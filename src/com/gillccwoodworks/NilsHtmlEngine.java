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

import data.ImageGallery;
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
		} catch (SQLException e)
		{
			System.out.println("DAO was not instanciated.");
			e.printStackTrace();
		}
	}

	/**
	 * Creates a 'gallery'.
	 * 
	 * @return html of a gallery.
	 */
	public String getGalleryBody()
	{
		String galleryBody = new String(getHtmlTemplate("gallery_body.html"));
		StringBuilder galleryItems = new StringBuilder("");

		ArrayList<ImageGallery> galleriesList = imageDAO.getAllGalleries();

		// for each gallery item, generate the html
		for (ImageGallery gallery : galleriesList)
		{
			// skips this gallery
			if(gallery.getTitle().equals(Constants.CAROUSEL_TITLE))
			{
				continue;
			}
			// for bootstrap class="col-md-colSize"
			int colSize = 12 / gallery.getImages().length;
			// becomes the title
			String title = gallery.getTitle();
			// turns the title into id, for swapping thumbnail to main image using javascript
			String id = title.replaceAll(" ", "_").toLowerCase();

			// template for each gallery item
			String galleryTemplate = getHtmlTemplate("gallery_item.html");
			// set the title and primary image
			galleryTemplate = galleryTemplate.replace("TITLE", gallery.getTitle());
			galleryTemplate = galleryTemplate.replace("PRIMARY_IMAGE", gallery.getImages()[0]);
			galleryTemplate = galleryTemplate.replace("ID", id);

			// to be the html for the thumbnails that belong to the gallery
			StringBuilder thumbnails = new StringBuilder("");
			// for each image in the gallery, make its html and append it to 'thumbnails',
			for (int i = 0; i < gallery.getImages().length; i++)
			{
				// template for each image div
				String str = getHtmlTemplate("gallery_thumb.html");
				str = str.replaceAll("ID", id);
				str = str.replaceAll("COL_SIZE", Integer.toString(colSize));
				str = str.replaceAll("SOURCE", gallery.getImages()[i]);
				// add this string as a thumbnail image
				thumbnails.append(str + "\n");
			}
			// add to the overall template
			galleryTemplate = galleryTemplate.replace("THUMBNAILS", thumbnails.toString());
			galleryItems.append(galleryTemplate);
		}

		galleryBody = galleryBody.replace("GALLERY", galleryItems.toString());

		return galleryBody.toString();
	}

	/**
	 * makes the general head that this site uses.
	 * 
	 * @param title - what goes in the browser's title bar
	 * @param description - page meta content. SEO buzzwords here. 
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
	 * @param fileName - name of the html template file.
	 * @return string containing the html in that file.
	 */
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
		} catch (Exception e)
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
		String output = getHtmlTemplate("image_upload_body.html");
		StringBuilder galleryTableData = new StringBuilder("");
		StringBuilder carouselTableData = new StringBuilder("");
		ArrayList<ImageGallery> galleryList = null;

		String noUploadAddress = "img//no_uploaded.png";

		// get the galleries table, generate html table, and put in output
		galleryList = imageDAO.getAllGalleries();
		ImageGallery carouselGallery = null;

		for (ImageGallery gallery : galleryList)
		{
			
			if(gallery.getTitle().equals(Constants.CAROUSEL_TITLE))
			{
				carouselGallery = gallery;
				continue;
			}
			
			String galleryRowTemplate = getHtmlTemplate("gallery_table_row.html");
			int numImages = gallery.getImages().length;

			galleryRowTemplate = galleryRowTemplate.replaceAll("TITLE", gallery.getTitle());

			galleryRowTemplate = galleryRowTemplate.replace("IMG_1",
					(numImages >= 1 ? gallery.getImages()[0] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_2",
					(numImages >= 2 ? gallery.getImages()[1] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_3",
					(numImages >= 3 ? gallery.getImages()[2] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_4",
					(numImages >= 4 ? gallery.getImages()[3] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_5",
					(numImages >= 5 ? gallery.getImages()[4] : noUploadAddress));
			galleryRowTemplate = galleryRowTemplate.replace("IMG_6",
					(numImages == 6 ? gallery.getImages()[5] : noUploadAddress));

			galleryTableData.append(galleryRowTemplate + "\n");
		}

		output = output.replace("CURRENT_GALLERY_IMAGES_TABLE", galleryTableData);

		// get the carosuel table, generate html table and put in output		

		if (carouselGallery != null)
		{
			for (String carosuelImgPath : carouselGallery.getImages())
			{
				String carouselRowTemplate = getHtmlTemplate("carousel_image_row.html");

				carouselRowTemplate = carouselRowTemplate.replace("TITLE", carouselGallery.getTitle());
				carouselRowTemplate = carouselRowTemplate.replaceAll("IMG", carosuelImgPath);

				carouselTableData.append(carouselRowTemplate + "\n");
			}

			output = output.replace("CAROUSEL_IMAGES_TABLE", carouselTableData);
		}

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
		ImageGallery carouselGallery = imageDAO.getGallery(Constants.CAROUSEL_TITLE);
		String[] carosuelImgPaths = null;
		
		if(carouselGallery != null)
		{
			carosuelImgPaths = carouselGallery.getImages();
		}
		
		for (int i = 0; carosuelImgPaths != null && i < carosuelImgPaths.length; i++)
		{
			// get the item
			String path = carosuelImgPaths[i];
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

		return output;
	}
}

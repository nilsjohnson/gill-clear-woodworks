package com.gillccwoodworks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletContext;

public class NilsHtmlEngine
{
	private ServletContext context;

	public NilsHtmlEngine(ServletContext context)
	{
		this.context = context;
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

			int colSize = 12 / item.imageList.size();
			String title = item.title;
			String id = title.replace(" ", "_");
			id = id.toLowerCase();

			// set the title and primary image
			galleryTemplate = galleryTemplate.replace("TITLE", item.title);
			galleryTemplate = galleryTemplate.replace("PRIMARY_IMAGE", "img/gallery/" + item.imageList.get(0));
			galleryTemplate = galleryTemplate.replace("ID", id);

			StringBuilder thumbnails = new StringBuilder("");
			// for each image in the item,
			for (int i = 0; i < item.imageList.size(); i++)
			{
				// template for each image div
				String str = getHtmlTemplate("gallery_thumb.html");
				str = str.replaceAll("ID", id);
				str = str.replaceAll("COL_SIZE", Integer.toString(colSize));
				str = str.replaceAll("SOURCE", "img/gallery/" + item.imageList.get(i));
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
		ArrayList<GalleryItem> galleriesList = new ArrayList<>();
		String path = context.getRealPath("/") + "WEB-INF\\" +
				"gallery_items.txt";

		// read gallery_items.txt into a 'long' String
		try (BufferedReader br = new BufferedReader(new FileReader(path)))
		{
			StringBuilder sb = new StringBuilder();
			String temp = br.readLine();

			while (temp != null)
			{
				sb.append(temp);
				temp = br.readLine();
			}

			String galleryInfo = sb.toString();

			// split that 'long' String up into smaller strings that represent each gallery
			// item
			ArrayList<String> gallerieList = new ArrayList<String>(Arrays.asList(galleryInfo.split("%")));

			// for each of those 'smaller strings' split it up again. the first string is
			// the name of the gallery, the rest are image addresses.
			// put them in an ArrayList of type GalleryItem
			for (String str : gallerieList)
			{
				ArrayList<String> galleryItem = new ArrayList<String>(Arrays.asList(str.split(",")));
				String name = galleryItem.get(0);
				galleryItem.remove(0);
				galleriesList.add(new GalleryItem(name, galleryItem));
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return galleriesList;
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

	private String getHtmlTemplate(String fileName)
	{

		// string to be returned
		String output = null;
		// the root path for resources, with fileName added
		// String path = NilsHtmlEngine.class.getResource("/").getPath() + fileName;

		String path = context.getRealPath("/") + "WEB-INF\\html\\" +
				fileName;
		System.out.println("Opening " + fileName +
				". Path: " +
				path);

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
}

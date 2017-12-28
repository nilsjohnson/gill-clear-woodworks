package com.gillccwoodworks;

import java.util.ArrayList;

public class GalleryItem
{
	public String title;
	public ArrayList<String> imageList;
	
	public GalleryItem(String title, ArrayList<String> images)
	{
		this.title = title;
		this.imageList = images;
	}
}

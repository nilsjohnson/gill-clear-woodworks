package data;

import java.util.List;

public class ImageGallery
{
	private String title;
	// to hold the address' of images
	private String[] images;
	
	// TODO get rid of where this is used
	public ImageGallery(String title, List<String> imageList)
	{
		this.title = title;
		
		images = new String[imageList.size()];
		images = imageList.toArray(images);
	}
	// this constructor is preferred
	public ImageGallery(String title, String[] imageList)
	{
		this.title = title;
		images = imageList;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String[] getImages()
	{
		return this.images;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(title + "\n");
		
		for(String str: images)
		{
			sb.append(str + "\n");
		}
		return sb.toString();
	}
}

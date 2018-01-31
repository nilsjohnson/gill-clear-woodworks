package data;

import java.util.List;

public class GalleryItem
{
	private String title;
	// to hold the address' of images
	private String[] images;
	
	public GalleryItem(String title, List<String> imageList)
	{
		this.title = title;
		
		// make the list a primitive array for less overhead?
		images = new String[imageList.size()];
		images = imageList.toArray(images);
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

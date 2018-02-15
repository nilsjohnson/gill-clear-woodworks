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
	
	public void setImages(String[] images)
	{
		this.images = images;
	}
	
	public void bumpRight(String path)
	{
		if(images != null)
		{
			for(int i = 0; i < images.length; i++)
			{
				if(images[i].equals(path))
				{
					images[i] = images[(i+1) % images.length];
					images[(i+1) % images.length] = path;
					break;
				}
			}
		}
	}
	
	public void bumpLeft(String path)
	{
		if(images != null)
		{
			for(int i = 0; i < images.length; i++)
			{
				if(images[i].equals(path) && i == 0)
				{
					images[i] = images[images.length-1];
					images[images.length-1] = path;
					break;
				}
				else if(i > 0 && images[i].equals(path))
				{
					images[i] = images[(i-1) % images.length];
					images[(i-1) % images.length] = path;
					break;
				}
			}
		}
	}
	
	
	public void addImage(String path)
	{
		if (images != null)
		{
			String[] newImages = new String[images.length + 1];
			
			for (int i = 0; i < newImages.length; i++)
			{
				if (i < newImages.length - 1)
				{
					newImages[i] = images[i];
				}
				else if (i == newImages.length-1)
				{
					newImages[i] = path;
				}
			}
			images = newImages;
		}
		else
		{
			images = new String[] {path};
		}
	
	}
	
	public void deleteImage(String path)
	{
		if(images == null)
		{
			return;
		}
		
		// if images contains this image
		if(contains(path, images))
		{
			String[] newArr = new String[images.length - 1];
			// to track index of new array
			int i = 0;
			// to track index of the original array
			int j = 0;
			
			while(i < newArr.length)
			{
				System.out.println("Checking to see if cur image is the one to delete");
				// if the current image doesnt equal the one to delete, copy it, and increment both arrays
				if(!images[j].equals(path))
				{
					newArr[i] = images[j];
					i++;
					j++;
				}
				// otherwise if it is the one to delete, skip over it
				else
				{
					j++;
				}
			}
			images = newArr;
		}
		
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(title + "\n");
		
		for(String str: images)
		{
			sb.append("\t" + str + "\n");
		}
		return sb.toString();
	}
	
	private boolean contains(String str, String[] arr)
	{	
		for(int i = 0; i < arr.length; i++)
		{
			if(arr[i].equals(str))
			{
				return true;
			}
		}
		return false;
	}
}

package data;

import java.util.ArrayList;
import java.util.UUID;

public class Collection
{
	private String title;
	private String description;
	private ArrayList<Image> imageList;
	private UUID id;
	
	
	public Collection(String title, String description, ArrayList<Image> imageList)
	{
		this(title, description, imageList, null);
	}
	
	public Collection(String title, String description, ArrayList<Image> imageList, UUID id)
	{
		this.title = title;
		this.description = description;
		this.imageList = imageList;
		this.id = id;
	}

	public String getTitle()
	{
		return this.title;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void addImage(String path)
	{
		imageList.add(new Image(path));
	}
	
	public void bumpRight(UUID uuid) throws Exception
	{
		for(int i = 0; i < imageList.size(); i++)
		{
			if(imageList.get(i).uuid.equals(uuid))
			{
				Image temp = imageList.get((i+1) % imageList.size());
				imageList.set((i+1) % imageList.size(), imageList.get(i));
				imageList.set(i, temp);
				return;
			}
		}
		throw new Exception("Image not found. Could not bump right.");
	}
	
	public void bumpLeft(UUID id) throws Exception
	{
		for(int i = 0; i < imageList.size(); i++)
		{
			if(imageList.get(i).uuid.equals(id))
			{
				Image temp;
				if(i-1 < 0)
				{
					temp = imageList.get(imageList.size()-1);
					imageList.set(imageList.size()-1, imageList.get(i));
				}
				else
				{
					temp = imageList.get(i-1);
					imageList.set(i-1, imageList.get(i));
				}
				
				imageList.set(i, temp);	
				return;
			}
		}
		throw new Exception("Image not found. Could not bump left.");
	}

	public Image getImageAt(int i)
	{
		return imageList.get(i);
	}
	
	public int getNumberOfImages()
	{
		if(imageList != null)
		{
			return imageList.size();
		}
		else
		{
			return 0;
		}
	}
	
	public UUID getId()
	{
		return this.id;
	}
}

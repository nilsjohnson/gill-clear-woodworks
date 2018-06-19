package data;

import java.util.UUID;

public class Image
{
	public String path;
	public UUID uuid;
	
	public Image(String path, UUID uuid)
	{
		this.path = path;
		this.uuid = uuid;
	}
	
	public Image(String path)
	{
		this.path = path;
		this.uuid = null;
	}
}

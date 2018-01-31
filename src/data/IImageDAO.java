package data;

import java.util.ArrayList;

public interface IImageDAO
{
	// 'get'
	public ArrayList<GalleryItem> getAllGalleries();
	public GalleryItem getGallery(String galleryName);
	
	// 'update
	public void deleteGallery(String galleryName);
	public void deleteImage(String path);
	public void moveImageUp(String galleryName, String path);
}

package data;

import java.util.ArrayList;

public interface IimageDAO
{
	// 'get'
	public ArrayList<ImageGallery> getAllGalleries();
	public ImageGallery getGallery(String galleryName);
	
	// 'update
	public void deleteGallery(String galleryName);
	public void deleteImage(String path);
	public void moveImageUp(String galleryName, String path);
	
	// make new things
	public void insertGallery(ImageGallery gallery);
}

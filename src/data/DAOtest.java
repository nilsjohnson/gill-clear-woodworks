package data;

import java.sql.SQLException;

public class DAOtest
{

	public static void main(String[] args)
	{

		String[] images = { "Image22One.jpg", "ImageT22wo.jpg" };
		String title = "My Gallery22";
		ImageDAOv2 dao = null;
		
		try
		{
			dao = new ImageDAOv2();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		ImageGallery gallery = new ImageGallery(title, images);
		
		dao.insertGallery(gallery);
		
		

	}

}

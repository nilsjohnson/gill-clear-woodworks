package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.gillccwoodworks.Constants;
import com.gillccwoodworks.FileUploadServlet;

import data.ImageDbSchema.GalleriesTable;


public class ImageDAO extends DAO
{
	public ImageDAO() throws SQLException
	{
		super();
	}


	public ArrayList<ImageGallery> getAllGalleries()
	{
		ArrayList<ImageGallery> galleryList = new ArrayList<>();
		
		try
		{
			openConnection();

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + GalleriesTable.NAME);

			ResultSet rSet = preparedStatement.executeQuery();

			// get the names of each gallery
			while (rSet.next())
			{
				String title = rSet.getString(1);
				
				ImageGallery gallery = new ImageGallery(title, new String[0]);
				
				for(int i = 2; i < Constants.MAX_GALLERY_SIZE+2; i++)
				{
					String str = rSet.getString(i);
					
					if(str != null)
					{
						gallery.addImage(str);
					}
				}
				
				galleryList.add(gallery);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
		
		return galleryList;
	}
	
	public void insertOrRelaceGallery(ImageGallery gallery) throws Exception
	{
		
		if(gallery.getImages().length > Constants.MAX_GALLERY_SIZE)
		{
			throw new Exception("Gallery has too many Images");
		}
		
		String[] images = new String[Constants.MAX_GALLERY_SIZE];
		
		for(int i = 0; i < gallery.getImages().length; i++)
		{
			images[i] = gallery.getImages()[i];
		}
		
		
		try
		{
			openConnection();
			String statement = "INSERT or REPLACE into " + GalleriesTable.NAME + " (" 
					+ GalleriesTable.Cols.GALLERY_NAME + ", "		
					+ GalleriesTable.Cols.IMG_1_PATH + ", "
					+ GalleriesTable.Cols.IMG_2_PATH + ", "
					+ GalleriesTable.Cols.IMG_3_PATH + ", "
					+ GalleriesTable.Cols.IMG_4_PATH + ", "
					+ GalleriesTable.Cols.IMG_5_PATH + ", "
					+ GalleriesTable.Cols.IMG_6_PATH + ") "
					+ "values (?, ?, ?, ?, ?, ?, ?)";
					
					PreparedStatement preparedInsertStatement = connection.prepareStatement(statement);

					preparedInsertStatement.setString(1, gallery.getTitle());
					preparedInsertStatement.setString(2, images[0]);
					preparedInsertStatement.setString(3, images[1]);
					preparedInsertStatement.setString(4, images[2]);
					preparedInsertStatement.setString(5, images[3]);
					preparedInsertStatement.setString(6, images[4]);
					preparedInsertStatement.setString(7, images[5]);

					preparedInsertStatement.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
	}
	
	public void deleteGallery(String galleryName)
	{
		try
		{
			openConnection();
			
			String statement = "DELETE from " + GalleriesTable.NAME + " WHERE " + GalleriesTable.Cols.GALLERY_NAME + " = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			preparedStatement.setString(1, galleryName);
			preparedStatement.executeUpdate();

		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
	}
	
	public ImageGallery getGallery(String GalleryName)
	{
		ImageGallery gallery = new ImageGallery(GalleryName, new String[0]);
		
		try
		{
			openConnection();
			
			//"SELECT userName, passwordCipher, filePath  FROM Users WHERE userName = ?";
			
			String statement = "SELECT "
					+ GalleriesTable.Cols.IMG_1_PATH + ", "
					+ GalleriesTable.Cols.IMG_2_PATH + ", "
					+ GalleriesTable.Cols.IMG_3_PATH + ", "
					+ GalleriesTable.Cols.IMG_4_PATH + ", "
					+ GalleriesTable.Cols.IMG_5_PATH + ", "
					+ GalleriesTable.Cols.IMG_6_PATH
					+ " FROM " + GalleriesTable.NAME
					+ " WHERE " + GalleriesTable.Cols.GALLERY_NAME
					+ " = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			preparedStatement.setString(1, GalleryName);

			ResultSet rSet = preparedStatement.executeQuery();

			// if the query returns results, the gallery exists
			if(rSet.next())
			{
				for(int i = 1; i < Constants.MAX_GALLERY_SIZE+1; i++)
				{
					String str = rSet.getString(i);
					
					if(str != null)
					{
						gallery.addImage(str);
					}
				}
				
				return gallery;
			}
			// otherwise return null
			else
			{
				return null;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
		
		return null;
	}
	
}

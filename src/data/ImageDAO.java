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

/*

	public ArrayList<ImageGallery> getAllGalleries()
	{
		ArrayList<ImageGallery> galleryList = new ArrayList<>();
		ArrayList<String> galleryTitles = new ArrayList<>();

		try
		{
			openConnection();

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT " + ImageTable.Cols.GALLERY_NAME + " FROM " + ImageTable.NAME);

			ResultSet rSet = preparedStatement.executeQuery();

			// get the names of each gallery
			while (rSet.next())
			{
				galleryTitles.add(rSet.getString(1));
			}
			// get a gallery for each name
			for(String galleryTitle : galleryTitles)
			{
				galleryList.add(getGalleryByName(galleryTitle));
			}

		}
		catch (SQLException e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		return galleryList;
	}
	
	/**
	 * Deletes Gallery by name
	 
	public void deleteGallery(String galleryName)
	{
		try
		{
			openConnection();

			String statement = "DELETE from " + ImageTable.NAME + " where " + ImageTable.Cols.GALLERY_NAME + " = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			preparedStatement.setString(1, galleryName);

			preparedStatement.executeUpdate();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
	}

	*//**
	 * 
	 * @param updatedGallery
	 *//*
	public void updateGalleryImages(ImageGallery updatedGallery)
	{
		try
		{
			openConnection();

			// get the old gallery and delete any images that are no longer present
			ImageGallery oldGallery = getGalleryByName(updatedGallery.getTitle());

			// compare each set of paths(old to new)
			// if an 'old path' does not exist in the new object, delete that path.
			if (oldGallery != null)
			{
				for (String oldPath : oldGallery.getImages())
				{
					if (!contains(oldPath, updatedGallery.getImages()))
					{
						deleteRow(oldPath);
					}
				}
			}

			for (int curIndex = 0; curIndex < updatedGallery.getImages().length; curIndex++)
			{
				insertOrReplace(updatedGallery.getImages()[curIndex], updatedGallery.getTitle(), curIndex);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
	}
	
	public void appendToGallery(String galleryTitle, String path)
	{
		try
		{
			openConnection();
			
			ImageGallery gallery = getGalleryByName(galleryTitle);
			if(gallery == null)
			{
				gallery = new ImageGallery(galleryTitle, new String[] { path });
			}
			else
			{
				gallery.addImage(path);
			}
			
			updateGalleryImages(gallery);
			
		}
		catch(SQLException e)
		{
			
		}
		finally
		{
			closeConnection();
		}
	}
	
	public ImageGallery getGallery(String galleryName)
	{
		ImageGallery gallery = null;
		try
		{
			openConnection();
			gallery = getGalleryByName(galleryName);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
		
		return gallery;
	}
	
	private void deleteRow(String path) throws SQLException
	{
		String statement = "DELETE from " + ImageTable.NAME + " where " + ImageTable.Cols.PATH + " = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(statement);
		preparedStatement.setString(1, path);
		preparedStatement.executeUpdate();
	}
	
	private ImageGallery getGalleryByName(String galleryName) throws SQLException
	{
		ImageGallery gallery = null;
		String[] imagePaths = null;

		String statement = "SELECT "
				+ ImageTable.Cols.PATH + ", " 
				+ ImageTable.Cols.GALLERY_INDEX
				+ " FROM "
				+ ImageTable.NAME + 
				" WHERE " 
				+ ImageTable.Cols.GALLERY_NAME
				+ " = " + "?" + "ORDER BY "
				+ ImageTable.Cols.GALLERY_INDEX 
				+ " DESC";

		PreparedStatement preparedStatement = connection.prepareStatement(statement);
		preparedStatement.setString(1, galleryName);

		ResultSet rSet = preparedStatement.executeQuery();

		int i = 0;
		while (rSet.next())
		{
			String path = rSet.getString(1);
			String index = rSet.getString(2);

			// if its the first iteration, make a new String array the size of the number of elements
			if (i == 0)
			{
				imagePaths = new String[Integer.parseInt(index) + 1];
			}

			imagePaths[Integer.parseInt(index)] = path;
			i++;
		}
		
		// make the gallery if there are images 
		if(imagePaths !=null)
		{
			gallery = new ImageGallery(galleryName, imagePaths);
		}
		
		return gallery;
	}
	
	public String[] getImagesByGalleryName(String galleryName)
	{
		String[] images = null;
		try
		{
			openConnection();

			ImageGallery gallery = getGalleryByName(galleryName);
			if (gallery != null)
			{
				images = gallery.getImages();
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
		
		return images;
	}
	
	private void insertOrReplace(String path, String title, int index) throws SQLException
	{
		String statement = "insert or replace into " + ImageTable.NAME + " (" 
				+ ImageTable.Cols.PATH + ", "		
				+ ImageTable.Cols.GALLERY_NAME + " , "
				+ ImageTable.Cols.GALLERY_INDEX + ") " 
				+ "values (?, ?, ?)";
				
				PreparedStatement preparedInsertStatement = connection.prepareStatement(statement);

				preparedInsertStatement.setString(1, path);
				preparedInsertStatement.setString(2, title);
				preparedInsertStatement.setString(3, Integer.toString(index));

				preparedInsertStatement.executeUpdate();
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
	
	private void deleteImage(String image)
	{
		BasicAWSCredentials creds = Constants.getAwsCreds(this.context);
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
				.withRegion("us-east-1").build();

		try
		{
			System.out.println("Image to delete: " + image);
			image = image.replace("https://s3.amazonaws.com/forestfriends/", "");
			System.out.println("Image Key: " + image);
			DeleteObjectRequest request = new DeleteObjectRequest(Constants.BUCKET_NAME, image);
			s3Client.deleteObject(request);
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

*/
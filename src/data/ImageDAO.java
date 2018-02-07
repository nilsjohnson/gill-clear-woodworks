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

import data.ImageDbSchema.ImageTable;


public class ImageDAO extends DAO
{
	public ImageDAO() throws SQLException
	{
		super();
	}
	
	/**
	 * Inserts a gallery item into the table.
	 * @param gallery - an ImageGallery.
	 */
	public void addGallery(ImageGallery gallery)
	{
		try
		{
			openConnection();

			for (int i = 0; i < gallery.getImages().length; i++)
			{
				insertOrReplace(gallery.getImages()[i], gallery.getTitle(), i);
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
	
	/**
	 * @return ArrayList of all the ImageGalleries in the database
	 */
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
	 */
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

	/**
	 * 
	 * @param updatedGallery
	 */
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
}

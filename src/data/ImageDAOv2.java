package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.amazonaws.services.cloudfront.model.Paths;

import data.ImageDbSchema;
import data.ImageDbSchema.ImageTable;

public class ImageDAOv2 extends DAO implements IimageDAO
{

	protected ImageDAOv2() throws SQLException
	{
		super();
	}

	@Override
	public ArrayList<ImageGallery> getAllGalleries()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageGallery getGallery(String galleryName)
	{
		ImageGallery gallery;
		String[] imagePaths;
		
		try
		{
			openConnection();
			String statement = "SELECT "
			+ ImageTable.Cols.PATH + ", "
			+ ImageTable.Cols.GALLERY_INDEX
			+ " FROM "
			+ ImageTable.NAME + " WHERE (" 
			+ ImageTable.Cols.GALLERY_NAME + " EQUALS " 
			+ "values (?)";
			PreparedStatement preparedStatement = connection.prepareStatement(statement);

			preparedStatement.setString(1, galleryName);

			ResultSet rSet = preparedStatement.executeQuery();

			// if the query returns, the user exists
			
			int numResults = rSet.getFetchSize();
			imagePaths = new String[numResults];
			String index;
			
			for(int i = 0; i < numResults; i++)
			{
				// get the image
				String path = rSet.getString(1);
				index = rSet.getString(2);
				// put it in the array
				imagePaths[Integer.parseInt(index)] = path;
			}
			gallery = new ImageGallery(galleryName, imagePaths);
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
		return null;
	}

	@Override
	public void deleteGallery(String galleryName)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteImage(String path)
	{
		

	}

	@Override
	public void moveImageUp(String galleryName, String path)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void insertGallery(ImageGallery gallery)
	{
		String title = gallery.getTitle();

		for (int curIndex = 0; curIndex < gallery.getImages().length; curIndex++)
		{
			String path = gallery.getImages()[curIndex];
			
			try
			{
				openConnection();
				String statement = "insert into " + ImageTable.NAME + " (" 
				+ ImageTable.Cols.PATH + ", " 
				+ ImageTable.Cols.GALLERY_NAME + " , " 
				+ ImageTable.Cols.GALLERY_INDEX + ") "
				+ "values (?, ?, ?)";
				PreparedStatement preparedInsertStatement = connection.prepareStatement(statement);

				preparedInsertStatement.setString(1, path);
				preparedInsertStatement.setString(2, title);
				preparedInsertStatement.setString(3, Integer.toString(curIndex));

				preparedInsertStatement.executeUpdate();

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
		}

	}

}

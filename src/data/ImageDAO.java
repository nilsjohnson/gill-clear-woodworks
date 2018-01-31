package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.gillccwoodworks.FileUploadServlet;


public class ImageDAO extends DAO
{
	public ImageDAO() throws SQLException
	{
		super();
	}

	private final String TABLE_NAME = "Galleries";
	// todo, define this once, make constants file or somethin' - multiple def!
	private final String BUCKET_NAME = "forestfriends";
	
	/**
	 * Inserts a gallery item into the table.
	 * @param item A gallery item to be displayed on the gallery page.
	 */
	public void insertGallery(GalleryItem item)
	{
		String title = item.getTitle();
		int numImages = item.getImages().length;
		
		try
		{
			openConnection();
			String statement = "insert into " + TABLE_NAME + " (title, thumbnail1, thumbnail2, thumbnail3, thumbnail4, thumbnail5, thumbnail6) " + "values (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedInsertStatement = connection.prepareStatement(statement);

			preparedInsertStatement.setString(1, title);
			preparedInsertStatement.setString(2, (numImages >= 1 ? item.getImages()[0] : null));
			preparedInsertStatement.setString(3, (numImages >= 2 ? item.getImages()[1] : null));
			preparedInsertStatement.setString(4, (numImages >= 3 ? item.getImages()[2] : null));
			preparedInsertStatement.setString(5, (numImages >= 4 ? item.getImages()[3] : null));
			preparedInsertStatement.setString(6, (numImages >= 5 ? item.getImages()[4] : null));
			preparedInsertStatement.setString(7, (numImages == 6 ? item.getImages()[5] : null));

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
	
	public ArrayList<GalleryItem> getAllGalleries()
	{
		ArrayList<GalleryItem> galleryList = new ArrayList<>();
		
		try
		{
			openConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME);
		
			ResultSet rSet = preparedStatement.executeQuery();

			while (rSet.next())
			{
				List<String> urlList = new ArrayList<>();
				
				String title = rSet.getString(1);
				
				addUrlToGalleryList(rSet.getString(2), urlList);
				addUrlToGalleryList(rSet.getString(3), urlList);
				addUrlToGalleryList(rSet.getString(4), urlList);
				addUrlToGalleryList(rSet.getString(5), urlList);
				addUrlToGalleryList(rSet.getString(6), urlList);
				addUrlToGalleryList(rSet.getString(7), urlList);
			
				GalleryItem item = new GalleryItem(title, urlList);
				galleryList.add(item);
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
	
	private void addUrlToGalleryList(String url, List<String> urlList)
	{
		if(url != null)
		{
			urlList.add(url);
		}
	}
	
	public void deleteGalleryRow(String title)
	{
		try
		{
			openConnection();

			String statement = "DELETE from " + TABLE_NAME + " where title = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			preparedStatement.setString(1, title);

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
	
	public void deleteCarouselImage(String image)
	{
		try
		{
			openConnection();

			String statement = "DELETE from SliderImages where img_1 = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			preparedStatement.setString(1, image);

			preparedStatement.executeUpdate();
			
			deleteImage(image);

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
	
	public void addCarouselImage(String sliderName, String imgUrl)
	{
		try
		{
			openConnection();
			String statement = "insert into SliderImages (slider, img_1) values (?, ?)";
			PreparedStatement preparedInsertStatement = connection.prepareStatement(statement);

			preparedInsertStatement.setString(1, sliderName);
			preparedInsertStatement.setString(2, imgUrl);
			
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

	public CarouselItem[] getAllCarouselImages()
	{
		List<CarouselItem> itemList = new ArrayList<>();
		CarouselItem[] items;
		
		try
		{
			openConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM SliderImages");
		
			ResultSet rSet = preparedStatement.executeQuery();

			while (rSet.next())
			{
				
				String title = rSet.getString(1);
				String url = rSet.getString(2);
			
				CarouselItem item = new CarouselItem(title, url);
				itemList.add(item);
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
		
		items = new CarouselItem[itemList.size()];
		items = itemList.toArray(items);
		
		return items;
	}
	
	public void deleteImage(String image)
	{

		// TODO get this from elsewhere?
		BasicAWSCredentials creds = FileUploadServlet.getAwsCreds(this.context);
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
				.withRegion("us-east-1").build();

		try
		{
			System.out.println("Image to delete: " + image);
			image = image.replace("https://s3.amazonaws.com/forestfriends/", "");
			System.out.println("Image Key: " + image);
			DeleteObjectRequest request = new DeleteObjectRequest(BUCKET_NAME, image);
			s3Client.deleteObject(request);
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

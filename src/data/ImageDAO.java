package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import data.DbSchema.CollectionTable;
import data.DbSchema.ImageTable;

public class ImageDAO extends DAO implements IImageDAO
{

	public ImageDAO() throws SQLException
	{
		super();
	}

	/**
	 * @return All collections in DB.
	 */
	@Override
	public ArrayList<Collection> getAllCollections() throws Exception
	{
		ArrayList<Collection> collectionList = new ArrayList<>();

		try
		{
			openConnection();
			
			String query = "SELECT "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.UUID + ", "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.TITLE + ", "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.DESC + ", "
					+ ImageTable.NAME + ". " + ImageTable.COLS.PATH
					+ " From " + CollectionTable.NAME + " INNER JOIN " + ImageTable.NAME + " ON "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.UUID 
					+ " = " + ImageTable.NAME + "." + ImageTable.COLS.COLLECTION_UUID;
			
			
			Statement stmt = connection.createStatement();
			
			ResultSet rSet = stmt.executeQuery(query);	
			
			UUID uuid;
			String title = null;
			String desc = null;
			ArrayList<String> urlList = new ArrayList<>();
			UUID lastUUID = null;
			
			while(rSet.next())
			{
				uuid = UUID.fromString(rSet.getString(CollectionTable.COLS.UUID));
				
				if(lastUUID == null || !uuid.equals(lastUUID))
				{
					if(lastUUID != null)
					{
						collectionList.add(new Collection(title, desc, urlList, lastUUID));
						urlList = new ArrayList<>();
					}
					title = rSet.getString(CollectionTable.COLS.TITLE);
					desc = rSet.getString(CollectionTable.COLS.DESC);
				}
				
				String url = rSet.getString(ImageTable.COLS.PATH);
				urlList.add(url);
				lastUUID = uuid;
			}
			
			if(lastUUID != null)
			{
				// to get the last collection
				collectionList.add(new Collection(title, desc, urlList, lastUUID));
			}
			return collectionList;
		}
		finally
		{
			closeConnection();
		}

	}

	/**
	 * @param collection 	The collection to be inserted into DB.
	 */
	@Override
	public void InsertCollection(Collection collection) throws Exception
	{
		String collectionInsert = "INSERT into " + CollectionTable.NAME + " ("
				+ CollectionTable.COLS.TITLE + ", "
				+ CollectionTable.COLS.DESC + ", "
				+ CollectionTable.COLS.UUID + ") "
				+ "VALUES ( ?, ?, ?)";
		
		String imgInsert = "INSERT  into " + ImageTable.NAME + " ("
				+ ImageTable.COLS.PATH + ", "
				+ ImageTable.COLS.COLLECTION_UUID + ") "
				+ "VALUES ( ?, ?)";
		
		try
		{
			openConnection();
			
			// Insert the Collection
			PreparedStatement collectionPrepStmt = connection.prepareStatement(collectionInsert);
			
			String uuid = UUID.randomUUID().toString();
			
			collectionPrepStmt.setString(1, collection.getTitle());
			collectionPrepStmt.setString(2, collection.getDescription());
			collectionPrepStmt.setString(3, uuid);
			collectionPrepStmt.executeUpdate();
			
			// Insert the images
			PreparedStatement imgPrepStmt = connection.prepareStatement(imgInsert);
			for(int i = 0; i < collection.getNumberOfImages(); i++)
			{
				imgPrepStmt.setString(1, collection.getImageAt(i));
				imgPrepStmt.setString(2, uuid);
				imgPrepStmt.executeUpdate();
			}	
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			closeConnection();
		}
	}

	/**
	 * @param path	The path of the image to remove from DB.
	 */
	@Override
	public void DeleteImage(String path) throws Exception
	{
		String imgDelStmt = "DELETE  FROM " + ImageTable.NAME + " WHERE "
				+ ImageTable.COLS.PATH
				+ " = ?";
		try
		{
			openConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(imgDelStmt);
			preparedStatement.setString(1, path);
			preparedStatement.executeUpdate();
			
		}
		finally
		{
			closeConnection();
		}
		
	}

	/**
	 * @param id 	The UUID of the collection to delete.
	 */
	@Override
	public void DeleteCollection(UUID id) throws Exception
	{
		try
		{
			String collectionDelStr = "DELETE FROM " + CollectionTable.NAME + " WHERE "
					+ CollectionTable.COLS.UUID
					+ " = ?";
			
			String imgDelStr = "DELETE FROM " + ImageTable.NAME + " WHERE "
					+ ImageTable.COLS.COLLECTION_UUID
					+ " = ?";
			
			openConnection();
			
			// remove the collection row
			PreparedStatement collectionStmt = connection.prepareStatement(collectionDelStr);
			collectionStmt.setString(1, id.toString());
			collectionStmt.executeUpdate();
			
			// remove the associated image rows
			PreparedStatement imgStmt = connection.prepareStatement(imgDelStr);
			imgStmt.setString(1, id.toString());
			imgStmt.executeUpdate();
		}
		finally
		{
			closeConnection();
		}
	}

	@Override
	public Collection getCollection(UUID uuid) throws Exception
	{
		if(uuid == null)
		{
			return null;
		}
		
		String quereeey = "SELECT "
				+ CollectionTable.NAME + "." + CollectionTable.COLS.UUID + ", "
				+ CollectionTable.NAME + "." + CollectionTable.COLS.TITLE + ", "
				+ CollectionTable.NAME + "." + CollectionTable.COLS.DESC + ", "
				+ ImageTable.NAME + ". " + ImageTable.COLS.PATH
				+ " From " + CollectionTable.NAME + " INNER JOIN " + ImageTable.NAME + " ON "
				+ CollectionTable.NAME + "." + CollectionTable.COLS.UUID 
				+ " = " + ImageTable.NAME + "." + ImageTable.COLS.COLLECTION_UUID;
		
		try
		{
			openConnection();
			
			String query = "SELECT "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.TITLE + ", "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.DESC + ", "
					+ ImageTable.NAME + "." + ImageTable.COLS.PATH
					+ " From " + CollectionTable.NAME + " INNER JOIN " + ImageTable.NAME + " ON "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.UUID 
					+ " = " + ImageTable.NAME + "." + ImageTable.COLS.COLLECTION_UUID
					+ " WHERE " + ImageTable.NAME + "." + ImageTable.COLS.COLLECTION_UUID + " = '" + uuid.toString() + "'";
			
			System.out.println(query);
			
			Statement stmt = connection.createStatement();
			
			ResultSet rSet = stmt.executeQuery(query);	
			
			
			if(rSet.next())
			{
				String name = rSet.getString(CollectionTable.COLS.TITLE);
				String desc = rSet.getString(CollectionTable.COLS.DESC);
				ArrayList<String> urlList = null;
				
				String url = rSet.getString(ImageTable.COLS.PATH);
				if(url != null)
				{
					urlList = new ArrayList<>();
					urlList.add(url);
					while(rSet.next())
					{
						urlList.add(rSet.getString(ImageTable.COLS.PATH));
					}
				}
				return new Collection(name, desc, urlList, uuid);
				
			}
			else
			{
				return null;
			}
		
		}
		finally
		{
			closeConnection();
		}
	}

}

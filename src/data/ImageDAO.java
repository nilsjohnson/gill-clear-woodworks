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
					+ ImageTable.NAME + "." + ImageTable.COLS.PATH + ", "
					+ ImageTable.NAME + "." + ImageTable.COLS.UUID
					+ " From " + CollectionTable.NAME + " INNER JOIN " + ImageTable.NAME + " ON "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.UUID 
					+ " = " + ImageTable.NAME + "." + ImageTable.COLS.COLLECTION_UUID
					+ " ORDER BY "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.TITLE + " ASC, "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.UUID  + " ASC," 
					+ ImageTable.COLS.INDEX  + " ASC;";
			
			
			Statement stmt = connection.createStatement();
			
			ResultSet rSet = stmt.executeQuery(query);	
			
			UUID uuid;
			String title = null;
			String desc = null;
			ArrayList<Image> imgList = new ArrayList<>();
			UUID lastUUID = null;
			
			
			
			while(rSet.next())
			{
				uuid = UUID.fromString(rSet.getString(CollectionTable.COLS.UUID));
				
				if(lastUUID == null || !uuid.equals(lastUUID))
				{
					if(lastUUID != null)
					{
						collectionList.add(new Collection(title, desc, imgList, lastUUID));
						imgList = new ArrayList<>();
					}
					title = rSet.getString(CollectionTable.COLS.TITLE);
					desc = rSet.getString(CollectionTable.COLS.DESC);
				}
				
				String url = rSet.getString(ImageTable.COLS.PATH);
				String imgUUID_str = rSet.getString(ImageTable.COLS.UUID);
				UUID imgUUID = UUID.fromString(imgUUID_str);
				
				Image img = new Image(url, imgUUID);
				
				imgList.add(img);
				lastUUID = uuid;
			}
			
			if(lastUUID != null)
			{
				// to get the last collection
				collectionList.add(new Collection(title, desc, imgList, lastUUID));
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
	public void insertCollection(Collection collection) throws Exception
	{
		String collectionInsert = "INSERT into " + CollectionTable.NAME + " ("
				+ CollectionTable.COLS.TITLE + ", "
				+ CollectionTable.COLS.DESC + ", "
				+ CollectionTable.COLS.UUID + ") "
				+ "VALUES ( ?, ?, ?)";
		
		String imgInsert = "INSERT  into " + ImageTable.NAME + " ("
				+ ImageTable.COLS.UUID + ", "
				+ ImageTable.COLS.PATH + ", "
				+ ImageTable.COLS.INDEX + ", "
				+ ImageTable.COLS.COLLECTION_UUID + ") "
				+ "VALUES ( ?, ?, ?, ?)";
		
		try
		{
			openConnection();
			
			// Insert the Collection
			PreparedStatement collectionPrepStmt = connection.prepareStatement(collectionInsert);
			
			String collectionUUID = UUID.randomUUID().toString();
			
			collectionPrepStmt.setString(1, collection.getTitle());
			collectionPrepStmt.setString(2, collection.getDescription());
			collectionPrepStmt.setString(3, collectionUUID);
			collectionPrepStmt.executeUpdate();
			
			// Insert the images
			PreparedStatement imgPrepStmt = connection.prepareStatement(imgInsert);
			for(int i = 0; i < collection.getNumberOfImages(); i++)
			{
				imgPrepStmt.setString(1, UUID.randomUUID().toString());
				imgPrepStmt.setString(2, collection.getImageAt(i).path);
				imgPrepStmt.setString(3, Integer.toString(i));
				imgPrepStmt.setString(4, collectionUUID);
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
	public void deleteImage(UUID id) throws Exception
	{
		String imgDelStmt = "DELETE  FROM " + ImageTable.NAME + " WHERE "
				+ ImageTable.COLS.UUID
				+ " = ?";
		try
		{
			openConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(imgDelStmt);
			preparedStatement.setString(1, id.toString());
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
	public void deleteCollection(UUID id) throws Exception
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
		try
		{
			openConnection();
			
			String query = "SELECT "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.TITLE + ", "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.DESC + ", "
					+ ImageTable.NAME + "." + ImageTable.COLS.PATH + ", "
					+ ImageTable.NAME + "." + ImageTable.COLS.UUID
					+ " From " + CollectionTable.NAME + " INNER JOIN " + ImageTable.NAME + " ON "
					+ CollectionTable.NAME + "." + CollectionTable.COLS.UUID 
					+ " = " + ImageTable.NAME + "." + ImageTable.COLS.COLLECTION_UUID
					+ " WHERE " + ImageTable.NAME + "." + ImageTable.COLS.COLLECTION_UUID + " = '" + uuid.toString() + "'"
					+ " ORDER BY " + ImageTable.COLS.INDEX  + " ASC";
			
			Statement stmt = connection.createStatement();
			
			ResultSet rSet = stmt.executeQuery(query);	
			
			
			if(rSet.next())
			{
				String name = rSet.getString(CollectionTable.COLS.TITLE);
				String desc = rSet.getString(CollectionTable.COLS.DESC);
				ArrayList<Image> imgList = null;
				
				String imgUrl = rSet.getString(ImageTable.COLS.PATH);
				UUID imgUUID = UUID.fromString(rSet.getString(ImageTable.COLS.UUID));
				if(imgUrl != null)
				{
					imgList = new ArrayList<>();
					imgList.add(new Image(imgUrl, imgUUID));
					while(rSet.next())
					{
						imgUrl = rSet.getString(ImageTable.COLS.PATH);
						imgUUID = UUID.fromString(rSet.getString(ImageTable.COLS.UUID));
						imgList.add(new Image(imgUrl, imgUUID));
					}
				}
				return new Collection(name, desc, imgList, uuid);
				
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

	
	public void upldateCollectionTitle(String newTitle, UUID uuid) throws Exception
	{
		String update = "UPDATE "
				+ CollectionTable.NAME 
				+ " SET " + CollectionTable.COLS.TITLE + " = ? "
				+ " WHERE " + CollectionTable.COLS.UUID + " = ? ";
		
		try
		{
			openConnection();
			PreparedStatement updateStmt = connection.prepareStatement(update);
			updateStmt.setString(1, newTitle);
			updateStmt.setString(2, uuid.toString());

			updateStmt.executeUpdate();
		}
		finally
		{
			closeConnection();
		}
	}
	
	public void upldateCollectionDesc(String newDesc, UUID uuid) throws Exception
	{
		String update = "UPDATE "
				+ CollectionTable.NAME 
				+ " SET " + CollectionTable.COLS.DESC + " = ? "
				+ " WHERE " + CollectionTable.COLS.UUID + " = ? ";
		
		try
		{
			openConnection();
			PreparedStatement updateStmt = connection.prepareStatement(update);
			updateStmt.setString(1, newDesc);
			updateStmt.setString(2, uuid.toString());

			updateStmt.executeUpdate();
		}
		finally
		{
			closeConnection();
		}
	}
	
	
	public void updateCollection(Collection col) throws Exception
	{	
		String imgUpdate = "UPDATE "
				+ ImageTable.NAME 
				+ " SET " + ImageTable.COLS.INDEX + " = ? "
				+ " WHERE " + ImageTable.COLS.UUID + " = ? ";
		
		try
		{
			openConnection();
			
			for(int i = 0; i < col.getNumberOfImages(); i++)
			{
				
				
				PreparedStatement updateStmt = connection.prepareStatement(imgUpdate);
				updateStmt.setString(1, Integer.toString(i));
				updateStmt.setString(2, col.getImageAt(i).uuid.toString());
				
				updateStmt.executeUpdate();
			}
		}
		finally
		{
			closeConnection();
		}
	}

}

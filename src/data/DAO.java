package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.gillccwoodworks.Constants;

import data.DbSchema.CollectionTable;
import data.DbSchema.ImageTable;


public abstract class DAO
{
	public static Connection connection = null;
	public static String dbPath = null;
	
	protected DAO() throws SQLException
	{		
		try
		{
			// load the driver
			dbPath = "jdbc:sqlite:" + Constants.DB_LOCATION + Constants.DB_NAME;
			Class.forName("org.sqlite.JDBC");
			
			openConnection();

			String createImgTable = "CREATE TABLE IF NOT EXISTS " + ImageTable.NAME + " ("
					+ ImageTable.COLS.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ ImageTable.COLS.PATH + " varchar(" + ImageTable.MAX_PATH_LENGTH + ") NOT NULL, "
					+ ImageTable.COLS.COLLECTION_UUID + " INTEGER);";
	
		
			String createCollectionTable = "CREATE TABLE IF NOT EXISTS " + CollectionTable.NAME + " ("
					+ CollectionTable.COLS.UUID + " varchar(" + CollectionTable.UUID_LENGTH + ") PRIMARY KEY, "
					+ CollectionTable.COLS.TITLE + " varchar(" + CollectionTable.MAX_NAME_LENGTH + ") NOT NULL, "
					+ CollectionTable.COLS.DESC + " varchar(" + CollectionTable.MAX_DESC_LENGTH + "));";
	
			
			
			Statement stmt = connection.createStatement();
			stmt.execute(createImgTable);
			stmt.execute(createCollectionTable);
			
		}
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
		
		
	}

	/**
	 * use to close connection after accessing database
	 */
	protected void closeConnection()
	{
		try
		{
			if (connection != null)
			{
				connection.close();
			}
		}
		catch (SQLException e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * use to open connection prior to accessing database
	 */
	protected void openConnection()
	{
		try
		{
			if (connection == null || connection.isClosed())
			{
				connection = DriverManager.getConnection(dbPath);
			}
		}
		catch (SQLException e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}
}


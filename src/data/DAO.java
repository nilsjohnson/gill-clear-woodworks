package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import data.ImageDbSchema;
import data.ImageDbSchema.ImageTable;

import javax.servlet.ServletContext;


public abstract class DAO
{
	
	public static String dbPath = "jdbc:sqlite:/home/nils/gccwoodworksImages.db";
	public static Connection connection = null;
	protected  ServletContext context;

	protected DAO() throws SQLException
	{		
		try
		{
			Class.forName("org.sqlite.JDBC");
		}
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
		
		String createImagesTable = "CREATE TABLE IF NOT EXISTS " + ImageTable.NAME + " ("
				+  ImageTable.Cols.PATH + " varchar(" + ImageTable.MAX_PATH_LENGTH + ") not null,"
				+  ImageTable.Cols.GALLERY_NAME + " varchar(" + ImageTable.MAX_NAME_LENGTH + "),"
				+  ImageTable.Cols.GALLERY_INDEX + " integer not null,"
				+ "primary key (" + ImageTable.Cols.PATH + ")" 
				+ ");";
		
		try (Connection conn = DriverManager.getConnection(dbPath); Statement stmt = conn.createStatement())
		{
			stmt.execute(createImagesTable);
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


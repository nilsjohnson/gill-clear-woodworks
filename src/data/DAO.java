package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import data.ImageDbSchema.GalleriesTable;

import javax.servlet.ServletContext;

import com.gillccwoodworks.Constants;


public abstract class DAO
{
	
	public static String dbPath = "jdbc:sqlite:" + Constants.HOME + "gccwoodworksImages.db";
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
	
		String createGalleriesTable = "CREATE TABLE IF NOT EXISTS " + GalleriesTable.NAME + " ("
				+ GalleriesTable.Cols.GALLERY_NAME + " varchar(" + GalleriesTable.MAX_NAME_LENGTH + ") not null, "
				+ GalleriesTable.Cols.IMG_1_PATH + " varchar(" + GalleriesTable.MAX_PATH_LENGTH + "), "
				+ GalleriesTable.Cols.IMG_2_PATH + " varchar(" + GalleriesTable.MAX_PATH_LENGTH + "), "
				+ GalleriesTable.Cols.IMG_3_PATH + " varchar(" + GalleriesTable.MAX_PATH_LENGTH + "), "
				+ GalleriesTable.Cols.IMG_4_PATH + " varchar(" + GalleriesTable.MAX_PATH_LENGTH + "), "
				+ GalleriesTable.Cols.IMG_5_PATH + " varchar(" + GalleriesTable.MAX_PATH_LENGTH + "), "
				+ GalleriesTable.Cols.IMG_6_PATH + " varchar(" + GalleriesTable.MAX_PATH_LENGTH + "), "
				+"PRIMARY KEY (" + GalleriesTable.Cols.GALLERY_NAME + "));";
		
		try (Connection conn = DriverManager.getConnection(dbPath); Statement stmt = conn.createStatement())
		{
			stmt.execute(createGalleriesTable);
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


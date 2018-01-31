package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;


public abstract class DAO
{
	public static String dbPath = "jdbc:sqlite:/home/nils/gccwoodworks.db";
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
		
		/*String createGalleries = "CREATE TABLE IF NOT EXISTS Galleries ("
				+ "title varchar(50) not null,"
				+ "thumbnail1 varchar(50),"
				+ "thumbnail2 varchar(50),"
				+ "thumbnail3 varchar(50),"
				+ "thumbnail4 varchar(50),"
				+ "thumbnail5 varchar(50),"
				+ "thumbnail6 varchar(50),"
				+ "primary key (title)" 
				+ ");";
		
		// TODO img_1? re think this.
		String createMainSlider = "CREATE TABLE IF NOT EXISTS SliderImages ("
				+ "slider varchar(50) not null,"
				+ "img_1 varchar(50) not null,"
				+ "primary key (img_1)" 
				+ ");";
		
		
		try (Connection conn = DriverManager.getConnection(dbPath); Statement stmt = conn.createStatement())
		{
			stmt.execute(createGalleries);
			stmt.execute(createMainSlider);
			//System.out.println(createLeaderBoardTableQuery);
		}*/
		
		String createImagesTable = "CREATE TABLE IF NOT EXISTS Images ("
				+ "path varchar(50) not null,"
				+ "gallery varchar(50),"
				+ "galleryIndex integer not null,"
				+ "primary key (path)" 
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


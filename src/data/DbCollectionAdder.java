package data;

import java.util.ArrayList;

public class DbCollectionAdder
{
	public static void main(String[] args)
	{
		try
		{
			ImageDAO dao = new ImageDAO();
			ArrayList<String> list = new ArrayList<>();
			list.add("www.gillccwoodworks.com/img/carousel/spalted-maple-table.jpg");
			list.add("www.gillccwoodworks.com/img/carousel/glass-table.jpg");
			
			Collection col = new Collection("Sample Title", "Sample Desc.", list, null);
			
			dao.InsertCollection(col);
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}

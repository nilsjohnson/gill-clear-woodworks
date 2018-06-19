package data;
// TODO consider lengths
public class DbSchema
{
	public static final String UUID_LENGTH = "36";
	
	public static final class ImageTable
	{
		public static final String NAME = "Images";
		public static final String MAX_PATH_LENGTH = "200";
		
		public static final class COLS
		{
			public static final String UUID = "Img_UUID";
			public static final String PATH = "Path";
			public static final String INDEX = "Array_Index";
			public static final String COLLECTION_UUID = "Collection_UUID";
		}
	}
	
	public static final class CollectionTable
	{
		public static final String NAME = "Collections";
		public static final String MAX_NAME_LENGTH = "50";
		public static final String MAX_DESC_LENGTH = "500";
		
		public static final class COLS
		{
			public static final String UUID = "Collection_UUID";
			public static final String TITLE = "Title";
			public static final String DESC = "Description";
		}
	}
	
}

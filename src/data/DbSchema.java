package data;
// TODO consider lengths
public class DbSchema
{
	public static final class ImageTable
	{
		public static final String NAME = "Images";
		public static final String MAX_COLLECTION_ID_LENGTH = "36";
		public static final String MAX_PATH_LENGTH = "100";
		
		public static final class COLS
		{
			public static final String ID = "Id";
			public static final String PATH = "Path";
			public static final String COLLECTION_UUID = "Collection_UUID";
		}
	}
	
	public static final class CollectionTable
	{
		public static final String NAME = "Collections";
		public static final String MAX_NAME_LENGTH = "50";
		public static final String MAX_DESC_LENGTH = "50";
		public static final String UUID_LENGTH = "36";
		
		public static final class COLS
		{
			public static final String UUID = "UUID";
			public static final String TITLE = "Title";
			public static final String DESC = "Description";
		}
	}
	
}

package data;

public class ImageDbSchema
{
	public static final class ImageTable
	{
		public static final String NAME = "Images";
		public static final String MAX_PATH_LENGTH = "80";
		public static final String MAX_NAME_LENGTH = "40";
		
		public static final class Cols
		{
			public static final String PATH = "path";
			public static final String GALLERY_NAME = "galleryName";
			public static final String GALLERY_INDEX = "galleryIndex";
		}
	}
}

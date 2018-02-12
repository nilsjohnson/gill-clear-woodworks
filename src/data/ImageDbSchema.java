package data;

public class ImageDbSchema
{
	public static final class GalleriesTable
	{
		public static final String NAME = "Galleries";
		public static final String MAX_PATH_LENGTH = "80";
		public static final String MAX_NAME_LENGTH = "40";
		
		public static final class Cols
		{
			public static final String GALLERY_NAME = "galleryName";
			public static final String IMG_1_PATH = "img_1";
			public static final String IMG_2_PATH = "img_2";
			public static final String IMG_3_PATH = "img_3";
			public static final String IMG_4_PATH = "img_4";
			public static final String IMG_5_PATH = "img_5";
			public static final String IMG_6_PATH = "img_6";
		}
	}
}

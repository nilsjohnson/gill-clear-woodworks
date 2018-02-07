package com.gillccwoodworks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.servlet.ServletContext;

import com.amazonaws.auth.BasicAWSCredentials;

public class Constants
{
	// bucket where all images are stored
	public final static String BUCKET_NAME = "forestfriends";
	// flags main carousel title by its title
	public static final String CAROUSEL_TITLE = "MAIN_CAROUSEL";
	public static final int MAX_GALLERY_SIZE = 6;
	private static BasicAWSCredentials credentials = null;
	
	
	
	public static BasicAWSCredentials getAwsCreds(ServletContext context)
	{
		if(credentials != null)
		{
			return credentials;
		}
		
		String accessKey = null;
		String secretKey = null;

		String path = context.getRealPath("/") + "WEB-INF/" + "aws_cred.txt";

		File file = new File(path);
		System.out.print(file.getAbsolutePath());
		try
		{
			Scanner scanner = new Scanner(file);

			accessKey = scanner.nextLine();
			secretKey = scanner.nextLine();

			scanner.close();

		}

		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		credentials = new BasicAWSCredentials(accessKey, secretKey);
		
		return credentials;
	}
	
}

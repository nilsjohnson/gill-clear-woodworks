package com.gillccwoodworks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.UUID;

import javax.servlet.ServletContext;

import com.amazonaws.auth.BasicAWSCredentials;

public class Constants
{
	public static UUID CAROUSEL_UUID = UUID.fromString("9b44fe9a-790b-4438-b0ef-2b1ec9fe08b5");
	
	public static final String DB_LOCATION = "/home/nils/";
	
	// bucket where all images are stored
	public final static String BUCKET_NAME = "forestfriends";
	public static final String DB_NAME = "gillccwoodworks.db";
	private static BasicAWSCredentials credentials = null;
	
	// for image sizing
	public static int WIDTH = 1200;
	public static int HEIGHT = 800;
	public static double DEFAULT_RATIO = (double)WIDTH/(double)HEIGHT;
	
	public static String getHome(ServletContext context)
	{
		return  context.getRealPath("/") + "WEB-INF/";
	}
	
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
	
	public static String getUsername(ServletContext context)
	{
		String path = context.getRealPath("/") + "WEB-INF/" + "username.txt";
		String username = null;
		
		File file = new File(path);
		System.out.print(file.getAbsolutePath());
		try
		{
			Scanner scanner = new Scanner(file);
			username = scanner.nextLine();
			scanner.close();
		}

		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return username;
	}
	
	public static String getPassword(ServletContext context)
	{
		String path = context.getRealPath("/") + "WEB-INF/" + "password.txt";
		String password = null;
		
		File file = new File(path);
		System.out.print(file.getAbsolutePath());
		try
		{
			Scanner scanner = new Scanner(file);
			password = scanner.nextLine();
			scanner.close();

		}

		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return password;
	}
	
}

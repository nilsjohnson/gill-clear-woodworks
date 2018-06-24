package com.gillccwoodworks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import javax.servlet.ServletContext;

import com.amazonaws.auth.BasicAWSCredentials;

public class Constants
{
	
	//public static final String HOME = "/home/nils/";
	public static final String HOME = "/home/ubuntu/";
	
	// bucket where all images are stored
	public final static String BUCKET_NAME = "forestfriends";
	public static final String DB_NAME = "gillccwoodworks.db";
	private static BasicAWSCredentials credentials = null;
	
	// for image sizing
	public static int WIDTH = 1200;
	public static int HEIGHT = 800;
	public static double DEFAULT_RATIO = (double)WIDTH/(double)HEIGHT;
	
	
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

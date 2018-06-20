package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.servlet.http.Part;

import com.gillccwoodworks.Constants;

public class FileUtil
{
	/**
	 * Saves to the home directory under the given name.
	 * @param filePart
	 */
	public static void saveFile(Part filePart)
	{
		saveFile(filePart, Constants.HOME + filePart.getSubmittedFileName());
	}
	
	/**
	 * Saves to a given location.
	 * @param filePart
	 * @param location
	 */
	public static void saveFile(Part filePart, String location)
	{
		InputStream input = null;
		FileOutputStream output = null;
		
		// write file to destination
		try
		{
			input = filePart.getInputStream();
			output = new FileOutputStream(location);
			byte[] buf = new byte[1024];
			int bytesRead;

			while ((bytesRead = input.read(buf)) > 0)
			{
				output.write(buf, 0, bytesRead);
			}

			input.close();
			output.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			input = null;
			output = null;
		}
	}
	

	public static void deleteFile(String path)
	{
		File file = new File(path);
		try
		{
			Files.deleteIfExists(file.toPath());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

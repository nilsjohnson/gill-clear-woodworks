package data;

import java.util.ArrayList;
import java.util.UUID;

public interface IImageDAO
{
	ArrayList<Collection> getAllCollections() throws Exception;
	void InsertCollection(Collection collection) throws Exception;
	void DeleteCollection(UUID id) throws Exception;
	void DeleteImage(String path) throws Exception;
}

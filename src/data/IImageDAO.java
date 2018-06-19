package data;

import java.util.ArrayList;
import java.util.UUID;

public interface IImageDAO
{
	Collection getCollection(UUID uuid) throws Exception;
	ArrayList<Collection> getAllCollections() throws Exception;
	void insertCollection(Collection collection) throws Exception;
	void deleteCollection(UUID id) throws Exception;
	void deleteImage(UUID id) throws Exception;
}

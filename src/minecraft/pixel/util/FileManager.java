package pixel.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	private static final File rootDir = new File("pixel");
	private File file;
	
	private FileManager(String fileName) {
		file = new File(rootDir, fileName + ".json");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static FileManager create(String fileName) {
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		
		return new FileManager(fileName);
	}
	
	public void put(String key, Object value) {
		JSONObject jsonObject = readJsonFromFile();
		
		jsonObject.put(key, value);
		
		writeJsonToFile(jsonObject);
	}
	
	public Object get(String key) {
		JSONObject jsonObject = readJsonFromFile();
		
		return jsonObject.get(key);
	}
	
	public boolean containsKey(String key) {
		JSONObject jsonObject = readJsonFromFile();
		
		return jsonObject.containsKey(key);
	}
	
	private JSONObject readJsonFromFile() {
		JSONParser parser = new JSONParser();
		
		try (FileReader reader = new FileReader(file.getPath())) {
			return (JSONObject) parser.parse(reader);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			
			return new JSONObject();
		}
	}
	
	private void writeJsonToFile(JSONObject jsonObject) {
		try (FileWriter fileWriter = new FileWriter(file.getPath())) {
			fileWriter.write(jsonObject.toJSONString());
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

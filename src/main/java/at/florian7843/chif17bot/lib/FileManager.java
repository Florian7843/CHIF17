package at.florian7843.chif17bot.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class FileManager {

  /**
   * @param file file to check if exists
   * @return true if exists or false if not
   */
  public static boolean isFileExisting(File file){
    if(file.exists()) return true; else return false;
  }

  /**
   * @param file file to check entry from
   * @param key key to check is exists
   * @return true if existing otherwise false
   * @throws IOException
   */
  public static boolean isJsonEntryExisting(File file, String key) throws IOException {
    try {
      getJsonEntry(file, key);
      return true;
    }catch (JSONException ex){
      return false;
    }
  }

  /**
   * @param fileName is the name the file should be ex. config
   * @param path     is where the file should be ex. data/files/config/
   * @return returns the file created
   * @throws IOException
   */
  public static File createJsonFile(String fileName, String path) throws IOException {
    File file = new File(path + fileName + ".json");
    if (!file.exists()) {
      file.createNewFile();
    }
    return file;
  }

  /**
   * @param file the file to read the Json string of.
   * @return the read Json string
   * @throws IOException
   */
  public static String getJsonString(File file) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    String fileString = "", read;
    read = br.readLine();
    while (read != null) {
      fileString += read;
      read = br.readLine();
    }
    return fileString;
  }


  /**
   * Converts a for Humans hard to readable Json String to a pretty Json String
   *
   * @param uglyJson ex. {"key1":"value1","key2":"value2"}
   * @return new prettyJson string
   */
  public static String convertToPrettyJson(String uglyJson) {
    String prettyJson = "";

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonParser jp = new JsonParser();
    JsonElement je = jp.parse(uglyJson);

    prettyJson = gson.toJson(je);

    return prettyJson;
  }


  /**
   * Adds an entry to the Json file you want.
   * @param file file to write to
   * @param key key you get your value from
   * @param value value to get by key
   * @throws IOException
   */
  public static void addEntryToJsonFile(File file, String key, String value) throws IOException {
    JSONObject jsonObject;
    try{
      jsonObject = new JSONObject(getJsonString(file));
    }catch (JSONException ex){
      jsonObject = new JSONObject();
    }

    jsonObject.put(key, value);

    FileWriter fw = new FileWriter(file);
    fw.write(convertToPrettyJson(jsonObject.toString()));
    fw.flush();
  }

  public static String getJsonEntry(File file, String key) throws IOException, JSONException {
    if(file.exists()){
      JSONObject jsonObject = new JSONObject(getJsonString(file));
      try {
        return jsonObject.get(key).toString();
      }catch (JSONException ex){
        throw new JSONException("Json entry is not existing!");
      }
    } else {
      throw new FileNotFoundException("The file " + file.getPath() + "doesn't exist!");
    }
  }

  public static File createTxtFile(String fileName, String path) throws IOException {
    File file = new File(path + fileName + ".txt");
    if (!file.exists()) {
      file.createNewFile();
    }
    return file;
  }

  public static void addTxtFileEntry(File file, String entry) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
    writer.write(entry + "\n");
    writer.close();
  }
}

package supervinicius.util;

import java.io.*;
import java.net.URL;

public class FileManager {
    private static FileManager instance;

    private FileManager(){}

    public static FileManager getInstance() {
        if(instance == null) instance = new FileManager();
        return instance;
    }

    public File getFileFromResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if(resource == null) {
            throw new IllegalArgumentException("File not found!");
        }
        else return new File(resource.getFile());
    }

    public String getFileContent(File file) {
        if(file == null) return null;

        String result = "";

        try(FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader)) {

            String line;

            while((line = br.readLine()) != null) result = result.concat(line + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean writeToFile(String str, String name){
        try {
            PrintWriter writer = new PrintWriter( new File(name), "UTF-8");
            writer.println(str);
            writer.close();
            return true;
        }catch (Exception e){
            System.err.println(e);
            return false;
        }
    }
}

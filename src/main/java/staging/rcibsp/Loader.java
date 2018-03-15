package staging.rcibsp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Loader {
	
	private List<Reader> readers = new ArrayList<Reader>();
	
	public void setReader(Reader reader){
		readers.add(reader);
		
	}
	public List<Reader> getReader(){
		return readers;
	}
	public  List<Map<String, String>>  read (String inputFolder, Country country){
		List<Map<String, String>>  result = new ArrayList<Map<String, String>>();
		File file = new File(inputFolder);
		if (file.isDirectory()){
			for (File f : file.listFiles()){
				String absolutePath = f.getAbsolutePath();
				String basename = f.getName();
				String[] name = basename.split("\\.");
				//System.out.println("absolutePath---------------"+absolutePath);
				if (name.length == 2){
					String extension = Arrays.asList(name).get(1);
					result.addAll(readerHelper(absolutePath, basename, extension, country));
				}
			}
		}
		return result;
	}
	public  List<Map<String, String>>  readFile (String filename, Country country){
		List<Map<String, String>>  result = new ArrayList<Map<String, String>>();
		File file = new File(filename);
		if (file.isFile()){
			String absolutePath = file.getAbsolutePath();
			String basename = file.getName();
			String[] name = filename.split("\\.");
		    //System.out.println("absolutePath---------------"+absolutePath);
			if (name.length == 2){
				String extension = Arrays.asList(name).get(1);
				result.addAll(readerHelper(absolutePath, basename, extension, country));
			}
		}
		return result;
	}

	private List<Map<String, String>> readerHelper(String filePath, String fileName,
			String extension, Country country) {
		List<Map<String, String>>  result = new ArrayList<Map<String, String>>();
		// TODO Auto-generated method stub
		for (Reader r : readers){
		    if (r.hasExtension(extension)){
		    	try {
					 result.addAll( r.read(filePath, country));
					return result;
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		return result;
	}

}

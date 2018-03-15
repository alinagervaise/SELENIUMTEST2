package staging.rcibsp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Reader {
	public boolean hasExtension(String extension);
	public List<Map<String, String>> read(String filePath, Country country) throws IOException;
	
}

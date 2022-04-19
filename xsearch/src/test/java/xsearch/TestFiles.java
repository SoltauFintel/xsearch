package xsearch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class TestFiles {
	private final List<TestFile> testFiles = new ArrayList<>();
	
	public TestFiles() {
		try {
			File[] files = new File("testpages").listFiles();
			for (File file : files) {
				String content = readTextFile(file.toURI().toURL());
				testFiles.add(new TestFile(file.getName(), content));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String readTextFile(URL file) throws IOException {
		return IOUtils.toString(file.openStream(), "UTF-8");
	}

	public List<TestFile> getTestFiles() {
		return testFiles;
	}

	public static class TestFile {
		private final String path;
		private final String content;
		
		TestFile(String path, String content) {
			this.path = path;
			this.content = content;
		}

		public String getPath() {
			return path;
		}

		public String getContent() {
			return content;
		}
	}
}

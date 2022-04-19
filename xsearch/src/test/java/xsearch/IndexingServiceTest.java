package xsearch;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.mwvb.maja.mongo.Database;
import xsearch.TestFiles.TestFile;
import xsearch.indexing.HtmlParser;
import xsearch.indexing.IndexingService;
import xsearch.model.Page;

public class IndexingServiceTest {
	private final TestFiles testFiles = new TestFiles();
	
	@BeforeClass
	public static void openDatabase() {
		Database.open(Page.class);
	}

	@Test
	public void indexingPages() {
		String site = "TEST-1";
		IndexingService sv = new IndexingService();
		sv.createSite(site, "local", "en");
		Assert.assertEquals(0, (int) sv.size(site));
		
		for (TestFile f : testFiles.getTestFiles()) {
			sv.createPage(sv.site(site), f.getPath(), f.getContent());
		}
		
		Assert.assertEquals(testFiles.getTestFiles().size(), (int) sv.size(site));
	}
	
	@Test
	public void indexingText() {
		String site = "TEST-2";
		IndexingService sv = new IndexingService();
		sv.createSite(site, "local", "en");
		Assert.assertEquals(0, (int) sv.size(site));
		
		for (TestFile f : testFiles.getTestFiles()) {
			sv.createText(sv.site(site), f.getPath(), f.getPath(), new HtmlParser(f.getContent()).extractText());
		}
		
		Assert.assertEquals(testFiles.getTestFiles().size(), (int) sv.size(site));
	}
}

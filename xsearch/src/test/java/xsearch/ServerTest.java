package  xsearch;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pmw.tinylog.Level;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.mwvb.maja.mongo.Database;
import de.mwvb.maja.rest.RestCaller;
import de.mwvb.maja.rest.RestException;
import de.mwvb.maja.web.AppConfig;
import xsearch.TestFiles.TestFile;
import xsearch.indexing.CreatePageRequest;
import xsearch.indexing.CreateSiteRequest;
import xsearch.indexing.CreateTextRequest;
import xsearch.indexing.HtmlParser;
import xsearch.indexing.IndexingService;
import xsearch.model.Page;
import xsearch.model.SiteDAO;

/**
 * Tests with running server
 */
public class ServerTest {
	private static final Type listPageType = new TypeToken<ArrayList<Page>>(){}.getType();
	private static String baseUrl;
	private final String site = "test-3";
	private final String site4 = "test-4";
	private final TestFiles testFiles = new TestFiles();

	@BeforeClass
	public static void run() {
		Database.open(Page.class);
		new XSearchWebApp() {
			protected Level getDefaultLoggingLevel() {
				return Level.DEBUG;
			}
		}.start("");
		baseUrl = "http://localhost:" + new AppConfig().get("port");
	}
	
	@Test
	public void ping() throws IOException {
		String url = baseUrl + "/rest/_ping";
		String r = new RestCaller().get(url);
		Assert.assertEquals("pong", r);
	}

	@Test
	public void createSite() throws IOException {
		new IndexingService().deleteSite(site);
		Assert.assertNull(new SiteDAO().byName(site));

		CreateSiteRequest r = new CreateSiteRequest();
		r.setHost("local");
		r.setLanguage("en");
		new RestCaller().post(baseUrl + "/indexing/" + site, r);
		
		Assert.assertNotNull(new SiteDAO().byName(site));
	}
	
	@Test
	public void createPage() throws IOException {
		TestFile file = testFiles.getTestFiles().get(0);
		createSite();
		
		CreatePageRequest r = new CreatePageRequest();
		r.setPath(file.getPath());
		r.setHtml(file.getContent());
		new RestCaller().post(baseUrl + "/indexing/" + site + "/page", r);
	}
	
	@Test
	public void createText() throws IOException {
		TestFile file = testFiles.getTestFiles().get(1);
		createSite();
		
		CreateTextRequest r = new CreateTextRequest();
		r.setPath(file.getPath());
		HtmlParser h = new HtmlParser(file.getContent());
		r.setText(h.extractText());
		r.setTitle(h.extractTitle());
		new RestCaller().post(baseUrl + "/indexing/" + site + "/text", r);
	}
	
	@Test
	public void wordSearch() throws IOException {
		prepareData();
		
		String s = new RestCaller().get(baseUrl + "/search/" + site4 + "?q=vogel");
		System.out.println("vogel -> response: " + s);
		List<Page> pages = new Gson().fromJson(s, listPageType);
		System.out.println(pages.size());
		Assert.assertEquals(2, pages.size());
	}

	@Test
	public void containsSearch() throws IOException {
		prepareData();
		
		String s = new RestCaller().get(baseUrl + "/search/" + site4 + "?q=piep");
		System.out.println("piep -> response: " + s);
		List<Page> pages = new Gson().fromJson(s, listPageType);
		System.out.println(pages.size());
		Assert.assertEquals(1, pages.size());
		Assert.assertEquals("1", pages.get(0).getPath());
	}

	@Test(expected = RestException.class)
	public void wrongSite() throws IOException {
		new RestCaller().get(baseUrl + "/search/quatsch?q=vogel");
	}

	@Test(expected = RestException.class)
	public void emptyQ() throws IOException {
		createSite();
		new RestCaller().get(baseUrl + "/search/" + site + "?q=");
	}

	@Test(expected = RestException.class)
	public void missingQ() throws IOException {
		createSite();
		new RestCaller().get(baseUrl + "/search/" + site);
	}

	private void prepareData() throws IOException {
		new IndexingService().deleteSite(site4);

		// site anlegen
		CreateSiteRequest r = new CreateSiteRequest();
		r.setHost("local");
		r.setLanguage("de");
		new RestCaller().post(baseUrl + "/indexing/" + site4, r);
		
		// texte speichern
		CreateTextRequest p = new CreateTextRequest();
		p.setPath("1");
		p.setText("Ein Vogel sitzt auf der Mauer und piept.");
		p.setTitle("Text 1");
		new RestCaller().post(baseUrl + "/indexing/" + site4 + "/text", p);
		p.setPath("2");
		p.setText("Die Vögel futtern im Garten und singen.");
		p.setTitle("Text 2");
		new RestCaller().post(baseUrl + "/indexing/" + site4 + "/text", p);
	}
}

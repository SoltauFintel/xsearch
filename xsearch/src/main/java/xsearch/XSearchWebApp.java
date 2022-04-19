package xsearch;

import de.mwvb.maja.mongo.Database;
import de.mwvb.maja.web.AbstractWebApp;
import xsearch.indexing.CreatePageAction;
import xsearch.indexing.CreateSiteAction;
import xsearch.indexing.CreateTextAction;
import xsearch.model.Page;
import xsearch.search.SearchAction;

public class XSearchWebApp extends AbstractWebApp {
	public static final String VERSION = "0.1.0";
	
	@Override
	protected void routes() {
		_post("/indexing/:name/page", CreatePageAction.class);
		_post("/indexing/:name/text", CreateTextAction.class);
		_post("/indexing/:name", CreateSiteAction.class);
		
		_get("/search/:name", SearchAction.class);
	}

	public static void main(String[] args) {
		new XSearchWebApp().start(VERSION);
	}
	
	@Override
	protected void initDatabase() {
        Database.open(Page.class);
    }
}

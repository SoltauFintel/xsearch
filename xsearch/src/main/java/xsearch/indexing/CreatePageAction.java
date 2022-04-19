package xsearch.indexing;

import org.pmw.tinylog.Logger;

import com.google.gson.Gson;

import de.mwvb.maja.web.ActionBase;

public class CreatePageAction extends ActionBase {
	
	@Override
	protected void execute() {
		String name = req.params("name");
		CreatePageRequest r = new Gson().fromJson(req.body(), CreatePageRequest.class);
		
		IndexingService sv = new IndexingService();
		sv.createPage(sv.site(name), r.getPath(), r.getHtml());
		Logger.debug("created page: " + r.getPath());
	}
}

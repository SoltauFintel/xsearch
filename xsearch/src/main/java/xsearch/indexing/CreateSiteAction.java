package xsearch.indexing;

import org.pmw.tinylog.Logger;

import com.google.gson.Gson;

import de.mwvb.maja.web.ActionBase;

public class CreateSiteAction extends ActionBase {

	@Override
	protected void execute() {
		String name = req.params("name");
		CreateSiteRequest r = new Gson().fromJson(req.body(), CreateSiteRequest.class);
		new IndexingService().createSite(name, r.getHost(), r.getLanguage());
		Logger.debug("Created site: " + name + ", language: " + r.getLanguage());
	}
}

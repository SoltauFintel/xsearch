package xsearch.search;

import org.pmw.tinylog.Logger;

import de.mwvb.maja.rest.ErrorMessage;
import de.mwvb.maja.web.JsonAction;

public class SearchAction extends JsonAction {
	
	@Override
	protected void execute() {
		String name = req.params("name");
		try {
			String q = req.queryParams("q");
			result = new SearchService().search(name, q);
		} catch (Exception e) {
			Logger.error(name + " | " + (e.getMessage() == null ? e.getClass().getName() : e.getMessage()));
			res.status(500);
			result = new ErrorMessage(e);
		}
	}
}

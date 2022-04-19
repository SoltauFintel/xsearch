package xsearch.indexing;

import org.pmw.tinylog.Logger;

import com.google.gson.Gson;

import de.mwvb.maja.web.ActionBase;
import xsearch.model.SiteDAO;

public class CreateTextAction extends ActionBase {
	private String rr = "";
	
	@Override
	protected void execute() {
		String name = req.params("name");
		if (new SiteDAO().byName(name) == null) {
			res.status(404);
			rr = "site not found";
			Logger.error("site not found: " + name);
			return;
		}
		CreateTextRequest r = new Gson().fromJson(req.body(), CreateTextRequest.class);
		if (r.getText() == null || r.getText().isEmpty()) {
			res.status(500);
			rr = "text must not be empty";
			Logger.error(rr);
			return;
		}
		if (r.getTitle() == null || r.getTitle().trim().isEmpty()) {
			res.status(500);
			rr = "title must not be empty";
			Logger.error(rr);
			return;
		}
		
		IndexingService sv = new IndexingService();
		sv.createText(sv.site(name), r.getPath(), r.getTitle(), r.getText());
		Logger.debug("created text: " + r.getPath());
	}
	
	@Override
	protected String render() {
		return rr;
	}
}

package xsearch.search;

import java.util.List;

import xsearch.model.Page;
import xsearch.model.PageDAO;
import xsearch.model.Site;
import xsearch.model.SiteDAO;

public class SearchService {
	private final SiteDAO siteDAO = new SiteDAO();
	private final PageDAO dao = new PageDAO();
	
	public List<Page> search(String name, String x) {
		if (x == null || x.trim().isEmpty()) {
			throw new RuntimeException("Please specify search word.");
		}
		Site site = siteDAO.byName(name);
		if (site == null) {
			throw new RuntimeException("Site does not exist.");
		}
		return dao.search(site.getName(), x, site.getLanguage(), true);
	}
}

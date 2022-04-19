package xsearch.indexing;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.mwvb.maja.mongo.AbstractDAO;
import xsearch.model.Page;
import xsearch.model.PageDAO;
import xsearch.model.Site;
import xsearch.model.SiteDAO;

public class IndexingService {
	private final SiteDAO siteDAO = new SiteDAO();
	private final PageDAO dao = new PageDAO();

	public void createSite(String name, String host, String language) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("site name must not be empty");
		}
		deleteSite(name);
		Site site = new Site();
		site.setId(AbstractDAO.code6(AbstractDAO.genId()));
		site.setName(name);
		site.setLanguage(language == null ? "" : language);
		site.setHost(host == null ? "" : host);
		site.setCreated(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		siteDAO.save(site);
	}
	
	public Site site(String name) {
		Site site = siteDAO.byName(name);
		if (site == null) {
			throw new RuntimeException("Site does not exist!");
		}
		return site;
	}

	public void createPage(Site site, String path, String html) {
		HtmlParser h = new HtmlParser(html);
		createText(site, path, h.extractTitle(), h.extractText());
	}
	
	private void validate(String content, String name) {
		if (content == null || content.trim().isEmpty()) {
			throw new IllegalArgumentException(name + " must not be empty");
		}
	}

	public void createText(Site site, String path, String title, String content) {
		if (site == null) {
			throw new IllegalArgumentException("site must not be null");
		}
		validate(path, "path");
		if (title == null) {
			throw new IllegalArgumentException("title must not be null");
		}
		if (content == null) {
			throw new IllegalArgumentException("content must not be null");
		}

		Page page = new Page();
		page.setId(AbstractDAO.genId());
		page.setSite(site.getName());
		page.setPath(path);
		page.setContent(content);
		page.setTitle(title);
		dao.deleteByPath(site.getName(), path);
		dao.save(page);
	}

	public long size(String siteId) {
		if (siteDAO.byName(siteId) == null) {
			throw new RuntimeException("Site not found: " + siteId);
		}
		return dao.sizeBySite(siteId);
	}
	
	public void deleteSite(String name) {
		dao.deleteSite(name);
		siteDAO.deleteByName(name);
	}
}

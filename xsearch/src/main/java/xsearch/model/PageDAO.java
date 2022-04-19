package xsearch.model;

import java.util.List;

import org.mongodb.morphia.query.Meta;
import org.pmw.tinylog.Logger;

import de.mwvb.maja.mongo.AbstractDAO;

public class PageDAO extends AbstractDAO<Page> {

	@Override
	protected Class<Page> getEntityClass() {
		return Page.class;
	}
	
	public long sizeBySite(String site) {
		return createQuery().field("site").equal(site).count();
	}

	public List<Page> search(String site, String x, String language, boolean withContainsSearchFallback) {
		List<Page> ret = createQuery()
			.field("site").equal(site)
			.search(x, language)
			.project("path", true)
			.project("title", true)
			.project(Meta.textScore())
			.order(Meta.textScore())
			.asList();
		if (ret.isEmpty() && withContainsSearchFallback) {
			Logger.debug("contains fallback: " + x);
			ret = contains(site, x);
		}
		return ret;
	}

	public List<Page> contains(String site, String x) {
		return createQuery()
			.field("site").equal(site)
			.field("content").containsIgnoreCase(x)
			.project("path", true)
			.project("title", true)
			.asList();
	}

	public void deleteSite(String site) {
		ds().delete(createQuery().field("site").equal(site));
	}

	public void deleteByPath(String site, String path) {
		ds().delete(createQuery()
				.field("site").equal(site)
				.field("path").equal(path));
	}
}

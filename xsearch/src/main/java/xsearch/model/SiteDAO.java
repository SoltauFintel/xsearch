package xsearch.model;

import de.mwvb.maja.mongo.AbstractDAO;

public class SiteDAO extends AbstractDAO<Site> {

	@Override
	protected Class<Site> getEntityClass() {
		return Site.class;
	}

	public void deleteByName(String name) {
		ds().delete(createQuery().field("name").equal(name));
	}

	public Site byName(String siteId) {
		return createQuery().field("name").equal(siteId).get();
	}
}

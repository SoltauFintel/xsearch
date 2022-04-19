package xsearch.indexing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class HtmlParser {
	// http://jericho.htmlparser.net/samples/console/src/ExtractText.java
	private final Source source;
	
	public HtmlParser(String html) {
		source = new Source(html);
	}
	
	public String extractText() {
		return source.getTextExtractor().toString();
	}
	
	public String extractTitle() {
		Element titleElement = source.getFirstElement(HTMLElementName.TITLE);
		if (titleElement == null) {
			return "";
		}
		// TITLE element never contains other tags so just decode it collapsing
		// whitespace:
		return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
	}
	
	public Set<Link> extractLinks() {
		Set<Link> links = new HashSet<>();
		List<Element> linkElements = source.getAllElements(HTMLElementName.A);
		for (Element linkElement : linkElements) {
			String href = linkElement.getAttributeValue("href");
			if (href != null) {
				String label = linkElement.getContent().getTextExtractor().toString();
				links.add(new Link(label, href));
			}
		}
		return links;
	}

	public static class Link {
		private final String text;
		private final String url;

		public Link(String text, String url) {
			this.text = text;
			this.url = url;
		}

		public String getText() {
			return text;
		}

		public String getUrl() {
			return url;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((url == null) ? 0 : url.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Link other = (Link) obj;
			if (url == null) {
				if (other.url != null)
					return false;
			} else if (!url.equals(other.url))
				return false;
			return true;
		}
	}
}

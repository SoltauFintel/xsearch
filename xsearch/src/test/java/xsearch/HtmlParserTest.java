package xsearch;

import org.junit.Assert;
import org.junit.Test;

import xsearch.indexing.HtmlParser;

public class HtmlParserTest {

	@Test
	public void simple() {
		Assert.assertEquals("Hello", new HtmlParser("<p>Hello</p>").extractText());
	}

	@Test
	public void head() {
		Assert.assertEquals("very good", new HtmlParser("<html>"
				+ "<head><style special=\"bad\">worse</style></head>"
				+ "<body>very<p>good</p></body>"
				+ "</html>").extractText());
	}
	
	@Test
	public void span() {
		Assert.assertEquals("HelloHTML", new HtmlParser("<p>Hello<span>HTML</span></p>").extractText());
		Assert.assertEquals("Hello HTML", new HtmlParser("<p>Hello</p><p>HTML</p>").extractText());
	}
}

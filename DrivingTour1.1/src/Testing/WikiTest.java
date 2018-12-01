package Testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import WikipediaSearch.Wiki;
/**
 * Tests for class Wiki
 * @author George Greenwood
 *
 */
class WikiTest {

	@Test
	void test1() {
		String url= "https://en.wikipedia.org/w/api.php?action=query&format=json&list=geosearch&gscoord=51.0|-1.0&gsradius=100&gslimit=20";
		String actualUrl= Wiki.buildUrl(51.00, -1.00, 100); 
			
		assertEquals(url, actualUrl);
	}
	
	@Test
	void test2() {
		String url= "https://en.wikipedia.org/w/api.php?action=query&format=json&list=geosearch&gscoord=0.0|0.0&gsradius=0&gslimit=20";
		String actualUrl= Wiki.buildUrl(0.0, 0.0, 0); 
			
		assertEquals(url, actualUrl);
	}
	
	@Test
	void test3() {
		String url= "https://en.wikipedia.org/w/api.php?action=query&format=json&list=geosearch&gscoord=0.0|0.0&gsradius=0&gslimit=20";
		String actualUrl= Wiki.buildUrl(0, 0, 0); 
			
		assertEquals(url, actualUrl);
	}
	
	@Test
	void test4() {
		String url= "https://en.wikipedia.org/w/api.php?action=query&format=json&list=geosearch&gscoord=1.11|1.11&gsradius=111&gslimit=20";
		String actualUrl= Wiki.buildUrl(1.11, 1.11, 111); 
			
		assertEquals(url, actualUrl);
	}
	
	@Test
	void test5() {
		String a= "two";
		String b= Wiki.getExtract("one\"extract\":two"); 
			
		assertEquals(a,b);
	}
	
	@Test
	void test6() {
		String a= "two";
		String b= Wiki.getExtract("\"extract\":two"); 
			
		assertEquals(a,b);
	}
	
	@Test( )
	void test7() {

		String a= "";
		String b= Wiki.getExtract("\"extract\":"); 
			
		assertEquals(a,b);
	}
	
	@Test
	void test8() {
		String a= "";
		String b= Wiki.getExtract("onetwo"); 
			
		assertEquals(a,b);
	}
	
	@Test
	void test9() {
		String a= "one two";
		String b= Wiki.getExtract("one\"extract\":one" +"\""  + "two"); 
			
		assertEquals(a,b);
	}
	
	@Test
	void test10() {
		String a= "two";
		Wiki.findImage("one\"source\":\"two}}");
		String b = Wiki.imageUrl; 
		assertEquals(a,b);
	}
	
	@Test
	void test11() {
		String a= "two";
		Wiki.findImage("\"source\":\"two}}");
		String b = Wiki.imageUrl; 
		assertEquals(a,b);
	}
	
	@Test
	void test12() {
		String a= "no image";
		Wiki.findImage("");
		String b = Wiki.imageUrl; 
		assertEquals(a,b);
	}
	
	@Test
	void test13() {
		String a= "no image";
		Wiki.findImage("onetwo");
		String b = Wiki.imageUrl; 
		assertEquals(a,b);
	}
	
	@Test
	void test14() {
		String a="one. two";
		String b=Wiki.reduceExtract("one. two. three. four. five");
		
		assertEquals(a,b);
	}
	
	@Test
	void test15() {
		String a="one. ";
		String b=Wiki.reduceExtract("one. ");
		
		assertEquals(a,b);
	}
	
	@Test
	void test16() {
		String a="";
		String b=Wiki.reduceExtract("");
		
		assertEquals(a,b);
	}
	
	
}

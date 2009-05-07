package se.vgr.webbisar.types;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.junit.Test;

public class TestWebbis {

	ClassValidator<Webbis> validator = new ClassValidator<Webbis>( Webbis.class, ResourceBundle.getBundle("se.vgr.webbisar.types.ValidatorMessages", new Locale("sv")) );

	
	@Test
	public void testOk() throws Exception {
		
		List<Name> parents = new ArrayList<Name>();
		List<Image> images = new ArrayList<Image>();
		
		parents.add(new Name("Gunnar","Bohlin"));
		parents.add(new Name("Jenny","Lind"));
		images.add(new Image("images/12343.jpg", "Detta är en fin bild"));
		images.add(new Image("images/12343.jpg", "Detta är en fin bild"));
		
		Webbis w = new Webbis("Kalle", "someId",Sex.Male, new BirthTime(2009,1,2,14,33),
			       2345, 55, Hospital.KSS, "Mölndal" ,parents, images, "Johanna",
			       "Ett meddelande", "email@email.se", "http://www.blog.se/mamma");
		
		InvalidValue [] invalidValues = validator.getInvalidValues(w);
		assertEquals(getInvalidValues(invalidValues), 0, invalidValues.length);
	}
	
	@Test
	public void testEmailFail() throws Exception {
		
		List<Name> parents = new ArrayList<Name>();
		List<Image> images = new ArrayList<Image>();
		
		parents.add(new Name("Gunnar","Bohlin"));
		parents.add(new Name("Jenny","Lind"));
		images.add(new Image("images/12343.jpg", "Detta är en fin bild"));
		images.add(new Image("images/12343.jpg", "Detta är en fin bild"));
		
		Webbis w = new Webbis("Kalle", "someId",Sex.Male, new BirthTime(2009,1,2,14,33),
			       2345, 55, Hospital.KSS, "Mölndal" ,parents, images, "Johanna",
			       "Ett meddelande", "email", "http://www.blog.se/mamma");
		
		InvalidValue [] invalidValues = validator.getInvalidValues(w);
		assertEquals("Unexpected " + getInvalidValues(invalidValues), 1, invalidValues.length);
		assertEquals("Expected failing email", "email", invalidValues[0].getPropertyName());
	}

	@Test
	public void testFailUrl() throws Exception {
		
		List<Name> parents = new ArrayList<Name>();
		List<Image> images = new ArrayList<Image>();
		

		parents.add(new Name("Gunnar","Bohlin"));
		parents.add(new Name("Jenny","Lind"));
		images.add(new Image("images/12343.jpg", "Detta är en fin bild"));
		images.add(new Image("images/12343.jpg", "Detta är en fin bild"));
		
		Webbis w = new Webbis("Kalle", "someId",Sex.Male, new BirthTime(2009,1,2,14,33),
			       2345, 55, Hospital.KSS, "Mölndal" ,parents, images, "Johanna",
			       "Ett meddelande", "email@email.se", "www.blog.se/mamma");
		
		InvalidValue [] invalidValues = validator.getInvalidValues(w);
		assertEquals("Unexpected " + getInvalidValues(invalidValues), 1, invalidValues.length);
		assertEquals("Expected failing homePage", "homePage", invalidValues[0].getPropertyName());
		
		w = new Webbis("Kalle", "someId",Sex.Male, new BirthTime(2009,1,2,14,33),
			       2345, 55, Hospital.KSS, "Mölndal" ,parents, images, "Johanna",
			       "Ett meddelande", "email@email.se", "http:/www.blog.se/mamma");
		
		invalidValues = validator.getInvalidValues(w);
		assertEquals("Unexpected " + getInvalidValues(invalidValues), 1, invalidValues.length);
		assertEquals("Expected failing homePage", "homePage", invalidValues[0].getPropertyName());

		w = new Webbis("Kalle", "someId",Sex.Male, new BirthTime(2009,1,2,14,33),
			       2345, 55, Hospital.KSS, "Mölndal" ,parents, images, "Johanna",
			       "Ett meddelande", "email@email.se", "http://www.blog.se/mamma?hello=2&id=88%3D");
		
		invalidValues = validator.getInvalidValues(w);
		assertEquals("Unexpected " + getInvalidValues(invalidValues), 0, invalidValues.length);
		
		w = new Webbis("Kalle", "someId",Sex.Male, new BirthTime(2009,1,2,14,33),
			       2345, 55, Hospital.KSS, "Mölndal" ,parents, images, "Johanna",
			       "Ett meddelande", "email@email.se", "");
		
		invalidValues = validator.getInvalidValues(w);
		assertEquals("Unexpected " + getInvalidValues(invalidValues), 0, invalidValues.length);
		
	}
	
	@Test
	public void testBirthDate() throws Exception {
		List<Name> parents = new ArrayList<Name>();
		List<Image> images = new ArrayList<Image>();
		

		parents.add(new Name("Gunnar","Bohlin"));
		parents.add(new Name("Jenny","Lind"));
		images.add(new Image("images/12343.jpg", "Detta är en fin bild"));
		images.add(new Image("images/12343.jpg", "Detta är en fin bild"));
		
		Webbis w = new Webbis("Kalle", "someId",Sex.Male, new BirthTime(2018,1,2,14,33),
			       2345, 55, Hospital.KSS, "Mölndal" ,parents, images, "Johanna",
			       "Ett meddelande", "email@email.se", "http://www.blog.se/mamma");
		
		InvalidValue [] invalidValues = validator.getInvalidValues(w);
		assertEquals("Unexpected " + getInvalidValues(invalidValues), 1, invalidValues.length);
		assertEquals("Expected failing birthTime", "birthTime", invalidValues[0].getPropertyName());		
	}
	
	private String getInvalidValues(InvalidValue[] values) {
		String ret = "";
		for(InvalidValue v : values) {
			ret += "[" + v.getPropertyName() + ":" + v.getMessage() + "]";
		}
		return ret;
	}
}

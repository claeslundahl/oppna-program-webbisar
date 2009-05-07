package se.vgr.webbisar.tool;

import java.util.Date;
import java.util.Random;

import se.vgr.webbisar.types.Name;

public class NameGenerator {

	static final Random rand = new Random(new Date().getTime());
	
	public static Name generateMaleName() {
		String fName = firstNameMale[rand.nextInt(firstNameMale.length)];
		String lName = lastName[rand.nextInt(lastName.length)];
		return new Name(fName,lName);
	}
	
	public static Name generateFemaleName() {
		String fName = firstNameFemale[rand.nextInt(firstNameFemale.length)];
		String lName = lastName[rand.nextInt(lastName.length)];
		return new Name(fName,lName);
	}
	
	public static String generateWebbisName(){
		return webbisName[rand.nextInt(webbisName.length)];
	}
	
	static final String [] firstNameMale = new String[]{ 
		"Erik", 
		"Lars", 
		"Karl", 
		"Anders", 
		"Per", 
		"Johan", 
		"Nils", 
		"Lennart", 
		"Jan", 
		"Hans", 
		"Olof", 
		"Mikael", 
		"Gunnar", 
		"Sven", 
		"Carl", 
		"Peter", 
		"Bengt", 
		"Åke", 
		"Fredrik", 
		"Göran", 
		"Bo"
	};
	static final String [] firstNameFemale = new String[]{ 
		"Maria", 
		"Anna", 
		"Margareta", 
		"Elisabeth", 
		"Eva", 
		"Birgitta", 
		"Kristina", 
		"Karin", 
		"Elisabet", 
		"Marie", 
		"Ingrid", 
		"Christina", 
		"Linnéa", 
		"Marianne", 
		"Kerstin", 
		"Sofia", 
		"Lena", 
		"Helena", 
		"Inger", 
		"Johanna", 
		"Emma"
	};
	static final String [] lastName = new String[]{ 
		"Johansson", 
		"Andersson", 
		"Karlsson", 
		"Nilsson", 
		"Eriksson", 
		"Larsson", 
		"Olsson", 
		"Persson", 
		"Svensson", 
		"Gustafsson", 
		"Pettersson", 
		"Jonsson", 
		"Jansson", 
		"Hansson", 
		"Bengtsson", 
		"Jönsson", 
		"Petersson", 
		"Carlsson", 
		"Lindberg", 
		"Magnusson", 
		"Gustavsson", 
		"Olofsson" 
	};
	
	static final String [] webbisName = new String [] {
		"fWilma",
		"pWilliam",
		"pLucas",
		"pElias",
		"fElla",
		"fMaja",
		"fEmma",
		"fJulia",
		"fAlice",
		"pOscar",
		"pHugo",
		"pViktor",
		"pFilip",
		"pErik",
		"fLinnea",
		"fAlva",
		"fIda",
		"pIsak",
		"pLiam",
		"pGustav",
		"fOliver",
		"pLeo"		
	};
}

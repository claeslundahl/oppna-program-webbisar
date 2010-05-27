/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.webbisar.tool;

import java.util.Date;
import java.util.Random;

import se.vgregion.webbisar.types.Name;

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

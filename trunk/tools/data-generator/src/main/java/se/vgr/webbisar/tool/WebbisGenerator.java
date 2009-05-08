package se.vgr.webbisar.tool;

import static se.vgr.webbisar.tool.NameGenerator.generateFemaleName;
import static se.vgr.webbisar.tool.NameGenerator.generateMaleName;
import static se.vgr.webbisar.tool.NameGenerator.generateWebbisName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import se.vgr.webbisar.svc.WebbisService;
import se.vgr.webbisar.types.BirthTime;
import se.vgr.webbisar.types.Hospital;
import se.vgr.webbisar.types.Image;
import se.vgr.webbisar.types.Name;
import se.vgr.webbisar.types.Sex;
import se.vgr.webbisar.types.Webbis;
public class WebbisGenerator {

	private static Random rand = new Random(new Date().getTime());
	 private static final String IMAGES_URL_ROOT = "http://localhost/~sofiajonsson/images/";
		
	/*
	 *private static final String IMAGES_URL_ROOT = "http://140.166.83.38:8080/images/";

	/*
	 *

	
	private static final String IMAGES_URL_ROOT = "http://140.166.208.204/~parwen/webbisar/images/";
	*/
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new WebbisGenerator().run();
	}
	
	public void run() {
		AbstractRefreshableApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"services-config.xml","applicationContext-hibernate.xml"});
		
		WebbisService svc = (WebbisService) ctx.getBean("webbisService");
		
		SortedSet<Webbis> webbisar = new TreeSet<Webbis>(new Comparator<Webbis>(){
			public int compare(Webbis o1, Webbis o2) {
				if(o1.getBirthTime().compareTo(o2.getBirthTime()) == 0){
					return -1;
				}
				return o1.getBirthTime().compareTo(o2.getBirthTime());
			}
		});
		for(int i = 0 ; i < 20000 ; i++) {
			webbisar.add(generateWebbis());					
		}
		svc.saveAll(webbisar);
		
		ctx.close();
	}
	
	private Webbis generateWebbis() {
		String n =  generateWebbisName();
		String name = (rand.nextInt(5) != 0 ) ? n.substring(1) : null;
		Sex sex = n.charAt(0) == 'f' ? Sex.Female : Sex.Male; 
		
		
		BirthTime birthTime = getBirthTime();
		
		int weight = 2000 + rand.nextInt(3000);
		int length = 40 + rand.nextInt(15);
		
		Hospital hospital = generateHospital();
		String home = "Göteborg";
		
		List<Name> parents = new ArrayList<Name>();
		parents.add(generateFemaleName());
		parents.add(generateMaleName());
		
		List<Image> images = new ArrayList<Image>();
		images.add(generateImage());
		for(int i = 0 ; i < rand.nextInt(3); i++) {
			images.add(generateImage());
		}
		
		String siblings = generateSiblings();
		String authorId = generateAuthorId();
		
		String message = "Välkommen till världen, åh vad vi har längtat efter dig!";
		String email = "mamma@pappa.se";
		String homePage = "http://www.blogg.se./mamma";
		
		return new Webbis(name, authorId, sex, birthTime, weight, length, hospital, home, parents, images, siblings, message, email, homePage);
	}
	
	private String generateSiblings() {
		int numSiblings = rand.nextInt(4);
		String siblings = "";
		if(numSiblings == 1) siblings = generateWebbisNameNoGender();
		if(numSiblings == 2) siblings = generateWebbisNameNoGender() + " och " + generateWebbisNameNoGender();
		if(numSiblings == 3) siblings = generateWebbisNameNoGender() + ", " + generateWebbisNameNoGender() + " och " + generateWebbisNameNoGender();
		
		return siblings;
	}

	private String generateWebbisNameNoGender() {
		return generateWebbisName().substring(1);
	}

	private Hospital generateHospital() {
		int l = Hospital.values().length;
		return Hospital.values()[rand.nextInt(l - 1)]; // remove unknown
	}

	private Image generateImage() {
		int n = rand.nextInt(11);
		//return new Image(IMAGES_URL_ROOT+ n +".jpg", "Lilla gosan");
		return new Image(n +".jpg", "lilla gosan");
	}
	
	private String generateAuthorId() {
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < 10 ; i++) {
			sb.append(rand.nextInt(10));
		}
		return sb.toString();
	}
	
	private BirthTime getBirthTime() {
		
		DateTime now = new DateTime();
		DateTime oneYearBack = now.minusYears(1);
		
		int secondsInYear = (int) ( (now.getMillis() - oneYearBack.getMillis()) / 1000L );
		
		int randomSecond = rand.nextInt(secondsInYear);
		
		DateTime bd = new DateTime(oneYearBack.getMillis() + randomSecond*1000L);
		return new BirthTime(bd.toDate());
	}
}
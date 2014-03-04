package metrics;

import java.util.ArrayList;
import java.util.HashMap;

public class GoogleSample {
	/** <query, urls> */
	public static HashMap<String, ArrayList<String>> sample = new HashMap<>();
	
	public static final int EVAL_NUM = 5;
	
	// XXX: hard coding
	public static void init() {
		// mondego
		sample.put("mondego", new ArrayList<String>());
		sample.get("mondego").add("http://mondego.ics.uci.edu/");
		sample.get("mondego").add("http://sdcl.ics.uci.edu/mondego-group/");
		sample.get("mondego").add("http://mailman.ics.uci.edu/mailman/listinfo/mondego");
		sample.get("mondego").add("http://mailman.ics.uci.edu/mailman/admin/mondego");
		sample.get("mondego").add("http://nile.ics.uci.edu:9000/wifi");
		
		// machine learning
		sample.put("machine learning", new ArrayList<String>());
		sample.get("machine learning").add("http://archive.ics.uci.edu/ml/");
		sample.get("machine learning").add("http://archive.ics.uci.edu/ml/datasets.html");
		sample.get("machine learning").add("http://cml.ics.uci.edu/");
		sample.get("machine learning").add("http://archive.ics.uci.edu/ml/datasets/Auto+MPG");
		sample.get("machine learning").add("http://mlearn.ics.uci.edu/MLRepository.html");

		// software engineering
		sample.put("software engineering", new ArrayList<String>());
		sample.get("software engineering").add("http://www.ics.uci.edu/prospective/en/degrees/software-engineering/");
		sample.get("software engineering").add("http://www.ics.uci.edu/grad/degrees/degree_se.php");
		sample.get("software engineering").add("http://se.ics.uci.edu/");
		sample.get("software engineering").add("http://www.ics.uci.edu/faculty/area/area_software.php");
		sample.get("software engineering").add("http://www.ics.uci.edu/~djr/DebraJRichardson/SE4S.html");
		
		// security
		sample.put("security", new ArrayList<String>());
		sample.get("security").add("http://www.ics.uci.edu/computing/linux/security.php");
		sample.get("security").add("http://sconce.ics.uci.edu/");
		sample.get("security").add("http://www.ics.uci.edu/faculty/area/area_security.php");
		sample.get("security").add("http://sprout.ics.uci.edu/projects/privacy-dna/");
		sample.get("security").add("http://sprout.ics.uci.edu/past_projects/odb/");
		
		// student affairs
		sample.put("student affairs", new ArrayList<String>());
		sample.get("student affairs").add("http://www.ics.uci.edu/about/search/search_sao.php");
		sample.get("student affairs").add("http://www.ics.uci.edu/prospective/en/contact/student-affairs/");
		sample.get("student affairs").add("http://www.ics.uci.edu/ugrad/sao/");
		sample.get("student affairs").add("http://www.ics.uci.edu/grad/sao/");
		sample.get("student affairs").add("http://www.ics.uci.edu/about/visit/");
		
		// graduate courses
		sample.put("graduate courses", new ArrayList<String>());
		sample.get("graduate courses").add("http://www.ics.uci.edu/grad/courses/");
		sample.get("graduate courses").add("http://www.ics.uci.edu/grad/");
		sample.get("graduate courses").add("http://www.ics.uci.edu/grad/courses/listing.php?year=2013&level=Graduate&department=CS&program=ALL");
		sample.get("graduate courses").add("http://www.ics.uci.edu/grad/courses/listing.php?year=2012&level=Graduate&department=CS&program=ALL");
		sample.get("graduate courses").add("http://www.ics.uci.edu/grad/policies/");

		// Crista Lopes
		sample.put("Crista Lopes", new ArrayList<String>());
		sample.get("Crista Lopes").add("http://www.ics.uci.edu/~lopes/");
		sample.get("Crista Lopes").add("http://www.ics.uci.edu/~lopes/publications.html");
		sample.get("Crista Lopes").add("http://www.ics.uci.edu/~lopes/patents.html");
		sample.get("Crista Lopes").add("http://luci.ics.uci.edu/blog/?tag=crista-lopes");
		sample.get("Crista Lopes").add("http://luci.ics.uci.edu/blog/?p=416");

		// REST
		sample.put("REST", new ArrayList<String>());
		sample.get("REST").add("http://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm");
		sample.get("REST").add("http://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm");
		sample.get("REST").add("http://www.ics.uci.edu/~fielding/pubs/dissertation/evaluation.htm");
		sample.get("REST").add("http://www.ics.uci.edu/~fielding/pubs/dissertation/introduction.htm");
		sample.get("REST").add("http://www.ics.uci.edu/~fielding/pubs/dissertation/conclusions.htm");

		// computer games
		sample.put("computer games", new ArrayList<String>());
		sample.get("computer games").add("http://www.ics.uci.edu/prospective/en/degrees/computer-game-science/");
		sample.get("computer games").add("http://www.ics.uci.edu/ugrad/degrees/degree_cgs.php");
		sample.get("computer games").add("http://cgvw.ics.uci.edu/");
		sample.get("computer games").add("http://www.ics.uci.edu/~eppstein/gina/vidgames.html");
		sample.get("computer games").add("http://www.ics.uci.edu/~jwross/courses/ics60/");
		
		// information retrieval
		sample.put("information retrieval", new ArrayList<String>());
		sample.get("information retrieval").add("http://www.ics.uci.edu/~lopes/teaching/cs221W12/");
		sample.get("information retrieval").add("http://www.ics.uci.edu/~djp3/classes/2014_01_INF141/calendar.html");
		sample.get("information retrieval").add("http://www.ics.uci.edu/~lopes/");
		sample.get("information retrieval").add("http://www.ics.uci.edu/~djp3/classes/2010_01_CS221/");
		sample.get("information retrieval").add("http://www.ics.uci.edu/~djp3/classes/2009_01_02_INF141/");

	}
}

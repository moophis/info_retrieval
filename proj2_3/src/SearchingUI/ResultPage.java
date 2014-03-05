package SearchingUI;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import structure.PureFileInfo;
import metrics.*;

import indexReader.Doc2MD5;
import indexReader.MD52Doc;
import indexReader.MD52Title;
import indexReader.Search;
import crawler.StringToFile;

public class ResultPage {
	public ResultPage(String query, String resultFolderPath, boolean basic) {
		this.query = query;
		this.workingFolderPath = resultFolderPath;
		this.basic = basic;
		
		queryTime = new Date();
		
		if (basic)
			nanoTime = Search.searchBasic(this.query, hitMD5s, hitPositions, scores);
		else 
			nanoTime = Search.search(this.query, hitMD5s, hitPositions, scores);
		
		GoogleSample.init();
		assemble();
	}
	
	private double evaluate(String query) {
		if (GoogleSample.sample.containsKey(query) 
				&& hitMD5s.size() >= GoogleSample.EVAL_NUM) {
			ArrayList<String> candidate = new ArrayList<>();
			for (int i = 0; i < GoogleSample.EVAL_NUM; i++) {
				candidate.add(MD52Doc.getInstance().getURL(hitMD5s.get(i)));
			}
			return NDCGMetric.compute(GoogleSample.sample.get(query), candidate)
					.get(GoogleSample.EVAL_NUM - 1);
		} else {
			return Double.NaN;
		}
	}
	
	public void assemble() {
		// name the result page
		resultPath = workingFolderPath + "/ResultPages/";
		resultPath += new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").format(queryTime);
		resultPath += ".html";
		
		StringToFile.toFile("<!DOCTYPE html>", resultPath);
		StringToFile.toFile("<html>", resultPath);
		StringToFile.toFile("<head>", resultPath);
		StringToFile.toFile("<title>" + query + " - UC Irvine ICS</title>", resultPath);
		StringToFile.toFile("</head>", resultPath);
		StringToFile.toFile("<body>", resultPath);
		StringToFile.toFile("<h1>Result Pages For: " + query + "</h1>" , resultPath);
		StringToFile.toFile("Query time: " + (nanoTime / 1000000l) + " ms<br>" , resultPath);
		StringToFile.toFile("Find " + hitMD5s.size() + " results<br>", resultPath);
		StringToFile.toFile("NDCG Value comparing with Google results: " + evaluate(query) 
							+ " <br>", resultPath);
		StringToFile.toFile("Using " + (basic ? "basic " : "optimized ") 
				+ " searching algorithm<br>", resultPath);
		
		int count = 0;
		for (String md5 : hitMD5s) {
			String url = MD52Doc.getInstance().getURL(md5);
			String title = MD52Title.getInstance().getTitle(md5);
			
			// sanitize the title
			if (title == null || title.length() <= 0 || title.matches("\\s+"))
				title = "(Untitled) " + url;
			System.out.println(url + ":" + title);
			
			PureFileInfo pureInfo = new PureFileInfo();
			String absBuf = null; // the abstract of the page
			String absInfoBuf = new String();
			
			// find doc
			if (!Doc2MD5.getInstance().getHtmlFileInfo(url, pureInfo)) {
				System.err.println("Cannot find url for md5: " + url);
				continue;
			} else {
				String file = pureInfo.fileName;
				String filePath = workingFolderPath + "/pure/text/" + file;
				absInfoBuf += file + " ";
				System.out.println(filePath);
				
				System.out.println("filePos: " + pureInfo.startPos 
						+ " fileLen: " + pureInfo.fileLen);
				absInfoBuf += "start @" + pureInfo.startPos + " len = " + pureInfo.fileLen;
				// find query word appearing in the doc
				int firstQueryPos = 0;
				if (hitPositions.containsKey(md5)) {
					long from, to;
					int size = 0; // [from, to), size = to - from
					
					firstQueryPos = hitPositions.get(md5);
					absInfoBuf += ", first query find @" + firstQueryPos + "\n";
					// around about 100 words
//					from = (firstQueryPos > 50) ? 
//							firstQueryPos - 50 : 0;
					from = firstQueryPos;
					to = (pureInfo.fileLen - firstQueryPos > 100) ?
							firstQueryPos + 100 : pureInfo.fileLen;
					System.out.println("first query pos " + firstQueryPos +
							"from " + from + ", to " + to);
					size = (int) (to - from);
					from += pureInfo.startPos;
					to += pureInfo.startPos;
					
					byte[] bytes = new byte[100];
					try {
						RandomAccessFile raf = new RandomAccessFile(filePath, "r");
						raf.seek(from);
						raf.read(bytes);
						raf.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						absInfoBuf += "first 10 bytes: ";
						absBuf = new String(bytes, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.err.println("Cannot find query in: " + url);
					continue;
				}
				
				// early termination
				if (++count >= MAX_RESULTS)
					break;
			}
			
			StringToFile.toFile("<p>", resultPath);
			StringToFile.toFile("<a href=\"" + url + "\"><b>" + title + "</b></a><br>", resultPath);
			StringToFile.toFile("<b>URL:</b><em>" + url+ "</em><br>", resultPath);
			if (absBuf != null && absInfoBuf != null) {
				StringToFile.toFile("<b>" + absInfoBuf+ "</b><br>", resultPath);
				StringToFile.toFile("<i>" + absBuf+ "</i><br>", resultPath);
			}
			StringToFile.toFile("</p>", resultPath);
		}
		
		StringToFile.toFile("</body>", resultPath);
		StringToFile.toFile("</html>", resultPath);
	}
	
	public String getResultPath() {
		return resultPath;
	}
		
	private String workingFolderPath = null;
	
	private static final int MAX_RESULTS = 50;
	
	private String query = null;
	
	private ArrayList<String> hitMD5s = new ArrayList<String>();
	
	private HashMap<String, Integer> hitPositions = new HashMap<>();
	
	private HashMap<String, Double> scores = new HashMap<>();
	
	private Date queryTime;
	
	private String resultPath = null;
	
	private boolean basic = false;
	
	private long nanoTime = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}

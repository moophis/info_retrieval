package SearchingUI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import structure.HtmlFileInfo;

import indexReader.MD52Doc;
import indexReader.Search;
import indexbuilder.*;
import crawler.StringToFile;

public class ResultPage {
	public ResultPage(String query, String resultFolderPath) {
		this.query = query;
		this.resultFolderPath = resultFolderPath;
		
		queryTime = new Date();
		
//		Search.search(this.query, hitMD5s, hitPositions, scores);
		
		assemble();
	}
	
	public void assemble() {
		// name the result page
		resultPath = resultFolderPath;
		resultPath += new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").format(queryTime);
		resultPath += ".html";
		
		StringToFile.toFile("<!DOCTYPE html>", resultPath);
		StringToFile.toFile("<html>", resultPath);
		StringToFile.toFile("<head>", resultPath);
		StringToFile.toFile("<title>" + query + " - UC Irvine ICS</title>", resultPath);
		StringToFile.toFile("</head>", resultPath);
		StringToFile.toFile("<body>", resultPath);
		StringToFile.toFile("<h1>Result Pages For: " + query + "</h1>" , resultPath);
		
		for (String md5 : hitMD5s) {
			String url = MD52Doc.getInstance().getURL(md5);
		}
		
		StringToFile.toFile("</body>", resultPath);
		StringToFile.toFile("</html>", resultPath);
	}
	
	public String getResultPath() {
		return resultPath;
	}
	
	private String resultFolderPath 
			= "/Users/liqiangw/Test/IR/ResultPages/";
	
	private String fileName = null;
	
	private String query = null;
	
	private ArrayList<String> hitMD5s = new ArrayList<String>();
	
	private HashMap<String, Integer> hitPositions = new HashMap<>();
	
	private HashMap<String, Double> scores = new HashMap<>();
	
	private Date queryTime;
	
	private String resultPath = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(args[0]);
		ResultPage rp = new ResultPage("machine learning", args[0]);
	}
}

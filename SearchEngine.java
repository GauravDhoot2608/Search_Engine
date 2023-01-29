package search_engine;

import java.util.*;

public class SearchEngine{
	
	private InvertedPageIndex miniSearchEngine;
	private MySet<PageEntry> pages;
	
	public SearchEngine(){
		
		miniSearchEngine = new InvertedPageIndex();
		pages = new MySet<PageEntry>();
	}
	
	public void performAction(String actionMessage){
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("Query  -> " + actionMessage);
		
		String[] query = actionMessage.split("\\s");
		
		if(query[0].equals("addPage")){
			System.out.print("Result -> ");
			if(contains(pages, query[1])){
				System.out.println("Webpage " + query[1] + " already added");
				return;
			}
			try{
				PageEntry webPage = new PageEntry(query[1]);
				miniSearchEngine.addPage(webPage);
				pages.addElement(webPage);
			}
			catch(RuntimeException e)
			{
				System.out.println("Webpage " + query[1] + " not found");
				return;
			}
			System.out.println("Webpage " + query[1] + " added");
		
		}else if(query[0].equals("queryFindPagesWhichContainWord")){
			
			System.out.print("Result -> ");
			String word = query[1];
			word = processString(word);
			String[] str = new String[1]; 
			str[0] = word;
			MySet<PageEntry> pageSet = miniSearchEngine.getPagesWhichContainWord(word);
			if(pageSet.size() == 0) {
				System.out.println("No webpage contains word " + word);
		    }else{
				ArrayList<SearchResult> search_results = getSortedSearchResults(pageSet, str, false);
				printPages(search_results);
			}
		
		}else if(query[0].equals("queryFindPositionsOfWordInAPage")){
			
			System.out.print("Result -> ");
			String word = processString(query[1]); 
			String document = query[2];
			
			if(!contains(pages, document)){
				System.out.println("No webpage " + document + " found");
				return;
			}
			WordEntry wordEntry = miniSearchEngine.getEntryFromWord(word);
			if(wordEntry == null){
				System.out.println("Webpage " + document + " does not contain word " + word);
				return;
			}
			MyLinkedList<Position>.Node tmp = wordEntry.getAllPositionsForThisWord().head;
			Vector<Integer> v = new Vector<Integer>();
			while(tmp != null){
				if(tmp.obj.getPageEntry().getName().equals(document))
					v.add(tmp.obj.getWordIndex());
				tmp = tmp.next;
			}
			int n = v.size();
			if(n == 0){
				System.out.println("Webpage " + document + " does not contain word " + word);
				return;
			}
			for(int i=0;i<n-1;i++) {
				System.out.print(v.get(i) + ", ");	
			}
			System.out.println(v.get(n-1));
			
		}else if(query[0].equals("queryFindPagesWhichContainAllWords")){
			
			System.out.print("Result -> ");
			String[] str = Arrays.copyOfRange(query, 1, query.length);
			str = processString(str);
			MySet<PageEntry> pageSet = miniSearchEngine.getPagesWhichContainAllWords(str);
			
			if(pageSet.size() == 0)
				System.out.println("No webpage contains all the given words");
			else{
				ArrayList<SearchResult> search_results = getSortedSearchResults(pageSet, str, false);
				printPages(search_results);
			}
		
		}else if(query[0].equals("queryFindPagesWhichContainAnyOfTheseWords")){
			
			System.out.print("Result -> ");
			String[] str = Arrays.copyOfRange(query, 1, query.length);
			str = processString(str);
			MySet<PageEntry> pageSet = miniSearchEngine.getPagesWhichContainAnyOfTheseWords(str);
			
			if(pageSet.size() == 0)
				System.out.println("No webpage contains any of the given words");
			else{
				ArrayList<SearchResult> search_results = getSortedSearchResults(pageSet, str, false);
				printPages(search_results);
			}
		
		}else if(query[0].equals("queryFindPagesWhichContainPhrase")){
			
			System.out.print("Result -> ");
			String[] str = Arrays.copyOfRange(query, 1, query.length);
			str = processString(str);
			MySet<PageEntry> pageSet = miniSearchEngine.getPagesWhichContainPhrase(str);
			
			if(pageSet.size() == 0)
				System.out.println("No webpage contains the given phrase");
			else{
				ArrayList<SearchResult> search_results = getSortedSearchResults(pageSet, str, true);
				printPages(search_results);
			}
		
		}else {
			System.out.print("Result -> ");
			System.out.println("Error - Invalid Input");
		}	
	}
	
	private String processString(String str){
		
		str = str.toLowerCase();
		if(str.equals("stacks")) str = "stack";
		else if(str.equals("structures")) str = "structure";
		else if(str.equals("applications")) str = "application";
		return str;
	}

	private String[] processString(String[] str){
		
		int n = str.length;
		for(int i = 0; i<n; i++)
			str[i] = processString(str[i]);
		return str;
	}
	
	private float getInverseDocumentFrequency(String word){
		
		int numberOfPages = pages.size();
		int nwp = miniSearchEngine.getPagesWhichContainWord(word).size();
		
		if(nwp == 0) return Float.MAX_VALUE;
		float ans = ((float)numberOfPages)/nwp;
		return (float)Math.log(ans);
	}

	private float getInverseDocumentFrequencyForPhrase(String[] str)
	{
		int numberOfPages = pages.size();
		int nwp = miniSearchEngine.getPagesWhichContainPhrase(str).size();
		
		if(nwp == 0) return Float.MAX_VALUE;
		float ans = ((float)numberOfPages)/nwp;
		
		return (float)Math.log(ans);	
	}

	private float getRelevanceOfPage(PageEntry page, String[] str, boolean doTheseWordsRepresentAPhrase){
		float ans = 0;
		int n = str.length;
		if(!doTheseWordsRepresentAPhrase){
			for(int i=0;i<n;i++) {
				ans += (page.getTermFrequency(str[i]) * getInverseDocumentFrequency(str[i]));	
			}
			
		}else{
			ans = page.getTermFrequencyForPhrase(str) * getInverseDocumentFrequencyForPhrase(str);
		}
		return ans;
	}
	
	private ArrayList<SearchResult> getSortedSearchResults(MySet<PageEntry> webPages, String[] words, boolean doTheseWordsRepresentAPhrase)
	{
		MyLinkedList<PageEntry>.Node tmp = webPages.getElements().head;
		MySet<SearchResult> search_results = new MySet<SearchResult>();
		float rel;
		while(tmp != null)
		{
			rel = getRelevanceOfPage(tmp.obj, words, doTheseWordsRepresentAPhrase);
			search_results.addElement(new SearchResult(tmp.obj, rel));
			tmp = tmp.next;
		}
		MySort<SearchResult> sorted = new MySort<SearchResult>();
		return sorted.sortThisList(search_results);
	}
	
	private boolean contains(MySet<PageEntry> pages, String document){
		
		MyLinkedList<PageEntry>.Node tmp = pages.getElements().head;
		while(tmp != null){
			
			if(tmp.obj.getName().equals(document))
				return true;
			tmp = tmp.next;
		}
		return false;
	}

	private void printPages(ArrayList<SearchResult> search_results){
		int n = search_results.size();
		for(int i=0;i<n-1;i++) {
			System.out.print(search_results.get(i).getPageEntry().getName() + ", ");			
		}
		System.out.println(search_results.get(n-1).getPageEntry().getName());
	}
	
	
}

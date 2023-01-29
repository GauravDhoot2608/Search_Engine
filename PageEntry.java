package search_engine;

import java.io.*;
import java.util.*;

public class PageEntry{
	
	// store the the information related to a webpage.
	private String pageName;
	private PageIndex index;
	private int number_of_words;
	private AVL<Position> non_connector_indexes;
	private MySet<String> connectorWords = new MySet<String>();
	
	// Read this file, and create the page index.
	public PageEntry(String pageName){
		
		this.pageName = pageName;
		index = new PageIndex();
		number_of_words = 0;
		non_connector_indexes = new AVL<Position>();
		connectorWords.addElement("a"); connectorWords.addElement("an"); connectorWords.addElement("the"); connectorWords.addElement("they");
		connectorWords.addElement("these"); connectorWords.addElement("this"); connectorWords.addElement("for"); connectorWords.addElement("is");
		connectorWords.addElement("are"); connectorWords.addElement("was"); connectorWords.addElement("of"); connectorWords.addElement("or");
		connectorWords.addElement("and"); connectorWords.addElement("does"); connectorWords.addElement("will"); connectorWords.addElement("whose");
		try
		{
			FileInputStream fstream = new FileInputStream ("./webpages/" + pageName);
			Scanner scan = new Scanner(fstream);
			int wordIndex = 1;
			while (scan.hasNextLine()){
				
				String str = scan.nextLine();
				str = str.toLowerCase();
				str = str.replace('{', ' ').replace('}', ' ').replace('[', ' ').replace(']', ' ');
				str = str.replace('<', ' ').replace('>', ' ').replace('=', ' ').replace('(', ' ');
				str = str.replace(')', ' ').replace('.', ' ').replace(',', ' ').replace(';', ' ');
				str = str.replace('\'', ' ').replace('"', ' ').replace('?', ' ').replace('#', ' ');
				str = str.replace('!', ' ').replace('-', ' ').replace(':', ' ');
				
				Scanner t = new Scanner(str);
				while(t.hasNext()){
					
					String word = t.next();
					if(!connectorWords.IsMember(word))	{
						
						if(word.equals("stacks")) word = "stack";
						else if(word.equals("structures")) word = "structure";
						else if(word.equals("applications")) word = "application";
						
						Position p = new Position(this, wordIndex);
						index.addPositionForWord(word, p);
						non_connector_indexes.Insert(p);
						number_of_words++;
					}
					wordIndex++;
				}
				t.close();
			}
			scan.close();
		}
		catch (FileNotFoundException e) { throw new RuntimeException("Error - File not found"); }
	}
	
	public boolean equals(Object page){
		
		PageEntry entry = (PageEntry)page;
		if(this == entry) return true;
		if(entry == null) return false;
		return  pageName.equals(entry.getName());
	}
	
	public String getName(){
		
		return pageName;
	}
	
	public PageIndex getPageIndex(){
		return index;
	}
	
	public float getTermFrequency(String word){
		
		MyLinkedList<WordEntry>.Node tmp = index.getWordEntries().head;
		WordEntry wordEntry = null;
		while(tmp != null){
			
			if(tmp.obj.getWord().equals(word)){
				wordEntry = tmp.obj;
				break;
			}
			tmp = tmp.next;
		}
		if(wordEntry == null) return 0;
		int fwp = wordEntry.getAllPositionsForThisWord().size();
		
		//if(word.equals("function"))
			//System.out.println(pageName + " " + fwp + " " + index.getWordEntries().size() + " " + number_of_words);
		return ((float)(fwp))/number_of_words;
	}

	private WordEntry getEntryFromWord(String word){
		
		MyLinkedList<WordEntry>.Node tmp = index.getWordEntries().head;
		while(tmp != null){
			
			if(tmp.obj.getWord().equals(word))
				return tmp.obj;
			tmp = tmp.next;
		}
		return null;
	}

	public int containsPhrase(String[] str){
		
		int n = str.length, i,freq = 0;
		WordEntry w;
		WordEntry[] words = new WordEntry[n];
		
		for(i=0;i<n;i++){
			
			w = getEntryFromWord(str[i]);
			if(w == null) return freq;
			words[i] = w;
		}
		MyLinkedList<Position>.Node head = words[0].getAllPositionsForThisWord().head;
		
		while(head != null){
			Position curr = head.obj;
			
			for(i=1;i<n;i++){
				curr = non_connector_indexes.inOrderSuccessor(curr);
				if(curr == null)
					break;
				if(!words[i].getTreeForThisWord().isMember(curr))
					break;
			}
			if(i == n) freq++;
			head = head.next;
		}
		return freq;
	}

	public float getTermFrequencyForPhrase(String[] str){
		
		int k = str.length, m = containsPhrase(str);
		int deno = number_of_words - m * (k - 1);
		return ((float)m)/deno; 
	}
}
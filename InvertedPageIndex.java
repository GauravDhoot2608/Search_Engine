package search_engine;

public class InvertedPageIndex{
	
	private MyHashTable hashTable;
	
	public InvertedPageIndex(){
		
		hashTable = new MyHashTable();
	}
	
	// Add a new page entry p to the inverted page index.
	public void addPage(PageEntry page){
		
		MyLinkedList<WordEntry>.Node tmp = page.getPageIndex().getWordEntries().head;
		while(tmp != null){
			hashTable.addPositionsForWord(tmp.obj);
			tmp = tmp.next;
		}
	}

	public WordEntry getEntryFromWord(String str){
		return hashTable.searchWord(str);
	}
	
	// Return a set of page-entries of webpages which contain the word str.
	public MySet<PageEntry> getPagesWhichContainWord(String str){
		
		MySet<PageEntry> page_entries = new MySet<PageEntry>();
		WordEntry entry = getEntryFromWord(str);
		if(entry == null) return page_entries;
		MyLinkedList<Position>.Node tmp = entry.getAllPositionsForThisWord().head;
		
		while(tmp != null){
			try { 
				page_entries.addElement(tmp.obj.getPageEntry()); 
			}
			catch(RuntimeException e) {}
			
			tmp = tmp.next;
		}
		return page_entries;
	}

	public MySet<PageEntry> getPagesWhichContainAllWords(String[] words){
		
		MySet<PageEntry> answer = new MySet<PageEntry>();
		if(words.length == 0) return answer;
		
		answer = getPagesWhichContainWord(words[0]);
		
		for(int i=1;i<words.length;i++){
			answer = answer.intersection(getPagesWhichContainWord(words[i]));
		}
		return answer;
	}

	public MySet<PageEntry> getPagesWhichContainAnyOfTheseWords(String[] words){
		
		MySet<PageEntry> answer = new MySet<PageEntry>();
		
		if(words.length == 0) return answer;
		answer = getPagesWhichContainWord(words[0]);
		
		for(int i=1;i<words.length;i++) {
			answer = answer.union(getPagesWhichContainWord(words[i]));	
		}
		return answer;
	}
	
	public MySet<PageEntry> getPagesWhichContainPhrase(String[] str){
		
		MySet<PageEntry> webPages = getPagesWhichContainAllWords(str);
		if(webPages.size() == 0) return webPages;
		
		MyLinkedList<PageEntry>.Node temp = webPages.getElements().head;
		MySet<PageEntry> phrasePages = new MySet<PageEntry>();
		
		while(temp != null){
			if(temp.obj.containsPhrase(str) > 0)
				phrasePages.addElement(temp.obj);
			temp = temp.next;
		}
		return phrasePages;
	}
}


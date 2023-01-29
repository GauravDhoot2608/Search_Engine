package search_engine;

public class Position implements Comparable<Position>{
	
	// represents a tuple <page p, word position i>.
	private PageEntry pageEntry;
	private int wordIndex;
	
	public Position(PageEntry pageEntry, int wordIndex){
		
		this.pageEntry = pageEntry;
		this.wordIndex = wordIndex;
	}
	
	public boolean equals(Object position){
		
		Position temp = (Position)position;
		if(this == temp) return true;
		if(temp == null) return false;
		return (pageEntry.equals(temp.getPageEntry()) && wordIndex == temp.getWordIndex());
	}

	public int compareTo(Position pos){ 
        return wordIndex - pos.getWordIndex(); 
    }
	
	public PageEntry getPageEntry(){
		return pageEntry;
	}
	
	public int getWordIndex(){
		return wordIndex;
	}
}

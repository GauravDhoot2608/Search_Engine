package search_engine;

public class WordEntry{
	
	// For a string str, this class stores the list
	// of word indice's where str is present in the document(s).
	private String word;
	private AVL<Position> entry; 
	
	public WordEntry(String word){
		
		this.word = word;
		entry = new AVL<Position>();
	}
	
	public boolean equals(Object wordEntry){
		
		WordEntry entry = (WordEntry)wordEntry;
		if(this == entry) return true;
		if(entry == null) return false;
		
		return word.equals(entry.getWord());
	}
	
	public void addPosition(Position position){
		
		entry.Insert(position);
	}
	
	public void addPositions(MyLinkedList<Position> positions){
		
		MyLinkedList<Position>.Node temp = positions.head;
		while(temp != null){
			
			addPosition(temp.obj);
			temp = temp.next;
		}
	}
	
	public String getWord(){
		
		return word;
	}
	
	public MyLinkedList<Position> getAllPositionsForThisWord(){
		
		return entry.getElements();
	}

	public AVL<Position> getTreeForThisWord(){
		
		return entry;
	}
	
	public WordEntry Clone(){
		
		WordEntry new_word = new WordEntry(word);
		new_word.addPositions(this.getAllPositionsForThisWord());
		return new_word;
	}
}
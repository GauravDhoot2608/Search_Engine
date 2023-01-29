package search_engine;

public class PageIndex{
	
	// stores one word-entry for each unique word in the document.
	private MyLinkedList<WordEntry> words;
	
	public PageIndex(){
		words = new MyLinkedList<WordEntry>();
	}
	
	public void addPositionForWord(String word, Position pos){
		
		MyLinkedList<WordEntry>.Node temp = words.head;
		while(temp != null){
			
			if(temp.obj.getWord().equals(word)){
				temp.obj.addPosition(pos);
				return;
			}
			temp = temp.next;
		}
		WordEntry new_word = new WordEntry(word);
		new_word.addPosition(pos);
		words.Insert(new_word);
	}
	
	public MyLinkedList<WordEntry> getWordEntries(){
		return words;
	}
}
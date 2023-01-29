package search_engine;

public class SearchResult implements Comparable<SearchResult>{
	
	private PageEntry page;
	private float relevance;
	
	public SearchResult(PageEntry page, float relevance){
		this.page = page;
		this.relevance = relevance;
	}

	public int compareTo(SearchResult otherObject) {
		
    	if(relevance > otherObject.getRelevance()) return 1;
    	if(relevance < otherObject.getRelevance()) return -1;
        return 0;
    }
	
	public PageEntry getPageEntry(){
		return page;
	}
	
	public float getRelevance(){
		return relevance;
	}
}

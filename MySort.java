package search_engine;

import java.util.*;

public class MySort<Sortable extends Comparable<Sortable>>{
	
	// sorts the set in descending order
	public ArrayList<Sortable> sortThisList(MySet<Sortable> listOfSortableEntries){
		
		MyLinkedList<Sortable>.Node tmp = listOfSortableEntries.getElements().head;
		ArrayList<Sortable> sortedList = new ArrayList<Sortable>();
		while(tmp != null)
		{
			sortedList.add(tmp.obj);
			tmp = tmp.next;
		}
		sortedList.trimToSize();
		int n = sortedList.size(),ind;
		// selection sort
		for(int i=0;i<n-1;i++){
			
			ind = i;
			for(int j=i+1;j<n;j++) {
				if(sortedList.get(j).compareTo(sortedList.get(ind)) > 0)
					ind = j;
			}	
			Sortable temp = sortedList.get(i);
			sortedList.set(i, sortedList.get(ind));
			sortedList.set(ind, temp);
		}
		return sortedList;
	}


}

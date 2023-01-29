package search_engine;


public class MyLinkedList<X> {
	
	Node head, tail;
	int size;
	class Node
	{
		X obj;
		Node next, prev;
		Node(X obj)
		{
			this.obj = obj;
			next = prev = null;
		}
	}
	
	public MyLinkedList(){
		
		head = tail = null;
		size = 0;
	}
	
	public boolean IsEmpty(){
		return (head==null);
	}
	
	public boolean IsMember(X obj){
		Node temp = head;
		while(temp != null){
			
			if(temp.obj.equals(obj)) {
				return true;	
			}
			temp = temp.next;
		}
		return false;
	}
	
	public void Insert(X o){
		
		Node temp = new Node(o);
		size++;
		if(head==null){
			head = tail = temp;
			return;
		}
		tail.next = temp;
		temp.prev = tail;
		tail = temp;
		return;
	}
	
	public void Delete(X obj){
		
		Node temp = head;
		while(temp != null){
			
			if(temp.obj.equals(obj)){
				Node t = temp.next;
				if(temp.prev != null) {
					temp.prev.next = t;
				}else {
					head = t;
				}
				if(t != null) {
					t.prev = temp.prev;
				}else {
					tail = temp.prev;
				}
				size--;
				return;
			}
			temp = temp.next;
		}
		throw new RuntimeException("Error - Given object is not present in list");
	}
	
	public int size(){
		return size;
	}
}
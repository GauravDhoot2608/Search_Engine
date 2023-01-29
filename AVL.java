package search_engine;


public class AVL<X extends Comparable<X>> {
	
	Node root;
	class Node
	{
		X data;
		Node left, right, parent;
		int height;
		
		public Node(X data)
		{
			this.data = data;
			left = right = parent = null;
			height = 0;
		}
	}
	
	private Node find(Node node, X data){
		if(node == null) {
			return null;
		}
		if(node.data.compareTo(data) == 0) {
			return node;
		}else if(node.data.compareTo(data) < 0) {
			return find(node.right, data);	
		}
		return find(node.left, data);		
	}

	public boolean isMember(X data){
		return (find(root, data) != null);
	}

	private Node bstInsert(Node root, Node node){
		
		if(root == null) {
			return node;
		}
		if(node.data.compareTo(root.data) <= 0){
			
			Node temp = bstInsert(root.left, node);
			temp.parent = root;
			root.left = temp;
		}
		else{
			Node temp = bstInsert(root.right, node);
			temp.parent = root;
			root.right = temp;
		}
		return root;
	}

	private int newHeight(Node node){
		
		if(node == null) {
			return -1;
		}
		int left = (node.left == null) ? -1 : node.left.height;
		int right = (node.right == null) ? -1 : node.right.height;
		
		return 1 + Math.max(left, right);
	}

	private boolean isImbalanced(Node node){
		
		if(node == null) {
			return false;
		}
		int left = (node.left == null) ? -1 : node.left.height;
		int right = (node.right == null) ? -1 : node.right.height;
		
		return (Math.abs(left-right) > 1);
	}

	private Node higherChild(Node node){
		
		if(node == null) return null;
		if(node.right == null && node.left == null) return null;
		if(node.right == null) return node.left;
		if(node.left == null) return node.right;
		if(node.left.height >= node.right.height) return node.left;
		return node.right;
	}

	public void Insert(X data){
		
		Node node = new Node(data);
		root = bstInsert(root, node);
		node = node.parent;
		while(node != null){
			
			if(node.height == newHeight(node)) {
				return;
			}
			node.height = newHeight(node);
			if(node.parent == null) {
				return;
			}
			if(isImbalanced(node.parent)) {
				break;
			}
			node = node.parent;
		}
		if(node == null) return;
		
		Node z = node.parent, y = node;
		Node x = higherChild(y);
		if(y == z.right && x == y.left){          // left of Right child
			
			x.parent = z.parent;
			if(z.parent != null){
				
				if(z.parent.right == z) {
					z.parent.right = x;
				}else {
					z.parent.left = x;
				}
			}else{
				
			   root = x;
			   Node leftx = x.left, rightx = x.right;
			   x.left = z; 
			   x.right = y;
			   y.parent = z.parent = x;
			   z.right = leftx; 
			   y.left = rightx;
			   if(leftx != null) {
				 leftx.parent = z;
			   }
			   if(rightx != null) {
				 rightx.parent = y;
			    }
			   int h = y.height;
			   x.height = h; 
			   y.height = h-1;
			   z.height = h-1;
			}
		}

		else if(y == z.right && x == y.right)  {    // right of right child
		
			y.parent = z.parent;
			if(z.parent != null){
				
				if(z.parent.right == z) {
					z.parent.right = y;
				}else {
					z.parent.left = y;
				}
			}
			else
				root = y;
			   Node lefty = y.left;
			   y.left = z; 
			   y.right = x;
			   z.parent = x.parent = y;
			   z.right = lefty;
			   if(lefty != null) lefty.parent = z; 
			   int h = y.height;
			   x.height = h-1;
			   z.height = h-1;
		}

		else if(y == z.left && x == y.right) {    // right of left child
			
			x.parent = z.parent;
			if(z.parent != null){
				if(z.parent.right == z) {
					z.parent.right = x;
				}else {
					z.parent.left = x;
				}
			}
			else
				root = x;
			    Node leftx = x.left, rightx = x.right;
			    x.left = y; 
			    x.right = z;
			    y.parent = z.parent = x;
			    y.right = leftx; 
			    z.left = rightx;
			    if(leftx != null) leftx.parent = y;
			    if(rightx != null) rightx.parent = z;
			    int h = y.height;
			    x.height = h; 
			    y.height = h-1;
			    z.height = h-1;
		}

		else                                  // left of left child
		{
			y.parent = z.parent;
			if(z.parent != null)
			{
				if(z.parent.right == z) {
					z.parent.right = y;
				}
				else {
					z.parent.left = y;
				}
			}
			else
				root = y;
			   Node righty = y.right;
			   y.left = x; 
			   y.right = z;
			   z.parent = x.parent = y;
			   z.left = righty;
			   if(righty != null) righty.parent = z; 
			   int h = y.height;
			   x.height = h-1;
			   z.height = h-1;
		}
	}

	private void inOrder(Node root, MyLinkedList<X> list){
		
		if(root != null){
			
			inOrder(root.left, list);
			list.Insert(root.data);
			inOrder(root.right, list);
		}
	}


	public MyLinkedList<X> getElements(){
		
		MyLinkedList<X> list = new MyLinkedList<X>();
		inOrder(root, list);
		return list;
	}

	public X inOrderSuccessor(X data){
		
		Node node = find(root, data);
		if(node != null)
		{
			if(node.right != null){
				
				Node ans = node.right;
				while(ans.left != null) {
					ans = ans.left;	
				}
				return ans.data;
			}else{
				
				Node ans = node;
				while(ans.parent != null && ans.parent.right == ans) {
					ans = ans.parent;	
				}
				if(ans.parent == null) {
					return null;
				}
				return ans.parent.data;
			}
		}
		return null;
	}
}

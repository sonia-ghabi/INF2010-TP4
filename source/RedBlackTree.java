import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree<T extends Comparable<? super T> > 
{
   private RBNode<T> root;  // Racine de l'arbre
   
   enum ChildType{ left, right }
   
   public RedBlackTree()
   { 
      root = null;
   }
   
   public void printFancyTree()
   {
      printFancyTree( root, "", ChildType.right);
   }
   
   private void printFancyTree( RBNode<T> t, String prefix, ChildType myChildType)
   {
      System.out.print( prefix + "|__"); // un | et trois _
      
      if( t != null )
      {
         boolean isLeaf = (t.isNil()) || ( t.leftChild.isNil() && t.rightChild.isNil() );
         
         System.out.println( t );
         String _prefix = prefix;
         
         if( myChildType == ChildType.left )
            _prefix += "|   "; // un | et trois espaces
         else
            _prefix += "   " ; // trois espaces
         
         if( !isLeaf )
         {
            printFancyTree( t.leftChild, _prefix, ChildType.left );
            printFancyTree( t.rightChild, _prefix, ChildType.right );
         }
      }
      else
         System.out.print("null\n");
   }
   
   public T find(int key)
   {
      return find(root, key);
   }

   private T find(RBNode<T> current, int key)
   {
      // À COMPLÉTER
	   return null;
   }
   
   
   public void insert(T val)
   {
      insertNode( new RBNode<T>( val ) );
   }
   
   private void insertNode( RBNode<T> newNode )
   { 
      if (root == null)  // Si arbre vide
         root = newNode;
      else
      {
         RBNode<T> position = root; // On se place a la racine
         
         while( true ) // on insere le noeud (ABR standard)
         {
            int newKey = newNode.value.hashCode();
            int posKey = position.value.hashCode();
            
            if ( newKey < posKey ) 
            {
               if ( position.leftChild.isNil() ) 
               {
                  position.leftChild = newNode;
                  newNode.parent = position;
                  break;
               } 
                  else 
                  position = position.leftChild;
            } 
            else if ( newKey > posKey ) 
            {
               if ( position.rightChild.isNil() )
               {
                  position.rightChild = newNode;
                  newNode.parent = position;
                  break;
               }
               else 
                  position = position.rightChild;
            }
            else // pas de doublons
               return;
         }
      }
      
      insertionCases( newNode );
   }

   private void insertionCases( RBNode<T> X )
   {
      // A MODIFIER/COMPLÉTER
      insertionCase1( X );
   }
   
   private void insertionCase1 ( RBNode<T> X )
   {
      // A MODIFIER/COMPLÉTER
	  if(X == root) {
		  X.setToBlack();
		  return;
	  }
      insertionCase2( X );
   }

   private void insertionCase2( RBNode<T> X )
   {
      // A MODIFIER/COMPLÉTER
	  RBNode<T> parent = X.parent;
	  if(parent.color == RBNode.RB_COLOR.BLACK) {
		return;
	  }
      insertionCase3( X );
   }

   private void insertionCase3( RBNode<T> X )
   {
      // A MODIFIER/COMPLÉTER
	   RBNode<T> parent = X.parent;
	  if(parent.color == RBNode.RB_COLOR.RED && X.uncle().color == RBNode.RB_COLOR.RED) {
			 parent.setToBlack();
			 X.uncle().setToBlack();
			 X.grandParent().setToRed();
			 insertionCases(X.grandParent());
			 return;
		  }
      insertionCase4( X );
   }

   private void insertionCase4( RBNode<T> X )
   {
      // A MODIFIER/COMPLÉTER
	   // Si P est rouge
		  if(X.parent.color == RBNode.RB_COLOR.RED 
				  // O est noir
				  && X.uncle().color == RBNode.RB_COLOR.BLACK
				  // X est enfant de gauche de P
				  && X == X.parent.leftChild
				  // P est enfant de droite de G
				  && X.parent == X.grandParent().rightChild) {
			  // Rotation a droite sur P
			  	rotateRight(X.grandParent());
			  	insertionCase5(X.rightChild);
				 return;
			  }
		   // Si P est rouge
		  if(X.parent.color == RBNode.RB_COLOR.RED 
				  // O est noir
				  && X.uncle().color == RBNode.RB_COLOR.BLACK
				  // X est enfant de droite de P
				  && X == X.parent.rightChild
				  // P est enfant de gauche de G
				  && X.parent == X.grandParent().leftChild) {
			  // Rotation a gauche sur P
			  	rotateLeft(X.grandParent());
			  	insertionCase5(X.leftChild);
				 return;
			  }
      insertionCase5( X );
   }

   private void insertionCase5( RBNode<T> X )
   {
      // A MODIFIER/COMPLÉTER
	// Si P est rouge
	   if(X.parent.color == RBNode.RB_COLOR.RED 
				  // O est noir
				  && X.uncle().color == RBNode.RB_COLOR.BLACK
				  // X est enfant de droite de P
				  && X == X.parent.rightChild
				  // P est enfant de droite de G
				  && X.parent == X.grandParent().rightChild) {
		   // On change la couleur de P et de G
		   X.parent.setToBlack();
		   if (X.grandParent().color == RBNode.RB_COLOR.RED){
			   X.grandParent().setToBlack();
		   } else {
			   X.grandParent().setToRed();
		   }
			  // Rotation a gauche sur G
			  	rotateLeft(X.grandParent());
				 return;
	   }
	   
	   if(X.parent.color == RBNode.RB_COLOR.RED 
				  // O est noir
				  && X.uncle().color == RBNode.RB_COLOR.BLACK
				  // X est enfant de gauche de P
				  && X == X.parent.leftChild
				  // P est enfant de gauche de G
				  && X.parent == X.grandParent().leftChild) {
		   // On change la couleur de P et de G
		   X.parent.setToBlack();
		   if (X.grandParent().color == RBNode.RB_COLOR.RED){
			   X.grandParent().setToBlack();
		   } else {
			   X.grandParent().setToRed();
		   }
			  // Rotation a droite sur G
			  	rotateRight(X.grandParent());
				 return;
	   }
      return; 
   }
   
   private void rotateLeft( RBNode<T> G )
   {
      // A MODIFIER/COMPLÉTER
	  // Cas droite gauche
	   RBNode<T> P;
	   RBNode<T> X;
	   if(G.leftChild.color == RBNode.RB_COLOR.RED){
		   P = G.leftChild;
		   X = P.rightChild;
		   RBNode<T> sousArbre = X.leftChild;
		   P.rightChild = sousArbre;
		   X.leftChild = P;
		   G.leftChild = X;
		   return;
	   }
	   // Cas droite droite
	   else {
		   P = G.rightChild;
		   X = P.rightChild;
		   RBNode<T> sousArbre = P.leftChild;
		   G.rightChild = sousArbre;
		   P.leftChild = G;
		   return;
	   }
   }
   
   private void rotateRight( RBNode<T> G )
   {
	   RBNode<T> P;
	   RBNode<T> X;
	   // cas gauche gauche
	   if(G.leftChild.color == RBNode.RB_COLOR.RED) {
		   P = G.leftChild;
		   X = P.leftChild;
		   RBNode<T> sousArbre = P.rightChild;
		   G.leftChild = sousArbre;
		   P.rightChild = G;
		   return;
	   }
	   // cas gauche droite
	   else {
		   P = G.rightChild;
		   X = P.leftChild;
		   RBNode<T> sousArbre = X.rightChild;
		   X.rightChild = P;
		   P.leftChild = sousArbre;
		   G.rightChild = X;
		   return;
	   }
   }

   public void printTreePreOrder()
   {
      if(root == null)
         System.out.println( "Empty tree" );
      else
      {
         System.out.print( "PreOrdre ( ");
         printTreePreOrder( root );
         System.out.println( " )");
      }
      return;
   }
   
   private void printTreePreOrder( RBNode<T> P )
   {
      // A MODIFIER/COMPLÉTER
      return; 
   }
   
   public void printTreePostOrder()
   {
      if(root == null)
         System.out.println( "Empty tree" );
      else
      {
         System.out.print( "PostOrdre ( ");
         printTreePostOrder( root );
         System.out.println( ")");
      }
      return;
   }
  
   private void printTreePostOrder( RBNode<T> P )
   {
      // A MODIFIER/COMPLÉTER
      return; 
   }
   

   public void printTreeAscendingOrder()
   {
      if(root == null)
         System.out.println( "Empty tree" );
      else
      {
         System.out.print( "AscendingOrdre ( ");
         printTreeAscendingOrder( root );
         System.out.println( " )");
      }
      return;
   }
  
   private void printTreeAscendingOrder( RBNode<T> P )
   {
      // A COMPLÉTER
	   
   }
   
   
   public void printTreeDescendingOrder()
   {
      if(root == null)
         System.out.println( "Empty tree" );
      else
      {
         System.out.print( "DescendingOrdre ( ");
         printTreeDescendingOrder( root );
         System.out.println( " )");
      }
      return;
   }

   private void printTreeDescendingOrder( RBNode<T> P )
   {
      // A COMPLÉTER
	   
   }
   
   public void printTreeLevelOrder()
   {
      if(root == null)
         System.out.println( "Empty tree" );
      else
      {
         System.out.print( "LevelOrdre ( ");
         
         Queue<RBNode<T>> q = new LinkedList<RBNode<T>>();
         
         q.add(root);
         
         //  À COMPLÉTER
         
         System.out.println( " )");
      }
      return;
   }
   
   private static class RBNode<T extends Comparable<? super T> > 
   {
      enum RB_COLOR { BLACK, RED }  // Couleur possible
      
      RBNode<T>  parent;      // Noeud parent
      RBNode<T>  leftChild;   // Feuille gauche
      RBNode<T>  rightChild;  // Feuille droite
      RB_COLOR   color;       // Couleur du noeud
      T          value;       // Valeur du noeud
      
      // Constructeur (NIL)   
      RBNode() { setToBlack(); }
      
      // Constructeur (feuille)   
      RBNode(T val)
      {
         setToRed();
         value = val;
         leftChild = new RBNode<T>();
         leftChild.parent = this;
         rightChild = new RBNode<T>();
         rightChild.parent = this;
      }
      
      RBNode<T> grandParent()
      {
         // À COMPLÉTER
    	  return this.parent.parent;
      }
      
      RBNode<T> uncle()
      {
         // À COMPLÉTER
    	  RBNode<T> parent = this.parent;
    	  return parent.sibling();
      }
      
      RBNode<T> sibling()
      {
         // À COMPLÉTER
    	  RBNode<T> parent = this.parent;
    	  if(parent.rightChild == this)
    	  {
    		  return parent.leftChild;
    	  }
    	  
    	  return parent.rightChild;
      }
      
      public String toString()
      {
         return value + " (" + (color == RB_COLOR.BLACK ? "black" : "red") + ")"; 
      }
      
      boolean isBlack(){ return (color == RB_COLOR.BLACK); }
      boolean isRed(){ return (color == RB_COLOR.RED); }
      boolean isNil(){ return (leftChild == null) && (rightChild == null); }
      
      void setToBlack(){ color = RB_COLOR.BLACK; }
      void setToRed(){ color = RB_COLOR.RED; }
   }
}

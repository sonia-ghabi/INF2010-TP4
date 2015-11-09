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
		if(current.value != null)
		{
			// Si la cle est plus petite que la valeur du noeud on parcourt a gauche
			if(key < (int) current.value){
				return find(current.leftChild, key);
			}
			// Si la cle est plus grande que la valeur du noeud on parcourt a droite
			else if (key > (int) current.value){
				return find(current.rightChild, key);
			}
			// Si la cle est plus egale a la valeur du noeud on a trouve le noeud cherche
			else if (key == (int) current.value) {
				return current.value;
			}
		}
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
		insertionCase1( X );
	}

	private void insertionCase1 ( RBNode<T> X )
	{
		// Si le noeud est la racine, il devient noir et on ne verifie pas le reste
		if(X == root) {
			X.setToBlack();
			return;
		}
		insertionCase2( X );
	}

	private void insertionCase2( RBNode<T> X )
	{
		RBNode<T> parent = X.parent;
		if(parent.color == RBNode.RB_COLOR.BLACK) {
			return;
		}
		insertionCase3( X );
	}

	private void insertionCase3( RBNode<T> X )
	{
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
		// Si P est rouge
		if(X.parent.color == RBNode.RB_COLOR.RED 
				// O est noir
				&& X.uncle().color == RBNode.RB_COLOR.BLACK
				// X est enfant de gauche de P
				&& X == X.parent.leftChild
				// P est enfant de droite de G
				&& X.parent == X.grandParent().rightChild) {
			// Rotation a droite sur P (on remonte le noeud X)
			rotateRight(X);
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
			// Rotation a gauche sur P (on remonte le noeud X)
			rotateLeft(X);
			insertionCase5(X.leftChild);
			return;
		}
		insertionCase5( X );
	}

	private void insertionCase5( RBNode<T> X )
	{
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
			// Rotation a gauche sur G (on remonte le parent)
			rotateLeft(X.parent);
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
			// Rotation a droite sur G (on remonte le parent)
			rotateRight(X.parent);
			return;
		}
		return; 
	}

	private void rotateLeft( RBNode<T> X )
	{
		// X est le noeud qu on veut remonter (il prend la place de son pere)
		RBNode<T> P = X.parent;
		RBNode<T> G = X.grandParent();
		// On regarde si le pere de X est a droite ou a gauche de son grand pere
		// pour pouvoir placer X à la bonne place apres 
		boolean aDroite = false;
		if(P == G.rightChild){
			aDroite = true;
		}

		// D abord on sauvegarde le fils gauche de X;
		RBNode<T> filsGauche = X.leftChild;

		// Le fils gauche de X devient fils droit du pere
		P.rightChild = filsGauche;
		filsGauche.parent = P;

		// Maintenant on dit que le pere de X est son fils gauche
		RBNode<T> nouveauFilsGauche = P;
		nouveauFilsGauche.parent = X;
		X.leftChild = nouveauFilsGauche;

		// On place X à la place de son pere, cad apres son grand pere
		X.parent = G;
		if(aDroite){
			G.rightChild = X;
			return;
		}
		G.leftChild = X;
	}

	private void rotateRight( RBNode<T> X )
	{
		// X est le noeud qu on veut remonter (il prend la place de son pere)
		RBNode<T> P = X.parent;
		RBNode<T> G = X.grandParent();
		// On regarde si le pere de X est a droite ou a gauche de son grand pere
		// pour pouvoir placer X à la bonne place apres 
		boolean aDroite = false;
		if(P == G.rightChild){
			aDroite = true;
		}

		// D abord on sauvegarde le fils droit de X;
		RBNode<T> filsDroit = X.rightChild;

		// Le fils droit de X devient fils gauche du pere
		P.leftChild = filsDroit;
		filsDroit.parent = P;

		// Maintenant on dit que le pere de X est son fils droit
		RBNode<T> nouveauFilsDroit = P;
		nouveauFilsDroit.parent = X;
		X.rightChild = nouveauFilsDroit;

		// On place X à la place de son pere, cad apres son grand pere
		X.parent = G;
		if(aDroite){
			G.rightChild = X;
			return;
		}
		G.leftChild = X;
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
		// On affiche seulement si la valeur du noeud n est null
		if(P.value != null){
			// Si on est au dernier fils de droite, on affiche pas la virgule
			if(P.leftChild.value == null && P.rightChild.value == null && P == P.parent.rightChild && P.grandParent().rightChild == P.parent)
			{
				System.out.print("{" + P.toString() + "}") ;
			}
			else {
				System.out.print("{" + P.toString() + "}, ") ;
			}
			// Appel recursif sur les fils
			printTreePreOrder(P.leftChild);
			printTreePreOrder(P.rightChild);
		}
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
			System.out.println( " )");
		}
		return;
	}

	private void printTreePostOrder( RBNode<T> P )
	{
		// On affiche seulement si la valeur du noeud n est null
		if(P.value != null)
		{
			// Appel recursif sur les fils
			printTreePostOrder(P.leftChild);
			printTreePostOrder(P.rightChild);
			// Si c est la racine, on affiche pas la virgule
			if(P == root)
			{
				System.out.print("{" + P.toString() + "}") ;
			}
			else {
				System.out.print("{" + P.toString() + "}, ") ;
			}

		}
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
		// On affiche seulement si la valeur du noeud n est pas null
		if(P.value != null)
		{
			// Appel recursif sur le fils de gauche
			printTreeAscendingOrder(P.leftChild);
			// Si on est au dernier fils de droite, on affiche pas la virgule
			if(P.leftChild.value == null && P.rightChild.value == null && P == P.parent.rightChild && P.grandParent().rightChild == P.parent)
			{
				System.out.print("{" + P.toString() + "}") ;
			}
			else {
				System.out.print("{" + P.toString() + "}, ") ;
			}
			// Appel recursif sur le fils de droite
			printTreeAscendingOrder(P.rightChild); 
		}
		return;  
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
		// On affiche seulement si la valeur du noeud n est pas null
		if(P.value != null)
		{
			// Appel recursif sur le fils de droite
			printTreeDescendingOrder(P.rightChild);
			// Si on est au dernier fils de gauche, on affiche pas la virgule
			if(P.leftChild.value == null && P.rightChild.value == null && P == P.parent.leftChild && P.grandParent().leftChild == P.parent)
			{
				System.out.print("{" + P.toString() + "}") ;
			}
			else {
				System.out.print("{" + P.toString() + "}, ") ;
			}
			// Appel recursif sur le fils de gauche
			printTreeDescendingOrder(P.leftChild); 
		}
		return;

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
			// Tant que la queue n est pas vide
			while(!q.isEmpty())
			{
				RBNode<T> N = q.peek();
				// On empile le fils de gauche du premier element de la queue
				if(N.leftChild.value != null){
					q.add(N.leftChild);
				}
				// On empile le fils de droite du premier element de la queue
				if(N.rightChild.value != null){
					q.add(N.rightChild);
				}
				// Si la taille est egale a 1, c est que c est le dernier noeud : on affiche pas la virgule
				if(q.size() == 1)
				{
					System.out.print("{" + N.toString() + "}") ;
				}
				else {
					System.out.print("{" + N.toString() + "}, ") ;
				}
				// On supprime la tete de la file (le noeud qu on vient d afficher)
				q.remove();
			}
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
			return this.parent.parent;
		}

		RBNode<T> uncle()
		{
			RBNode<T> parent = this.parent;
			return parent.sibling();
		}

		RBNode<T> sibling()
		{
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

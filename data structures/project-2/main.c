#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include <time.h>

struct bstnode
{
    char key[50];
    struct bstnode *left, *right;
};
typedef struct bstnode bstnode;
typedef bstnode* bstnodePtr;
bstnodePtr r, temp, bnode, t;
struct node
{
    char key[50];
    struct node *left;
    struct node *right;
    int height;
};

struct node *root, *temp2, *node1, *node2, *newN, *N;

// C function to search a given key in a given BST
bstnodePtr bst_search(bstnodePtr r, char key[50])
{
    // Base Cases: root is null or key is present at root
    if (r == NULL || r->key == key)
       return r;
    
    // Key is greater than root's key
    if (strcmp(r->key, key)<0)
       return bst_search(r->right, key);
 
    // Key is smaller than root's key
    return bst_search(r->left, key);
}

bstnodePtr minValueNode(bstnodePtr node1)
{
    bstnodePtr c = node1;
 
    /* loop down to find the leftmost leaf */
    while (c->left != NULL)
        c = c->left;
 
    return c;
}
// A utility function to create a new BST node
bstnodePtr bst_newNode(char item[50])
{
    bstnodePtr temp =  (bstnodePtr)malloc(sizeof(bstnode));
    memcpy(temp->key, item, sizeof temp->key);
    temp->left = temp->right = NULL;
    return temp;
}
// A utility function to do inorder traversal of BST
void inorder(bstnodePtr r)
{
    if (r != NULL)
    {
        inorder(r->left);
        printf("%s\n", r->key);
        inorder(r->right);
    }
}

bstnodePtr deleteNode(bstnodePtr t, char key[50])
{
    // base case
    if (t == NULL) return t;
 
    // If the key to be deleted is smaller than the root's key,
    // then it lies in left subtree
    if (strcmp(key, t->key)<0)
        t->left = deleteNode(t->left, key);
 
    // If the key to be deleted is greater than the root's key,
    // then it lies in right subtree
    else if (strcmp(key, t->key)>0)
        t->right = deleteNode(t->right, key);
 
    // if key is same as root's key, then This is the node
    // to be deleted
    else
    {
        // node with only one child or no child
        if (t->left == NULL)
        {
            bstnodePtr temp = t->right;
            free(t);
            return temp;
        }
        else if (t->right == NULL)
        {
            bstnodePtr temp = t->left;
            free(t);
            return temp;
        }
 
        // node with two children: Get the inorder successor (smallest
        // in the right subtree)
        bstnodePtr temp = minValueNode(t->right);
 
        // Copy the inorder successor's content to this node
        memcpy(t->key,  temp->key, sizeof t->key);
 
        // Delete the inorder successor
        t->right = deleteNode(t->right, temp->key);
    }
    return t;
}
/* A utility function to insert a new node with given key in BST */
bstnodePtr bst_insert(bstnodePtr bnode, char key[50])
{
    /* If the tree is empty, return a new node */
    if (bnode == NULL) return bst_newNode(key);
 
    /* Otherwise, recur down the tree */
    if (strcmp(key, bnode->key)<0) 
        bnode->left  = bst_insert(bnode->left, key);
    else if (strcmp(key, bnode->key)>0)
        bnode->right = bst_insert(bnode->right, key);   
 
    /* return the (unchanged) node pointer */
    return bnode;
}
// A utility function to get maximum of two integers
int max(int a, int b);
 
// A utility function to get height of the tree
int height(struct node *N)
{
    if (N == NULL)
        return 0;
    return N->height;
}

int getBalance(struct node *N)
{
    if (N == NULL)
        return 0;
    return height(N->left) - height(N->right);
}
 
// A utility function to get maximum of two integers
int max(int a, int b)
{
    return (a > b)? a : b;
}

/* Helper function that allocates a new node1 with the given key and
    NULL left and right pointers. */
struct node* newnode(char key[50])
{
    struct node* node1 = (struct node*)malloc(sizeof(struct node));
    memcpy(node1->key, key, sizeof node1->key);
    node1->left   = NULL;
    node1->right  = NULL;
    node1->height = 1;  // new node1 is initially added at leaf
    return(node1);
}
// A utility function to right rotate subtree rooted with y
// See the diagram given above.
struct node *rightRotate(struct node *y)
{
    struct node *x = y->left;
    struct node *T2 = x->right;
 
    // Perform rotation
    x->right = y;
    y->left = T2;
 
    // Update heights
    y->height = max(height(y->left), height(y->right))+1;
    x->height = max(height(x->left), height(x->right))+1;
 
    // Return new root
    return x;
}
 
// A utility function to left rotate subtree rooted with x
// See the diagram given above.
struct node *leftRotate(struct node *x)
{
    struct node *y = x->right;
    struct node *T2 = y->left;
 
    // Perform rotation
    y->left = x;
    x->right = T2;
 
    //  Update heights
    x->height = max(height(x->left), height(x->right))+1;
    y->height = max(height(y->left), height(y->right))+1;
 
    // Return new root
    return y;
}
struct node *aminValueNode(struct node *newN)
{
    struct node *current = newN;
 
    /* loop down to find the leftmost leaf */
    while (current->left != NULL)
        current = current->left;
 
    return current;
}
 
// Recursive function to delete a node with given key
// from subtree with given root. It returns root of
// the modified subtree.
struct node *avldeleteNode(struct node *node2, char key[20])
{
    // STEP 1: PERFORM STANDARD BST DELETE
 
    if (node2 == NULL)
        return node2;
 
    // If the key to be deleted is smaller than the
    // root's key, then it lies in left subtree
    if ( strcmp(key, node2->key)<0 )
        node2->left = avldeleteNode(node2->left, key);
 
    // If the key to be deleted is greater than the
    // root's key, then it lies in right subtree
    else if( strcmp(key, node2->key)>0 )
        node2->right = avldeleteNode(node2->right, key);
 
    // if key is same as root's key, then This is
    // the node to be deleted
    else
    {
        // node with only one child or no child
        if( (node2->left == NULL) || (node2->right == NULL) )
        {
            struct node *temp = node2->left ? node2->left : node2->right;
 
            // No child case
            if (temp == NULL)
            {
                temp = node2;
                node2 = NULL;
            }
            else // One child case
             *node2 = *temp; // Copy the contents of
                            // the non-empty child
            free(temp);
        }
        else
        {
            // node with two children: Get the inorder
            // successor (smallest in the right subtree)
            struct node* temp = aminValueNode(node2->right);
 
            // Copy the inorder successor's data to this node
            memcpy(node2->key, temp->key, sizeof node2->key);
 
            // Delete the inorder successor
            node2->right = avldeleteNode(node2->right, temp->key);
        }
    }
 
    // If the tree had only one node then return
    if (node2 == NULL)
      return node2;
 
    // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
    node2->height = 1 + max(height(node2->left), height(node2->right));
 
    // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to
    // check whether this node became unbalanced)
    int balance = getBalance(node2);
 
    // If this node becomes unbalanced, then there are 4 cases
 
    // Left Left Case
    if (balance > 1 && getBalance(node2->left) >= 0)
        return rightRotate(node2);
 
    // Left Right Case
    if (balance > 1 && getBalance(node2->left) < 0)
    {
        node2->left =  leftRotate(node2->left);
        return rightRotate(node2);
    }
 
    // Right Right Case
    if (balance < -1 && getBalance(node2->right) <= 0)
        return leftRotate(node2);
 
    // Right Left Case
    if (balance < -1 && getBalance(node2->right) > 0)
    {
        node2->right = rightRotate(node2->right);
        return leftRotate(node2);
    }
 
    return node2;
}
void preOrder(struct node *root)
{
    if(root != NULL)
    {
        printf("%s ", root->key);
        preOrder(root->left);
        preOrder(root->right);
    }
}
// Recursive function to insert key in subtree rooted
// with node1 and returns new root of subtree.
struct node* insert(struct node* node1, char key[50])
{
    /* 1.  Perform the normal BST insertion */
    if (node1 == NULL)
        return(newnode(key));
 
    if (strcmp(key, node1->key)<0)
        node1->left  = insert(node1->left, key);
    else if (strcmp(key, node1->key)>0)
        node1->right = insert(node1->right, key);
    else // Equal keys are not allowed in BST
        return node1;
 
    /* 2. Update height of this ancestor node1 */
    node1->height = 1 + max(height(node1->left), height(node1->right));
 
    /* 3. Get the balance factor of this ancestor
          node1 to check whether this node1 became
          unbalanced */
    int balance = getBalance(node1);
 
    // If this node1 becomes unbalanced, then
    // there are 4 cases
 
    // Left Left Case
    if (balance > 1 && strcmp(key, node1->left->key)<0)
        return rightRotate(node1);
 
    // Right Right Case
    if (balance < -1 && strcmp(key, node1->right->key)>0)
        return leftRotate(node1);
 
    // Left Right Case
    if (balance > 1 && strcmp(key, node1->left->key)>0)
    {
        node1->left =  leftRotate(node1->left);
        return rightRotate(node1);
    }
 
    // Right Left Case
    if (balance < -1 && strcmp(key, node1->right->key)<0)
    {
        node1->right = rightRotate(node1->right);
        return leftRotate(node1);
    }
 
    /* return the (unchanged) node1 pointer */
    return node1;
}
int main(){
	
	r = NULL;
    bstnodePtr r = (bstnodePtr)malloc(sizeof(bstnode));
    r->left = NULL;
    r->right = NULL;
   
	FILE *fp1, *fp2;
    char word[50];
	char word2[50];//create a string to get words from files
	int c1, c2,current1,current2;
	int usage1, usage2;
	fp1 = fopen("input1.txt", "r");//open the first file
    clock_t time1 = clock();
    //get words from file
    while(fscanf(fp1, "%49s", word)>0){
		insert(r, word);
	}
    fclose(fp1);
	clock_t time2 = clock();
	
	printf("Time spent for inserting: %f\n", (double)(time2-time1));
	usage1 = sizeof(*r);
	printf("Usage after deletion is: %d\n", usage1);
    
	fp2 = fopen("input1.txt", "r");
	clock_t time3 = clock();
	int count = 1;
	while(fscanf(fp2, "%49s", word2)>0 && count <=1000){
		deleteNode(r, word2);
		count++;		
    }
    
    clock_t time4 = clock();
    printf("Time spent for deletion: %f\n", (double)(time4-time3));
    usage2 = sizeof(*r);  
	printf("Usage after deletion is: %d\n", usage2);
   fclose(fp2);
   
   //second part of program for avl tree
    root = NULL;
  	struct node *root = (struct node *)malloc(sizeof(struct node)); 
  	
  	root->left = NULL;
    root->right = NULL;
 
    FILE *fp3, *fp4;
    char word3[50];
	char word4[50];//create a string to get words from filesjhjhjhjhjhjhjnbnb

    fp3 = fopen("input1.txt", "r");//open the first file
	    
    clock_t t1 = clock();
    //get words from file
    while(fscanf(fp3, "%49s", word)>0){
		insert(root, word3);
			
    }
    fclose(fp3);
    clock_t t2 = clock();
    printf("Time spent for inserting: %f\n", (double)t2-t1);
    usage1 = sizeof(*root);  
	printf("Usage after insertion is: %d\n", usage1);
    clock_t t3 = clock();
    fp4 = fopen("input1.txt", "r");
    int count2 = 1;
    while(fscanf(fp2, "%24s", word2)>0 && count2 <=1000){
		deleteNode(root, word2);
		count++;		
    }
	clock_t t4 = clock();
	printf("Time spent for removing: %f: \n", (double)(t4-t3));
	usage2 = sizeof(*root);  
	printf("Usage after insertion is: %d\n", usage2);
    
    fclose(fp2);
    fclose(fp4);
	return 0;
}

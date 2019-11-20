
/**  * 
	* What is this program and what its purpose ? 
	* 
	* Well, simply this program enables us to compare two different text fields using Linked Lists.
	* Its purpose is to compare two text fields and finding common one and two words separately with the length of 55.
	* To do so, txt files must be named as "file1.txt" and "file2.txt".
	* If file1.txt or file2.txt does not exist it won't execute the program.
	* So please check the file names.
	* 
	*
	* This program created by Muhammet ÞERAMET.
	* ID:150115069
	* Marmara University 
	* Department Of Engineering
	* Computer Enginner Sophomore student
	* 
	*
	* email : mseramet02 at gmail dot com   **/
	
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

struct node {
    char string[55]; /* length of the data */
    struct node *next;
};
typedef struct node LIST;
typedef LIST *ListNodePtr;

ListNodePtr head1,head2;
FILE *fp1,*fp2;

void readFile1();
void readFile2();
void printList( ListNodePtr );
void filter_alnum(char *);
void bubbleSort(ListNodePtr);
void swap(ListNodePtr , ListNodePtr);
int is_repeat(ListNodePtr, char[]);

ListNodePtr common_words();
ListNodePtr insert_list(ListNodePtr,char[]);
ListNodePtr common_two_gram_words();

main(void){
	/* reading files and creating linkedlist for both files */
	readFile1();
	readFile2();	
	
	printf("\n\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2 MAIN MENU \xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\n");
	printf("First file is");
	printList(head1);
	printf("Second file is");
	printList(head2);
	
	ListNodePtr common = common_words();
	printf("Common words are");
	bubbleSort(common);
	printList(common);
	
	ListNodePtr common2 = common_two_gram_words();
	printf("Common two gram words are");
	bubbleSort(common2);
	printList(common2);
	
	printf("Program ends.\n");
	printf("\t Bye.\n\n");
	printf("\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\xB2\n");
	
	/* closing the files */
	fclose(fp1);
	fclose(fp2);
}
/* function to read first file */
void readFile1()
{	
	char text[55];
	int c;
	fp1 = fopen("file1.txt","r");
	if(fp1 == NULL){
		printf("File1 is not created \nProgram ends.");
		exit(0);
	}
	
	do{
		c = fscanf(fp1,"%s",text);
		/* string lower function */
		strlwr(text);
		if( c !=EOF){
			head1 = insert_list(head1,text); /* insert to linkedlist1 */
		}	
	} while(c != EOF); /* while end of file */		
}
/* function to read second file */
void readFile2()
{
	char text[55];
	int c;
	fp2 = fopen("file2.txt","r");
	if(fp2 == NULL){
		printf("File2 is not created \nProgram ends.");
		exit(0);
	}
	
	do{
		c = fscanf(fp2,"%s",text);
		strlwr(text);
		if( c !=EOF){ 
			head2 = insert_list(head2,text); /* insert to linkedlist2 */
		}	
	} while(c != EOF); /* while end of file */
	
}
/* function to create linkedlist */
ListNodePtr insert_list(ListNodePtr head,char *word)
{
	ListNodePtr temp = (ListNodePtr)malloc(sizeof(LIST));
	strcpy(temp->string, word);
	temp->next=NULL;
	if(head == NULL) head=temp; /* if linkedlist empty */
	else { 
		ListNodePtr temp1 = head;
		while(temp1->next != NULL){
			temp1 = temp1->next;
		} 
		temp1->next = temp;
	}
	return head;
}
/* function to print LinkedList */
void printList( ListNodePtr currentPtr )
{
 	if ( currentPtr == NULL ){ /* if linkedlist empty */
 		printf( "List is empty.\n\n" );
 	}
 	else {
 		printf( " :\n" );
 		while ( currentPtr != NULL ) {
 			filter_alnum(currentPtr->string); /* removing all punctuation from string */
			printf("%s --> ", currentPtr->string);
			currentPtr = currentPtr->next;
		}
		printf( "NULL\n\n" );
	}
}
/* function to remove all punctuations */
void filter_alnum(char *str) {
	char *p;
  for ( p=str ; *p; ++p) /* while end of char* */
    if (isalnum(*p)) /* function that returns nonzero if p is alphanumeric */
      *str++ = *p; /* move to the next char */
  *str = '\0'; /* empty char for punctuations */
}
/* function to find common words on both linkedlists */
ListNodePtr common_words(){
	/* local variables */
	ListNodePtr temp = (ListNodePtr) malloc(sizeof(ListNodePtr));
	temp = NULL;
	char tempStr[55];
	ListNodePtr current1,current2;
	current1 = head1;
	current2 = head2;
		
	if( (current1 == NULL) || (current2 == NULL)){ /* checking both lists whether empty or not */
		printf("One of the lists is empty.");
	}
	else{
		while( current1 != NULL){ /* while eof #1 */
			while( current2 != NULL){ /* while eof #2 */
				if( !strcmp(current1->string,current2->string) ){ /* if they are equal */
					strcpy(tempStr, current1->string);
					if(is_repeat(temp,tempStr)){ /* check the string is repeating or not */
						temp = insert_list(temp,tempStr); /* insertion */
						break;
					}
				}
				current2 = current2->next; 
			}
			current1 = current1->next;
			current2 = head2; /* relocation current2 */
		}
	}
	return temp;
}
/* function to find whether the string is repeating or not */
/* return 0 if the word is repeating else return 1 */
int is_repeat(ListNodePtr firstPtr, char search[]){
	
	while(firstPtr != NULL){ 
		if( !strcmp(firstPtr->string, search) ){
			return 0;
		}
		firstPtr = firstPtr->next;
	}
	return 1;
}
/* function to find common two words on both lists */
ListNodePtr common_two_gram_words(){
	/* local variables */
	ListNodePtr t = (ListNodePtr) malloc(sizeof(ListNodePtr));
	t = NULL;
	ListNodePtr current1,current2;
	current1 = head1;
	current2 = head2;
		
	if( (current1 == NULL) || (current2 == NULL)){ /* checking both lists whether empty or not */
		printf("One of the lists is empty.");
	}
	else{
		while( current1 != NULL){ /* while eof #1 */
			while( current2 != NULL){ /* while eof #2 */	
				if( !strcmp(current1->string,current2->string) ){ /* if first word are equal */
					/* local variables to store next word */
					ListNodePtr next1,next2;
					next1 = current1->next;
					next2 = current2->next;
					
					if( (next1 != NULL) && (next2 != NULL) ){ 
						if( !strcmp(next1->string, next2->string) ){ /* if second word are equal */
							char temp[55];
							strcpy(temp, current1->string);
							strcat(temp,next2->string); /* adding second word to first word*/
							if(is_repeat(t,temp)){ /* is temp repeating  */
								t = insert_list(t,temp); /* inserting temp to list */
								break;
							}
						}
					}
						
					
				}
				current2 = current2->next;
			}
			current1 = current1->next;
			current2 = head2; /* relocation current2 */
		}
	}
	return t;
}
/* function for bubble sort */
void bubbleSort(ListNodePtr start)
{
	/* local variables */
    int swapped, i;
    ListNodePtr ptr1;
    ListNodePtr lptr = NULL;
 
    /* Checking for empty list */
    if (ptr1 == NULL)
        return;
 
    do
    {
        swapped = 0;
        ptr1 = start;
 
        while (ptr1->next != lptr) /* while end of list */
        {
            if ( strcmp(ptr1->string, ptr1->next->string) > 0) 
            { 
                swap(ptr1, ptr1->next); /* function to swap two nodes */
                swapped = 1;
            }
            ptr1 = ptr1->next;
        }
        lptr = ptr1;
    }
    while (swapped); /* while nodes swapped */
}
 
/* function to swap data of two nodes a and b*/
void swap(ListNodePtr a, ListNodePtr b)
{
    char temp[55];
	strcpy(temp, a->string);
    strcpy(a->string, b->string);
    strcpy(b->string, temp);
}

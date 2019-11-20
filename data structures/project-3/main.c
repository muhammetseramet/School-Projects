#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Graph{
	int numberOfVertex;
	int* visited; //for DFS
	int* color; //for GRAPH-COLOR-ALGORITHM
	struct Vertex** adjLists;
};

struct Vertex{
	char* name;
	struct Vertex* next;
};

struct Graph* newGraph(int verticies)
{
	struct Graph* tempGraph = malloc(sizeof (struct Graph));
	tempGraph->numberOfVertex = verticies;
	tempGraph->adjLists = malloc(verticies * sizeof(struct Vertex*));
	tempGraph->visited = malloc(verticies * sizeof(int));
	tempGraph->color = malloc(verticies * sizeof(int));
	
	int i;
    for (i = 0; i < verticies; i++) {
       	tempGraph->adjLists[i] = NULL;
       	tempGraph->visited[i] = 0;
       	tempGraph->color[i] = -1;
   	}
	return tempGraph;
}

struct Vertex* newVertex(char *temp)
{
	struct Vertex* newNode = malloc(sizeof(struct Vertex));
    newNode->name = temp;
	newNode->next = NULL;
    return newNode;
}

struct Graph* insert(struct Graph* graph, char* src){
	// bu kýsým artýk çalýþýyor
	if(find(graph,src) != -1){
		return graph;
	}
	else{
		//struct Vertex* vertex = newVertex(src);
		int i;
		for(i = 0; i < graph->numberOfVertex; i++){
			struct Vertex* temp = graph->adjLists[i];
			if(temp == NULL){
				graph->adjLists[i] = newVertex(src);
				printf(" %d sirada  %s eklendi \n", i, graph->adjLists[i]->name);
				return graph;
			}
		}
	}
}

int find(struct Graph* graph, char* name)
{
		
	if(graph->adjLists[0] == NULL)
		return -1;
	int i;
	for(i = 0; i < graph->numberOfVertex; i++){
		struct Vertex* temp = graph->adjLists[i];
		if(temp){
			if(strcmp(temp->name, name) == 0){
				return i; //bulursa location
			}
		}
	}
	return -1; //bulamazsa eksi bir
}

void addEdge(struct Graph* graph, char* src, char* dest)
{
	if(src == dest)//no clyle allowed
		return;
	
	graph = insert(graph, src);
	graph = insert(graph, dest);
	int srcLoc = find(graph, src);
	int destLoc = find(graph, dest);
	
	struct Vertex* temp = newVertex(dest);	
	temp->next = graph->adjLists[srcLoc]->next;
	graph->adjLists[srcLoc]->next = temp;
	
	temp = newVertex(src);
	temp->next = graph->adjLists[destLoc]->next;
	graph->adjLists[destLoc]->next = temp;
}

void printGraph(struct Graph* graph)
{
    int v;
    for (v = 0; v < graph->numberOfVertex; v++)
    {
        struct Vertex* temp = graph->adjLists[v];
        //if(temp){
        	while(temp){
        		printf("%s -> ", temp->name);
        		temp = temp->next;
			}
			//printf("\n");
		//}
		printf("\n");
    }
}

void DFS(struct Graph* graph, int index)
{
	struct Vertex* list = graph->adjLists[index];
	struct Vertex* temp = list;
	
	graph->visited[index] = 1;
	printf(" %s is visited \n", list->name);
	
	while(temp != NULL){
		int connected = find(graph, temp->name);
		
		if(graph->visited[connected] == 0){
			DFS(graph, connected);
		}
		temp = temp->next;
	}
}

void swap(struct Graph* graph, int i, int j)
{
	
	struct Vertex* temp = graph->adjLists[i];
	graph->adjLists[i] = graph->adjLists[j];
	graph->adjLists[j] = temp;
	
}

void bubbleSort(struct Graph* graph)
{
	int i, j;
	for(i = 0; i < graph->numberOfVertex; i++){ 
		for(j = 0; j < (graph->numberOfVertex - i -1); j++){
			struct Vertex* key1 = graph->adjLists[j];
			struct Vertex* key2 = graph->adjLists[j+1];
			
			if(key1 && key2){
				if(strcmp(key1->name, key2->name) == 1){
					swap(graph, j, j+1);
				}
			}
		}			
	}
}

int isColorUsed(struct Graph* graph, int index, int color)
{
	struct Vertex* temp = graph->adjLists[index];
	
	while(temp){
		int adjLoc = find(graph, temp->name);
		int adjColor = graph->color[adjLoc];
		
		if(adjColor == color)
			return 1;
		temp = temp->next;
	}
	return 0;	
}

void colorAlgorithm(struct Graph* graph, int index)
{
	int j = 0;
	int used = isColorUsed(graph, index, j);
	while(used){
		
		j++;
		used = isColorUsed(graph, index, j);
	}
	graph->color[index] = j;
}

int countLessons(FILE* fp)
{
	int count = 0;
	char line[200];
	const char* delim = ",\n\t\r";
	while ( fgets(line, sizeof line, fp) != NULL ){
		char* l = strdup(line);
		char* temp = strtok(l, ":");
		char* lessons = strtok(NULL, delim);
		char* firstLesson = lessons;
		while(lessons){
			count++;
			lessons = strtok(NULL, delim);
		}
	}
	return count;
}

int main(){
	
	FILE *fp;
	fp = fopen("example.txt","r");
	int numberOfLessons = countLessons(fp);
	struct Graph* graph = newGraph(numberOfLessons);
	rewind(fp);
	
	char line[200];
	const char* delim = ",";
	while ( fgets(line, sizeof line, fp) != NULL ){
		char* l = strdup(line);
		char* temp = strtok(l, ":");
		char* lessons = strtok(NULL, "\n\t\r");
		char* firstLessons = strtok(lessons, delim);
		char* first = firstLessons;
		while(firstLessons){
			addEdge(graph, first, firstLessons);
			firstLessons = strtok(NULL, delim);
		}
	}
	bubbleSort(graph);
	printf("\n\n\n");
	DFS(graph, 0);
	printf("\n\n\n");
	
	int i;
	for(i = 0; i < graph->numberOfVertex; i++)
		colorAlgorithm(graph, i);
	
	
	int j;
	for(j = 0; j < graph->numberOfVertex; j++)
		if(graph->adjLists[j])
			printf("\n %d -> %s ", graph->color[j], graph->adjLists[j]->name);
	fclose(fp);
	return 0;
}

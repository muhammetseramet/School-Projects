#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <signal.h>
#include <stdarg.h>
#include <fcntl.h>

char **inputArgs;
char **outputArgs;

#define MAX_LINE 80 /* 80 chars per line, per command, should be enough. */

/* The setup function below will not return any value, but it will just: read
in the next command line; separate it into distinct arguments (using blanks as
delimiters), and set the args array entries to point to the beginning of what
will become null-terminated, C-style strings. */

struct alias {
    char *name; /* The name of the alias */
    char *script; /* The script that the alias will hold*/
    int status; /* To check the alias status */
    struct alias *next; /* Stack structure */
};
struct alias *root = NULL;

struct alias *find(char *string) {

    struct alias *temp = root;
    while (temp != NULL) {
        /* If the alias that we looking for is found
         * and it is still active */
        if ((strncmp(temp->name, string, 127) == 0) && (temp->status == 1)){
            return temp;
        }

        temp = temp->next;
    }

    return NULL;
}

/* Insert operation for STACK structure */
void insert(char *name, char *bash) {

    struct alias *new_node = malloc(sizeof(struct alias));
    new_node->next = (struct alias *) malloc(sizeof(struct alias));
    new_node->name = name;
    new_node->script = bash;
    new_node->status = 1;
    new_node->next = root;

    root = new_node;
}

/* Unalias structure */
void delete_node(char *string) {

    struct alias *node = find(string);

    if (node == NULL) {
        fprintf(stderr, "Command :%s not found.\n", string);
        return;
    }
    /* The alias will no longer be used */
    node->status = 0;
}

/* Print the alias structure */
void print() {

    struct alias *temp = root;
    while (temp != NULL) {

        if (temp->status == 1)
            fprintf(stdout,"\t%s  %s\n", temp->name, temp->script);

        temp = temp->next;
    }
}

char* findTheLastArg(char* args[], int counter){

    while(1){

        if(args[counter] == NULL){
            return args[counter-1];
        }
        counter++;
    }

}

char** pipeInput(char* args[], int counter){
    char **inputArgs;
    inputArgs = malloc((MAX_LINE/2)* sizeof(*inputArgs));

    for(unsigned int x=0; x<counter; x++){

        if(strcmp(args[x], "|")==0){
            for(int i=0; i<x; i++){
                inputArgs[i] = args[i];
            }
            return inputArgs;

        }

    }

}

char** pipeOutput(char* args[], int counter){
    char **outputArgs;
    outputArgs = malloc((MAX_LINE/2)* sizeof(*inputArgs));
    int b=0;
    for(unsigned int x=0; x<counter; x++){

        if(strcmp(args[x], "|")==0){
            b=x+1;
            for(int i=0; i<counter-x; i++){
                outputArgs[i] = args[b];
                b++;
            }
            return outputArgs;

        }

    }

}

char* findInputFile(char *args[], int counter){

    while (1){

        if(strcmp(args[counter], "<") == 0){

            return args[counter+1];

        }
        counter++;

    }

}

void setArrowToNull(char *args[], int counter){

    for(int i=0; i < counter; i++) {
        char *temp = strdup(args[i]);
        if(strncmp(temp, "<", 128) == 0){
            args[i] = NULL;
            args[i + 1] = NULL;
            /* if(strcmp(args[i+2], ">") == 0){      // bu if olmadan çalışıyor inputtan alıp direkt ekrana basma işlemi.
                 args[i+2] = NULL;
                 args[i+3] = NULL;
             }*/

            return;

        }else if (strncmp(temp, ">>", 128) == 0) {
            args[i] = NULL;
            args[i + 1] = NULL;
            return;

        }else if (strncmp(temp, ">", 128) == 0) {
            args[i] = NULL;
            args[i + 1] = NULL;
            return;
        }else if(strncmp(temp, "2>", 128)==0){
            args[i] = NULL;
            args[i + 1] = NULL;
            return;

        }

    }

    for (int j = 0; j < counter; ++j) {
        fprintf(stdout, "args%d :%s\n", j, args[j]);
    }


}

void setLineToNull(char* args[], int counter){

    for(int i=0; i<counter; i++){
        if(strcmp(args[i], "|")==0){
            args[i] = NULL;
            return;
        }
    }

}

/* General PATH finder and executer */
void splitter(char *argv[]){

    const char *path = getenv("PATH");
    const char *dot = ".";
    char del = ':';

    int i;
    char *temp = strdup(path);

    /* how many path exactly do we have */
    int number_of_path = 0;
    int len = strlen(temp);
    for (i = 0;  i<len ; i++) {
        if(temp[i] == del)
        {
            number_of_path++;
            temp[i] = '\0';
        }
    }
    /* To hold all possible PATH */
    const char **arr = malloc((number_of_path + 1) * sizeof(*arr));
    arr[0] = temp;

    int count = 0;
    for (i = 0; i < len; i++){
        if (temp[i] == '\0')
        {
            count++;
            arr[count] = temp+i+1;
            if (arr[count][0] == '\0')
                arr[count] = dot;
        }
    }

    char *sep = "/";
    for (i = 0;  i<number_of_path+1 ; i++) {
        char buf[256];
        snprintf(buf, sizeof buf, "%s%s%s", arr[i], sep, argv[0]);
        /* Find all PATH's with starting with arr and ends with argv[0] */

        if(access(buf, F_OK) == 0){
            /* If the file exist in this path execute it*/
            execl(buf, argv[0], argv[1], argv[2], argv[3], argv[4], argv[5], argv[6], argv[7], argv[8], argv[9],
                  argv[9], argv[10], argv[11], argv[12], argv[13], argv[14], argv[15], argv[16], argv[17], argv[18],
                  argv[19], argv[20], argv[21], (char *)0);
        }

    }
    /* Couldn't found any path to execute */
    fprintf(stderr, "Command not found..\n");
    exit(errno);
}

// burada pek bir değişiklik yok..
void setup(char inputBuffer[], char *args[], int *background, int *counter, int *redirect, int *isPipe) {
    int length, /* # of characters in the command line */
            i,      /* loop index for accessing inputBuffer array */
            start,  /* index where beginning of next command parameter is */
            append,
            ct;     /* index of where to place the next parameter into args[] */

    ct = 0;
    append=0;
    int inputToOutput =0;
    int *ptrInputToOutput = &inputToOutput;

    /* read what the user enters on the command line */
    length = read(STDIN_FILENO, inputBuffer, MAX_LINE);

    /* 0 is the system predefined file descriptor for stdin (standard input),
       which is the user's screen in this case. inputBuffer by itself is the
       same as &inputBuffer[0], i.e. the starting address of where to store
       the command that is read, and length holds the number of characters
       read in. inputBuffer is not a null terminated C-string. */

    start = -1;
    if (length == 0)
        exit(0);            /* ^d was entered, end of user command stream */

    /* the signal interrupted the read system call */
    /* if the process is in the read() system call, read returns -1
    However, if this occurs, errno is set to EINTR. We can check this  value
    and disregard the -1 value */
    if ((length < 0) && (errno != EINTR)) {
        perror("error reading the command");
        exit(-1);           /* terminate with error code of -1 */
    }

    for (i = 0; i < length; i++) { /* examine every character in the inputBuffer */
        switch (inputBuffer[i]) {
            case ' ':
            case '\t': /* argument separators */
                if (start != -1) {
                    args[ct] = &inputBuffer[start];    /* set up pointer */
                    ct++;
                }
                inputBuffer[i] = '\0'; /* add a null char; make a C string */
                start = -1;
                break;

            case '\n':                 /* should be the final char examined */
                if (start != -1) {
                    args[ct] = &inputBuffer[start];
                    ct++;
                }
                inputBuffer[i] = '\0';
                args[ct] = NULL; /* no more arguments to this command */
                break;

            default :             /* some other character */
                if (start == -1)
                    start = i;
                if (inputBuffer[i] == '&') {
                    *background = 1;
                    inputBuffer[i - 1] = '\0';
                }

                if(inputBuffer[i] == '>' && append==0){
                    *redirect=2;
                    if(inputBuffer[i+1] == '>'){
                        *redirect=1;
                        append = 1;
                    }


                }else if(inputBuffer[i] == '<' && inputToOutput == 0){
                    *redirect=3;
                    for(int z=0; z<length; z++){
                        if(inputBuffer[z] == '>'){

                            *redirect = 5;
                            inputBuffer[i-1] = '\0';
                            start = -1;
                            inputToOutput=1;                       //olay burası ve null yapma methodu arasında geçiyor
                            break;
                        }
                    }

                }else if(inputBuffer[i] == 2 && inputBuffer[i+1] == '>'){
                    *redirect = 4;// error
                }
                if(inputBuffer[i] == '|'){
                    *isPipe=1;
                }

        } /* end of switch */
    }    /* end of for */
    args[ct] = NULL; /* just in case the input line was > 80 */

    *counter = ct;

} /* end of setup routine */

// job structure
#define UNDEF 0 // undefinded for Main Process
#define FG 1    // foreground processes
#define BG 2    // background processes
#define ST 3    // stopped processes

struct job_t {
    pid_t pid;              // process id
    int state;              // state of the process
    int is_active;          // for deletion
    char *cmdline;          // command line
    struct job_t *next;     // STACK structure
};
struct job_t *main_j = NULL; /* The job list */

// search job structure
struct job_t *search_j(pid_t pid){

    struct job_t *temp = main_j;

    while(temp){

        if((temp->pid == pid) && temp->is_active)
            return temp;

        temp = temp->next;
    }

    return NULL;
}

// insert to job structure
void insert_j(pid_t pid, char *string, int state){

    struct job_t *node = search_j(pid);

    if (node != NULL){
        return;
    }

    node = (struct job_t *) malloc(sizeof(struct job_t));
    node->pid = pid;
    node->cmdline = string;
    node->state = state;
    node->is_active = 1;
    node->next = main_j;

    main_j = node;
}

// deletion for job
void delete_j(pid_t pid){

    struct job_t *temp = search_j(pid);

    if(temp){
        temp->state = ST;
        temp->is_active = 0;
        return;
    }
    else
        return;
}

// print all the BACKGROUND processes
void print_j(){

    struct job_t *structjob = main_j;

    while (structjob->state == BG){
        /*fprintf(stdout, "PID : %ld ; CMDLINE : %s STATE : %d ; ISACTIVE : %d ; \n", (long) structjob->pid,
                structjob->cmdline, structjob->state, structjob->is_active);*/
        structjob = structjob->next;
    }
}

// count the number of spaces
int string_parser(char *string){
    int ret = 1;
    char del = ' ';
    for (int i = 0; i < strlen(string); i++) {
        if(string[i] == del){
            ret++;
        }
    }
    return  ret;
}

// check BG process
int is_there_any_BG_process(){

    struct job_t *temp = main_j;

    while (temp){

        if((temp->state == BG) && (getpgid(temp->pid) != -1)){

            fprintf(stdout, "DEGER : %ld\n", (long) temp->pid);
            fprintf(stderr, "There is a background process. You can't exit now..\n");
            return 1;
        }
        temp = temp->next;
    }

    return 0;
}

// to make sure that user did not send & to execl command
void setAmpersandToNull(char *args[], int counter){
    for (int i = 0; i < counter; i++) {

        if (strcmp(args[i], "&") == 0){
            args[i] = NULL;
            return;
        }
    }
}

// return the name of alias
char *find_name(char *args[], int counter){
    char del = '"';
    char *name = NULL;
    int number = 0;

    for (int i = 0; i < counter; i++) {
        name = args[i];
        char *temp = strdup(name);

        for (int j = 0; j < strlen(temp); j++) {
            if(temp[j] == del){
                number++;
                if(number == 2){
                    name = args[i+1];

                    if(args[i+2]){
                        //BURADA ERROR VER.
                        fprintf(stderr, "Command not found : too many alias names..\n");
                        return NULL;
                    }

                    if(name){
                        return name;
                    }
                    else{
                        fprintf(stderr, "Command not found : alias name cannot be empty..\n");
                        return NULL;
                    }
                }
            }
        }
    }
    return NULL;
}

// return the bash script of alias
char *find_command(char *args[], int counter){
    char del = '"';
    int number = 0;
    char *cmd = "";
    int start=0, last=0;

    for (int i = 0; i < counter; i++) {
        cmd = args[i];
        char *temp = strdup(cmd);

        for(int z = 0; z < strlen(temp); z++){
            if(temp[z] == del){
                start = i;
                number++;

                if(temp[strlen(temp)-1] == del){
                    return temp;
                }

                int j=i+1;
                for (j; j < counter-1; j++){
                    cmd = args[j];
                    temp = strdup(cmd);
                    for (int k = 0; k < strlen(temp); k++) {
                        if(temp[k] == del){
                            number++;
                            if (number == 2){
                                last = j;
                                // BURADADAN DEVAM ET
                                // ARGS DAN TEK SATIRDA OKUMA...
                                const char *arr[counter];
                                for (int p = 0; p < counter; p++)
                                    arr[p] = strdup(args[p]);

                                char buffer[128];
                                const char *sep = " ";
                                strcpy(buffer, arr[1]);
                                for (int z = 2; z <= last; z++) {
                                    strcat(buffer, sep);
                                    strcat(buffer, arr[z]);
                                }
                                char *ret = buffer;

                                return ret;
                            }
                        }
                    }
                }
                fprintf(stderr, "Command not found : there should be two quotes..\n");
                return NULL;
            }
        }
    }
    fprintf(stderr, "Command not found : there should be two quotes..\n");
    return NULL;
}

// to find pid of a Foreground process
pid_t found_pid(struct job_t *jobs) {

    while (jobs){

        if(jobs->state == FG)
            return jobs->pid;

        jobs = jobs->next;
    }

    return 0;
}

// signal handler for Foreground process
void sigtstp_handler(int sig) {
    int pid = found_pid(main_j);

    if (pid != 0) {

        printf("Job (%d) Stopped by signal %d Ctrl+Z\n", pid, sig);
        kill(pid, SIGKILL);
    }
    return;
}

// carry bg processes to foreground
void carry_process(){
    struct job_t *temp = main_j;
    struct job_t *myshell = NULL;
    int status;
    while(temp){

        if(temp->state == BG){

            pid_t pid = getpgid(temp->pid);

            // if process is terminated
            if(pid == -1){
                temp->state = ST;
                temp->is_active = 0;
                temp = temp->next;
                continue;
            }

            fprintf(stdout, "The %s is stopping now..\n", temp->cmdline);
            fprintf(stdout, "The %s is in the foreground now..\n", temp->cmdline);
            // stop bg process
            kill(temp->pid,SIGSTOP);

            //if(tcsetpgrp(STDIN_FILENO, getpgid(temp->pid)) == -1)
            //    perror("tcsetpgrp failed..\n");


            // continue bg process
            kill(temp->pid, SIGCONT);
            signal(SIGTSTP, sigtstp_handler);
            // wait until it closes
            waitpid(temp->pid, &status, WUNTRACED);
            fprintf(stdout, "The %s is closed..\n", temp->cmdline);
            temp->state = ST;
            temp->is_active = 0;
        }

        if(temp->state == UNDEF)
            myshell = temp;

        temp = temp->next;
    }
    fprintf(stdout, "Returning to myshell..\n");
    // get back to main process
    if(tcsetpgrp(STDIN_FILENO, getpgid(myshell->pid)))
        perror("tcsetpgrp failed..\n");

}

int main(void) {
    char inputBuffer[MAX_LINE]; /*buffer to hold command entered */
    int background; /* equals 1 if a command is followed by '&' */
    char *args[MAX_LINE / 2 + 1]; /*command line arguments */
    int isPipe;
    int redirect;
    int counter;    // to count how many argumants that user typed
    int pipefd[2];
    pipe(pipefd);

    // insert main process to job structure
    insert_j(getpid(), "myshell", UNDEF);

    while (1) {
        background = 0;
        redirect = 0;
        isPipe=0;

        fprintf(stdout, "myshell :");
        fflush(stdout);

        /*setup() calls exit() when Control-D is entered */
        setup(inputBuffer, args, &background, &counter, &redirect, &isPipe);

        if (counter == 0)
            continue;

        /** the steps are:
        (1) fork a child process using fork()
        (2) the child process will invoke execv()
		(3) if background == 0, the parent will wait,
        otherwise it will invoke the setup() function again. */

        setAmpersandToNull(args, counter);

        // clr
        if (strncmp(args[0], "clr", 127) == 0){
            system("clear");
        }
        // alias
        else if(strncmp(args[0], "alias", 127) == 0){
            if(args[1] != NULL){

                // print all aliases
                if(strncmp(args[1], "-l", 127) == 0){
                    print();
                }
                else{

                    // find name and command from args
                    char *name = find_name(args, counter);
                    char *command = find_command(args, counter);
                    const char *asd = name;
                    const char *asd2 = command;

                    if(asd && asd2){
                        char *cmd = strdup(asd);
                        char *bash = strdup(asd2);

                        // alias added
                        insert(cmd, bash);
                    }
                    else{
                        // couldn't find name or command from args
                        fprintf(stderr, "Command not found : wrong alias statement..\n");
                    }
                }
            }
            else{
                fprintf(stderr, "Command not found : too few arguments..\n");
            }
        }

        // unalias
        else if((strncmp(args[0], "unalias", 127) == 0) && (args[1] != NULL)){
            delete_node(args[1]);
        }

        // exit
        else if (strncmp(args[0], "exit", 127) == 0){

            // check for background process
            if(!is_there_any_BG_process())
                exit(0);
        }

        // when user types alias name
        else if(find(args[0])){
            // find alias script
            struct alias *run = find(args[0]);
            char *temp = run->script;
            const char delim = '\"';
            char *buf = &temp[1];
            char *dene;

            int deger = string_parser(temp);
            char *arr[deger];
            int c = 0;
            // split the quotes from the script
            while(dene = strsep(&buf, &delim)){

                if(dene[0] != '\0'){
                    char *temp = dene;
                    const char sep = ' ';
                    // split the spaces
                    while (temp = strsep(&dene, &sep)){
                        if(temp[0] != '\0'){
                            // store each command one by one at arr
                            arr[c] = strdup(temp);
                            c++;
                        }
                    }

                }
            }

            pid_t pid;

            pid = fork();

            if (pid == -1) {
                perror("Failed to fork");
                return 1;
            }
            if (pid == 0){
                splitter(arr);
                exit(0);
            }
            else if (pid < 0){
                fprintf(stderr, "A signal must have interrupted the wait!\n");
            }
            else {
                waitpid(pid, 0, WUNTRACED);
            }

        }

        // fg command
        else if ((strncmp(args[0], "fg", 127) == 0)){

            carry_process();
            // if BG THEN TO FG
        }
        else{
            pid_t childpid;
            childpid = fork();

            // error handling
            if (childpid == -1) {
                perror("Failed to fork");
                return 1;
            }
            if (childpid == 0){
                // child process

                if (isPipe == 1) {

                    dup2(pipefd[1], 1);
                    close(pipefd[1]);
                    splitter(pipeInput(args, counter));
                    exit(0);
                }
                else{

                    if(redirect == 1) {

                        int fd = open(findTheLastArg(args, 0), O_CREAT | O_RDWR | O_APPEND, S_IRUSR | S_IWUSR);
                        dup2(fd, 1);   // make stdout go to file

                        close(fd);
                    }else if(redirect == 2){
                        int fd = open(findTheLastArg(args,0), O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);

                        dup2(fd, 1);

                        close(fd);
                    }else if(redirect == 3){
                        int fd = open(findTheLastArg(args, 0), O_RDONLY, 0);
                        dup2(fd, 0);
                        close(fd);

                    }else if(redirect == 4){

                        int fd = open(findTheLastArg(args, 0), O_CREAT | O_RDWR, S_IRUSR | S_IWUSR);

                        dup2(fd, 2);
                        close(fd);
                    }
                    else if(redirect == 5){

                        int output = open(findTheLastArg(args, 0), O_CREAT | O_RDWR, S_IRUSR | S_IWUSR);
                        int input = open(findInputFile(args, 0), O_RDONLY, 0);
                        dup2(input, 0);
                        dup2(output, 1);
                        close(output);
                        close(input);
                    }
                    else if(redirect == 0){
                        splitter(args);
                        exit(0);
                    }

                    char *arr[counter];
                    for(int p=0; p < counter; p++) {
                        arr[p] = strdup(args[p]);
                    }
                    setArrowToNull(arr, counter);
                    splitter(arr);
                    exit(0);
                }


            }

            // interrupt error handler
            else if (childpid < 0){
                fprintf(stderr, "A signal must have interrupted the wait!\n");
            }
            else {

                if(isPipe==1){
                    dup2(pipefd[0], 0);
                    close(pipefd[0]);
                    close(pipefd[1]);
                    wait(NULL);
                    splitter(pipeOutput(args, counter));
                }


                // parent pid
                if (background) {

                    // ignore ctrl+z signal
                    signal(SIGTSTP, SIG_IGN);

                    setpgid(childpid, childpid);
                    // insert this process to job structure

                    const char *buf = args[0];
                    char *command = strdup(buf);
                    insert_j(childpid, command, BG);
                }
                else{
                    // to handle ctrl+z signal on foreground process
                    signal(SIGTSTP, sigtstp_handler);


                    // insert this process to job structure
                    const char *buf = args[0];
                    char *command = strdup(buf);
                    insert_j(childpid, command, FG);

                    // wait until child finishes
                    waitpid(childpid, 0, WUNTRACED);

                    // update job structure
                    struct job_t *temp = search_j(childpid);
                    temp->state = ST;
                }
                //fprintf(stdout, "PARENT ID: %ld with child id: %ld \n", (long) getpid(), childpid);
                print_j();
            }
        }
    }
}

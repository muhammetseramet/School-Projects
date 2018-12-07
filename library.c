#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <signal.h>
#include <stdarg.h>
#include <fcntl.h>
#include <bits/types/siginfo_t.h>

#define MAX_LINE 80 /* 80 chars per line, per command, should be enough. */

/* The setup function below will not return any value, but it will just: read
in the next command line; separate it into distinct arguments (using blanks as
delimiters), and set the args array entries to point to the beginning of what
will become null-terminated, C-style strings. */

// alias struct yapısı ve linked list için gerekli olan
// yapılar..
// burada hala eksilikler olabilir henüz test edemedim.
struct alias {
    /* bir örnek ile açıklayayım burayı..
     *
     * burada adam diyelim ki alias "ls -l" direct komutunu girdi
     *
     *  name -> direct dediğimiz yeri tutacak
     *  script -> ls -l komutunu tutacak
     *                  daha sonra bunu splitter fonksiyonu ile çalıştırcaz
     *  status -> bu sadece o alias ın kullanılıp veyahut kullanılmadığını belirlemek için..
     *             delete yapmamak için böyle bir yol buldum
     *             status 1 ise bu alias hala kullanılıyordur
     *             status 0 ise demekki kullanıcı unalias ile bunu bulup silmiş..
     *  next -> linked list yapısı için..
    */
    char *name;
    char *script;
    int status;
    struct alias *next;
};
struct alias *root = NULL;

// alias için search etcek varsa struct ı yoksa NULL döndürcek..
struct alias *find(char *string) {

    struct alias *temp = root;
    while (temp != NULL) {

        if ((strncmp(temp->name, string, 127) == 0) && (temp->status == 1)){
            return temp;
        }

        temp = temp->next;
    }

    return NULL;
}

// alias için insert methodu
void insert(char *name, char *bash) {

    struct alias *new_node = malloc(sizeof(struct alias));
    new_node->next = (struct alias *) malloc(sizeof(struct alias));
    new_node->name = name;
    new_node->script = bash;
    new_node->status = 1;
    //fprintf(stdout, "new node %s NAME, %s BASH.\n", new_node->name, new_node->script);
    new_node->next = root;

    root = new_node;
}

// burasını unalias için kullancaz
void delete_node(char *string) {

    struct alias *node = find(string);

    if (node == NULL) {
        fprintf(stderr, "Command :%s not found.\n", string);
        return;
    }

    node->status = 0;
}

// bu aliasları yazdırmak için, alias -l inputunda run etcez..
void print() {

    struct alias *temp = root;
    while (temp != NULL) {

        if (temp->status == 1)
            fprintf(stdout,"\t%s  \"%s\"\n", temp->name, temp->script);

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

        if(strcmp(args[i], "<") == 0){
            args[i] = NULL;
            args[i + 1] = NULL;
            /* if(strcmp(args[i+2], ">") == 0){      // bu if olmadan çalışıyor inputtan alıp direkt ekrana basma işlemi.
                 args[i+2] = NULL;
                 args[i+3] = NULL;
             }*/

            return;

        }else if (strcmp(args[i], ">>") == 0) {
            args[i] = NULL;
            args[i + 1] = NULL;
            return;

        }else if (strcmp(args[i], ">") == 0) {
            args[i] = NULL;
            args[i + 1] = NULL;
            return;
        }else if(strcmp(args[i], "2>")==0){
            args[i] = NULL;
            args[i + 1] = NULL;
            return;

        }

    }


}


/* bu fonksiyon path bulmak için.
mesela ls komutu /usr/bin dosyasının içinde
bu fonksiyon o dosyayı bulup içindeki ls i execl komutuyla
çaluştırıyor.. */
void splitter(char *argv[]){

    const char *path = getenv("PATH");
    const char *dot = ".";
    char del = ':';

    int i;
    char *temp = strdup(path);

    int number_of_path = 0;
    int len = strlen(temp);
    for (i = 0;  i<len ; i++) {
        if(temp[i] == del)
        {
            number_of_path++;
            temp[i] = '\0';
        }
    }

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

        if(access(buf, F_OK) == 0){
            //fprintf(stdout, "buffer : %s\n", buf);
            execl(buf, argv[0], argv[1], argv[2], argv[3], argv[4], argv[5], argv[6], argv[7], argv[8], argv[9],
                  argv[9], argv[10], argv[11], argv[12], argv[13], argv[14], argv[15], argv[16], argv[17], argv[18],
                  argv[19], argv[20], argv[21], (char *)0);
        }

    }
    exit(errno);
}

// burada pek bir değişiklik yok..
void setup(char inputBuffer[], char *args[], int *background, int *counter, int *redirect) {
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
                //case '"':
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

        } /* end of switch */
    }    /* end of for */
    args[ct] = NULL; /* just in case the input line was > 80 */

    *counter = ct;

} /* end of setup routine */


// burada hiçbir şey yok lazım olur belki diye bıraktım..
/*
void execute(char *string[]){

    // burada o stringi iyi split etmiş olması lazım.
    struct alias *node = find(string[3]);

    if(node == NULL){
        splitter(string);
        return;
    }
    splitter(string);
} // burasi tamamen hayal ürünüdür. */

/*
 * Buralar signal için gerekli
 * temel olarak oluşturulan processleri takip etmem lazım
 * bunun için yine bir linked list şeklinde processleri saklayabildiğim bir yapı
 * içeriği için struct a bak..
 * */
// job states
#define UNDEF 0 // undefinded // BU MAIN PROCESS İÇİN..
#define FG 1    // foreground // BU FOREGROUND PROCESSLER İÇİN
#define BG 2    // background // BU BACKGROUND PROCESSLER İÇİN
#define ST 3    // stopped    // BU DA DURDURULAN VEYA KAPATILAN PROCESSLER İÇİN

struct job_t {
    pid_t pid;              // bu process id yi tutcak
    //int jid;              /* bundan emin değilim
    //                       * temel mantığı location tutmak
    //                       */
    int state;              /* Bunlar processlerin yukarıda tanımlağım state durumları
                             * 4 DURUM VAR TOTALDE ONLARDA YUKARDA */
    int is_active;          // for deletion
    char *cmdline;  /* command line */
    struct job_t *next;
};
struct job_t *main_j = NULL; /* The job list */

// process linked list yapısı için search methodu
struct job_t *search_j(pid_t pid){

    struct job_t *temp = main_j;

    while(temp){

        if((temp->pid == pid) && temp->is_active)
            return temp;

        temp = temp->next;
    }

    return NULL;
}

// process LL yapısı için insert
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

// process LL yapısı için delete
void delete_j(pid_t pid){

    struct job_t *temp = search_j(pid);

    if(temp){
        temp->is_active = 0;
        return;
    }
    else
        return;
}

void print_j(){

    struct job_t *structjob = main_j;

    while (structjob->state == BG){
        /*fprintf(stdout, "PID : %ld ; CMDLINE : %s STATE : %d ; ISACTIVE : %d ; \n", (long) structjob->pid,
                structjob->cmdline, structjob->state, structjob->is_active);*/
        structjob = structjob->next;
    }

}

void create_new_args(char *string, int counter){
    char **args = malloc(counter * sizeof(char *));
    const char *del = " ";
    char *temp = strdup(string);
    int i = 0;
    while (temp){
        args[i] = temp;
        i++;
        if (i > counter)
            break;

        temp = strtok(NULL, del);
    }
    // fork yapıp çalıştıracak splitterda..
    pid_t pid;
    pid = fork();

    if (pid == -1) {
        perror("Failed to fork");
        return;
    }
    if (pid == 0){
        splitter(args);
        exit(0);
    }
    else if (pid < 0){
        fprintf(stderr, "A signal must have interrupted the wait!\n");
    }
    else{
        waitpid(pid, 0, WUNTRACED);
    }

}

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


int is_there_any_BG_process(){

    struct job_t *temp = main_j;

    while (temp){

        if(temp->pid > 0){
            if(temp->state == BG){
                fprintf(stdout, "DEGER : %ld\n", (long) temp->pid);
                fprintf(stderr, "There is a background process. You can't exit now..\n");
                return 1;
            }
        }
        temp = temp->next;
    }

    return 0;
}

// bu signal handler
// burada eksiklik var tamamlayınca anlatcam..
void handle_signal(int signal) {
    pid_t  process = getpid();

    // Find out which signal we're handling
    switch (signal) {
        case SIGTSTP:
            //kill(process, SIGTSTP);
            //fprintf(stdout, "%ld\n", (long) getsid(getpid()));
            //kill(tcgetpgrp((int)getpid()), signal);
            //pid_t foregroundpgid=tcgetpgrp(0);
            //printf("%ld\n", (long) foregroundpgid);
            //printf("Caught SIGTSTP now stop.%ld\n", (long)getpid());
            break;
        case SIGINT:
            printf("Caught SIGINT, exiting now\n");
            exit(0);
        default:
            fprintf(stderr, "Caught wrong signal: %d\n", signal);
            return;
    }
}

/* burada da eğer kullanıcı inputunda & gönderiyorsa
onu execl methoduna göndermeden önce NULL yapmak için
yoksa arg olarak & da yolluyor */
void setAmpersandToNull(char *args[], int counter){
    for (int i = 0; i < counter; i++) {

        if (strcmp(args[i], "&") == 0){
            args[i] = NULL;
            return;
        }
    }
}


/*void handle_signal(int signal, siginfo_t *siginfo, void* uncontext_txt) {

    // Find out which signal we're handling
    switch (signal) {
        case SIGTSTP:
            //fprintf(stdout, "%ld\n",);;
            fprintf(stdout, "%ld\n", (long)getpid());
            //kill(tcgetpgrp((int)getpid()), signal);
            //pid_t foregroundpgid=tcgetpgrp(0);
            //printf("%ld\n", (long) foregroundpgid);
            //printf("Caught SIGTSTP now stop.%ld\n", (long)getpid());
            break;
        case SIGINT:
            printf("Caught SIGINT, exiting now\n");
            exit(0);
        default:
            fprintf(stderr, "Caught wrong signal: %d\n", signal);
            return;
    }
}*/

/*
void scriptToArgs(char *args[], char *string, int counter){
    char *path = string;
    char del = "\0";

    int i;
    for (i = 0; i < counter; ++i)
        args[i] = "";

    int count = 1;
    for (i = 0;  i<strlen(path) ; i++) {
        if(path[i] == del)
            count++;
    }

    for(i = 0; i < count; i++){
    }

}*/


char* concat(int count, ...)
{
    va_list ap;
    int i;

    // Find required length to store merged string
    int len = 1; // room for NULL
    va_start(ap, count);
    for(i=0 ; i<count ; i++)
        len += strlen(va_arg(ap, char*));
    va_end(ap);

    // Allocate memory to concat strings
    char *merged = calloc(sizeof(char),len);
    int null_pos = 0;

    // Actually concatenate strings
    va_start(ap, count);
    for(i=0 ; i<count ; i++)
    {
        char *s = va_arg(ap, char*);
        strcpy(merged+null_pos, s);
        null_pos += strlen(s);
    }
    va_end(ap);

    return merged;
}


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

// bu burada dursun eğer lazım olursa buradan devam edelim
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
                                /*
                                char *buf = "";
                                for (int m = start; m <= last; m++) {
                                    fprintf(stdout, "string :%s\n",args[m]);
                                    //fprintf(stdout, "EX: %s\n", buf);
                                }*/

                                // BURADADAN DEVAM ET
                                // ARGS DAN TEK SATIRDA OKUMA...


                                return NULL;
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

// bu fonksiyon FG yzıldığında o anda çalışan foreground processin pid
// bulmak için..
pid_t found_pid(struct job_t *jobs) {

    while (jobs){

        if(jobs->state == FG)
            return jobs->pid;

        jobs = jobs->next;
    }

    return 0;
}
// BUDA SIGNAL HANDLER GELINCE KAFASINI KOPARIYOZ..
void sigtstp_handler(int sig)
{
    int pid = found_pid(main_j);

    if (pid != 0) {

        printf("Job (%d) Stopped by signal %d Ctrl+Z\n", pid, sig);
        kill(pid, SIGKILL);
    }
    return;
}


int main(void) {
    char inputBuffer[MAX_LINE]; /*buffer to hold command entered */
    int background; /* equals 1 if a command is followed by '&' */
    char *args[MAX_LINE / 2 + 1]; /*command line arguments */
    int redirect;
    // bu counter arg sayısını sayıyor
    // setup fonksiyonuna gönderiyom
    int counter;

    insert_j(getpid(), "myshell", UNDEF);

    while (1) {
        background = 0;
        redirect = 0;
        // bu yazı için
        fprintf(stdout, "myshell :");
        // bu da düzgün gözüksün die
        fflush(stdout);

        /*setup() calls exit() when Control-D is entered */
        setup(inputBuffer, args, &background, &counter, &redirect);

        if (counter == 0)
            continue;

        /** the steps are:
        (1) fork a child process using fork()
        (2) the child process will invoke execv()
		(3) if background == 0, the parent will wait,
        otherwise it will invoke the setup() function again. */

        setAmpersandToNull(args, counter);
        // clr komutu için
        if (strncmp(args[0], "clr", 127) == 0){
            system("clear");
        }
            // alias için burasını da tamamlıycam.
        else if(strncmp(args[0], "alias", 127) == 0){
            if(args[1] != NULL){
                if(strncmp(args[1], "-l", 127) == 0){
                    print();
                }
                else{

                    // name buluyor sonra null die check et.
                    char *name = find_name(args, counter);
                    char *command = find_command(args, counter);
                    const char *asd = name;
                    const char *asd2 = command;
                    if(asd && asd2){
                        char *cmd = strdup(asd);
                        char *bash = strdup(asd2);
                        insert(cmd, bash);
                    }
                    else{
                        // hata verdirebilirsin..
                        fprintf(stderr, "Command not found : wrong alias statement..\n");
                    }

                }
            }
            else{
                fprintf(stderr, "Command not found : too few arguments..\n");
            }
        }
        else if((strncmp(args[0], "unalias", 127) == 0) && (args[1] != NULL)){
            delete_node(args[1]);
        }
            // kullanıcının komunutu run et..
        else if (strncmp(args[0], "exit", 127) == 0){
            // burada background processleri kontrol et. yoksa bitir.
            if(!is_there_any_BG_process())
                exit(0);
        }
        else if(find(args[0])){
            /* BURADA USER ALIAS TANIMLAMIŞ VE ONUN AKTİF OLDUĞUNU GÖSTERMEKTEDİR
             * FORK YAPIP CHILD I SPLITTER A YOLLUYACAK KOMANSE COK BARIZ
             * */
            struct alias *run = find(args[0]);

            //scriptToArgs(args, run->script, counter);

            /*for (int i = 0; i < counter ; ++i) {
                args[i] = run->script;
                fprintf(stdout, "0%s0\n", args[i]);
            }*/

            pid_t pid;

            pid = fork();

            if (pid == -1) {
                perror("Failed to fork");
                return 1;
            }
            if (pid == 0){
                //fprintf(stdout, "0%s0\n", run->script);
                splitter(args);
                exit(0);
            }
            else if (pid < 0){
                fprintf(stderr, "A signal must have interrupted the wait!\n");
            }
            else {
                waitpid(pid, 0, WUNTRACED);
            }


            //fprintf(stdout, "%s\n", run->script);
            //system(run->script);
            //fprintf(stdout, "%s\n", run->script);

        }
        else if ((strncmp(args[0], "fg", 127) == 0) && (!args[1])){
            /* ne yapıyordu?

            şimdi bir fonksiyona göndercek
            orada bütün processleri iterate etcek
            pid -1 değilse kill fonksiyonu ile stop olacak
            sonra aynı fonksiyon içinde
            kill fonksiyonu ile fonksiyonları çalıştırcak
            waitpid */


            struct job_t *temp = main_j;
            while (temp){

                if(!temp->is_active){
                    temp = temp->next;
                    continue;
                }

                pid_t pid = fork();

                if (pid == -1) {
                    perror("Failed to fork");
                    return 1;
                }
                if(pid == 0){
                    args[1] = "1";
                    splitter(args);
                    exit(0);
                    /*char buf[50];
                    args[1] += snprintf(args[1], "%ld", temp->pid);
                    splitter(args);*/
                }
                else if (pid < 0){
                    fprintf(stderr, "A signal must have interrupted the wait!\n");
                }
                else{
                    temp->pid = -1;
                    temp->is_active = 0;
                    temp->state = FG;
                    waitpid(pid, 0, WUNTRACED);
                }


                temp = temp->next;
            }
            // if BG THEN TO FG

        }
        else{
            // bütün işleri childpid yapacak..
            // parent dediğimiz main process sadece foreground ve background da değişecek
            pid_t childpid;
            /* set up signal handlers here ... */
            childpid = fork();

            if (childpid == -1) {
                perror("Failed to fork");
                return 1;
            }
            if (childpid == 0){

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

                splitter(args);
                exit(0);
            }
            else if (childpid < 0){
                fprintf(stderr, "A signal must have interrupted the wait!\n");
            }
            else {
                /* burası main process...
                bu if koşulundan emin değilim ama yinede kalsın..
                temel olarak main processin pid sine bakıp
                ona eşit bir process grubu oluşturuyor..
                googledan process group yazarsan bulursun..
                if(setpgid(getpid(), getpid()) == 0)
                    perror("setpid");

                bu signal.h kütüphanesinde tanımlı struct yapısı..
                sinyal için kullanıyoz. */
                struct sigaction sa;
                if (background) {

                    /*SIGTSTP -> ctrl+z
                    SIG_IGN -> signal ignore demek.. yani ctrl+z yi görmezden gel dioz.. */
                    setpgid(childpid,childpid);
                    signal(SIGTSTP, SIG_IGN);
                    const char *buf = args[0];
                    char *command = strdup(buf);
                    insert_j(childpid, command, BG);
                }
                else{
                    // bunlar foreground processteki ctrl+z sinyali handle etmek için
                    // BURAYA YAZCAN SIGNALİ daha tamamlanmadı burası..
                    signal(SIGTSTP, sigtstp_handler);


                    // foreground işleminin bitmesi için bekle...
                    const char *buf = args[0];
                    char *command = strdup(buf);
                    insert_j(childpid, command, FG);
                    waitpid(childpid, 0, WUNTRACED);
                }
                fprintf(stdout, "PARENT ID: %ld with child id: %ld \n", (long) getpid(), childpid);
                struct job_t *temp = search_j(childpid);
                temp->state = ST;
                print_j();
            }
        }
    }
}

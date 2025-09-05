// Algoritmos e Estruturas de Dados II
// Trabalho Prático 2 - Questão 2 - IS
// Marina Campidelli 

#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <stdbool.h>


bool somenteVogais(char *str) {
    for (int i = 0; str[i] != '\0'; i++) {
        char c = tolower(str[i]);
        if (c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u') {
            return false;
        }
    }
    return true;
}


bool somenteConsoantes(char *str) {

    for (int i = 0; str[i] != '\0'; i++) {
        char c = tolower(str[i]);
        
        if (!isalpha(str[i]) || c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
            return false;
        }
    }
    return true;
}


bool ehInteiro(char *str) {
    if (str[0] == '\0') return false; 
    
    
    int start = 0;
    if (str[0] == '+' || str[0] == '-') {
        start = 1;
        if (str[1] == '\0') return false; 
    }
    
    for (int i = start; str[i] != '\0'; i++) {
        if (!isdigit(str[i])) {
            return false;
        }
    }
    return true;
}


bool ehReal(char *str) {
    if (str[0] == '\0') return false; 
    
    int start = 0;
    int pontoCount = 0;
    
    
    if (str[0] == '+' || str[0] == '-') {
        start = 1;
        if (str[1] == '\0') return false; 
    }
    
    for (int i = start; str[i] != '\0'; i++) {
        if (str[i] == '.' || str[i] == ',') {
            pontoCount++;
            
            if (pontoCount > 1 || i == start || str[i + 1] == '\0') {
                return false;
            }
        } else if (!isdigit(str[i])) {
            return false;
        }
    }
    return true;
}

int main() {
    char entrada[1000];
    
    while (fgets(entrada, sizeof(entrada), stdin) != NULL) {
        
        entrada[strcspn(entrada, "\n")] = '\0';
        
        bool x1 = somenteVogais(entrada);
        bool x2 = somenteConsoantes(entrada);
        bool x3 = ehInteiro(entrada);
        bool x4 = ehReal(entrada);
        
        
        printf("%s %s %s %s\n",
               x1 ? "SIM" : "NAO",
               x2 ? "SIM" : "NAO",
               x3 ? "SIM" : "NAO",
               x4 ? "SIM" : "NAO");
    }
    
    return 0;
}
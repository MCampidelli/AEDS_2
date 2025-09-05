#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

bool PalindromoRec(char str[], int i, int j) {
    
    while (i < j && !isalpha((unsigned char)str[i])) {
        i++;
    }
    
    while (i < j && !isalpha((unsigned char)str[j])) {
        j--;
    }

    
    if (i >= j) return true;

    
    if (tolower((unsigned char)str[i]) != tolower((unsigned char)str[j])) {
        return false;
    }

    
    return PalindromoRec(str, i+1, j-1);
}

int main() {
    char linha[1000];

    while (fgets(linha, sizeof(linha), stdin) != NULL) {
        
        linha[strcspn(linha, "\n")] = '\0';

        if (PalindromoRec(linha, 0, strlen(linha)-1))
            printf("SIM\n");
        else
            printf("NAO\n");
    }

    return 0;
}

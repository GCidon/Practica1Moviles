// ConsoleApplication1.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <conio.h>
#include <stdio.h>
#include <algorithm>

enum type { vacio, azul, rojo, rojofijo, azulfijo };
struct Casilla {
    int n;
    type state;
};

struct Vector2D {
    int x;
    int y;
};

using namespace std;
vector<vector<Casilla>> lee(int& tamOcup, int& tamTablero);
void fillingConstruct(vector<vector<Casilla>>& tablero, int& tamOcup);
void clearConstruct(vector<vector<Casilla>>& tablero);
vector<vector<Casilla>> generaAleatoria(int& tamOcup, int& tamTablero);
int alrededor(const vector<vector<Casilla>>& tablero, int x, int y);
void fillingWin(vector<vector<Casilla>>& tablero);
bool checkWin(const vector<vector<Casilla>>& tablero);
void pintaTabla(const vector<vector<Casilla>>& tablero, Vector2D& pos);


vector<vector<Casilla>> lee(int& tamOcup, int& tamTablero)
{
    cout << "Hello World!\n";
    vector<vector<Casilla>> tablero;
    ifstream file;

    file.open("level.txt");

    int n;
    file >> n;
    tamTablero = n;
    string aux;
    int num;
    bool st;

    tablero.resize(n);

    for (int i = 0; i < n; i++) {
        tablero[i].resize(n);
        for (int j = 0; j < n; j++) {
            file >> aux;
            if (aux[0] == 'v')
            {
                tablero[i][j] = { -1, type::vacio };
            }
            else if (aux[0] == 'a')
            {
                tablero[i][j] = { int(aux[1]) - (int)'0', azulfijo };
                tamOcup++;
            }
            else if (aux[0] == 'r')
            {
                tablero[i][j] = { -3, rojofijo };
                tamOcup++;
            }

        }
    }

    return tablero;
}
void fillingConstruct(vector<vector<Casilla>>& tablero , int &tamOcup)
{
    int n;
    vector<Vector2D>azulesf;
    for (int i = 0; i < tablero.size(); i++)
    {
        for (int j = 0; j < tablero.size(); j++)
        {
            Casilla* actual = &tablero[i][j];
            if (actual->state == azul)
            {
                 n = alrededor(tablero, i, j);
                 if (n == 0)
                 {
                     actual->state = rojo;
                 }
                 else
                 {
                     actual->n = n;
                     int r = rand() % 3;
                     if (n >= tablero.size())
                         r++;
                     if (r  >1)
                     {
                         actual->state = azulfijo;
                         tamOcup++;
                     }
                 }


            }
            else if (actual->state == rojo)
            {
                if (rand() % 2 == 1)
                {
                    actual->state = rojofijo;
                    actual->n = -3;
                    tamOcup++;
                }
            }

        }
    }
}

void clearConstruct(vector<vector<Casilla>>& tablero)
{
    int n = tablero.size();
    for (int i = 0; i < n; i++) {
        tablero[i].resize(n);
        for (int j = 0; j < n; j++) {
            if (tablero[i][j].state != rojofijo && tablero[i][j].state != azulfijo)
            {
                tablero[i][j] = { -1,vacio };
            }

        }
    }
}

vector<vector<Casilla>> generaAleatoria(int& tamOcup, int& tamTablero)
{
    cout << "GeneracionAleatoria\n";
    vector<vector<Casilla>> tablero;
    vector<Vector2D> azules;
    vector<Vector2D> rojos;
    int n = tamTablero;
    int numBlue = 0, numRed = 0;
    int minBlue = (n * n) / 3;
    int maxBlue = (n * n) *(2/3) ;
    int serAzul = 0;
    tablero.resize(n);

    for (int i = 0; i < n; i++) {
        tablero[i].resize(n);
        for (int j = 0; j < n; j++) {
            serAzul = rand() % 3;
            if (serAzul >0)
            {
                numBlue++;
                tablero[i][j] = { -1,azul };
                azules.push_back({ i,j });
            }
            else
            {
                numRed++;
                tablero[i][j] = { -1,rojo };
                rojos.push_back({ i,j });
            }

        }
    }

    if (numBlue < minBlue)
    {
        int aC = minBlue - numBlue + 1;
        std::random_shuffle(rojos.begin(), rojos.end());
        int x, y;
        for (int i = 0; i < aC; i++)
        {
            x = rojos[i].x;
            y = rojos[i].y;

            tablero[x][y].state = rojo;
        }

    }
    else if (numBlue > maxBlue)
    {
        int aC = maxBlue - numBlue - 1;
        std::random_shuffle(azules.begin(), azules.end());
        int x, y;
        for (int i = 0; i < aC; i++)
        {
            x = azules[i].x;
            y = azules[i].y;

            tablero[x][y].state = azul;
        }
    }

    fillingConstruct(tablero , tamOcup);
    clearConstruct(tablero);

    return tablero;
}

int alrededor(const vector<vector<Casilla>>& tablero, int x, int y)
{
    int al = 0;
    int iz = x - 1;
    int dr = x + 1;
    int ari = y - 1;
    int ab = y + 1;

    bool continuarIz = true;
    bool continuarDr = true;
    bool continuarAri = true;
    bool continuarAb = true;
    while (iz >= 0 && continuarIz)
    {
        switch (tablero[iz][y].state)
        {
        case azul:
        case azulfijo:
            al++;
            iz--;
            break;
        case vacio:
        case rojo:
        case rojofijo:
            continuarIz = false;
            break;

        }
    }
    while (dr < tablero.size() && continuarDr)
    {
        switch (tablero[dr][y].state)
        {
        case azul:
        case azulfijo:
            al++;
            dr++;
            break;
        case vacio:
        case rojo:
        case rojofijo:
            continuarDr = false;
            break;

        }
    }
    while (ari >= 0 && continuarAri)
    {
        switch (tablero[x][ari].state)
        {
        case azul:
        case azulfijo:
            al++;
            ari--;
            break;
        case vacio:
        case rojo:
        case rojofijo:
            continuarAri = false;
            break;

        }
    }
    while (ab < tablero.size() && continuarAb)
    {
        switch (tablero[x][ab].state)
        {
        case azul:
        case azulfijo:
            al++;
            ab++;
            break;
        case vacio:
        case rojo:
        case rojofijo:
            continuarAb = false;
            break;

        }
    }

    return al;
}

void fillingWin(vector<vector<Casilla>>& tablero)
{
    for (int i = 0; i < tablero.size(); i++)
    {
        for (int j = 0; j < tablero.size(); j++)
        {
            Casilla* actual = &tablero[i][j];
            if (actual->state == azul)
            {
                actual->n = alrededor(tablero, i, j);
                actual->state = azulfijo;

            }
            else if (actual->state == vacio || actual->state == rojo)
            {
                actual->state = rojofijo;
            }

        }
    }
}

bool checkWin(const vector<vector<Casilla>>& tablero)
{
    int n = 0, cal = 0;
    for (int i = 0; i < tablero.size(); i++)
    {
        for (int j = 0; j < tablero.size(); j++)
        {
            Casilla actual = tablero[i][j];
            n = actual.n;
            if (n > 0)
            {
                cal = alrededor(tablero, i, j);
                if (cal != n)
                    return false;

            }

        }
    }
    cout << "Has ganado\n";
    return true;
}

void pintaTabla(const vector<vector<Casilla>>& tablero, Vector2D& pos)
{
    std::cout << "\x1B[2J\x1B[H";

    for (int i = 0; i < tablero.size(); i++) {
        for (int j = 0; j < tablero.size(); j++) {
            if (tablero[i][j].state == vacio)
            {
                printf("\x1B[37m\x1B[48;2;150;150;150m   ");
            }
            else if (tablero[i][j].state == rojo || tablero[i][j].state == rojofijo)
            {
                printf("\x1B[37m\x1B[41m   ");
            }
            else if (tablero[i][j].state == azul)
            {
                printf("\x1B[37m\x1B[46m   ");
            }
            else if (tablero[i][j].state == azulfijo)
            {

                printf("\x1B[37m\x1B[46m %d ", tablero[i][j].n);
            }

            printf("\x1B[37m\x1B[40m");
            if (pos.x == i && pos.y == j)
                cout << "*  ";
            else
                cout << "   ";
        }

        cout << "\n\n";
    }
}


int main()
{
    int tamOcup = 0;
    int tamTotal = 5;
    //vector<vector<Casilla>> tablero = lee(tamOcup, tamTotal);
    vector<vector<Casilla>> tablero = generaAleatoria(tamOcup, tamTotal);
    int tabTotal = tamTotal * tamTotal;
    bool ganado = false;
    bool movido = false;
    Vector2D pos{ 0, 0 };
    int c;

    pintaTabla(tablero, pos);
    while (!ganado)
    {


        if (_kbhit())
        {
            c = _getch();

            if (c == 'w')
            {
                movido = true;
                if (pos.x > 0)
                    pos.x--;
            }
            else if (c == 'a')
            {
                movido = true;
                if (pos.y > 0)
                    pos.y--;
            }
            else if (c == 'd')
            {
                movido = true;
                if (pos.y < tamTotal - 1)
                    pos.y++;
            }
            else if (c == 's')
            {
                movido = true;
                if (pos.x < tamTotal - 1)
                    pos.x++;
            }
            else if (c == 'l')
            {
                movido = true;
                if (tablero[pos.x][pos.y].state == vacio)
                {
                    tablero[pos.x][pos.y].state = azul;
                    tamOcup++;
                }
                else if (tablero[pos.x][pos.y].state == azul)
                {
                    tablero[pos.x][pos.y].state = rojo;
                }
                else if (tablero[pos.x][pos.y].state == rojo)
                {
                    tablero[pos.x][pos.y].state = vacio;
                    tamOcup--;
                }
            }
            if (movido)
            {

                pintaTabla(tablero, pos);
                cout << "Casillas ocupados: " << tamOcup << "/" << tabTotal << endl;
                movido = false;
            }
        }

        ganado = checkWin(tablero);
    }
    char ver = ' ';
    while (ver == ' ')
    {
        cin >> ver;
        fillingWin(tablero);
        pintaTabla(tablero, pos);
    }

    system("pause");


}
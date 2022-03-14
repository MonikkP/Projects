#pragma once
#include "Activitate.h"
#include<vector>
#include "Repository.h"
#include"Observer.h"
using namespace std;
class CosException {
private:
	string msg;
public:
	CosException(string msg) :msg{ msg } {}
	string get_mesaj() {
		return msg;
	}
};
class Cos:public Observable
{    
private:
	RepoAbstract& repo;
	vector<Activitate> activitati_de_facut;
public:
	Cos(RepoAbstract& repo) :repo{ repo } {}
	//sterge elementele din lista -activitati de facut-
	void goleste_activitati_de_facut();
	//adauga activitati de tip Activitate in lista
	//date de intrare: string titlu
	//date de iesire:returneaza un int ( cate activitati sunt in lista)
	int adauga_activitati_de_facut(const string& titlu);
	//date de intrare: size_t numar
	//genereaza un numar introudus de la tastaura de activitati luate din lista din repo
	void genereaza_random_activitati_de_facut(const size_t numar);
	//returneaza un vector de activitati ce contine toate activitatile din lista
	vector<Activitate> get_all_activitati_de_facut();
	void salveaza_in_fisier(const string FileName);


};


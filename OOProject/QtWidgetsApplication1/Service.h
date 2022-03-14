#pragma once
#include <functional>
#include "Repository.h"
#include "RepoFile.h"
#include "RepoLab.h"
//#include"CelMaiBunVectorDinamic.h"
#include <vector>
#include<map>
#include "undo.h"

using std::function;
class ValidationException {
private:
	string msg;
public:
	ValidationException(string msg) :msg{ msg } {}
	string get_mesaj() {
		return msg;
	}
};

class DTO_tip {
private:
	string tip;
	int numar;
public:
	DTO_tip() :tip{ " " }, numar{ 0 }{}
	DTO_tip(string tip) :tip{ tip }, numar{ 1 }{}
	void increment() noexcept{
		numar++;
	}
	string get_tip() noexcept {
		return tip;
	}
	int get_count() noexcept{
		return numar;
	}

};
class Service
{ private:
	RepoAbstract& repo;
	//vector<Activitate> filtrareSRV(function<bool(const Activitate&)> fct);
	vector<unique_ptr<UndoAction>>listUndo;
public:

	//constructor Service care primeste referinta la repository
	Service(RepoAbstract& repo) :repo{ repo }{}
	//adauga un element in lista
	//date de intrare:titlu->string,descriere->string,tip->string,durata->string
	void adaugaSRV( const string& titlu, const string& descriere, const string& tip, int durata);
	//cautare activitate dupa titlu->string
	//returneaza o Activitate
	Activitate cautaSRV(const string& titlu) const;
	//returneaza o lista de Activitati cu toate activitatile
	const vector<Activitate> getAllSRV();
	//sterge o activitate dupa titlu
	//date de intrare titlu-string
	void stergeSRV(const string& titlu);
	//update activitate
	//acrualizeaza pretul unei activitati
	//date de intrare- string titlu,string descriere
	void updateSRV(const string& titlu, const string& descriere);
	//filtarare lista dupa tip 
	//date de intrare: tip->string
	//date de iesire->o lisa de Activitati 
	vector<Activitate>filtrareTIP(const string& tip);
	vector<Activitate>filtrareDescriere(const string& descriere);
	//functie de sortare
	//date de intrare: pointer la o functie de tip bool care ne ofera crieteriul de comparare
	//date de iesire: o copie la lista sortata in functie de criteriul ales
	vector<Activitate> sortSRV(bool(*Function)(const Activitate& a1, const Activitate&a2))const;
	map<string, DTO_tip>raport();
	void undo();

};


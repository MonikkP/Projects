#pragma once
#include"Activitate.h"
//#include "CelMaiBunVectorDinamic.h"
#include <vector>
#include "RepoLab.h"
using namespace std;
class RepoException {
	string msg;
public:
	RepoException(string m) :msg{ m } {

	}
	string getMesaj() {
		return msg;
	}
}; 
class Repository: public RepoAbstract
{private:
	vector<Activitate> ListaActivitati;
	
public:
	Repository() = default;
	
	virtual ~Repository() = default;
	//adaugare in lista de activitati a unui obiect de tip Activitate
	//date de intrare->Activitate a
	 virtual void add(const Activitate& a);
	//cautare activitate dupa titlu->string
	//returneaza o Activitate
	virtual Activitate cauta(string titlu);
	//sterge o activitate dupa titlu
	//date de intrare titlu-string
	virtual void sterge(string titlu);
	//update activitate
	//acrualizeaza pretul unei activitati
	//date de intrare- obiect de tipul Activitate
	virtual void update(Activitate& a);
	//returneaza o lista de Activitati cu toate activitatile
	virtual vector<Activitate> getAll();

};


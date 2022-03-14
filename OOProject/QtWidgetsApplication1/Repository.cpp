#include "Repository.h"
//#include"CelMaiBunVectorDinamic.h"
#include<exception>
using namespace std;


void Repository::add(const Activitate& act) {
	for (auto& a : ListaActivitati) {
		if (a==act)

			throw RepoException("Element existent!\n");

	}
	
		
	ListaActivitati.push_back(act);

}
Activitate Repository::cauta(string titlu)   {
	/*for (auto& act : ListaActivitati)
		if (act.get_titlu() == titlu)
			return act;
		
		throw RepoException("Activitate inexistenta!\n");*/

	auto rez = find_if(ListaActivitati.begin(), ListaActivitati.end(), [&titlu](const Activitate& act) {
		return act.get_titlu() == titlu;
		});
	if (rez != ListaActivitati.end()) {
		Activitate a= *rez;
		return a;
	}
	else {
		throw RepoException("Activitate inexistenta!\n");

	}

	
}
 vector<Activitate> Repository::getAll()  {
	return ListaActivitati;
}
void Repository::sterge(string titlu) {
	for (auto& act : ListaActivitati) {
		if (act.get_titlu() == titlu) {
			ListaActivitati.erase(std::remove(ListaActivitati.begin(), ListaActivitati.end(), act), ListaActivitati.end());
			return;
		}
	}
		
}

void Repository::update(Activitate& a) {
	for (auto& act : ListaActivitati) {
		if (act == a) {
			act.set_descriere(a.get_descriere());
			return;
		}
	}
	

}
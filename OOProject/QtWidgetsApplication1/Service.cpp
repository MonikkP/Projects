#include "Service.h"
#include<algorithm>
#include <vector>
#include<exception>
#include <functional>
#include<map>




void Service::adaugaSRV(const string& titlu, const string& descriere, const string& tip, int durata) {
	Activitate a{ titlu,descriere,tip,durata };
	//validare
	if (titlu == "" || descriere == "" || tip == "")
		throw ValidationException("Date invalide!\n");
	if (durata <= 0) {
		throw ValidationException("Durata invalida!\n");
	}
	repo.add(a);
	listUndo.push_back(make_unique<AdaugaUndo>(repo, a));

}
 Activitate Service::cautaSRV(const string& titlu) const {
	return repo.cauta(titlu);

}
const vector<Activitate> Service::getAllSRV() {
	return repo.getAll();
	

}

void Service::stergeSRV(const string& titlu) {
	Activitate a = repo.cauta(titlu);

	listUndo.push_back(make_unique<StergeUndo>(repo, a));

	repo.sterge(titlu);

}
void Service::updateSRV(const string& titlu, const string& descriere) {
	
	string null = "";
	int nullint = -1;
	Activitate a{ titlu,descriere,null,nullint };
	Activitate b = cautaSRV(titlu);
	listUndo.push_back(make_unique<ModificaUndo>(repo, b));
	repo.update(a);
	
}

/*MyVector<Activitate> Service::filtrareSRV(const string& tip, const string& descr) const {
	MyVector<Activitate> rez;

	for (auto& act : repo.getAll()) {
		if (act.get_tip() == tip && act.get_descriere() == descr) {
			rez.push_back(act);
		}


	}
}*/
/*vector<Activitate> Service::filtrareSRV(function<bool(const Activitate&)> fct)  {
	vector<Activitate> rez;

	for (auto& act : repo.getAll()) {
		if (fct(act)) {
			rez.push_back(act);
		}


	}
	return rez;
}*/
/*vector<Activitate> Service::filtrareTIP(const string& tip) {
	return filtrareSRV([tip](const Activitate& act) {
		return act.get_tip() == tip;
		});
}*/

vector<Activitate> Service::filtrareTIP(const string& tip) {
	vector<Activitate> dest;
	auto activitati = repo.getAll();
	copy_if(activitati.begin(), activitati.end(), back_inserter(dest), [tip](const Activitate& act) {
		return act.get_tip() == tip;});

	return dest;
}


/*vector<Activitate> Service::filtrareDescriere(const string& descriere) {
	return filtrareSRV([descriere](const Activitate& act) {
		return act.get_descriere() == descriere;
		});
}*/

vector<Activitate> Service::filtrareDescriere(const string& descriere) {
	vector<Activitate> dest;
	auto activitati = repo.getAll();
	copy_if(activitati.begin(), activitati.end(), back_inserter(dest), [descriere](const Activitate& act) {
		return act.get_descriere() == descriere; });
	return dest;

}


vector<Activitate> Service::sortSRV(bool(*Function)(const Activitate& a1, const Activitate& a2))const{
	vector<Activitate> copy{ repo.getAll() };

	sort(copy.begin(), copy.end(), Function);

	
	return copy;
}

map<string, DTO_tip>Service::raport() {
	map<string, DTO_tip> rap;
	auto activitati = repo.getAll();
	for (auto& act : activitati) {
		if (rap.find(act.get_tip()) != rap.end())
			rap[act.get_tip()].increment();
		else
			rap[act.get_tip()] = DTO_tip(act.get_tip());
	}
	return rap;
}

void Service::undo() {
	if (listUndo.size() == 0)
		throw ValidationException("Nu mai pueti face undo!\n");
	listUndo.back()->doUndo();;
	listUndo.pop_back();
}
#include "RepoLab.h"

void RepoLab::random_float() {
	float random = ((float)rand()) / (float)RAND_MAX;
	if (random < this->get_prob())
		throw RepoLabException("Probability Exception!\n");
}
bool RepoLab::existacheie(string titlu) const {
	return ListaActivitatimap.find(titlu) != ListaActivitatimap.end();
}
void RepoLab::add(const Activitate& act) {
	random_float();
	if (existacheie(act.get_titlu()))
		throw RepoLabException("Activitate exsitenta!\n");
	ListaActivitatimap.insert(make_pair(act.get_titlu(), act));
}

void RepoLab::sterge(string titlu) {
	random_float();
	if (existacheie(titlu)) {
		ListaActivitatimap.erase(titlu);
		return;
	}
	throw RepoLabException("Activitate inexistenta!\n");
}

void RepoLab::update(Activitate& a) {
	random_float();
	for (auto& pair : ListaActivitatimap) {
		if (pair.second == a) {
			pair.second.set_descriere(a.get_descriere());
			return;
		}
	}

     throw RepoLabException("Activitate inexistenta!\n");

	
}

Activitate RepoLab::cauta(string titlu) {
	for (auto& pair : ListaActivitatimap) {
		if (existacheie(titlu))
			return pair.second;
	}
	
	throw RepoLabException("Activitate inexistenta!\n");
}
vector<Activitate> RepoLab::getAll() {
	vector<Activitate>activitati;
	for (auto& a : ListaActivitatimap) {
		activitati.push_back(a.second);
	}
	return activitati;

}





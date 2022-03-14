#pragma once
#include "RepoFile.h"
#include <fstream>

void RepoFile::load_from_file() {
	ifstream fin(fName);
	if (!fin.is_open()) {
		throw RepoException("Unable to open file!\n");
	}
	string titlu;
	while (fin>>titlu) {

		string descr;
		fin >> descr;
		string tip;
		fin >> tip;
		int durata;
		fin >> durata;
		
		Activitate act{ titlu.c_str(), descr.c_str(), tip.c_str(), durata};
		Repository::add(act);
	}
	fin.close();
}

void RepoFile::write_to_file() {
	ofstream fout(fName);
	if (!fout.is_open()) {
		throw RepoException("Unable to open file!\n");

	}
	for (auto& act : getAll()) {
		fout << act.get_titlu()<<" ";
		fout << act.get_descriere() << " ";
		fout << act.get_tip() << " ";
		fout << act.get_durata() << endl;

	}
	fout.close();
}


#pragma once
#include "Activitate.h"
#include "Repository.h"
#include "Service.h"
#include "CompareFunctions.h"
#include<assert.h>
#include"CelMaiBunVectorDinamic.h"
#include <vector>
#include"Cos.h"
#include <fstream>


void test_domain() {
	const string titlu = "drumetie";
	
	const string descriere = "as";
	string tip = "sas";
	const int durata = 4;
	Activitate a1{titlu,descriere,tip,durata};
	assert(a1.get_descriere() == "as");
	assert(a1.get_durata() == 4);
	assert(a1.get_tip()== "sas");
	assert(a1.get_titlu() == "drumetie");
	a1.set_descriere("bbb");
	assert(a1.get_descriere() == "bbb");

}
void testAddRepo() {
	Repository repo;
	const string titlu = "drumetie";
	
	const string descriere = "as";
	string tip = "sas";
	const int durata = 4;
	Activitate a1{ titlu,descriere,tip,durata };
	;
	repo.add(a1);
	assert((repo.getAll()).size() == 1);
	try {
		repo.add(a1);
		assert(false);
	}
	catch (RepoException& e) {
		assert(e.getMesaj()=="Element existent!\n");
	}
}
void testAddSRV(){
	Repository repo;
	Service srv{ repo };
	const string titlu = "drumetie";
	const string titlu1 = "drumetiea";
	const string descriere = "as";
	string tip = "sas";
	const int durata = 4;
	const int durata1 = -3;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	try {
		srv.adaugaSRV(titlu1, descriere, tip, durata1);
		assert(false);
	}
	catch (ValidationException& e) {
		assert(e.get_mesaj() == "Durata invalida!\n");
	}

	assert((srv.getAllSRV()).size() == 1);
}
void testCautaSRV() {
	Repository repo;
	Service srv{ repo };
	const string titlu = "drumetie";
	const string descriere = "as";
	string tip = "sas";
	const int durata = 4;
	const string titlu1 = "a";
	const string descriere1 = "a";
	string tip1 = "c";
	const int durata1 = 4;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	Activitate a = srv.cautaSRV(titlu1);
	assert(a.get_tip() == "c");
	const string titlu_rau = "asasa";
	try {
		srv.cautaSRV(titlu_rau);
		assert(false);
	}
	catch (RepoException& e) {
		assert(e.getMesaj() == "Activitate inexistenta!\n");
	}

}

void teststergeSRV() {
	Repository repo;
	Service srv{ repo };
	const string titlu = "drumetie";
	const string descriere = "as";
	string tip = "sas";
	const int durata = 4;
	const string titlu1 = "a";
	const string descriere1 = "a";
	string tip1 = "c";
	const int durata1 = 4;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	assert(srv.getAllSRV().size() == 2);
	srv.stergeSRV(titlu1);
	assert(srv.getAllSRV().size() == 1);
	const string titlu_rau = "asasa";
	try {
		srv.stergeSRV(titlu_rau);
		assert(false);
	}
	catch (RepoException& e) {
		assert(e.getMesaj() == "Activitate inexistenta!\n");
	}

}
void testUpdateSRV() {
	Repository repo;
	Service srv{ repo };
	const string titlu = "drumetie";
	const string descriere = "as";
	string tip = "sas";
	const int durata = 4;
	const string titlu1 = "a";
	const string descriere1 = "a";
	string tip1 = "c";
	const int durata1 = 4;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	assert(srv.getAllSRV().size() == 2);
	const string descriere_noua = "fain";
	srv.updateSRV(titlu1, descriere_noua);
	Activitate a = srv.cautaSRV(titlu1);
	assert(a.get_descriere() == descriere_noua);
	const string titlu_rau = "asasa";
	try {
		srv.updateSRV(titlu_rau,descriere_noua);
		assert(false);
	}
	catch (RepoException& e) {
		assert(e.getMesaj() == "Activitate inexistenta!\n");
	}


}
void test_filtrare() {
	Repository repo;
	Service srv{ repo };
	const string titlu = "drumetie";
	const string descriere = "a";
	string tip = "b";
	const int durata = 4;
	const string titlu1 = "a";
	const string descriere1 = "a";
	string tip1 = "c";
	const int durata1 = 4;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	vector<Activitate> rez = srv.filtrareTIP(tip);
	assert(rez.size() == 1);
	assert(rez[0].get_titlu() == "drumetie");
	vector<Activitate> rez1 = srv.filtrareDescriere(descriere);
	assert(rez1.size() == 2);
	assert(rez1[0].get_titlu() == "drumetie");

}
void testSortSRV() {
	Repository repo;
	Service srv{ repo };
	const string titlu = "a";
	const string descriere = "b";
	string tip = "a";
	const int durata = 4;
	const string titlu1 = "b";
	const string descriere1 = "d";
	string tip1 = "c";
	const int durata1 = 4;
	const string titlu2 = "z";
	const string descriere2 = "c";
	string tip2 = "c";
	const int durata2 = 1;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	srv.adaugaSRV(titlu2, descriere2, tip2, durata2);
	vector<Activitate>rez = srv.sortSRV(ComparaTip);
	assert(rez[0].get_titlu() == "a");
	vector<Activitate>rez1 = srv.sortSRV(ComparaDescriere);
	assert(rez1[0].get_titlu() == "a");
	vector<Activitate>rez2 = srv.sortSRV(ComparaTipDurata);
	assert(rez2[0].get_titlu() == "a");
	srv.stergeSRV(titlu);
	vector<Activitate>rez3 = srv.sortSRV(ComparaTipDurata);
	assert(rez3[0].get_titlu() == "z");
}

void test_cos() {
	Repository repo;
	Service srv{ repo };
	Cos cos{ repo };
	const string titlu = "a";
	const string descriere = "b";
	string tip = "a";
	const int durata = 4;
	const string titlu1 = "b";
	const string descriere1 = "d";
	string tip1 = "c";
	const int durata1 = 4;
	const string titlu2 = "z";
	const string descriere2 = "c";
	string tip2 = "c";
	const int durata2 = 1;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	srv.adaugaSRV(titlu2, descriere2, tip2, durata2);
	assert(cos.get_all_activitati_de_facut().size() == 0);
	cos.adauga_activitati_de_facut(titlu);
	assert(cos.get_all_activitati_de_facut().size() == 1);
	try {
		cos.adauga_activitati_de_facut(titlu);
		assert(false);
	}
	catch (CosException& e){
		assert(e.get_mesaj() == "Activitate existenta!\n");
	}
	cos.goleste_activitati_de_facut();
	assert(cos.get_all_activitati_de_facut().size() == 0);
	

}
void test_random() {
	Repository repo;
	Service srv{ repo };
	Cos cos{ repo };
	const string titlu = "a";
	const string descriere = "b";
	string tip = "a";
	const int durata = 4;
	const string titlu1 = "b";
	const string descriere1 = "d";
	string tip1 = "c";
	const int durata1 = 4;
	const string titlu2 = "z";
	const string descriere2 = "c";
	string tip2 = "c";
	const int durata2 = 1;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	srv.adaugaSRV(titlu2, descriere2, tip2, durata2);
	assert(cos.get_all_activitati_de_facut().size() == 0);
	cos.genereaza_random_activitati_de_facut(2);
	assert(cos.get_all_activitati_de_facut().size() == 2);

}
void test_raport() {
	Repository repo;
	Service srv{ repo };
	Cos cos{ repo };
	const string titlu = "a";
	const string descriere = "b";
	string tip = "a";
	const int durata = 4;
	const string titlu1 = "b";
	const string descriere1 = "d";
	string tip1 = "a";
	const int durata1 = 4;
	const string titlu2 = "z";
	const string descriere2 = "c";
	string tip2 = "c";
	const int durata2 = 1;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	srv.adaugaSRV(titlu2, descriere2, tip2, durata2);
	auto dict = srv.raport();


	map<string, DTO_tip>::iterator i = dict.begin();
	string fir = i->first;
	auto sec = i->second;

	assert(sec.get_count() == 2);
	i++;
	auto sec1 = i->second;
	assert(sec1.get_count() == 1);

}
void test_undo() {
	Repository repo;
	Service srv{ repo };
	Cos cos{ repo };
	const string titlu = "a";
	const string descriere = "b";
	string tip = "a";
	const int durata = 4;
	const string titlu1 = "b";
	const string descriere1 = "d";
	string tip1 = "a";
	const int durata1 = 4;
	const string titlu2 = "z";
	const string descriere2 = "c";
	string tip2 = "c";
	const int durata2 = 1;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	srv.adaugaSRV(titlu2, descriere2, tip2, durata2);
	
	assert(repo.getAll().size() == 3);
	srv.undo();
	assert(repo.getAll().size() == 2);
	srv.stergeSRV(titlu);
	assert(repo.getAll().size() == 1);
	srv.undo();
	assert(repo.getAll().size() == 2);
	srv.updateSRV(titlu, descriere1);
	srv.undo();
	Activitate a=srv.cautaSRV(titlu);
	assert(a.get_descriere() == descriere);

}

void test_export_fisier() {
	Repository repo;
	Service srv{ repo };
	Cos cos{ repo };
	const string titlu = "a";
	const string descriere = "b";
	string tip = "a";
	const int durata = 4;
	const string titlu1 = "b";
	const string descriere1 = "d";
	string tip1 = "c";
	const int durata1 = 4;
	const string titlu2 = "z";
	const string descriere2 = "c";
	string tip2 = "c";
	const int durata2 = 1;
	srv.adaugaSRV(titlu, descriere, tip, durata);
	srv.adaugaSRV(titlu1, descriere1, tip1, durata1);
	srv.adaugaSRV(titlu2, descriere2, tip2, durata2);
	assert(cos.get_all_activitati_de_facut().size() == 0);
	cos.genereaza_random_activitati_de_facut(3);
	cos.salveaza_in_fisier("test.html");
	

}
void test_repo_file() {
	ofstream out;
	out.open("test.txt", ios::trunc|ios::out);
	RepoFile repo{ "test.txt" };
    
	const string titlu = "a";
	const string descriere = "b";
	string tip = "a";
	const int durata = 4;
	Activitate a{ titlu,descriere,tip,durata };
	repo.add(a);
	string descriere1 = "asdfghj";
	Activitate a1{ titlu,descriere1,"",0 };
	repo.update(a1);
	Activitate b=repo.cauta(titlu);
	assert(b.get_descriere() == descriere1);
	repo.sterge(titlu);
	assert(repo.getAll().size() == 0);
	out.close();
	
}
void test_citire_fisier() {
	RepoFile repo{ "testcitire.txt" };
	assert(repo.getAll().size() == 3);
	try {
		RepoFile{ "asasas" };
		assert(false);
	}
	catch (RepoException& e){
		assert(e.getMesaj() == "Unable to open file!\n");
		
	}
}

void test_Repo_cu_prob() {

	RepoLab repo {0.1F};

	const string titlu = "a";
	const string titlu1 = "b";
	const string descriere = "b";
	string tip = "a";
	const int durata = 4;
	Activitate a{ titlu,descriere,tip,durata };
	Activitate a2{ titlu1,descriere,tip,durata };
	assert(repo.getAll().size() == 0);
	repo.add(a);
	try {
		repo.add(a);
		assert(false);
	}
	catch(RepoLabException& e){
		assert(e.get_msg() == "Activitate exsitenta!\n");

	}
	assert(repo.getAll().size() == 1);
	string descriere1 = "asdfghj";
	Activitate a1{ titlu,descriere1,"",0 };
	try {
		repo.update(a2);
		assert(false);
	}
	catch (RepoLabException& e) {
		assert(e.get_msg() == "Activitate inexistenta!\n");
	}
	repo.update(a1);
	try {
		repo.cauta(titlu1);
		assert(false);
	}
	catch (RepoLabException& e) {
		assert(e.get_msg() == "Activitate inexistenta!\n");
	}
	Activitate b = repo.cauta(titlu);
	assert(b.get_descriere() == descriere1);
	repo.sterge(titlu);
	try {
		repo.sterge(titlu1);
		assert(false);
	}
	catch (RepoLabException& e) {
		assert(e.get_msg() == "Activitate inexistenta!\n");
	}

	assert(repo.getAll().size() == 0);
	


}

void run_all_test() {
	test_domain();
	testAddRepo();
	testAddSRV();
	testCautaSRV();
	teststergeSRV();
	testUpdateSRV();
	test_filtrare();
	testSortSRV();
	test_vector();
	test_iterator();
	test_cos();
	test_random();
	test_raport();
	test_undo();
	test_export_fisier();
	test_repo_file();
	test_citire_fisier();
	test_Repo_cu_prob();
}
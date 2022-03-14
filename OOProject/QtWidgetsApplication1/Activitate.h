#pragma once
#include <string>
#include<iostream>
using namespace std;
class Activitate
{private:
	string titlu;
	string descriere;
	string tip;
	int durata;
	friend ostream& operator<<(ostream& stream, const Activitate& act);
public:
	//constructor activitate
	// titlu-string
	//tip-string
	//tip-string
	//durata-int
	Activitate() = default;
	Activitate(const string& titlu, const string& descr, const string& tip, int durata) :titlu{ titlu }, descriere{ descr}, tip{ tip }, durata{ durata }{
	}
	Activitate(const Activitate& ot) :titlu{ ot.titlu }, descriere{ ot.descriere }, tip{ ot.tip }, durata{ ot.durata }{
		std::cout << "Copie constructor apelata!\n";
	}
	//getter titlu->returneaza titlul obiectului curent-string
	string get_titlu() const {
		return titlu;
	}
	//getter descriere->returneaza descrierea obiectului curent-string
	string get_descriere() const{
		return descriere;
	}
	//getter tip->returneaza tipul obiectului curent-string
	string get_tip() const{
		return tip;
	}
	//getter durata->returneaza durata obiectului curent-int
	int get_durata() const noexcept{
		return durata;
	}
	//setter pret->seteaza pretul obiectului curent-int
	void set_descriere(string des) {
		descriere = des;
	}
	//suprascriere operator == 
	//dpua obiecte sunt egale daca au acelasi titlu
	bool operator==(const Activitate& Other) const noexcept {
		return titlu == Other.titlu;
	}
	
	
	
};



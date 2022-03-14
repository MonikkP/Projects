#pragma once
#include "Activitate.h"
#include<vector>
#include<map>

class RepoLabException {
private:
	string msg;
public:
	RepoLabException(string m) :msg{ m } {

	}
	string get_msg() {
		return msg;
	}
};
class RepoAbstract {
public:
	virtual ~RepoAbstract() = default;
	virtual void add(const Activitate& act) = 0;
	virtual void sterge(string titlu) = 0;
	virtual void  update(Activitate& a) = 0;
	virtual Activitate cauta(string titlu)=0;
	virtual vector<Activitate> getAll()=0;

};
class RepoLab:public RepoAbstract
{
private:
	map<string, Activitate> ListaActivitatimap;
	float prob;
public:
	RepoLab(float prob) :prob{ prob } {}
	void add(const Activitate& act) override;
	void sterge(string titlu) override;
	void update(Activitate& a) override;
	Activitate cauta(string titlu) override;
    vector<Activitate> getAll() override;
	void random_float();
	bool existacheie(string titlu) const;
	float get_prob() noexcept{
		return prob;
	}
	


};


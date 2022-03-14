#include "Activitate.h"
#include "Repository.h"

class UndoAction {
public:
	virtual void doUndo() = 0;
	virtual ~UndoAction() = default;
};

class AdaugaUndo : public UndoAction{
private:
	RepoAbstract& repo;
	Activitate actAdaugata;
public:
	AdaugaUndo(RepoAbstract& repo, const Activitate& act) :repo{ repo }, actAdaugata{ act }{}
	void doUndo() override{
		repo.sterge(actAdaugata.get_titlu());
	}
};

class StergeUndo :public UndoAction {
private:
	RepoAbstract& repo;
	Activitate actStearsa;
public:
	StergeUndo(RepoAbstract& repo, const Activitate& act) :repo{ repo }, actStearsa{ act }{}
	void doUndo() override {
		repo.add(actStearsa);
	}
};

class ModificaUndo :public UndoAction {
private:
	RepoAbstract& repo;
	Activitate actVeche;
public:
	ModificaUndo(RepoAbstract& repo,const Activitate& act) :repo{ repo }, actVeche{ act }{}
	void doUndo() override {
		repo.update(actVeche);
	}

	
};
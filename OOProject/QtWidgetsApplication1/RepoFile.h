#include "Repository.h"
#include<assert.h>


class RepoFile : public Repository {
private:
	string fName;
	void load_from_file();
	void write_to_file();
public:
	RepoFile(string file) : Repository(), fName{ file }{
		load_from_file();
	}
	void add(const Activitate& act) override {
		Repository::add(act);
		write_to_file();

	}
	void sterge(string titlu) override {
		Repository::sterge(titlu);
		write_to_file();
	}
	Activitate cauta(string titlu) override {
	return Repository::cauta(titlu);
	
	}
	void update(Activitate& a) override {
		Repository::update(a);
		write_to_file();
	}
	
};


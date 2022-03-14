#include "Cos.h"
#include<algorithm>
#include<fstream>
using namespace std;



vector<Activitate>Cos::get_all_activitati_de_facut() {
    return activitati_de_facut;
}
void Cos::goleste_activitati_de_facut() {

    activitati_de_facut.erase(activitati_de_facut.begin(), activitati_de_facut.end());
}
int Cos::adauga_activitati_de_facut(const string& titlu) {
    for (auto& a : activitati_de_facut) {
        if (a.get_titlu() == titlu)
            throw CosException("Activitate existenta!\n");
    }
    
    auto v = repo.getAll();
    auto rez=copy_if(v.begin(), v.end(), back_inserter(activitati_de_facut), [titlu](const Activitate& act) {
        return act.get_titlu() == titlu;
          });

    return activitati_de_facut.size();
  
}

void Cos::genereaza_random_activitati_de_facut(const size_t numar) {
    vector<Activitate> copy_activitati_repo{ repo.getAll() };
    random_shuffle(copy_activitati_repo.begin(), copy_activitati_repo.end());
    goleste_activitati_de_facut();
    if (activitati_de_facut.size() == 0)
        for (auto& act : copy_activitati_repo) {
            if (activitati_de_facut.size() < numar)
                activitati_de_facut.push_back(act);
            else
                return;
        }
   
}
void Cos::salveaza_in_fisier(const string FileName) {
    ofstream fout(FileName);
    if (!fout.is_open()) {
        throw CosException("File not opened\n");
    }
 
    for (auto& act : activitati_de_facut) {
        fout << "Titlu: " << act.get_titlu() << " Descriere: " << act.get_descriere() << " Tip: " << act.get_tip() << " Durata: " << act.get_durata();
        fout << "<br>";
    }
   fout.close();
    
   

    
    
}
from business.services import ServiceCurs
from domain.entities import Curs
from infrastucture.repositories import FileRepoCurs


class Teste():
    def run_all_tests(self):
        self.test_domain()
        self.test_repo_curs()
        #self.test_srv()

    def test_domain(self):
        curs=Curs("ENG1","limba_engleză","A1",70)
        assert(curs.get_id_curs()=="ENG1")
        assert(curs.get_nivel()=="A1")
        assert(curs.get_pret_pe_ora()==70)
        curs0=Curs("ENG1","","s",77)
        assert(curs==curs0)

    def test_repo_curs(self):
        file=open("test.txt","w")
        repo=FileRepoCurs("test.txt")
        assert(len(repo.get_all())==0)
        file.close()
    def test_srv(self):

        file = open("test.txt", "w")
        repo = FileRepoCurs("test.txt")
        srv=ServiceCurs(repo)
        curs=Curs("ENG1","limba_engleză","A1",70)
        curs0=Curs("ENG2","limba_engleză","A1",970)
        repo.store(curs) #teste nu are rost sa faci pe functionalitati pe care nu le folosesti
        #daca nu ti se cere explicit sa salvezi ceva nici nu testezi functionalitatea de store
        #ti se cere sa testezi doar get all, dar tu ai deja chestii in fisier. stii deja ca ai 10 chestii in fisier, testezi ca len(get_all()) sa fie 10, este suficient
        repo.store(curs0)
        assert(len(repo.get_all())==2)
        file.close()



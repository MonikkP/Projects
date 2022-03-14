from infrastucture.repositories import RepositoryException


class UI:
    def __init__(self, srvCurs):
        self.__srvCurs = srvCurs

    def run(self):
        while True:
            self.print()
            try:
                optiune = int(input("Introduceti optiune: "))
                try:
                    if optiune == 1:
                        self.__ui_cauta_dupa_limba()
                    if optiune == 2:
                        self.__ui_inscriere()
                except RepositoryException as re:
                    print(str(re))
            except ValueError:
                print("optiune invalida")

    def print(self):
        menu = "1.Cautare curs dupa limba.\n" \
               "2.Inscriere curs.\n"
        print(menu)

    def __ui_cauta_dupa_limba(self):
        limba = input("Introduceti o limba: ")
        lista = self.__srvCurs.cauta_curs_dupa_limba(limba)
        for curs in lista:
            print(curs.get_id_curs() + " " + curs.get_limba_straina() + " " + curs.get_nivel() + " " + str(
                curs.get_pret_pe_ora()))

    def __ui_inscriere(self):
        id_curs = input("Introduceti id-ul cursului la care doriti sa va inscriei: ")
        numar_ore = input("Introduceti numar ore: ")
        inscriere = self.__srvCurs.inscriere_curs(id_curs, numar_ore)
        print("Limba: "+inscriere.get_curs().get_limba_straina()+" nivel: "+str(inscriere.get_curs().get_nivel())+" pret: "+str(inscriere.get_total())+"\n")

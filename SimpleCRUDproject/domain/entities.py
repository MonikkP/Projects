class Curs:
    def __init__(self, id_curs, limba_straina, nivel, pret_pe_ora):
        """
        creaza obiectul curs definit:
        :param id_curs: string unic
        :param limba_straina: string nevid
        :param nivel: string nevid
        :param pret_pe_ora: numar natural>0
        """
        self.__id_curs = id_curs
        self.__limba_straina = limba_straina
        self.__nivel = nivel
        self.__pret_pe_ora = pret_pe_ora

    def get_id_curs(self):
        return self.__id_curs

    def get_limba_straina(self):
        return self.__limba_straina

    def get_nivel(self):
        return self.__nivel

    def get_pret_pe_ora(self):
        return self.__pret_pe_ora

    def __eq__(self, other):
        return self.__id_curs == other.__id_curs


class Inscriere:
    def __init__(self, curs, numar_ore):
        """
                creaza obiectul inscriere definit:
                :param curs
                :param numar_ore: nr natural > 0
                """
        self.__curs = curs
        self.__numar_ore = numar_ore

    def get_curs(self):
        return self.__curs

    def get_total(self):
        return self.__curs.get_pret_pe_ora() * self.__numar_ore #aici apelai functia curs care nu exista. asta este formula dupa care calculezi pretul in functie de numarul de ore

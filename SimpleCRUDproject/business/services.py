from domain.entities import Inscriere


class ServiceCurs():
    def __init__(self, repoCurs):
        self.__repoCurs = repoCurs

    def cauta_curs_dupa_limba(self, limba):
        """
        cauta un curs dupa limba
        :param limba:string introdus de utilizator de la tastaura
        :return: lista cursuri disponibile pe limba
        """
        cursuri = self.__repoCurs.get_all()
        lista_cursuri = []
        for curs in cursuri:
            if limba in curs.get_limba_straina():
                lista_cursuri.append(curs)
        return lista_cursuri

    def inscriere_curs(self, id_curs, numar_ore):
        curs = self.__repoCurs.search(id_curs)
        inscriere = Inscriere(curs, int(numar_ore))
        return inscriere

from domain.entities import Curs, Inscriere


class RepositoryException(Exception):
    pass


class FileRepoCurs:
    def __init__(self, fileName):
        self.__fileName = fileName
        """
        citeste fisierul si returneaza lista cu cursuri
        """

    def __load_from_file(self):
        try:
            file = open(self.__fileName, "r")
        except IOError:
            print("Nu se poate deschide fisierul!")
            return
        cursuri = []
        for line in file:
            if line.strip() == "": #aici aveai strip simplu, fara ()
                continue
            curs = self.__createCursFromLine(line.strip()) #aici aveai line fara strip. nu este necesar dar ajuta ca in caz ca ai uitat ceva spatii
            cursuri.append(curs)
        file.close()
        return cursuri

    def __createCursFromLine(self, line):
        """
        creaza un obiect de tip curs din linie
        :param line: linie din fisier
        :return: un curs
        """
        fields = line.split(",")
        curs = Curs(fields[0], fields[1], fields[2], int(fields[3]))
        return curs

    def __write_to_file(self, cursuri): #nu iti cere sa salvezi cursuri in fisier, deci nu ai nevoie de functia asta
        """
        scrie in fisier cursurile
        :param cursuri:lista cu cursuri

        """
        file = open(self.__fileName, "w")
        for curs in cursuri:
            line = curs.get_id_curs() + "," + curs.get_limba_straina() + "," + curs.get_nivel() + "," + str(
                curs.get_pret_pe_ora()) + "\n"
            file.write(line)
        file.close()

    def get_all(self):
        return self.__load_from_file()

    def search(self, id_curs):
        """
        cauta un curs dupa id
        :param id_curs: string nevid
        :return: cursul cautat
        """
        cursuri = self.__load_from_file()
        curs = Curs(id_curs, "", "", 0)
        if curs not in cursuri:
            raise RepositoryException("curs inexistent!\n")
        for c in cursuri: #aici aveai curs in cursuri; nu e ok ca ai declarat variabila curs mai sus si compilatorul nu stie la care sa se uite
            if curs == c: #aici aveai curs.get_id() == curs. nu merge pentru ca tu verificai daca un id este egal cu un obiect de tipul curs
                #este de ajuns sa verifici daca cele 2 cursuri sunt egale pentru ca tu ai suprascris operatorul de egal sa verifice daca id-urile la 2 cursuri sunt egale
                return c

    def store(self, curs): #nici asta
        cursuri = self.__load_from_file()
        if curs in cursuri:
            raise RepositoryException("element existent!\n")
        cursuri.append(curs)
        self.__write_to_file(cursuri)


'''
nu ai nevoie deloc de repo pt inscrieri. ti se cere doar sa zici la ce curs te inscrii si sa tiparesti pretul.
se poate face totul doar la nivel de service si UI
'''
#
# class FileRepoInscriere():
#     def __init__(self, fileName):
#         self.__fileName = fileName
#         """
#         salveaza inscrierile la cursuri in fisier
#         """
#
#     def __load_from_file(self): #pentru inscrieri nu citesti nimic din fisier, doar o sa salvezi, deci nu ai nevoie de metoda asta
#         try:
#             file = open(self.__fileName, "r")
#         except IOError:
#             print("Nu se poate deschide fisierul!")
#             return
#         cursuri = []
#         for line in file:
#             if line.strip == "":
#                 continue
#             curs = self.__createCursFromLine(line)
#             cursuri.append(curs)
#         file.close()
#         return cursuri
#
#     def __createCursFromLine(self, line): #nici asta
#         """
#
#         """
#         fields = line.split(",")
#         curs = Inscriere(Curs(fields[0], fields[1], fields[2], str(fields[3])), str(fields[4]))
#         return curs
#
#     def __createLineFromInscriere(self, inscriere):
#         return inscriere.get_curs().get_id_curs()+","+inscriere.get_curs().get_limba_strina()+","+inscriere.get_curs().get_nivel()+","+inscriere.get_curs().get_pret_pe_ora()+"\n"
#
#     def __write_to_file(self, inscrieri):
#         """
#         scrie in fisier cursurile
#         :param cursuri:lista cu cursuri
#
#         """
#         file = open(self.__fileName, "w")
#         for inscriere in inscrieri:
#             line = self.__createLineFromInscriere(inscriere)
#             file.write(line)
#         file.close()
#
#     def get_all(self):
#         return self.__load_from_file()

from business.services import ServiceCurs
from infrastucture.repositories import FileRepoCurs
from presentation.console import UI
from tests.teste import Teste

if __name__ == '__main__':
    # teste=Teste()
    # teste.run_all_tests()
    repoCurs=FileRepoCurs("cursuri.txt")
    srvCurs=ServiceCurs(repoCurs)
    #repoInscriere=FileRepoInscriere("inscrieri.txt")
    #srvInscriere=ServiceInscriere(repoInscriere)
    consola=UI(srvCurs)
    consola.run()



#include"GUI.H"
#include"teste.h"
#include"Cos.h"


int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

	run_all_test();

		//Repository r;
		RepoFile r{ "repo.txt" };
		//RepoLab r{ 0.1F };
		Service srv{ r };
		Cos cos{ r };
		GUI gui{ srv,cos };
		gui.show(); 

	return a.exec();
	
	


	
   
}

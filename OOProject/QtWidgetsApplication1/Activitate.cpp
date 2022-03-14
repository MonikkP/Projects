#include "Activitate.h"
ostream& operator<<(ostream& stream, const Activitate& act) {
	stream << act.titlu << " ";
	stream << act.descriere << " ";
	stream << act.tip << " ";
	stream << act.durata;
	return stream;
}
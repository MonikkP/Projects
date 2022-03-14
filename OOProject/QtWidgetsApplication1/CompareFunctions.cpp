#include"CompareFunctions.h"

bool ComparaTip(const Activitate& a1, const Activitate& a2) {
	return a1.get_tip() < a2.get_tip();

}
bool ComparaDescriere(const Activitate& a1, const Activitate& a2) {
	return a1.get_descriere() < a2.get_descriere();
}
bool ComparaTipDurata(const Activitate& a1, const Activitate& a2) {
	if (a1.get_tip() == a2.get_tip())
		return a1.get_durata() < a2.get_durata();
	return a1.get_tip() < a2.get_tip();
}
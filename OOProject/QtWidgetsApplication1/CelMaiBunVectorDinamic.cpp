#include "CelMaiBunVectorDinamic.h"
#include<assert.h>








void test_vector() {

	string titlu = "a";
	string descriere = "b";
	string tip = "a";
	const int durata = 4;
	Activitate a1{ titlu,descriere,tip,durata };
	Activitate a2{ titlu,descriere,tip,durata };

	MyVector<Activitate> v;
	v.push_back(a1);
	v.push_back(a2);
	assert(v.get(0) == a1);
	assert(v.get(1) == a2);
	assert(v.size() == 2);
	MyVector<Activitate> v1;
	v1.push_back(a1);
	v1.push_back(a1);
	v1 = v;
	assert(v1.get(1) == a2);
	v1.set(1, a1);
	assert(v1.get(1) == a1);
	v.push_back(a1);
	v.push_back(a1);
	v = v;
	MyVector<Activitate> v2{ v };
	assert(v2.get(0) == a1);
	v2.pop(a1);
	assert(v2.size() == 3);
	
}

void test_iterator() {
	MyVector<Activitate>v;
	string titlu1 = "a";
	string titlu2 = "b";
	string titlu3 = "c";
	string descriere = "b";
	string tip = "a";
	const int durata = 4;
	Activitate a1{ titlu1,descriere,tip,durata };
	Activitate a2{ titlu2,descriere,tip,durata };
	Activitate a3{ titlu3,descriere,tip,durata };
	v.push_back(a1);
	v.push_back(a2);
	v.push_back(a3);
	auto it = v.begin();
	assert(it == v.begin());
	assert(*it==a1);
	++it;
	assert(*it == a2);
	for (auto& p : v) {
		
		assert(p.get_durata() > 0);
	}


}
#pragma once
#include "Activitate.h"
template <typename ElementT>
class IteratorVector;

template <typename ElementT>
class MyVector {
private:
	int cp;
	int lg;
	ElementT* elems;
public:
	MyVector();

	
	//Rule of 3
	//constructor copiere
	MyVector(const MyVector& ot);

	
	//operator assigment
	MyVector& operator=(const MyVector& ot);
	//destructor
	~MyVector() {
		delete[]elems;
	}

	void push_back(const ElementT& e);
	int size() const;
	void pop(const ElementT& e);
	Activitate& get(int poz) const;
	void set(int poz, const ElementT& e);

	//iterator
	friend class IteratorVector<ElementT>;
	IteratorVector<ElementT>begin() const;
	IteratorVector<ElementT>end()const ;
};


template <typename ElementT>
MyVector<ElementT>::MyVector() :cp{ 2 }, lg{ 0 }, elems{ new ElementT[cp] }{}


template <typename ElementT>
 MyVector<ElementT>&MyVector<ElementT>::operator=(const MyVector& ot) {
	if (this == &ot) {
		return *this;
	}
	delete[]this->elems;
	this->elems = new ElementT[ot.cp];
	for (int i = 0; i < ot.lg; i++) {
		this->elems[i] = ot.elems[i];
	}
	this->cp = ot.cp;
	this->lg = ot.lg;
	return *this;

}

template <typename ElementT>
MyVector<ElementT>::MyVector(const MyVector& ot) {
	this->cp = ot.cp;
	this->lg = ot.lg;
	this->elems = new ElementT[cp];
	for (int i = 0; i < ot.lg; i++) {
		this->elems[i] = ot.elems[i];
	}

}
template <typename ElementT> 
void MyVector<ElementT>::push_back(const ElementT& e) {
	if (cp == lg) {
		int newMem = cp * 2;
		ElementT* elemsNew = new ElementT[newMem];
		for (int i = 0; i < lg; i++) {
			elemsNew[i] = elems[i];
		}
		delete[]elems;
		elems = elemsNew;
		cp = newMem;
	}
	elems[lg++] = e;
}
template <typename ElementT>
int MyVector<ElementT>::size()const {
	return lg;
}
template <typename ElementT>
Activitate& MyVector<ElementT>::get(int poz) const {
	return elems[poz];

}
template <typename ElementT>
void MyVector<ElementT>::set(int poz, const ElementT& e) {
	elems[poz] = e;

}
template <typename ElementT>
void MyVector<ElementT>::pop(const ElementT& e) {
	for (int i = 0; i < lg; i++) {
		if (elems[i] == e) {
			for (int j = i ;j < lg - 1; j++)
				elems[j] = elems[j + 1];
		}
	}
	lg = lg - 1;
}

void test_vector();

template <typename ElementT>
IteratorVector<ElementT>MyVector<ElementT>::end() const  {
	return IteratorVector<ElementT>(*this, lg);
}
template <typename ElementT>
IteratorVector<ElementT>MyVector<ElementT>::begin() const{
	return IteratorVector<ElementT>(*this);
}


template <typename ElementT>
class IteratorVector {
private:
	const MyVector<ElementT>& v;
	int poz = 0;

public:
	IteratorVector(const MyVector<ElementT>& v);
	IteratorVector(const MyVector<ElementT>& v, int poz);
	bool valid() const;
	ElementT& element() const;
	void next();
	ElementT& operator*();
	IteratorVector& operator++();
	bool operator==(const IteratorVector& ot);
	bool operator!=(const IteratorVector& ot);
};

template <typename ElementT>
IteratorVector<ElementT>::IteratorVector(const MyVector<ElementT>& v) :v{ v }{

}

template <typename ElementT>
IteratorVector<ElementT>::IteratorVector(const MyVector<ElementT>& v, int poz) : v{ v }, poz{ poz }{
}

template <typename ElementT>
void IteratorVector<ElementT>::next()  {
	poz++;

}
template <typename ElementT>
IteratorVector<ElementT>& IteratorVector<ElementT>::operator++() {
	next();
	return *this;
}

template <typename ElementT>
ElementT& IteratorVector<ElementT>::element() const  {
	return v.elems[poz];

}

template <typename ElementT>
ElementT& IteratorVector<ElementT>::operator*() {
	return element();
}
template <typename ElementT>
bool IteratorVector<ElementT>::valid() const {
	return poz <v.lg;
}

template <typename ElementT>
bool IteratorVector<ElementT>::operator==(const IteratorVector& ot) {
	return poz == ot.poz;

}

template <typename ElementT>
bool IteratorVector<ElementT>::operator!=(const IteratorVector& ot) {
	return !(*this== ot);

}
void test_iterator();
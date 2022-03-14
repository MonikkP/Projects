
#pragma once
#include<vector>
class Observer {
public:
	virtual void update() = 0;
};

class Observable {
private:
	std::vector<Observer*>observers;
public:
	void addObserver(Observer* o) {
		observers.push_back(o);
	}
	void removeObserver(Observer* o) {
		observers.erase(remove(observers.begin(), observers.end(), o), observers.end());
	}
	void notify() {
		for (auto obs : observers) {
			obs->update();
		}
	}
};


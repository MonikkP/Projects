#pragma once
#include <QtWidgets/QApplication>
#include <QtWidgets/qboxlayout.h >
#include <QtWidgets/qpushbutton.h>
#include<QtWidgets/qlistwidget.h>
#include <QPainter> 
#include"Cos.h"
#include"Observer.h"
#include<vector>
class CosPaint: public QWidget,public Observer
{

private:
	Cos& cos;
	void paintEvent(QPaintEvent* ) override {
		QPainter p{ this };
		for (int i = 0; i < cos.get_all_activitati_de_facut().size(); i++) {
			std::vector<double> list{ 100.0,200.0,150.0,300,0,256.0,144.0,340.0,425.0,346,0,230.0,399.44,270.99 };
			int index1 = rand() % list.size();
			int index2 = rand() % list.size();
			double left = list[index1];
			double top = list[index2];
			QRectF target(left, top, 200.0, 200.0);
			p.drawImage(target, QImage("sad.jpg"));
		}
	
	}

	
	void update() {
		repaint();
	}
public:
	CosPaint(Cos& cos) :cos{ cos } {
		cos.addObserver(this);
		
	}
	~CosPaint() {
		cos.removeObserver(this);
	}

};


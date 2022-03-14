#pragma once
#include <QtWidgets/QApplication>
#include <QtWidgets/qboxlayout.h >
#include <QtWidgets/qpushbutton.h>
#include<QtWidgets/qlistwidget.h>
#include"Cos.h"
#include <QLabel>
#include <QtWidgets/qlineedit.h>
#include"Observer.h"
class CosGUI:public QWidget,public Observer
{
private:
	QHBoxLayout* lymain = new QHBoxLayout;
	QLabel* label = new QLabel;
	QLineEdit* txtCate = new QLineEdit;
	QListWidget* listaCos = new QListWidget;
	QPushButton* btnrandom = new QPushButton{ "Random" };
	QPushButton* btngoleste = new QPushButton{ "Goleste Cos" };
	QPushButton* btnexport = new QPushButton{ "Export" };
private:
	Cos& cos;
	void initCosGUI();
	void loadfromRepo();
	void initconnectGoleste();
	void initconnectRandom();
	void initconnectExport();
	void update() override;
	
public:
	
	CosGUI(Cos& cos) :cos{ cos } {
		cos.addObserver(this);
		initCosGUI();
		loadfromRepo();
		initconnectGoleste();
		initconnectRandom();
		initconnectExport();
	}
	
	~CosGUI() {
		cos.removeObserver(this);
	}

};


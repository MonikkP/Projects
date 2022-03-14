#include "CosGUI.h"
#include<qobject.h>
#include<qformlayout.h>
#include<qmessagebox.h>



void CosGUI:: initCosGUI() {
	
	
	QVBoxLayout* lybtn = new QVBoxLayout;
	QVBoxLayout* lylista = new QVBoxLayout;
	lylista->addWidget(listaCos);
	lybtn->addWidget(btnrandom);
	lybtn->addWidget(btngoleste);
	lybtn->addWidget(btnexport);
	label->setFrameStyle(QFrame::Panel | QFrame::Sunken);
	label->setLineWidth(2);
	label->setText(QString::number(cos.get_all_activitati_de_facut().size()));
	auto fromly = new QFormLayout;
	fromly->addRow("Nr/Nume Fisier", txtCate);
	lymain->addLayout(fromly);
	lylista->addWidget(label);
	lymain->addLayout(lylista);
	lymain->addLayout(lybtn);
	setLayout(lymain);
}
void CosGUI::loadfromRepo() {
	listaCos->clear();
	for (const auto& el : cos.get_all_activitati_de_facut()) {
		listaCos->addItem(QString::fromStdString(el.get_titlu()));
	}
	
}
void CosGUI::initconnectGoleste() {
	QObject::connect(btngoleste, &QPushButton::clicked, [&]() {
		cos.goleste_activitati_de_facut();
		label->setText(QString::number(cos.get_all_activitati_de_facut().size()));
		loadfromRepo();
		});
}

void CosGUI::initconnectRandom() {
	QObject::connect(btnrandom, &QPushButton::clicked, [&]() {
		auto cate=txtCate->text();
		cos.genereaza_random_activitati_de_facut(cate.toInt());
		label->setText(QString::number(cos.get_all_activitati_de_facut().size()));
		loadfromRepo();
		});
}
void CosGUI::initconnectExport() {
	QObject::connect(btnexport, &QPushButton::clicked, [&]() {
		auto fisier = txtCate->text();
		try {
			cos.salveaza_in_fisier(fisier.toStdString());
			QMessageBox::information(nullptr, "Status export", "successfully Exported ");
		}
		catch (CosException& e) {
			QMessageBox::information(nullptr, "Error export", QString::fromStdString(e.get_mesaj()));
		}
		});
}

void CosGUI::update() {
	loadfromRepo();
	label->setText(QString::number(cos.get_all_activitati_de_facut().size()));
}



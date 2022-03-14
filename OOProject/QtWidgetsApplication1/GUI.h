#pragma once
#include "Service.h"
#include<vector>
#include "CompareFunctions.h"
#include <QtWidgets/QApplication>
#include <QtWidgets/qboxlayout.h >
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qformlayout.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qlistwidget.h>
#include<QtWidgets/qwidget.h>
#include <QTableWidget> 
#include <QTableWidgetItem>
#include <QButtonGroup>
#include<qmessagebox.h>
#include<map>
#include "CosGUI.h"
#include "CosPaint.h"
#include"qspinbox.h"
using namespace std;
class GUI: public QWidget
{
private:
    QListWidget* lista = new QListWidget;
    QLineEdit* txtTiltu = new QLineEdit;
    QLineEdit* txtDescriere = new QLineEdit;
    QLineEdit* txtTip = new QLineEdit;
    QLineEdit* txtDurata = new QLineEdit;
    QPushButton* btnAdauga = (new QPushButton{ "Adauga" });
    QPushButton* btnStergere = new QPushButton{ "Sterge" };
    QPushButton* btnModifica = new QPushButton{ "Modifica" };
    QPushButton* btnCauta=new QPushButton{ "Cauta" };
    QPushButton* btnSortTip = new QPushButton{ "Sortare tip" };
    QPushButton* btnUndo = new QPushButton{ "Undo" };
    //QPushButton* btnCos = new QPushButton(QIcon("E:\FACULTATE\ANU 1\SEMESTRUL 2\oop\QtWidgetsApplication1\descărcare.png"), "");
    QPushButton* btnCos = new QPushButton{ "Cos" };
    QPushButton* btnAdaugaCos = new QPushButton{ "Add Cos" };
    QPushButton* btnCosPaint = new QPushButton{ "Meme Cos" };
    QPushButton* btnRaport = new QPushButton{ "Raport" };
    QPushButton* btnClear = new QPushButton{ "Clear" };
    QPushButton* btntip = new QPushButton{ "Filtreaza tip" };
    QPushButton* btndescriere = new QPushButton{ "Filtrare descriere" };
    QPushButton* btnSortDescr = new QPushButton{ "Sortare descriere" };
    QPushButton* btnSortTip_Descr = new QPushButton{ "Sortare tip+durata" };
    QPushButton* btninitList= new QPushButton{ "Lista initiala" };
    QSlider* slider = new QSlider;
    QSpinBox* spin = new QSpinBox;
   

private:
	Service& srv;
    Cos& cos;
	void initGUI() {
        QHBoxLayout* lyMain = new QHBoxLayout{};
        setLayout(lyMain);
        

        auto lyBtns = new QHBoxLayout;
        auto fromly = new QFormLayout;
        auto layBtnFrom = new QVBoxLayout;
        auto lysort = new QVBoxLayout;
        auto sortList = new QVBoxLayout;
        auto lylista = new QVBoxLayout;
        auto lyUndobtn = new QVBoxLayout;
        auto lyslidere = new QVBoxLayout;

        lyslidere->addWidget(slider);
        lyslidere->addWidget(spin);

        lylista->addWidget(lista);
        sortList->addLayout(lylista);
        lysort->addWidget(btntip);
        lysort->addWidget(btndescriere);
        lysort->addWidget(btnSortTip);
        lysort->addWidget(btnSortDescr);
        lysort->addWidget(btnSortTip_Descr);
        lysort->addWidget(btninitList);


        sortList->addLayout(lysort);
        lyMain->addLayout(sortList);

        fromly->addRow("Titlu", txtTiltu);
        fromly->addRow("Descriere", txtDescriere);
        fromly->addRow("Tip",  txtTip);
        fromly->addRow("Durata", txtDurata);

       


        layBtnFrom->addLayout(fromly);

        lyBtns->addWidget(btnAdauga);
        
        lyBtns->addWidget(btnStergere);
        QPalette pal = btnStergere->palette();
        pal.setColor(QPalette::Button, QColor(Qt::red));
        btnStergere->setAutoFillBackground(true);
        btnStergere->setPalette(pal);
        btnStergere->update();
        lyBtns->addWidget(btnModifica);
        lyBtns->addWidget(btnCauta);

        
        layBtnFrom->addLayout(lyBtns);
        slider->setValue(0);
        spin->setValue(0);
        lyMain->addLayout(lyslidere);
        lyUndobtn->addWidget(btnClear);
        lyUndobtn->addWidget(btnAdaugaCos);
        lyUndobtn->addWidget(btnCos);
        QPalette pal2 = btnCos->palette();
        pal2.setColor(QPalette::Button, QColor(Qt::green));
        btnCos->setAutoFillBackground(true);
        btnCos->setPalette(pal2);
        btnCos->update();
        QPalette pal3 = btnAdaugaCos->palette();
        pal3.setColor(QPalette::Button, QColor(Qt::magenta));
        btnAdaugaCos->setAutoFillBackground(true);
        btnAdaugaCos->setPalette(pal3);
        btnAdaugaCos->update();
        lyUndobtn->addWidget(btnCosPaint);
        lyUndobtn->addWidget(btnUndo);
        QPalette pal1 = btnUndo->palette();
        pal1.setColor(QPalette::Button, QColor(Qt::yellow));
        btnUndo->setAutoFillBackground(true);
        btnUndo->setPalette(pal1);
        btnUndo->update();
        lyUndobtn->addWidget(btnRaport);
       
        lyMain->addLayout(layBtnFrom);

        lyMain->addLayout(lyUndobtn);
     
	}

    
    void loadActivitati() {
        lista->clear();
        for (const auto& act : srv.getAllSRV()) {
            auto item = new QListWidgetItem{ QString::fromStdString(act.get_titlu()) };
            lista->addItem(item);
        }
    }
    void loadActivitatiFromList(vector<Activitate> a) {
        lista->clear();
        for (const auto& act : a ) {
            lista->addItem(QString::fromStdString(act.get_titlu()));
        }

    }

    void initconnectAdauga() {
        QObject::connect(btnAdauga, &QPushButton::clicked, [&](){
            auto nume = txtTiltu->text();
            auto descr = txtDescriere->text();
            auto tip = txtTip->text();
            auto durata = txtDurata->text();
            try {
                srv.adaugaSRV(nume.toStdString(), descr.toStdString(), tip.toStdString(), durata.toInt());
            }
            catch (RepoException& e) {
                QMessageBox::information(nullptr, "Error Adugare", QString::fromStdString(e.getMesaj()));
            }
            catch (ValidationException& e) {
                QMessageBox::information(nullptr, "Error Adugare", QString::fromStdString(e.get_mesaj()));
            }
            loadActivitati();
            });

    }
    void initconectSterge() {
        QObject::connect(btnStergere, &QPushButton::clicked, [&]() {
            auto titlu = txtTiltu->text();
            try {
                srv.stergeSRV(titlu.toStdString());
            }
            catch (RepoException& e) {
                QMessageBox::information(nullptr, "Error Stergere", QString::fromStdString(e.getMesaj()));
            }
            loadActivitati();
            });
        
    }
    void initconnectFTip() {
        QObject::connect(btntip, &QPushButton::clicked, [&]() {
            auto tip = txtTip->text();
            vector<Activitate>filtarte;
            QList<QListWidgetItem*> items;
            auto activitai = srv.getAllSRV();
            filtarte = srv.filtrareTIP(tip.toStdString());
            for (int i = 0; i < filtarte.size(); i++) {

                for (auto j : lista->findItems(QString::fromStdString(filtarte[i].get_titlu()), Qt::MatchExactly)) {
                    items.push_back(j);
                   }
            }
            if (items.size() > 0) {
                
                for(auto item:items)
                   item ->setBackground(QBrush{ Qt::red, Qt::SolidPattern });
                }
                  
           
            });
    }
    void initconnectFDescriere() {
        QObject::connect(btndescriere, &QPushButton::clicked, [&]() {
            auto desc = txtDescriere->text();
            loadActivitatiFromList(srv.filtrareDescriere(desc.toStdString()));

            });

    }
    void initconnectSortDescr() {
        QObject::connect(btnSortDescr, &QPushButton::clicked, [&]() {
            loadActivitatiFromList(srv.sortSRV(ComparaDescriere));
            

            });
    }
    void initconnectSortTip() {
        QObject::connect(btnSortTip, &QPushButton::clicked, [&]() {
            loadActivitatiFromList(srv.sortSRV(ComparaTip));
           

            });
    }

    void initconnectSortTip_Durata() {
        QObject::connect(btnSortTip_Descr, &QPushButton::clicked, [&]() {
            loadActivitatiFromList(srv.sortSRV(ComparaTipDurata));
            

            });

    }

    void initList() {
        QObject::connect(btninitList, &QPushButton::clicked, [&]() {
            loadActivitati();
            });
    }

    void initconnectModifica() {
        QObject::connect(btnModifica, &QPushButton::clicked, [&]() {
            auto  titlu = txtTiltu->text();
            auto descriere = txtDescriere->text();
            try {
                srv.updateSRV(titlu.toStdString(), descriere.toStdString());
            }
            catch (RepoException& e) {
                QMessageBox::information(nullptr, "Error Update", QString::fromStdString( e.getMesaj()));
            }
            loadActivitati();
            });

    }

    void initconnectCauta() {
        QObject::connect(btnCauta, &QPushButton::clicked, [&]() {
            auto titlu = txtTiltu->text();
            try {
                auto a = srv.cautaSRV(titlu.toStdString());
                txtDescriere->setText(QString::fromStdString(a.get_descriere()));
                txtTip->setText(QString::fromStdString(a.get_tip()));
                txtDurata->setText(QString::number(a.get_durata()));
            }
            catch (RepoException& e) {
                QMessageBox::information(nullptr, "Error Cautare", QString::fromStdString(e.getMesaj()));
            }

            });

    }
    void initconnectCLickLista() {
        QObject::connect(lista, &QListWidget::itemSelectionChanged, [&]() {
            if (lista->selectedItems().isEmpty()) {
                txtTiltu->setText("");
                txtDescriere->setText("");
                txtTip->setText("");
                txtDurata->setText("");                
                return;
            }
            QListWidgetItem* selectedItem = lista->currentItem();
            txtTiltu->setText(selectedItem->text());
            auto titlu = txtTiltu->text();
            auto a= srv.cautaSRV(titlu.toStdString());
            txtDescriere->setText( QString::fromStdString(a.get_descriere()));
            txtTip->setText(QString::fromStdString(a.get_tip()));
            txtDurata->setText(QString::number(a.get_durata()));
            
            
            });

    }
    void initconnectCLear() {
        QObject::connect(btnClear, &QPushButton::clicked, [&]() {
            txtTiltu->setText("");
            txtDescriere->setText("");
            txtTip->setText("");
            txtDurata->setText("");
            });
    }

    void initconnectUndo() {
    QObject::connect(btnUndo, &QPushButton::clicked, [&]() {
        try {
            srv.undo();
            loadActivitati();
        }
        catch (ValidationException& r) {
            QMessageBox::information(nullptr, "Error Undo", QString::fromStdString(r.get_mesaj()));
        }

        });

    }
    void deschideRaport() {
        QWidget* rap=new QWidget{};
        QHBoxLayout* lyr = new QHBoxLayout;

        auto raport=srv.raport();
        auto it = raport.begin();
        int nrColoane = 2;
        int NrLinii = int(raport.size());
       QTableWidget* tabel = new QTableWidget{ NrLinii,nrColoane };
       lyr->addWidget(tabel);
       rap->setLayout(lyr);
       int linie = -1;
       for(const auto& el:raport){
           linie++;
               string first = el.first;
               DTO_tip sec = el.second;
            
               tabel->setItem(linie, 0, new QTableWidgetItem(QString::fromStdString(first)));
               tabel->setItem(linie, 1, new QTableWidgetItem(QString::number(sec.get_count())));
 
       }        
     
        rap->show();
    }

    void initconnectbtnRaport() {
        QObject::connect(btnRaport, &QPushButton::clicked,this, &GUI::deschideRaport);

    }
    void initconnectCos() {
        QObject::connect(btnCos, &QPushButton::clicked, [&]() {
       
            CosGUI* c = new CosGUI{cos};
            c->show();

            });
    }
    void initconnectCosPaint() {
        QObject::connect(btnCosPaint, &QPushButton::clicked, [&]() {

            CosPaint* c = new CosPaint{ cos };
            c->show();

            });
    }
    void initconnectAdaugaCos() {
        QObject::connect(btnAdaugaCos, &QPushButton::clicked, [&]() {
            auto titlu = txtTiltu->text();
            try {
                cos.adauga_activitati_de_facut(titlu.toStdString());
            }
            catch (CosException& e){
                QMessageBox::information(nullptr, "Error Add Cos", QString::fromStdString( e.get_mesaj()));

            }
            
            
            });
    }
    void initslider() {
        QObject::connect(slider, &QSlider::valueChanged, [&]() {
            txtDurata->setText(QString::number(slider->value()));
            });
    }
    void initspin() {
        QObject::connect(spin, qOverload<int>(&QSpinBox::valueChanged), [&]() {
            txtDurata->setText(QString::number(spin->value()));
            });
    }
public:
    GUI(Service& srv, Cos& cos) :srv{ srv }, cos{ cos } {
		initGUI();
        loadActivitati();
        initconnectAdauga();
        initconectSterge();
        initconnectSortDescr();
        initconnectSortTip();
        initconnectSortTip_Durata();
        initList();
        initconnectModifica();
        initconnectCLickLista();
        initconnectCLear();
        initconnectCauta();
        initconnectUndo();
        initconnectbtnRaport();
        initconnectCos();
        initconnectCosPaint();
        initconnectAdaugaCos();
        initconnectFTip();
        initconnectFDescriere();
        initslider();
        initspin();
		 
	}


};


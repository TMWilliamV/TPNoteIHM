package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.control.ToggleGroup;


public class Controller {

	@FXML
	private Label options;
	@FXML
	private RadioButton select;
	@FXML
	private RadioButton ellipse;
	@FXML
	private RadioButton rectangle;
	@FXML
	private RadioButton line;
	@FXML
	private ColorPicker color;
	@FXML
	private Button delete;
	@FXML
	private Button clone;
	@FXML
	private Pane pane;

	@FXML
	public void initialize()
    {
		//On regroupe les boutons radios pour avoir acc�s � leur �tat
		ToggleGroup choice = new ToggleGroup();
        select.setToggleGroup(choice);
        ellipse.setToggleGroup(choice);
        rectangle.setToggleGroup(choice);
        line.setToggleGroup(choice);

        //id du dernier objet s�lectionn�
        Id id = new Id();

        //compteur pour avoir une valeur d'id attribuable pouvant s'incr�menter
        Compteur compteur = new Compteur(0);

        //Liste des figures
        List<Shape> list = new ArrayList<Shape>();



        //On initialise les boutons Delete et Clone en non accessible car le bouton radio Select/Move n'est pas s�lectionn�
        delete.setDisable(true);
        clone.setDisable(true);

        //Evenement pour g�rer l'accessibilit� des boutons Delete et Clone
        EventHandler<MouseEvent> disable = new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent event) {
        		if(!select.isSelected()) {
                	delete.setDisable(true);
                	clone.setDisable(true);
                }
                else {
                	delete.setDisable(false);
                	clone.setDisable(false);
                }
        	}
        };

        //Evenement g�r� par les boutons radios
        select.setOnMouseClicked(disable);
        ellipse.setOnMouseClicked(disable);
        rectangle.setOnMouseClicked(disable);
        line.setOnMouseClicked(disable);



		//Permet l'initialisation du dessin
        pane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	double x = event.getX();
				double y = event.getY();

				//Ellipse
				if(Controller.this.ellipse.isSelected()) {

					compteur.setCompteur(compteur.getCompteur() + 1); //incr�mentation du compteur pour pouvoir assigner un id diff�rent � chaque fois
					Ellipse e = new Ellipse(x, y, 10, 10);
					e.setFill(color.getValue()); //couleur
					e.setId(Integer.toString(compteur.getCompteur())); //assignation d'un id unique

					pane.getChildren().add(e); //affichage
		    		list.add(e); //ajout dans la liste des figures pour y avoir acc�s une fois l'�v�nement fini



		    		//Seule la s�lection pour l'ellipse fonctionne...

		    		//Permet la s�lection pour ellipse
		    		e.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    	@Override
                    	public void handle(MouseEvent eventE) {

                    		System.out.println("id de la figure : " + e.getId());
                    		id.setId(e.getId());
                    		System.out.println("id s�lectionn� : " + id.getId());

                    		e.setFill(color.getValue()); //permet de changer la couleur une fois cliqu�

                    	}
		    		});

				}

				//On va faire la m�me chose mais pour les autres types de figures

				//Rectangle
		    	else if(Controller.this.rectangle.isSelected()) {

		    		compteur.setCompteur(compteur.getCompteur() + 1);
		    		Rectangle r = new Rectangle(x-10,y-10,20,20);
		    		r.setFill(color.getValue());
		    		r.setId(Integer.toString(compteur.getCompteur()));

		    		pane.getChildren().add(r);
		    		list.add(r);


		    		//Permet la s�lection pour rectangle
		    		r.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    	@Override
                    	public void handle(MouseEvent eventR) {
                    		id.setId(r.getId());

                    		r.setFill(color.getValue());
                    	}
		    		});

		    	}

				//Line
		    	else if(Controller.this.line.isSelected()) {
		    		compteur.setCompteur(compteur.getCompteur() + 1);
		    		Line l = new Line(x,y,10,10);
		    		l.setStroke(color.getValue());
		    		l.setId(Integer.toString(compteur.getCompteur()));
		    		pane.getChildren().add(l);
		    		list.add(l);


		    		//Permet la s�lection pour line
		    		l.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    	@Override
                    	public void handle(MouseEvent eventL) {
                    		id.setId(l.getId());

                    		l.setFill(color.getValue());
                    	}
		    		});
		    	}

            }
        });


        //Permet le scaling, il n'est absolument pas fonctionnel cependant
        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
        	@Override
        	public void handle(MouseEvent event) {
        		double x = event.getX();
        		double y = event.getY();

        		Shape shape = list.get(list.size()-1); //on r�cup�re le dernier �l�ment de la liste qui n'est d'autre que la figure qui vient d'�tre juste cr�ee par l'�v�nement pr�c�dent le scaling que l'on va effectuer

        		Scale scaler = new Scale(1,1);
		        shape.getTransforms().add(scaler);

		        //Le probl�me doit probablement venir d'ici, je ne connais pas la formule math�matique pour le faire correctement
	            double scaleX=Math.abs(x-shape.localToParent(0,0).getX()) / 100;
	            double scaleY=Math.abs(y-shape.localToParent(0,0).getY()) / 100;
	            scaler.setX(scaleX);
	            scaler.setY(scaleY);

        	}
        });




        //Je ne sais pas pourquoi, mais � partir de 2 figures cr�es d'affil�e, delete et clone ne fonctionnent plus...
        //On rappelle que seule la s�lection pour les ellipses fonctionnent si des tests sont � faire...

        //Permet d'effacer une figure
		delete.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {

            	if(select.isSelected()) { //si le radio bouton Select/Move est s�lectionn�
            		for(int i=list.size()-1; i<=0; i=i-1) { //On parcourt la liste des figures � partir de la fin de celle-ci pour g�rer les superpositions
            			if(list.get(i).getId().equals(/*id_selected*/ id.getId())) { //si l'id de la liste correspond � l'id s�lectionn�, on recupere la figure correspondante
            				pane.getChildren().remove(list.get(i)); //on supprime l'affichaqe
            				list.remove(i); //on enleve de la liste
            				break;
            			}
            		}
            	}
            }
           });

		//Permet de cloner une figure
		clone.setOnAction(new EventHandler() {
			@Override
			public void handle(Event event) {
				if(select.isSelected()) { //si le bouton radio Select/Move est s�lectionn�
					for(int i=list.size()-1; i<=0; i=i-1) { //On parcourt la liste
						if(list.get(i).getId().equals(/*id_selected*/ id.getId())) { //si l'id de la liste correspond � l'id s�lectionn�
							if(list.get(i).getClass().getSimpleName().equals("Ellipse")) { //si c'est une Ellipse
								Ellipse el = (Ellipse) list.get(i);
								Ellipse new_el = new Ellipse(el.getCenterX()+20, el.getCenterY()+20, el.getRadiusX(), el.getRadiusY()); //On cr�er avec un espacement avec le pr�c�dent
								compteur.setCompteur(compteur.getCompteur() + 1);
								new_el.setFill(color.getValue());
								new_el.setId(Integer.toString(compteur.getCompteur()));
								pane.getChildren().add(new_el);
								list.add(new_el);
								break;
							}

							//On fait pareil mais avec les autres cas de figures

							else if(list.get(i).getClass().getSimpleName().equals("Rectangle")) {
								Rectangle rec = (Rectangle) list.get(i);
								Rectangle new_rec = new Rectangle(rec.getX()+20, rec.getY()+20, rec.getWidth(), rec.getHeight());
								compteur.setCompteur(compteur.getCompteur() + 1);
								new_rec.setFill(color.getValue());
					    		new_rec.setId(Integer.toString(compteur.getCompteur()));
								pane.getChildren().add(new_rec);
								list.add(new_rec);
								break;
							}
							else if(list.get(i).getClass().getSimpleName().equals("Line")) {
								Line li = (Line) list.get(i);
								Line new_li = new Line(li.getStartX()+20, li.getStartY()+20, li.getEndX()+20, li.getEndY()+20);
								compteur.setCompteur(compteur.getCompteur() + 1);
								new_li.setStroke(color.getValue());
					    		new_li.setId(Integer.toString(compteur.getCompteur()));
								pane.getChildren().add(new_li);
								list.add(new_li);
								break;
							}
						}
					}
				}
			}
		});




    }
}

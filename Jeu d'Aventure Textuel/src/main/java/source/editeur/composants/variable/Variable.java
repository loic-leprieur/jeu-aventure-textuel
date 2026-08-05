package source.editeur.composants.variable;

import javafx.beans.property.SimpleStringProperty;

/**
 * Classe Variable
 */
public class Variable implements Comparable<Variable> {

    //Nom de la Variable
    private SimpleStringProperty nom;
    //Valeur de la Variable
    private SimpleStringProperty valeur;

    /**
     * Constructeur de Variable
     * @param nom Nom de la Variable
     * @param valeur Valeur de la Variable
     */
    public Variable(String nom,String valeur){
        this.nom = new SimpleStringProperty(nom.trim());
        this.valeur = new SimpleStringProperty(valeur.trim());
    }

    /**
     * Retourne nom de la Variable
     * @return String
     */
    public String getNom() {
        return nom.get();
    }


    /**
     * Retourne valeur de la Variable
     * @return valeur
     */
    public String getValeur() {
        return valeur.get();
    }


    @Override
    public String toString(){
        return getNom() + ":" + getValeur();
    }

    @Override
    public boolean equals(Object obj){
        boolean res = false;
        if(obj instanceof Variable){
            Variable o = (Variable) obj;
            if(o.getNom().equalsIgnoreCase(this.getNom())){
                res = true;
            }
        }
        return res;
    }

    @Override
    public int hashCode(){
        return getNom().hashCode();
    }

    @Override
    public int compareTo(Variable o) {
        return getNom().compareTo(o.getNom());
    }
}

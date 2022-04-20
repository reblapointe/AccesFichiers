package ca.cegepjonquiere.jha.proteome;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

public class Main {

    /**
     * Lit un fichier texte et l'imprime à la console.
     * Idéal pour les fichiers longs
     * @param fileName nom du fichier
     */
    public static void lireEtImprimerLignesLongFichier(String fileName) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(fileName);
             Scanner sc = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println(line);
            }
        }
    }

    /**
     * Lit un fichier texte et l'imprime à la console.
     * Idéal pour les fichiers courts
     * @param fileName nom du fichier
     */
    public static void lireEtImprimerLignesPetitFichier(String fileName) throws IOException {
        String data = new String(Files.readAllBytes(Paths.get(fileName)));
        System.out.println(data);
    }

    /**
     * Écrit du texte dans un fichier
     * @param fileName Emplacement du fichier
     * @param texte Texte à écrire
     */
    public static void ecrireTexte(String fileName, String texte) throws IOException{
        // true : Concaténer au contenu du fichier. False : Écraser le contenu du fichier
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(texte);
        }
    }

    /**
     * Enregistre un employé sérialisé dans un fichier
     * @param fileName emplacement du fichier
     * @param e employé à sauvegarder
     */
    public static void ecrireEmploye(String fileName, Employe e) throws IOException{
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(e);
        }
    }

    /**
     * Désérialize un employé à partir d'un fichier.
     * @param fileName emplacement du fichier
     * @return Un employé
     */
    public static Employe lireEmploye(String fileName) throws IOException, ClassNotFoundException{
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (Employe) in.readObject();
        }
    }

    /**
     * Affiche à la console le contenu d'un fichier XML contenant une liste d'employés.
     * Basé sur : https://www.w3schools.com/xml/schema_dtypes_date.asp
     * @param fileName Emplacement du fichier
     */
    public static void lireXML(String fileName) throws IOException, ParserConfigurationException, SAXException {
        File fXmlFile = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("employe");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println("id : " + eElement.getAttribute("id"));
                System.out.println("Prénom : " + eElement.getElementsByTagName("prenom").item(0).getTextContent());
                System.out.println("Nom : " + eElement.getElementsByTagName("nom").item(0).getTextContent());
                System.out.println("Date d'embauche : " + eElement.getElementsByTagName("dateEmbauche").item(0).getTextContent());
                System.out.println("Salaire : " + eElement.getElementsByTagName("salaire").item(0).getTextContent());
            }
        }
    }

    public static void main(String[] args) {
        try {
            String fileName = "Liste_francais.txt";
            // Option fichiers longs
            lireEtImprimerLignesLongFichier(fileName);

            // Option fichiers courts
            lireEtImprimerLignesPetitFichier(fileName);

            ecrireTexte("TestEcriture.txt", "Banane");

            // Sérialiser
            Employe e = new Employe("Robert", "Robert", new Date(), BigDecimal.valueOf(50_000));
            ecrireEmploye("Employe.ser", e);

            // Désérialiser
            Employe f = lireEmploye("Employe.ser");
            System.out.println(f);

            //XML
            lireXML("Employes.xml");
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}

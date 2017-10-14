package ro.mta.se.chat.adapters;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ro.mta.se.chat.factory.KeyFactory;
import ro.mta.se.chat.factory.abstracts.AbstractKey;
import ro.mta.se.chat.models.HistoryModel;
import ro.mta.se.chat.models.UserModel;
import ro.mta.se.chat.utils.logging.Logger;

import java.util.Base64;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Created by Alberto-Daniel on 11/14/2015.
 * This is a class responsable for  reading data from XML database
 */
public class XmlDataReader {

    /**
     * This is an helper function which get an XMLDocument from a file
     *
     * @param path The path of the xml file
     * @return the XMLDocument
     * @throws NullPointerException if @path is null
     * @null if the file could not be loaded
     */
    private Document getDocument(String path) {
        if (path == null)
            throw new NullPointerException();
        Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            dom = db.parse(path);
        } catch (ParserConfigurationException e) {
            Logger.warn("Error loading usesrs file.Format error");
        } catch (SAXException e) {
            Logger.warn("Error loading usesrs file.Format error");
        } catch (IOException e) {
            Logger.warn("Error loading usesrs file.Could not find file");
        }
        return dom;
    }

    /**
     * This function extract values from an XMLDocument which has the tag (@param tag)
     *
     * @param start the document to start
     * @param tag   the tag to match
     * @return a nested linked list containing the values from XML Document
     * @throws NullPointerException if @para, tag is null
     */
    private LinkedList<LinkedList<String>> readXml(Document start, String tag) {
        if (tag == null)
            throw new NullPointerException();
        LinkedList<LinkedList<String>> retList = new LinkedList<>();
        NodeList userData = start.getElementsByTagName(tag);
        for (int i = 0; i < userData.getLength(); i++) {
            Node item = userData.item(i);
            if (item.hasChildNodes()) {
                NodeList items = item.getChildNodes();
                LinkedList<String> partialList = new LinkedList<>();
                for (int j = 0; j < items.getLength(); j++) {
                    Node currentChild = items.item(j);
                    if (currentChild.getNodeType() == Node.ELEMENT_NODE)
                        partialList.add(items.item(j).getFirstChild().getTextContent());
                }
                retList.add(partialList);
            }
        }
        return retList;
    }

    /**
     * This function  extract the values from an xml document which match a specific tag
     * including a specific attribute
     *
     * @param start    the XMLDocument
     * @param tag      the tag to match
     * @param atribute the attribute value
     * @return the values from document
     * @throws NullPointerException if @param tag is null or @param atribute is null
     */
    private LinkedList<LinkedList<String>> readXml(Document start, String tag, String atribute) {
        if (tag == null && atribute == null)
            throw new NullPointerException();
        LinkedList<LinkedList<String>> retList = new LinkedList<>();
        NodeList userData = start.getElementsByTagName(tag);
        for (int i = 0; i < userData.getLength(); i++) {
            Node item = userData.item(i);
            if (item.hasChildNodes()) {
                NodeList items = item.getChildNodes();
                LinkedList<String> partialList = new LinkedList<>();
                for (int j = 0; j < items.getLength(); j++) {
                    if (items.item(j).hasAttributes())
                        partialList.add(items.item(j).getAttributes().getNamedItem(atribute).getNodeValue());
                }
                retList.add(partialList);
            }
        }
        return retList;
    }

    /**
     * This function returns all the users from  XMLDatabase
     *
     * @return a linked list containing all the users
     */
    public LinkedList<UserModel> getUsers() {
        String database = DataAdapter.History_Path;
        File f = new File(database);
        LinkedList<UserModel> list_users = new LinkedList<>();
        for (final File fileEntry : f.listFiles()) {
            String filename = fileEntry.getName();
            UserModel model = new UserModel();
            model.setNickname(filename);
            list_users.add(model);
        }
        return list_users;
    }

    /**
     * This function returns the history from local database given a nickname
     *
     * @param nickname the nickname given
     * @return a linked list containing all the history
     * @throws NullPointerException if @param nickname is null
     */
    public LinkedList<HistoryModel> readHistory(String nickname) {
        if (nickname == null)
            throw new NullPointerException();
        String database = DataAdapter.History_Path + "/" + nickname;
        File f = new File(database);
        if (f.exists() && f.isDirectory()) {
            LinkedList<HistoryModel> models = new LinkedList<>();
            for (final File fileEntry : f.listFiles()) {
                String filename = fileEntry.getName();
                StringTokenizer tokenizer = new StringTokenizer(filename, "_");
                long start = Long.parseLong(tokenizer.nextToken());
                long end = Long.parseLong(tokenizer.nextToken("._"));
                Document doc = getDocument(database + "/" + filename);
                LinkedList<LinkedList<String>> list_elements = readXml(doc, "messages");
                LinkedList<LinkedList<String>> list_attributes = readXml(doc, "messages", "fromMe");
                UserModel model = new UserModel();
                model.setNickname(nickname);
                HistoryModel history = new HistoryModel(model);
                history.setTimestampStart(start);
                history.setTimestampEnd(end);
                for (int i = 0; i < list_elements.size(); i++) {
                    LinkedList<String> elements = list_elements.get(i);
                    LinkedList<String> atributes = list_attributes.get(i);
                    for (int j = 0; j < elements.size() - 1; j++) {
                        String atribute = atributes.get(j);
                        history.addNewMsg(Base64.getDecoder().decode(elements.get(j)), Boolean.parseBoolean(atribute));
                    }
                    AbstractKey asymetricKey = KeyFactory.getKey(KeyFactory.ASYMETRIC_KEY, null);
                    byte[] aes = asymetricKey.decrypt(Base64.getDecoder().decode(elements.get(elements.size() - 1)));
                    history.setAesKey(aes);
                }
                models.add(history);
            }
            return models;
        } else {
            return null;
        }
    }


}

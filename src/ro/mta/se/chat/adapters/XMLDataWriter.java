package ro.mta.se.chat.adapters;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ro.mta.se.chat.factory.KeyFactory;
import ro.mta.se.chat.factory.abstracts.AbstractKey;
import ro.mta.se.chat.models.HistoryModel;
import ro.mta.se.chat.utils.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alberto-Daniel on 11/14/2015.
 * This class is responsable for reading  data from XMLDocument
 */
public class XmlDataWriter {

    /**
     * This function returns a XML Document from a xml file
     *
     * @param path the path to the file
     * @return xml document
     */
    private Document getDocument(String path) {
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
            Logger.warn("Error loading users file.Could not find file");
        }
        return dom;
    }

    /**
     * This function write xml data to a specific file
     *
     * @param root        of rhe xml document
     * @param item        the item to pe stored
     * @param values      the values to be stored
     * @param atributeVal the atrributeVlue
     * @param atributes   the list of stributes
     * @return an XMLDocument
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws FileNotFoundException
     * @throws NullPointerException   if @param root os @param item or @param atributes is null
     */
    private Document writeXML(String root, String item, List<String> values, String atributeVal, List<String> atributes)
            throws ParserConfigurationException, TransformerException, FileNotFoundException {
        if (root == null || item == null || atributes == null)
            throw new NullPointerException();
        Document dom;
        Element e = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        dom = db.newDocument();
        Element rootEle = dom.createElement(root);
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            e = dom.createElement(item);
            e.setTextContent(value);
            if (atributeVal != null) {
                e.setAttribute(atributeVal, atributes.get(i));
            }
            rootEle.appendChild(e);
        }
        dom.appendChild(rootEle);
        return dom;
    }

    /**
     * This function write the History model to the XML_History path
     *
     * @param theModel the model to store
     * @throws NullPointerException if @param theModel is null
     */
    public void writeHistory(HistoryModel theModel) {
        if (theModel == null)
            throw new NullPointerException();
        if (theModel.size() == 0)
            return;
        String filename = theModel.getXMLFilename();
        String path = DataAdapter.History_Path + "/" + theModel.getUser().getNickname();
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        LinkedList<String> msg = new LinkedList<>();
        LinkedList<String> fromMe = new LinkedList<>();
        for (int i = 0; i < theModel.size(); i++) {
            msg.add(new String(Base64.getEncoder().encode(theModel.getMsg(i))));
            fromMe.add(String.valueOf(theModel.getType(i)));
        }
        try {
            Document doc = writeXML("messages", "message", msg, "fromMe", fromMe);
            Element aes = doc.createElement("aes_key");
            AbstractKey asymetricKey = KeyFactory.getKey(KeyFactory.ASYMETRIC_KEY, null);
            byte[] enc_aes = asymetricKey.encrypt(theModel.getAesKey());
            aes.setTextContent(new String(Base64.getEncoder().encode(enc_aes)));
            doc.getFirstChild().appendChild(aes);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new java.io.File(path + "/" + filename));
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            Logger.error("Error parsing file");
        } catch (TransformerException e) {
            Logger.error("Error transorming file");
        } catch (FileNotFoundException e) {
            Logger.error("Error file does not exists");
        }
    }

    public void updateNickname(String oldNickname, String newNickname) throws NullPointerException {
        String path = DataAdapter.History_Path + "/" + oldNickname;
        File f = new File(path);
        if (newNickname == null)
            throw new NullPointerException("Nickname could not be null");
        if (f.exists()) {
            f.renameTo(new File(newNickname));
        }

    }
}

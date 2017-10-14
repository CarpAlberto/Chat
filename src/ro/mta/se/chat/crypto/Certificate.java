package ro.mta.se.chat.crypto;

import ro.mta.se.chat.utils.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by Alberto-Daniel on 11/23/2015.
 * This class is responsable for generating and loading Certificates
 */
public class Certificate {

    public static final String javaKeystoreFilename = "securechat_conf.jks";

    /**
     * This method generates a certificates and the private key associated with the user
     *
     * @param username The username as an alias for private key
     * @param password Password used to encrypt the private key
     */
    public static void generate(String username, String password) {

        try {

            String cmdOrBash;
            String absPath;
            String processParam;
            String preCmd;
            StringBuilder generateCmdB = new StringBuilder();
            StringBuilder extractCmdB = new StringBuilder();
            cmdOrBash = "cmd.exe";
            processParam = "/c";
            absPath = "%USERPROFILE%\\";
            preCmd = "start /WAIT ";
            generateCmdB.append("keytool -genkeypair -alias ");
            generateCmdB.append(username);
            generateCmdB.append(" -keyalg RSA -keysize 2048 -validity 360 -keypass ");
            generateCmdB.append(password);
            generateCmdB.append(" -storepass ");
            generateCmdB.append(password);
            generateCmdB.append(" -keystore ");
            generateCmdB.append(absPath);
            generateCmdB.append(javaKeystoreFilename);
            extractCmdB.append("keytool -exportcert -keystore ");
            extractCmdB.append(absPath);
            extractCmdB.append(javaKeystoreFilename);
            extractCmdB.append(" -alias ");
            extractCmdB.append(username);
            extractCmdB.append(" -storepass ");
            extractCmdB.append(password);
            extractCmdB.append(" -file ");
            extractCmdB.append(absPath);
            extractCmdB.append("SecureChat.cer");
            ProcessBuilder pGenBuilder = new ProcessBuilder(cmdOrBash, processParam, preCmd + generateCmdB.toString());
            Process generateProcess = pGenBuilder.start();
            int exitval = generateProcess.waitFor();
            pGenBuilder = new ProcessBuilder(cmdOrBash, processParam, preCmd + extractCmdB.toString());
            Process extractProcess = pGenBuilder.start();
            exitval = extractProcess.waitFor();
        } catch (Exception e) {
            Logger.error("Error generating certificate");
        }
    }

    /**
     * This function loads a Certificate from a file
     *
     * @param filename the path to the certificate
     * @return the byteArray containing the certificate
     */
    public static byte[] loadFromFile(String filename) {

        byte[] certBytes = null;

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            FileInputStream inStream = new FileInputStream(filename);
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(inStream);
            certBytes = certificate.getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return certBytes;
        }
    }

    /**
     * This function load the certificate from byte Array
     *
     * @param DEREncoding the certificate
     * @return the X509Certificate object
     */
    public static X509Certificate loadFromByteArray(byte[] DEREncoding) {

        X509Certificate cert = null;

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inputStream = new ByteArrayInputStream(DEREncoding);
            cert = (X509Certificate) cf.generateCertificate(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            return cert;
        }


    }

    /**
     * This function load X509Certificate object from a filename
     *
     * @param Filename
     * @return
     */
    public static X509Certificate loadCertificateFromFile(String Filename) {
        return loadFromByteArray(loadFromFile(Filename));
    }
}

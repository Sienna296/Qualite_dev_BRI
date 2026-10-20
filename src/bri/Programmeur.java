package bri;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Programmeur {
    private final String login;
    private final String hashMdp;
    private volatile String ftpUrl;

    public Programmeur(String login, String mdp, String ftpUrl) {
        this.login = login;
        this.hashMdp = hacher(mdp);
        this.ftpUrl = ftpUrl;
    }

    public String getLogin() { return login; }
    public String getFtpUrl() { return ftpUrl; }
    public void setFtpUrl(String ftpUrl) { this.ftpUrl = ftpUrl; }
    public boolean verifierMdp(String mdp) { return hashMdp.equals(hacher(mdp)); }

    private static String hacher(String mdp) {
        try {
            byte[] h = MessageDigest.getInstance("SHA-256").digest(mdp.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(h);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}

package uk.gov.dwp.cmg.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import uk.gov.dwp.cmg.cucumber.Hooks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class JwtHelper {
    //Token Issuer for different environments
    private static final String TokenIssuerLocal = ReadData.readDataFromPropertyFile("localTokenIssuer");
    private static final String TokenIssuerDi = ReadData.readDataFromPropertyFile("diTokenIssuer");
    private static final String TokenIssuerDp = ReadData.readDataFromPropertyFile("dpTokenIssuer");
    private static final String TokenIssuerDt = ReadData.readDataFromPropertyFile("dtTokenIssuer");

    private static String scope = "cmg-cps";
    private static String motoScope = "cmg-moto lambda";
    private static String ivrScope = "cmg-moto lambda";
    private static String AUTHORIZED_PARTY = "azp";

    // secret keys for different environments
    private static String localSecretKey = ReadData.readDataFromPropertyFile("localSecretKey");
    private static String diSecretKey = ReadData.readDataFromPropertyFile("diSecretKey");
    private static String dpSecretKey = ReadData.readDataFromPropertyFile("dpSecretKey");
    private static String dtSecretKey = ReadData.readDataFromPropertyFile("dtSecretKey");

    public static String generateTestJwt() {
        String[] audience = new String[1];
        Map<String, Object> claimsMap = new LinkedHashMap<>();
        Map<String, Object> header = new LinkedHashMap<>();
        audience[0] = "cmg-cps-service";

        claimsMap.put("scope", scope);
        claimsMap.put("iss", getIssuer());
        claimsMap.put("aud", audience);
        claimsMap.put(AUTHORIZED_PARTY, "cmg-cps-uihandler");
        Instant now = Instant.now();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        String jwt = Jwts.builder()
                .setHeader(header)
                .setClaims(claimsMap)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(now.plusSeconds(86400)))
                .signWith(SignatureAlgorithm.HS256,
                        TextCodec.BASE64.encode(getSecretKey()))
                .compact();
        return jwt;
    }

    public static String generateMototJwt() {
        String[] audience = new String[2];
        Map<String, Object> claimsMap = new LinkedHashMap<>();
        Map<String, Object> header = new LinkedHashMap<>();
        audience[0] = "cmg-moto-service";
        audience[1] = "cmg-moto-notification";
        System.out.println("Get Issuer for DI :"+getIssuer());
        claimsMap.put("scope", motoScope);
        claimsMap.put("iss", getIssuer());
        claimsMap.put(AUTHORIZED_PARTY, "cmg-moto-payment");
//        claimsMap.put("password", "secret");
//        claimsMap.put("grant_type", "cmg_client_credentials");
        claimsMap.put("aud", audience);
        Instant now = Instant.now();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        String jwt = Jwts.builder()
                .setHeader(header)
                .setClaims(claimsMap)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(now.plusSeconds(86400)))
                .signWith(SignatureAlgorithm.HS256,
                        TextCodec.BASE64.encode(getSecretKey()))
                .compact();
        return jwt;
    }

    public static String generateIvrtJwt() {
        String[] audience = new String[2];
        Map<String, Object> claimsMap = new LinkedHashMap<>();
        Map<String, Object> header = new LinkedHashMap<>();
        audience[0] = "cmg-moto-service";
        audience[1] = "cmg-moto-notification";

        claimsMap.put("scope", ivrScope);
        claimsMap.put("iss", getIssuer());
        claimsMap.put("username", "cmg-moto-payment");
        claimsMap.put("password", "secret");
        claimsMap.put("grant_type", "cmg_client_credentials");
        claimsMap.put("aud", audience);
        Instant now = Instant.now();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        String jwt = Jwts.builder()
                .setHeader(header)
                .setClaims(claimsMap)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(now.plusSeconds(86400)))
                .signWith(SignatureAlgorithm.HS256,
                        TextCodec.BASE64.encode(getSecretKey()))
                .compact();
        return jwt;
    }

    private static String getSecretKey() {
        String env = ReadData.readDataFromPropertyFile("targetEnvironment");
        switch (env) {
            case "local":
                return localSecretKey;
            case "di":
                return diSecretKey;
            case "dp":
                return dpSecretKey;
            case "dt":
                return dtSecretKey;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static String getIssuer() {
        String env = ReadData.readDataFromPropertyFile("targetEnvironment");
        switch (env) {
            case "local":
                return TokenIssuerLocal;
            case "di":
                return TokenIssuerDi;
            case "dp":
                return TokenIssuerDp;
            case "dt":
                return TokenIssuerDt;
            default:
                throw new IllegalArgumentException();
        }
    }
}
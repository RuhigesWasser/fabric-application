package com.example.application.gateway;

import com.google.gson.Gson;
import io.grpc.ChannelCredentials;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.identity.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author ASUS
 */
@Configuration
public class FabricGateway {
    private static final String MSP_ID = System.getenv().getOrDefault("MSP_ID", "Org1MSP");
    private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "mychannel");
    private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "basic");

    // Path to crypto materials.
    private static final Path CRYPTO_PATH = Paths.get("/home/user/go/src/github.com/ruhigesw/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com");
    // Path to user certificate.
    private static final Path CERT_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/signcerts/cert.pem"));
    // Path to user private key directory.
    private static final Path KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));
    // Path to peer tls certificate.
    private static final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer0.org1.example.com/tls/ca.crt"));

    // Gateway peer end point.
    private static final String PEER_ENDPOINT = "localhost:7051";
    private static final String OVERRIDE_AUTH = "peer0.org1.example.com";

    private final Gson gson = new Gson();

    private static Path getFirstFilePath(Path dirPath) throws IOException {
        try (var keyFiles = Files.list(dirPath)) {
            return keyFiles.findFirst().orElseThrow();
        }
    }

    @Bean
    public Gateway gateway() throws IOException, InvalidKeyException, CertificateException {
        Reader certReader = Files.newBufferedReader(CERT_DIR_PATH);
        X509Certificate certificate = Identities.readX509Certificate(certReader);
        Identity identity = new X509Identity(MSP_ID, certificate);

        Reader keyReader = Files.newBufferedReader(getFirstFilePath(KEY_DIR_PATH));
        PrivateKey privateKey = Identities.readPrivateKey(keyReader);
        Signer signer = Signers.newPrivateKeySigner(privateKey);

        ChannelCredentials tlsCredentials = TlsChannelCredentials.newBuilder()
                .trustManager(TLS_CERT_PATH.toFile())
                .build();
        ManagedChannel grpcChannel = Grpc.newChannelBuilder(PEER_ENDPOINT, tlsCredentials)
                .overrideAuthority(OVERRIDE_AUTH)
                .build();

        Gateway.Builder builder = Gateway.newInstance()
                .identity(identity)
                .signer(signer)
                .connection(grpcChannel);
        try {
            return builder.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Gateway.newInstance().connect();
        }
    }

    @Bean
    public Contract setContract() throws CertificateException, IOException, InvalidKeyException {
        return gateway().getNetwork(CHANNEL_NAME).getContract(CHAINCODE_NAME);
    }


}

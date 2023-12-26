package com.example.booklending.fireconfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final String firebasePrivateKeyJson = "{" +
        "\"type\": \"service_account\"," +
        "\"project_id\": \"book-lending-sys\"," +
        "\"private_key_id\": \"c0e4f3d0cf25f2ec59650ae080ad32d9eb3565f7\"," +
        "\"private_key\": \"-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCb19+bqKqIcwnz\nZvUU0wcOIjq2nETI/QgBGp7pX3U8NPRz5x/G51qzQWrRUOLjf6E3F3torE3/yO9v\nId46AZGE5zAxQrI3Jz9pOO9tNFFbfonLWnlQbgjVz+aZZBeWzyvea1Rn+yBfaJoh\neGpcj2bkW+nRMsCX66XM+H/WKHpWmR5ZQ0erMHuiH+/VGiPoOeQ5Zx6S+VpEceNm\nXNihCSeOyiR14kR328DQJOgnNfycyszNseNL2vwqZWEVslgk5UPSQqVHkWgAvQSB\nf4JhG7xOMaVZ+7AbIzUYyBKvjgJ4GKeNSJm0PshPyZmSgsLnM1Ci47T67NZqjJI3\nVSSKxCyXAgMBAAECggEAD3t9+1oMKHEmqxfulN/gLWRzfnfTkj99FOr+9ZgRBBG4\nusOv5v6jW/f4SOI8dYsqSi2wNMvFa09xkLywdgUjiVdpyhhiWdNcMTPBqZDkh5lF\nzujmm3nLBpQtyHhmqt9bGtuBL7FbBnopsdXjhIg57x7ogZWFXQfBWYsohsSlCXGW\nGo0XyG5QOSK1A2vTlgQ3zWV4vDLBWIxx+aQ3sp0JCcwUBsSCR8vJebZgCCrgRb2s\nVF1NSwOU/aSA0qH051VTuzKN171PeDF8ipotx7c1DmEzp17CJ9pT6ZrAHfCBFtp6\nLB3lJXA8xD+sx84k/5KKExKAdEfPkHUUNUXtdAHsJQKBgQDOSnofO4u2WKeFWiET\nfatFprM9QHVE49KC7+LD86WNZWORW4xGa5m7R68ZT4YXm7jTjapbY+4Cl0FFYDN0\n6XqEf2s7xrsIpSDfF90A1kTMf/Ksk3ANmlA46B4kPDtxkDtp5KXTczmq5qgMc7CD\n4gmVbVozLOPDPnaDkRCNrNUaEwKBgQDBZWrqqIjZGWl+uy/wr3OsXptx2kP1eksE\nS6NBEZJM/dLx6EFPA3epbZkYA3+sC79a27b3aLUHu+HlcXt41G5lGuER5uP+sRW2\nJjT+b9+uXLcQZZ7tI7r2vZIHo9Zm3AvVPJWXmhUQVm46lyWtBsReEij6R88wsLeH\nE5iKLrnz7QKBgHxAm8IIKaTRTUWi/d0ximHPgJJaKsgtuPQo7Rs9u++orAo3bAxW\nbyhAixThs1Cw+OhjocKeNBPXavcQdK3sm2EHJl3A25Lfcycsxm7koFfUkSSlQY7P\n7ZX5KX6F0QAb8c1cbQ0pkAiCGaI2ZJFrKn6YeQXt+dn124hvbdsHyOuFAoGBAJMb\njP9gIXBaZyeyRV4kZ6227TDmYQlJ8+QZkN3++rU74giGrZ7nuU8Ugf6FMslwyibX\nuE5rxEBUxxkbgFtB45NepdNTORxUIXrDh03/VTsxZULAueoSYrYrLvhGxODLAuk5\nw2RlHR6nUAObU1pRKm+QCsGOe+jCk2fZHRAHSQVlAoGARf0cT1QywjsG6X8Ml3Sf\nYo+6wflPTMJHQR0a9Cm8V8NI1hg054GeoGWsM0cRNTJ1kx+wiJZI8iOtC6Dz5+zd\n5AxcIERFJiIlH9mwEnjVYi1EEHC8J7VsRdABjWqChEtRCYsFY8ISyuUDJ1znRSIn\nlvjfR/0PFtQHNvyibUaMuPQ=\n-----END PRIVATE KEY-----\n\"," +
        "\"client_email\": \"firebase-adminsdk-lr9d2@book-lending-sys.iam.gserviceaccount.com\"," +
        "\"client_id\": \"114169714457873164052\"," +
        "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\"," +
        "\"token_uri\": \"https://oauth2.googleapis.com/token\"," +
        "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\"," +
        "\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-lr9d2%40book-lending-sys.iam.gserviceaccount.com\"," +
        "\"universe_domain\": \"googleapis.com\"" +
      "}";

    @Bean
    public DatabaseReference firebaseDatabaseReference() throws IOException {
        initializeFirebase();
        return FirebaseDatabase.getInstance().getReference();
    }

    private void initializeFirebase() throws IOException {

        FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(firebasePrivateKeyJson.getBytes())))
                    .setDatabaseUrl("https://book-lending-sys-default-rtdb.firebaseio.com/")
                    .build();

        FirebaseApp.initializeApp(options);
    }
}


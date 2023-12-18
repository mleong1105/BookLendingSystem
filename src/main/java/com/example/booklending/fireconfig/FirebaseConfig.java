package com.example.booklending.fireconfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final String firebasePrivateKeyJson = "{" +
        "\"type\": \"service_account\"," +
        "\"project_id\": \"book-lending-ms\"," +
        "\"private_key_id\": \"082d09945ff8ca4e5ed25dd22ae7a03872c6d28d\"," +
        "\"private_key\": \"-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCpeqA3znQfwfO/\n7UUn/rxMFm9ZUs0bd/ITn60kuWE3O5cK1FMOzBVuyy6pAsJarTJQWz/V7ikSzi8s\nNI6/2Bv3WzFoWvn/wfON+Aex1nrsnx4wcrDtFDgqI/t4MeY1WP3xjToWU91VWH4e\neRQ+fwwv52OJJIdeZnCH2USJF57OL9yr3bwicIzm3XAkk0KuXo+wxYi1GcTN86P5\nHrbXYWJ8HzoFOHCs+6ec8go/2LJfH40mWG5rnHZhNiwIwTtXVIVlrJnt5HsNjpOC\n/mndyARw8PqXrxQCsbI+VCMv7d034LmezJNwK+092U/vdiKqPOfG2mWZPhkDg29L\n2/Ubh6XhAgMBAAECggEASNYMJF/EdV+QB6SLq6AlQZgEu8FDp/H5JijiKx3Go4un\nIr7UgVABSA/nWjh22ayEqpdEzIahvVTCXj+qN9XDUx+skLpEpf+vUgzOFcZlJutN\neKnZ0tH1P1seeG4Nj1DTNmwpEi5xuXuiHCuSITfNv4q0stxxlwb0htW/yCFQHTEt\nskwBlBGimb1y27tv2eE0QGFiia20aRIoWNCHn2j3Q0wCSKH/y8zabncY01wcParK\nLu36A867mKkBoDdpvudw0o0o5CkYOxtKrh9PdUyp/JBpPzJu0Ucq5iIumg3P7Qg8\n5J3px768onbH44oetI4I9MMsPFnYyapWVXpFkiS/LwKBgQDmhmP2eu1/il3nwK4U\nKXo9J7eZj8FdHWLCSxffv9WTpJvewMSo1W2t7cXUuSCohFMsmW3VJewTuawoEG4n\nr8za3B+MqQAaoIZFGt7cNaG63fq1e9FnO5Ci4f/Ys8Hvl/s+90j/guY+ONVcr8ZR\nvlnGlUd7oHtDmoRnXGazTylTwwKBgQC8NTrXDgfm6AU9/CPdOkGGlovHTG0oxbA/\nuIRGflWW9/MjdEt+Yq0SWpRx9v9fp1E1BUX0QHqKzoICin7grf7DeTH72My5Ik97\nDaeW9WR31e+rHX7rqeTiP6EsfkkdFb112PcVX3MijeYHtJv8ZdDWckq5ONDkrNvt\nw9DLEoB5iwKBgQCtGFhTKbbPv9wHOvz7krGA3iPUoMb23ivb7Hr+vZLnjACmYGTK\nyYfTw+h6dI/7OXEi2gpItHLx1MdaRgwQrMbLx+HAwt2bINHxbd3NyE+qeyfXwiNY\nurc+NyjfVQrYl2xGuXQMuAOe0RoUwL/PfXj3zfug7r3f41RsMEvFdDl59QKBgGvl\nTDUrmwYaL58oZFsYYaZJuhpoSxAbvP3axyIunNyuitnhesIaycOC4CUxh5eLsTdn\nLYJoUJgw6UGRqkkeFrZzTV9nG35rGAugeVpGlfFJWVvMzg1GWawG9FwwjbO52eKs\nnoHHNZcyqBP0y5P3vrCh1wq2crMWEgMNxi4VDkgfAoGABw26dQrb9GgRpC4ysrx3\nY2qEhiCSHSDGE7JQIk7nCsKxBUNFG0+E9nXahE4yAWMsej/Ye0fZYw8qbHWTXCL2\nX3xHWc6i310Uv/XbHaUb+gL+bYcuqPWBAVD2tazVToJ/yyL91GFv9j8eT+tngBSn\nMpti2x0IH3OsfJ1YWFzNnT4=\n-----END PRIVATE KEY-----\n\"," +
        "\"client_email\": \"firebase-adminsdk-xk2ku@book-lending-ms.iam.gserviceaccount.com\"," +
        "\"client_id\": \"110673771467306941310\"," +
        "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\"," +
        "\"token_uri\": \"https://oauth2.googleapis.com/token\"," +
        "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\"," +
        "\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-xk2ku%40book-lending-ms.iam.gserviceaccount.com\"," +
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
                    .setDatabaseUrl("https://book-lending-ms-default-rtdb.firebaseio.com/")
                    .build();

        FirebaseApp.initializeApp(options);
    }
}


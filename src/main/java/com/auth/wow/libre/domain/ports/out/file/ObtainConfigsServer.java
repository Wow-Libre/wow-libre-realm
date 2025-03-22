package com.auth.wow.libre.domain.ports.out.file;

import com.auth.wow.libre.domain.model.*;

public interface ObtainConfigsServer {
    AuthServerConfig getFileConfigServer(String rute, String transactionId);
}

package com.auth.wow.libre.domain.ports.out.file;

import java.util.*;

public interface SaveConfigsServer {
    void updateConfigFile(String filePath, Map<String, String> replacements);
}

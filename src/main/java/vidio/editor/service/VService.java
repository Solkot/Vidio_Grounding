package vidio.editor.service;

import vidio.editor.dto.TimeDTO;

import java.io.IOException;
import java.nio.file.Path;

public interface VService {
    Path cutAndMerge(TimeDTO timeDTO) throws Exception;
    void deleteTemporaryDirectory(String userId) throws IOException;
}

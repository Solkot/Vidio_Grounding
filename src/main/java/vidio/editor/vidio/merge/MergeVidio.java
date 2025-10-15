package vidio.editor.vidio.merge;

import java.util.List;

public interface MergeVidio {
    void mergeVideos(List<String> inputPaths, String outputPath) throws Exception;
}

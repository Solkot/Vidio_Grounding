package vidio.editor.vidio.cut;

public interface CutVidio {
    void cutVideo(String inputPath, String outputPath, long start, long duration) throws Exception;
}

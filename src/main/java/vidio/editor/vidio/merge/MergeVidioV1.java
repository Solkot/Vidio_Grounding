package vidio.editor.vidio.merge;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service("mergeVidioV1")
@RequiredArgsConstructor
public class MergeVidioV1 implements MergeVidio{

    private final FFmpeg ffmpeg;

    public void mergeVideos(List<String> inputVideos, String output) throws Exception {
        File listFile = new File("file_list.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(listFile))) {
            for (String video : inputVideos) {
                writer.write("file '" + video.replace("\\", "/") + "'");
                writer.newLine();
            }
        }

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(listFile.getAbsolutePath())
                .addExtraArgs("-f", "concat", "-safe", "0")
                .addOutput(output)
                .done();

        executor.createJob(builder).run();
        listFile.delete();

        log.info("Video merge complete: {}", output);
    }
}

package vidio.editor.ffmpeg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.*;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CutVidio {

    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    public void cutVideo(String input, String output, long start, long duration) throws Exception {
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input)
                .addOutput(output)
                .setStartOffset(start, TimeUnit.SECONDS)
                .setDuration(duration, TimeUnit.SECONDS)
                .done();

        new FFmpegExecutor(ffmpeg, ffprobe).createJob(builder).run();
        log.info("Video cut complete: {}", output);
    }
}

package vidio.editor.vidio.cut;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.*;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service("cutVidioV1")
@RequiredArgsConstructor
public class CutVidioV1 implements CutVidio{

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

package vidio.editor.vidio.util;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GetVidioEndTime {

    private final FFprobe ffprobe;

    public double getEndTime(String videoPath) throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(videoPath);
        return probeResult.getFormat().duration;
    }
}

    //    public double getEndTime(String vidioPath) throws IOException {
//        FFprobe ffprobe = new FFprobe("C:\\ffmpeg\\bin\\ffprobe.exe");
//        FFmpegProbeResult probeResult = ffprobe.probe(vidioPath);
//        double duration = probeResult.getFormat().duration;
//        return duration;
//    }


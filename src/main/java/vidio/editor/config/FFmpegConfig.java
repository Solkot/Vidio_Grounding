package vidio.editor.config;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class FFmpegConfig {

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Value("${ffmpeg.ffprobe-path}")
    private String ffprobePath;

    @Bean
    public FFmpeg ffmpeg() throws Exception {
        return new FFmpeg(ffmpegPath);
    }

    @Bean
    public FFprobe ffprobe() throws Exception {
        return new FFprobe(ffprobePath);
    }
}
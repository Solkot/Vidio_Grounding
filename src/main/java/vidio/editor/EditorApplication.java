package vidio.editor;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vidio.editor.service.VService;
import vidio.editor.service.impl.V1ServiceImpl;
import vidio.editor.service.impl.V2ServiceImpl;
import vidio.editor.vidio.cut.CutVidio;
import vidio.editor.vidio.cut.CutVidioV1;
import vidio.editor.vidio.cut.CutVidioV2;
import vidio.editor.vidio.merge.MergeVidio;
import vidio.editor.vidio.merge.MergeVidioV1;
import vidio.editor.vidio.merge.MergeVidioV2;
import vidio.editor.vidio.util.GetVidioEndTime;

@SpringBootApplication
public class EditorApplication {
	public static void main(String[] args) {
		SpringApplication.run(EditorApplication.class, args);
	}

	@Bean
	public CutVidio cutVidio(FFmpeg ffmpeg, FFprobe ffprobe) {
		return new CutVidioV2(ffmpeg, ffprobe);
	}

	@Bean
	public MergeVidio mergeVidio(FFmpeg ffmpeg) {
		return new MergeVidioV2(ffmpeg);
	}

	@Bean
	public GetVidioEndTime getVidioEndTime(FFprobe ffprobe) {
		return new GetVidioEndTime(ffprobe);
	}

	@Bean
	public VService vService(CutVidio cutVidio, MergeVidio mergeVidio, GetVidioEndTime getVidioEndTime) {
		return new V2ServiceImpl(cutVidio, mergeVidio, getVidioEndTime);
	}
}


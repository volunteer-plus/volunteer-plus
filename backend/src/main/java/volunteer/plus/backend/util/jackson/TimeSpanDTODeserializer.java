package volunteer.plus.backend.util.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import volunteer.plus.backend.domain.dto.TimeSpanDTO;

import java.io.IOException;

public class TimeSpanDTODeserializer extends StdDeserializer<TimeSpanDTO> {

    public TimeSpanDTODeserializer() {
        super(TimeSpanDTO.class);
    }

    @Override
    public TimeSpanDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();             // e.g. "04:29:21.2989810"
        String[] parts = text.split(":");
        if (parts.length != 3) {
            throw new IOException("Invalid time-span format: " + text);
        }

        long hours      = Long.parseLong(parts[0]);
        int minutes     = Integer.parseInt(parts[1]);

        // split seconds and fraction
        String[] secParts = parts[2].split("\\.");
        int seconds      = Integer.parseInt(secParts[0]);
        double fracSec   = 0.0;
        if (secParts.length > 1) {
            // make "0.2989810"
            fracSec = Double.parseDouble("0." + secParts[1]);
        }

        // total seconds as double
        double totalSecsDouble = hours * 3600.0 + minutes * 60.0 + seconds + fracSec;

        // compute ticks (100 ns per tick)
        long ticks = (long)(totalSecsDouble * 10_000_000L);

        // breakdown
        long days    = hours / 24;
        int remHours = (int)(hours % 24);
        int millis   = (int)Math.round(fracSec * 1000);

        return TimeSpanDTO.builder()
                .ticks(ticks)
                .days((int) days)
                .hours(remHours)
                .minutes(minutes)
                .seconds(seconds)
                .milliseconds(millis)
                .totalDays(totalSecsDouble / 86400.0)
                .totalHours(totalSecsDouble / 3600.0)
                .totalMinutes(totalSecsDouble / 60.0)
                .totalSeconds(totalSecsDouble)
                .totalMilliseconds(totalSecsDouble * 1000.0)
                .build();
    }
}

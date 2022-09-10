package spodlivoi.dvach;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dvach-client", url = "${dvach.url}", decode404 = true)
public interface DvachClient {
    @GetMapping("/b/catalog.json")
    JsonNode getThreads();

    @GetMapping("/b/res/{threadNumber}.json")
    JsonNode getThread(@PathVariable String threadNumber);
}

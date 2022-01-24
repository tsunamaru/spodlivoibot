package spodlivoi.dvach;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dvach-client", url = "${dvach.url}", decode404 = true)
public interface DvachClient {
    @GetMapping("/b/catalog.json")
    JsonNode getThreads();

    @GetMapping("/makaba/mobile.fcgi?task=get_thread&board=b&thread={threadNumber}&post=0")
    ArrayNode getThread(@PathVariable String threadNumber);
}

package spodlivoi.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;
import spodlivoi.utils.Log;

@Component
@RequiredArgsConstructor
public class RabbitErrorHandler implements ErrorHandler {

    private final Log log;

    @Override
    public void handleError(Throwable t) {
        log.error(t);
    }
}

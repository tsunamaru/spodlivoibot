package spodlivoi.service;

import org.json.JSONArray;
import spodlivoi.database.enums.Copypaste;

public interface CopyPasteService {

    JSONArray getJSONCopyPaste(Copypaste copypaste);

    String getRandomCopyPaste(Copypaste type);

}

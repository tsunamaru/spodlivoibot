package spodlivoi.interactor;

import org.json.JSONArray;
import spodlivoi.database.enums.Copypaste;

public interface CopyPasteInteractor {

    JSONArray getJSONCopyPaste(Copypaste copypaste);

    String getRandomCopyPaste(Copypaste type);

}
